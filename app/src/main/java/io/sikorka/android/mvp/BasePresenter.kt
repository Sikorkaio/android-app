package io.sikorka.android.mvp

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.sikorka.android.helpers.fail

open class BasePresenter<T : BaseView> : Presenter<T>, LifecycleOwner {

  private val lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)

  override fun getLifecycle(): Lifecycle = lifecycleRegistry

  private var view: T? = null
  protected val disposables = CompositeDisposable()

  private val isAttached: Boolean
    get() = view != null

  override fun attach(view: T) {
    this.view = view
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
  }

  override fun detach() {
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    this.view = null
    disposables.clear()
  }

  protected fun addDisposable(disposable: Disposable) {
    this.disposables.add(disposable)
  }

  fun attachedView(): T {
    if (!isAttached) {
      throw ViewNotAttachedException()
    }
    return view ?: fail(PRESENTER_NOT_ATTACHED)
  }

  protected class ViewNotAttachedException : RuntimeException(PRESENTER_NOT_ATTACHED)

  companion object {
    private const val PRESENTER_NOT_ATTACHED = "Please call Presenter.attach(BaseView) before calling a method on the presenter"
  }
}
