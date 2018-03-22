package io.sikorka.android.ui.dialogs.fileselection

import android.support.annotation.IntDef
import io.sikorka.android.ui.dialogs.fileselection.Selection.DIRECTORY
import io.sikorka.android.ui.dialogs.fileselection.Selection.FILE

object Selection {
  const val DIRECTORY = 1
  const val FILE = 2
}

@IntDef(DIRECTORY, FILE)
@Retention(AnnotationRetention.SOURCE)
annotation class SelectionMode