package io.sikorka.android.ui.gasselectiondialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import butterknife.ButterKnife
import com.afollestad.materialdialogs.MaterialDialog
import io.sikorka.android.R
import toothpick.Toothpick

class GasSelectionDialog : DialogFragment() {

  private lateinit var fm: FragmentManager

  @SuppressLint("InflateParams")
  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val scope = Toothpick.openScopes(context.applicationContext, context, this)
    Toothpick.inject(this, scope)

    val inflater = LayoutInflater.from(context)
    val view = inflater.inflate(R.layout.dialog__account_create, null, false)
    ButterKnife.bind(this, view)

    val builder = MaterialDialog.Builder(context)
        .title(R.string.gas_selection__dialog_title)
        .customView(view, false)


    return builder.build()
  }

  companion object {
    fun create(fm: FragmentManager): GasSelectionDialog {
      val dialog = GasSelectionDialog()
      dialog.fm = fm
      return dialog
    }
  }
}