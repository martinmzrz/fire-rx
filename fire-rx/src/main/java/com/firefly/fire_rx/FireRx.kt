package com.firefly.fire_rx

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class FireRx() {
    constructor(defaultFailureCallback: ((Throwable) -> Unit)) : this() {
        this.mDefaultFailureCallback = defaultFailureCallback
    }

    private var mDefaultFailureCallback: ((Throwable) -> Unit)? = null
    private val mDisposables = mutableListOf<Disposable>()

    fun dispose() {
        mDisposables.disposeAll()
    }

    fun execute(fireDisposable: FireDisposable, subscribeOn: Scheduler = Schedulers.io(), observeOn: Scheduler = AndroidSchedulers.mainThread()) {

        if (fireDisposable.failureCallback == null) {
            fireDisposable.failureCallback = mDefaultFailureCallback
        } else {
            val oldFailureCallback = fireDisposable.failureCallback

            val newFailureCallback: (Throwable) -> Unit = {
                oldFailureCallback?.invoke(it)
                mDefaultFailureCallback?.invoke(it)
            }
            fireDisposable.failureCallback = newFailureCallback
        }
        mDisposables.add(fireDisposable.execute(subscribeOn, observeOn))
    }

}