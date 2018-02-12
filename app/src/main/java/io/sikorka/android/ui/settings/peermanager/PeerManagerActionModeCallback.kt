package io.sikorka.android.ui.settings.peermanager

import android.app.Activity
import android.support.v7.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import io.sikorka.android.R
import io.sikorka.android.ui.MenuTint

class PeerManagerActionModeCallback
constructor(
  private val activity: Activity
) : ActionMode.Callback {

  init {
    require(activity is Actions) { "activity must implement the actions interface" }
  }

  private val actions: Actions = activity as Actions

  override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
    return when (item?.itemId) {
      R.id.peer_manager_action__delete -> {
        actions.delete()
        mode?.finish()
        true
      }
      R.id.peer_manager_action__select_none -> {
        actions.selectNone()
        true
      }
      R.id.peer_manager_action__select_all -> {
        actions.selectAll()
        true
      }
      else -> false
    }
  }

  override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {

    activity.menuInflater.inflate(R.menu.menu_action__peer_manager, menu)
    menu?.apply {
      MenuTint.on(this)
        .setSubMenuIconColor(R.color.white)
        .setMenuItemIconColor(R.color.white)
        .create()
        .apply(activity)
    }

    return true
  }

  override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
    return false
  }

  override fun onDestroyActionMode(mode: ActionMode?) {
    actions.done()
  }

  interface Actions {
    fun selectAll()
    fun selectNone()
    fun delete()
    fun done()
  }
}