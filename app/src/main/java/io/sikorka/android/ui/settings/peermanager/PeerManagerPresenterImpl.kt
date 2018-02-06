package io.sikorka.android.ui.settings.peermanager

import io.sikorka.android.mvp.BasePresenter
import javax.inject.Inject

@PeerManagerActivity.Presenter
class PeerManagerPresenterImpl
@Inject
constructor() : PeerManagerPresenter, BasePresenter<PeerManagerView>() {

}