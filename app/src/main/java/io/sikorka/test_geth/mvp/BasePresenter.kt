package io.sikorka.test_geth.mvp

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.sikorka.test_geth.helpers.fail

open class BasePresenter<T : BaseView> : Presenter<T> {
  var view: T? = null
    private set

  private val compositeDisposable = CompositeDisposable()

  internal val isAttached: Boolean
    get() = view != null

  override fun attach(view: T) {
    this.view = view
  }

  override fun detach() {
    this.view = null
    compositeDisposable.clear()
  }

  protected fun addDisposable(disposable: Disposable) {
    this.compositeDisposable.add(disposable)
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
