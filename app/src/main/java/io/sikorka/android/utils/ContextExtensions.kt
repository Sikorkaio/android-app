package io.sikorka.android.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.support.annotation.DrawableRes
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.content.res.AppCompatResources
import io.sikorka.android.helpers.fail

fun Context.getBitmapFromVectorDrawable(@DrawableRes drawableId: Int): Bitmap {

  var drawable = AppCompatResources.getDrawable(this, drawableId) ?: fail("couldn't retrieve the drawable")
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