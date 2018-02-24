package io.sikorka.android.ui.settings.peermanager

import io.sikorka.android.core.configuration.peers.PeerEntry
import io.sikorka.android.mvp.BaseView
import io.sikorka.android.mvp.Presenter
import toothpick.config.Module
import java.io.File

interface PeerManagerView : BaseView {
  fun update(data: List<PeerEntry>)

  fun loadingError()

  fun loading(loading: Boolean)

  fun downloadComplete()

  fun openComplete()

  fun downloadFailed()

  fun openFailed()
}


interface PeerManagerPresenter : Presenter<PeerManagerView> {
  fun load()

  fun save(peers: List<PeerEntry>)

  fun download(url: String, merge: Boolean)

  fun saveFromFile(file: File)
}


class PeerManagerModule : Module() {
  init {
    bind(PeerManagerPresenter::class.java).to(PeerManagerPresenterImpl::class.java)
      .singletonInScope()
  }
}