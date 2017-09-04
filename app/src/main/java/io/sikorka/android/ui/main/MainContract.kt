package io.sikorka.android.ui.main

import io.sikorka.android.mvp.BaseView
import io.sikorka.android.mvp.Presenter
import toothpick.config.Module

interface MainView : BaseView {

}


interface MainPresenter : Presenter<MainView> {
  fun loadAccountInfo()

}

class MainModule : Module() {
  init {
    bind(MainPresenter::class.java).to(MainPresenterImpl::class.java)
  }
}