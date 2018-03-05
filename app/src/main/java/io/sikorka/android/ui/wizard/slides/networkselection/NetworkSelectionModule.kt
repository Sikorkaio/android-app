package io.sikorka.android.ui.wizard.slides.networkselection

import toothpick.config.Module

class NetworkSelectionModule : Module() {
  init {
    bind(NetworkSelectionPresenter::class.java)
        .to(NetworkSelectionPresenterImpl::class.java)
        .singletonInScope()
  }
}