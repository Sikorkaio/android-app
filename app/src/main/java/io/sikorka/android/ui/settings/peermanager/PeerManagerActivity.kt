package io.sikorka.android.ui.settings.peermanager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import io.sikorka.android.R
import io.sikorka.android.core.configuration.peers.PeerEntry
import io.sikorka.android.ui.BaseActivity
import kotterknife.bindView
import toothpick.Scope
import toothpick.Toothpick
import toothpick.smoothie.module.SmoothieSupportActivityModule
import javax.inject.Inject

class PeerManagerActivity : BaseActivity(), PeerManagerView {

  private val peers: RecyclerView by bindView(R.id.peer_manager__peers)

  @Inject
  lateinit var presenter: PeerManagerPresenter

  private val peerAdapter: PeerManagerAdapter by lazy { PeerManagerAdapter() }

  private lateinit var scope: Scope

  override fun onCreate(savedInstanceState: Bundle?) {
    Toothpick.openScope(PRESENTER_SCOPE).installModules(PeerManagerModule())
    scope = Toothpick.openScopes(application, PRESENTER_SCOPE, this)
    scope.installModules(SmoothieSupportActivityModule(this))
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_peer_manager)
    Toothpick.inject(this, scope)

    setupToolbar()

    peers.adapter = peerAdapter
    peers.layoutManager = LinearLayoutManager(this)

    presenter.attach(this)
    presenter.load()
  }

  override fun onDestroy() {
    presenter.detach()
    Toothpick.closeScope(this)
    if (isFinishing) {
      Toothpick.closeScope(PRESENTER_SCOPE)
    }
    super.onDestroy()
  }

  override fun update(data: List<PeerEntry>) {
    peerAdapter.setList(data)
  }

  override fun showError() {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  @javax.inject.Scope
  @Target(AnnotationTarget.CLASS)
  @Retention(AnnotationRetention.RUNTIME)
  annotation class Presenter

  companion object {
    var PRESENTER_SCOPE: Class<*> = Presenter::class.java

    fun start(context: Context) {
      val intent = Intent(context, PeerManagerActivity::class.java)
      context.startActivity(intent)
    }
  }
}
