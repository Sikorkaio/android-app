package io.sikorka.android.core.monitor

import android.arch.lifecycle.Observer
import io.reactivex.Completable
import io.sikorka.android.contract.BasicInterface
import io.sikorka.android.contract.SikorkaRegistry
import io.sikorka.android.core.ethereumclient.LightClientProvider
import io.sikorka.android.data.contracts.ContractRepository
import io.sikorka.android.data.contracts.deployed.DeployedSikorkaContract
import io.sikorka.android.data.contracts.deployed.DeployedSikorkaContractDao
import io.sikorka.android.data.location.UserLocationProvider
import io.sikorka.android.data.syncstatus.SyncStatus
import io.sikorka.android.data.syncstatus.SyncStatusProvider
import io.sikorka.android.utils.schedulers.SchedulerProvider
import timber.log.Timber
import java.math.BigDecimal
import javax.inject.Inject

class DeployedContractMonitor
@Inject
constructor(
  syncStatusProvider: SyncStatusProvider,
  userLocation: UserLocationProvider,
  private val lightClientProvider: LightClientProvider,
  private val deployedSikorkaContractDao: DeployedSikorkaContractDao,
  private val schedulerProvider: SchedulerProvider
) : LifecycleMonitor() {

  init {
    val observer = Observer<SyncStatus> {
      cacheContracts()
      syncStatusProvider.removeObservers(this)
    }

    userLocation.observe(this, Observer {
      Timber.v("location-updated")
      val value = syncStatusProvider.value ?: return@Observer

      if (!value.syncing) {
        syncStatusProvider.observe(this, observer)
        return@Observer
      }

      cacheContracts()
    })
  }

  private fun cacheContracts() {
    cache().subscribeOn(schedulerProvider.monitor())
      .subscribe({
        Timber.v("done")
      }) {
        Timber.e(it, "failed")
      }
  }

  private fun cache(): Completable = Completable.fromAction {
    if (!lightClientProvider.initialized) {
      return@fromAction
    }

    val lightClient = lightClientProvider.get()

    val registry = lightClient.bindContract(
      SikorkaRegistry.REGISTRY_ADDRESS,
      SikorkaRegistry.ABI,
      {
        SikorkaRegistry(it)
      })

    val contractAddresses = registry.getContractAddresses()
    val contractCoordinates = registry.getContractCoordinates()

    val contracts: MutableList<DeployedSikorkaContract> = ArrayList()

    for (i in 0 until contractCoordinates.size() step 2) {
      val address = contractAddresses[i / 2]
      val modifier = BigDecimal(ContractRepository.COORDINATES_MODIFIER)

      val cordLat = contractCoordinates[i].int64
      val cordLong = contractCoordinates[i + 1].int64
      Timber.v("hex: ${address.hex} ($cordLat, $cordLong)")

      val latitude = BigDecimal(cordLat).divide(modifier)
      val longitude = BigDecimal(cordLong).divide(modifier)
      val contract = lightClient.bindContract(
        address.hex,
        BasicInterface.ABI,
        { BasicInterface(it) }
      )
      val deployed = DeployedSikorkaContract(
        name = contract.name(),
        addressHex = address.hex,
        latitude = latitude.toDouble(),
        longitude = longitude.toDouble()
      )

      contracts.add(deployed)
    }

    deployedSikorkaContractDao.insertAll(contracts)
  }
}