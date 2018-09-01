package io.sikorka.android.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources

fun Context.getBitmapFromVectorDrawable(@DrawableRes drawableId: Int): Bitmap {

  val drawable = checkNotNull(AppCompatResources.getDrawable(this, drawableId)) {
    "couldn't retrieve the drawable"
  }

  val bitmap = Bitmap.createBitmap(
    drawable.intrinsicWidth,
    drawable.intrinsicHeight,
    Bitmap.Config.ARGB_8888
  )
  val canvas = Canvas(bitmap)
  drawable.run {
    setBounds(0, 0, canvas.width, canvas.height)
    draw(canvas)
  }
  return bitmap
}