package io.sikorka.android.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.DrawableCompat
import androidx.appcompat.content.res.AppCompatResources

fun Context.getBitmapFromVectorDrawable(@DrawableRes drawableId: Int): Bitmap {

  var drawable = checkNotNull(AppCompatResources.getDrawable(this, drawableId)) {
    "couldn't retrieve the drawable"
  }
  if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
    drawable = (DrawableCompat.wrap(drawable)).mutate()
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