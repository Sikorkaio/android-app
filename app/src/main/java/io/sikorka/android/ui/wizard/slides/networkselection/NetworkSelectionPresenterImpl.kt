package io.sikorka.android.ui.wizard.slides.networkselection

import io.sikorka.android.mvp.BasePresenter
import io.sikorka.android.settings.AppPreferences
import javax.inject.Inject

class NetworkSelectionPresenterImpl
@Inject
constructor(private val appPreferences: AppPreferences) : NetworkSelectionPresenter,
    BasePresenter<NetworkSelectionView>() {
  override fun updateSelected() {
    val selectedNetwork = appPreferences.selectedNetwork()
    appPreferences.selectNetwork(selectedNetwork)
    attachedView().updateNetworkSelection(selectedNetwork)
  }

  override fun selectNetwork(network: Int) {
    appPreferences.selectNetwork(network)
    attachedView().updateNetworkSelection(network)
  }
}