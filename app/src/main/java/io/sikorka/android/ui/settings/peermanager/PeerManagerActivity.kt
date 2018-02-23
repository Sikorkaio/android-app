package io.sikorka.android.ui.settings.peermanager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import io.sikorka.android.R
import io.sikorka.android.core.configuration.peers.PeerEntry
import io.sikorka.android.ui.BaseActivity
import io.sikorka.android.ui.MenuTint
import io.sikorka.android.ui.gone
import io.sikorka.android.ui.settings.peermanager.PeerManagerActionModeCallback.Actions
import io.sikorka.android.ui.show
import kotterknife.bindView
import toothpick.Scope
import toothpick.Toothpick
import toothpick.smoothie.module.SmoothieSupportActivityModule
import javax.inject.Inject

class PeerManagerActivity : BaseActivity(), PeerManagerView, Actions {

  private val peers: RecyclerView by bindView(R.id.peer_manager__peers)
  private val loading: ProgressBar by bindView(R.id.peer_manager__loading_bar)

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

    setupToolbar(R.string.peer_manager__title)

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

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu__peer_manager, menu)
    menu?.let {
      MenuTint.on(it)
        .setMenuItemIconColor(ContextCompat.getColor(this, R.color.white))
        .apply(this)
    }
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.action__enable_selection_mode -> {
        startSupportActionMode(PeerManagerActionModeCallback(this))
        peerAdapter.selectionMode(true)
        true
      }
      R.id.action__download_dialog -> {
        urlInputDialog { url, merge ->
          presenter.download(url, merge)
        }.show()
        return true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun downloadComplete() {
    snackBar(R.string.peer_manager__peer_list_download_complete)
  }

  override fun update(data: List<PeerEntry>) {
    loading(false)
    peerAdapter.setList(data)
  }

  override fun showError() {
    loading(false)
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun delete() {
    peerAdapter.deleteSelection()
    presenter.save(peerAdapter.getList())
  }

  override fun loading(loading: Boolean) {
    if (loading) {
      this.loading.show()
    } else {
      this.loading.gone()
    }
  }

  override fun selectAll() {
    peerAdapter.selectAll()
  }

  override fun selectNone() {
    peerAdapter.selectNode()
  }

  override fun done() {
    peerAdapter.selectionMode(false)
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
