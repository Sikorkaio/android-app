package io.sikorka.android.ui.settings.peermanager

import io.reactivex.rxkotlin.plusAssign
import io.sikorka.android.core.configuration.peers.PeerDataSource
import io.sikorka.android.core.configuration.peers.PeerEntry
import io.sikorka.android.helpers.Lce
import io.sikorka.android.mvp.BasePresenter
import io.sikorka.android.utils.schedulers.SchedulerProvider
import timber.log.Timber
import javax.inject.Inject

@PeerManagerActivity.Presenter
class PeerManagerPresenterImpl
@Inject
constructor(
  private val peerDataSource: PeerDataSource,
  private val schedulerProvider: SchedulerProvider
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
          it.success() -> {
            attachedView().showError()
          }
        }

      }) {
        attachedView().showError()
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
}