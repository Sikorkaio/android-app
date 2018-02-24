package io.sikorka.android.ui.settings.peermanager

import io.reactivex.rxkotlin.plusAssign
import io.sikorka.android.core.ServiceManager
import io.sikorka.android.core.configuration.peers.PeerDataSource
import io.sikorka.android.core.configuration.peers.PeerEntry
import io.sikorka.android.events.RxBus
import io.sikorka.android.helpers.Lce
import io.sikorka.android.mvp.BasePresenter
import io.sikorka.android.utils.schedulers.SchedulerProvider
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@PeerManagerActivity.Presenter
class PeerManagerPresenterImpl
@Inject
constructor(
  private val peerDataSource: PeerDataSource,
  private val schedulerProvider: SchedulerProvider,
  private val serviceManager: ServiceManager,
  private val bus: RxBus
) : PeerManagerPresenter, BasePresenter<PeerManagerView>() {

  override fun load() {
    disposables += peerDataSource.peers()
      .toObservable()
      .startWith(Lce.loading())
      .subscribeOn(schedulerProvider.io())
      .observeOn(schedulerProvider.main())
      .subscribe({
        when {
          it.loading() -> {
            attachedView().loading(true)
          }
          it.success() -> {
            attachedView().update(it.data())
          }
          it.failure() -> {
            attachedView().loadingError()
          }
        }

      }) {
        attachedView().loadingError()
      }
  }

  override fun save(peers: List<PeerEntry>) {
    disposables += peerDataSource.savePeers(peers)
      .subscribeOn(schedulerProvider.io())
      .observeOn(schedulerProvider.main())
      .subscribe({
        Timber.v("ok")
      }) {
        Timber.e(it, "Failure")
      }
  }

  override fun download(url: String, merge: Boolean) {
    disposables += peerDataSource.loadPeersFromUrl(url, merge)
      .subscribeOn(schedulerProvider.io())
      .observeOn(schedulerProvider.main())
      .subscribe({
        load()
        attachedView().downloadComplete()
        serviceManager.restart()
      }) {
        attachedView().downloadFailed()
      }
  }

  override fun saveFromFile(file: File) {
    disposables += peerDataSource.loadPeersFromFile(file, true)
      .subscribeOn(schedulerProvider.io())
      .observeOn(schedulerProvider.main())
      .subscribe({
        load()
        attachedView().openComplete()
        serviceManager.restart()
      }) {
        attachedView().openFailed()
      }
  }
}