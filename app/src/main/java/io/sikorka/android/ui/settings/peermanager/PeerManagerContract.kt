package io.sikorka.android.ui.settings.peermanager

import io.sikorka.android.mvp.BaseView
import io.sikorka.android.mvp.Presenter
import toothpick.config.Module

interface PeerManagerView : BaseView


interface PeerManagerPresenter: Presenter<PeerManagerView>


class PeerManagerModule : Module() {

}