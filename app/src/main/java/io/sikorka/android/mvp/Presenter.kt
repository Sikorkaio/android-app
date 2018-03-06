package io.sikorka.android.mvp

interface Presenter<in T : BaseView> {
  fun attach(view: T)

  fun detach()
}