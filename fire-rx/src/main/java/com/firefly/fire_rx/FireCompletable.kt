package com.firefly.fire_rx

import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable

class FireCompletable(private val completable: Completable): FireDisposable {
    internal var successCallback: (() -> Unit)? = null
    internal var onComplete: ((Throwable?) -> Unit)? = null
    override var failureCallback: ((Throwable) -> Unit)? = null

    override fun execute(subscribeOn: Scheduler, observeOn: Scheduler): Disposable {
        return completable.subscribeOn(subscribeOn)
            .observeOn(observeOn)
            .subscribe({
                onComplete?.invoke(null)
                successCallback?.invoke()
            }, {
                onComplete?.invoke(it)
                failureCallback?.invoke(it)
            })
    }
}