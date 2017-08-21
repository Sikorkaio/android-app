package io.sikorka.android.ui.wizard.slides.network_selection

import toothpick.config.Module

class NetworkSelectionModule : Module() {
  init {
    bind(NetworkSelectionPresenter::class.java)
        .to(NetworkSelectionPresenterImpl::class.java)
        .singletonInScope()
  }
}