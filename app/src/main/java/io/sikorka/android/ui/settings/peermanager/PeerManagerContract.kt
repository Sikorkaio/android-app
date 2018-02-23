package io.sikorka.android.ui.settings.peermanager

import io.sikorka.android.core.configuration.peers.PeerEntry
import io.sikorka.android.mvp.BaseView
import io.sikorka.android.mvp.Presenter
import toothpick.config.Module

interface PeerManagerView : BaseView {
  fun update(data: List<PeerEntry>)

  fun showError()

  fun loading(loading: Boolean)

  fun downloadComplete()
}


interface PeerManagerPresenter : Presenter<PeerManagerView> {
  fun load()

  fun save(peers: List<PeerEntry>)

  fun download(url: String, merge: Boolean)
}


class PeerManagerModule : Module() {
  init {
    bind(PeerManagerPresenter::class.java).to(PeerManagerPresenterImpl::class.java)
      .singletonInScope()
  }
}