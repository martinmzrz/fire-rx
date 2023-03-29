package com.firefly.fire_rx

import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class FireSingle<T>(private val single: Single<T>): FireDisposable {
    private var successCallback: ((T) -> Unit)? = null
    private var onComplete: ((T?, Throwable?) -> Unit)? = null
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

    companion object{
        fun <T> Single<T>.onSuccess(successCallback: ((T) -> Unit)): FireSingle<T>{
            return FireSingle(this).apply {
                this.successCallback = successCallback
            }
        }

        fun <T> Single<T>.onComplete(onComplete: (T?, Throwable?) -> Unit): FireSingle<T>{
            return FireSingle(this).apply {
                this.onComplete = onComplete
            }
        }

        fun <T> Single<T>.onFailure(failureCallback: ((Throwable) -> Unit)): FireSingle<T>{
            return FireSingle(this).apply {
                this.failureCallback = failureCallback
            }
        }

        fun <T> FireSingle<T>.onSuccess(successCallback: ((T) -> Unit)): FireSingle<T>{
            return this.apply {
                this.successCallback = successCallback
            }
        }

        fun <T> FireSingle<T>.onComplete(onComplete: (T?, Throwable?) -> Unit): FireSingle<T>{
            return this.apply {
                this.onComplete = onComplete
            }
        }

        fun <T> FireSingle<T>.onFailure(failureCallback: ((Throwable) -> Unit)): FireSingle<T>{
            return this.apply {
                this.failureCallback = failureCallback
            }
        }
    }
}