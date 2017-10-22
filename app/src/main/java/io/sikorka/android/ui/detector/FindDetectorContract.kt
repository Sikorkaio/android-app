package io.sikorka.android.ui.detector

import io.sikorka.android.io.detectors.BtConnector
import io.sikorka.android.io.detectors.BtConnectorImpl
import io.sikorka.android.io.detectors.BtScanner
import io.sikorka.android.io.detectors.BtScannerImpl
import io.sikorka.android.mvp.BaseView
import io.sikorka.android.mvp.Presenter
import toothpick.config.Module

interface FindDetectorView : BaseView {

}

interface FindDetectorPresenter : Presenter<FindDetectorView> {

}

class FindDetectorModule : Module() {
  init {
    bind(FindDetectorPresenter::class.java).to(FindDetectorPresenterImpl::class.java).singletonInScope()
    bind(BtScanner::class.java).to(BtScannerImpl::class.java).singletonInScope()
    bind(BtConnector::class.java).to(BtConnectorImpl::class.java).singletonInScope()
  }
}