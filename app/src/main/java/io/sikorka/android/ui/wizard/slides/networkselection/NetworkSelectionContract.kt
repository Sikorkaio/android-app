package io.sikorka.android.ui.wizard.slides.networkselection

import io.sikorka.android.core.configuration.Network
import io.sikorka.android.mvp.BaseView
import io.sikorka.android.mvp.Presenter

interface NetworkSelectionView : BaseView {
  fun updateNetworkSelection(@Network.Selection network: Int)
}


interface NetworkSelectionPresenter : Presenter<NetworkSelectionView> {
  fun selectNetwork(@Network.Selection network: Int)
  fun updateSelected()
}