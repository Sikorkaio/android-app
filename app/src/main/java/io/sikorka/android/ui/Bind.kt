package io.sikorka.android.ui

import android.app.Activity
import android.app.Fragment
import android.support.annotation.IdRes
import android.view.View
import io.sikorka.android.helpers.fail
import android.support.v4.app.Fragment as SupportFragment

fun <T : View> Activity.bind(@IdRes idRes: Int): Lazy<T> {
  @Suppress("UNCHECKED_CAST")
  return unsafeLazy { findViewById<T>(idRes) }
}

fun <T : View> View.bind(@IdRes idRes: Int): Lazy<T> {
  @Suppress("UNCHECKED_CAST")
  return unsafeLazy { findViewById<T>(idRes) }
}

fun <T : View> Fragment.bind(@IdRes idRes: Int): Lazy<T> {
  return unsafeLazy { activity.findViewById<T>(idRes) }
}

fun <T : View> SupportFragment.bind(@IdRes idRes: Int): Lazy<T> {
  return unsafeLazy { activity?.findViewById<T>(idRes) ?: fail("activity was null") }
}

private fun <T> unsafeLazy(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)