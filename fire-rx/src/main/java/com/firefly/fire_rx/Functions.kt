package com.firefly.fire_rx

import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers







fun <T> Maybe<T>.subscribeOnIOAndObserveOnMain(): Maybe<T> {
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