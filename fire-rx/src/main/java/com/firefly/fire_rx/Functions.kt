package com.firefly.fire_rx

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

fun <T : Any> Maybe<T>.subscribeOnIOAndObserveOnMain(): Maybe<T> {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun Disposable?.disposeIfOpen() {
    if (this?.isDisposed == false) {
        this.dispose()
    }
}

fun Iterable<Disposable?>.disposeAll() {
    forEach { it.disposeIfOpen() }
}