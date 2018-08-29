package io.sikorka.android.ui.settings.peermanager

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.sikorka.android.R
import io.sikorka.android.core.configuration.peers.PeerEntry
import io.sikorka.android.io.copyToFile
import io.sikorka.android.ui.BaseActivity
import io.sikorka.android.ui.MenuTint
import io.sikorka.android.ui.settings.peermanager.PeerManagerActionModeCallback.Actions
import kotterknife.bindView
import org.koin.android.ext.android.inject
import java.io.File

class PeerManagerActivity : BaseActivity(), PeerManagerView, Actions {

  private val peers: RecyclerView by bindView(R.id.peer_manager__peers)
  private val loading: ProgressBar by bindView(R.id.peer_manager__loading_bar)

  private val presenter: PeerManagerPresenter by inject()

  private val peerAdapter: PeerManagerAdapter by lazy { PeerManagerAdapter() }

  private fun performFileSearch() {
    val intent = Intent(Intent.ACTION_GET_CONTENT)
    intent.addCategory(Intent.CATEGORY_OPENABLE)
    intent.type = "*/*"
    startActivityForResult(intent, READ_REQUEST_CODE)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_peer_manager)

    setupToolbar(R.string.peer_manager__title)

    peers.adapter = peerAdapter
    peers.layoutManager = LinearLayoutManager(this)

    presenter.attach(this)
    presenter.load()
  }

  override fun onDestroy() {
    presenter.detach()
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
      R.id.action__open_file -> {
        performFileSearch()
        true
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

  override fun loadingError() {
    loading(false)
  }

  override fun downloadFailed() {
    snackBar(R.string.peer_manager__error_downloading_file)
  }

  override fun openFailed() {
    snackBar(R.string.peer_manager__error_opening_file)
  }

  override fun delete() {
    peerAdapter.deleteSelection()
    presenter.save(peerAdapter.getList())
  }

  override fun loading(loading: Boolean) {
    this.loading.isVisible = loading
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

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if (requestCode != READ_REQUEST_CODE || resultCode != Activity.RESULT_OK) {
      return
    }

    data?.let {
      val uri = checkNotNull(it.data)
      val file = contentResolver.openInputStream(uri)?.use { stream ->
        val temp = File.createTempFile("peers", "list")
        stream.copyToFile(temp)
        temp
      }

      presenter.saveFromFile(checkNotNull(file))
    }
  }

  override fun openComplete() {
    snackBar(R.string.peer_manager__peer_list_loaded)
  }

  companion object {
    private const val READ_REQUEST_CODE = 12

    fun start(context: Context) {
      val intent = Intent(context, PeerManagerActivity::class.java)
      context.startActivity(intent)
    }
  }
}