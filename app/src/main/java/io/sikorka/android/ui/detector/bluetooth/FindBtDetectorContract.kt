package io.sikorka.android.ui.detector.bluetooth

import io.sikorka.android.io.detectors.BtConnector
import io.sikorka.android.io.detectors.BtConnectorImpl
import io.sikorka.android.io.detectors.BtScanner
import io.sikorka.android.io.detectors.BtScannerImpl
import io.sikorka.android.mvp.BaseView
import io.sikorka.android.mvp.Presenter
import toothpick.config.Module

interface FindBtDetectorView : BaseView {

}

interface FindBtDetectorPresenter : Presenter<FindBtDetectorView> {

}

class FindBtDetectorModule : Module() {
  init {
    bind(FindBtDetectorPresenter::class.java).to(FindBtDetectorPresenterImpl::class.java).singletonInScope()
    bind(BtScanner::class.java).to(BtScannerImpl::class.java).singletonInScope()
    bind(BtConnector::class.java).to(BtConnectorImpl::class.java).singletonInScope()
  }
}