package io.sikorka.android.ui.wizard.slides.network_selection

import io.sikorka.android.mvp.BaseView
import io.sikorka.android.mvp.Presenter
import io.sikorka.android.node.configuration.Network

interface NetworkSelectionView : BaseView {
  fun updateNetworkSelection(@Network.Selection network: Long)
}


interface NetworkSelectionPresenter : Presenter<NetworkSelectionView> {
  fun selectNetwork(@Network.Selection network: Long)
}