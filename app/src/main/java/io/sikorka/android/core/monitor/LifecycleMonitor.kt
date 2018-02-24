package io.sikorka.android.core.monitor

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.support.annotation.CallSuper
import timber.log.Timber

abstract class LifecycleMonitor : IMonitor, LifecycleOwner {
  private val lifecycleRegistry = LifecycleRegistry(this)

  init {
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
  }

  override fun getLifecycle(): Lifecycle = lifecycleRegistry

  @CallSuper
  override fun start() {
    Timber.v("Starting monitor $this")
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
  }

  @CallSuper
  override fun stop() {
    Timber.v("Stopping monitor $this")
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
  }
}