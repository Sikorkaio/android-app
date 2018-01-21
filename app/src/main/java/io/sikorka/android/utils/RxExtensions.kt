package io.sikorka.android.utils

import io.reactivex.disposables.Disposable

fun Disposable?.isDisposed(): Boolean = this?.isDisposed ?: true