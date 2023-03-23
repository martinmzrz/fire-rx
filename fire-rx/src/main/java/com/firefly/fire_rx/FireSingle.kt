package com.firefly.fire_rx

import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.Disposable

class FireSingle<T>(private val single: Single<T>): FireDisposable {
    internal var successCallback: ((T) -> Unit)? = null
    internal var onComplete: ((T?, Throwable?) -> Unit)? = null
    override var failureCallback: ((Throwable) -> Unit)? = null

    override fun execute(subscribeOn: Scheduler, observeOn: Scheduler): Disposable {
        return single.subscribeOn(subscribeOn)
            .observeOn(observeOn)
            .subscribe { result, throwable ->
                onComplete?.invoke(result, throwable)
                if (result != null) {
                    successCallback?.invoke(result)
                } else if (throwable != null) {
                    failureCallback?.invoke(throwable)
                }
            }
    }
}