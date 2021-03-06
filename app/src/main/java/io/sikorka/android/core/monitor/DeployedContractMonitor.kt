package io.sikorka.android.core.monitor

import androidx.lifecycle.Observer
import io.reactivex.Completable
import io.reactivex.disposables.Disposable
import io.sikorka.android.contract.BasicInterface
import io.sikorka.android.contract.SikorkaRegistry
import io.sikorka.android.core.NoContractCodeAtGivenAddressException
import io.sikorka.android.core.ethereumclient.LightClientProvider
import io.sikorka.android.core.messageValue
import io.sikorka.android.data.contracts.ContractRepository
import io.sikorka.android.data.contracts.deployed.DeployedSikorkaContract
import io.sikorka.android.data.contracts.deployed.DeployedSikorkaContractDao
import io.sikorka.android.data.location.UserLocationProvider
import io.sikorka.android.data.syncstatus.SyncStatus
import io.sikorka.android.data.syncstatus.SyncStatusProvider
import io.sikorka.android.utils.isDisposed
import io.sikorka.android.utils.schedulers.AppSchedulers
import timber.log.Timber
import java.math.BigDecimal

class DeployedContractMonitor(
  syncStatusProvider: SyncStatusProvider,
  userLocation: UserLocationProvider,
  private val lightClientProvider: LightClientProvider,
  private val deployedSikorkaContractDao: DeployedSikorkaContractDao,
  private val appSchedulers: AppSchedulers
) : LifecycleMonitor() {

  private var disposable: Disposable? = null

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

      if (!disposable.isDisposed()) {
        return@Observer
      }

      disposable = cacheContracts()
    })
  }

  private fun cacheContracts(): Disposable? {
    return cache().subscribeOn(appSchedulers.monitor)
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
      SikorkaRegistry.ABI
    ) {
      SikorkaRegistry(it)
    }

    val contractAddresses = try {
      registry.getContractAddresses()
    } catch (e: Exception) {
      if (e.messageValue.contains("no contract code at given address")) {
        throw NoContractCodeAtGivenAddressException(e)
      }
      throw e
    }
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
        BasicInterface.ABI
      ) { BasicInterface(it) }
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