package com.firefly.fire_rx

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

interface FireDisposable {
    companion object{
        fun FireDisposable.onFailure(failureCallback: ((Throwable) -> Unit)): FireDisposable {
            return this.apply {
                this.failureCallback = failureCallback
            }
        }
        fun FireDisposable.defaultSubscribe(rx: FireRx) {
            rx.execute(this, Schedulers.io(), AndroidSchedulers.mainThread())
        }

        fun FireDisposable.defaultSubscribe(): Disposable {
            return this.execute(Schedulers.io(), AndroidSchedulers.mainThread())
        }
    }

    var failureCallback: ((Throwable) -> Unit)?
    fun execute(subscribeOn: Scheduler, observeOn: Scheduler): Disposable
}