package com.firefly.fire_rx

import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable

interface FireDisposable {
    var failureCallback: ((Throwable) -> Unit)?
    fun execute(subscribeOn: Scheduler, observeOn: Scheduler): Disposable
}