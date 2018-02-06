package io.sikorka.android.ui.settings.peermanager

import android.os.Bundle
import io.sikorka.android.R
import io.sikorka.android.ui.BaseActivity
import toothpick.Scope
import toothpick.Toothpick
import toothpick.smoothie.module.SmoothieSupportActivityModule
import javax.inject.Inject

class PeerManagerActivity : BaseActivity(), PeerManagerView {

  private lateinit var scope: Scope

  @Inject
  lateinit var presenter: PeerManagerPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    Toothpick.openScope(PRESENTER_SCOPE).installModules(PeerManagerModule())
    scope = Toothpick.openScopes(application, PRESENTER_SCOPE, this)
    scope.installModules(SmoothieSupportActivityModule(this))
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_peer_manager)
    Toothpick.inject(this, scope)
  }

  @javax.inject.Scope
  @Target(AnnotationTarget.CLASS)
  @Retention(AnnotationRetention.RUNTIME)
  annotation class Presenter

  companion object {
    var PRESENTER_SCOPE: Class<*> = Presenter::class.java
  }
}
