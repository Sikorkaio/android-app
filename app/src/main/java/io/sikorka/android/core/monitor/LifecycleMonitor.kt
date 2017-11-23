package io.sikorka.android.core.monitor

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.support.annotation.CallSuper

abstract class LifecycleMonitor : IMonitor, LifecycleOwner {
  private val lifecycleRegistry = LifecycleRegistry(this)

  init {
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
  }

  override fun getLifecycle(): Lifecycle = lifecycleRegistry

  @CallSuper
  override fun start() {
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
  }

  @CallSuper
  override fun stop() {
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
  }
}