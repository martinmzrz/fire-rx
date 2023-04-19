package com.firefly.fire_rx

import io.reactivex.Maybe
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class FireMaybe<T>(private val maybe: Maybe<T>) : FireDisposable {
    private var successCallback: ((T) -> Unit)? = null
    private var onComplete: (() -> Unit)? = null
    override var failureCallback: ((Throwable) -> Unit)? = null

    override fun execute(subscribeOn: Scheduler, observeOn: Scheduler): Disposable {
        return maybe.subscribeOn(subscribeOn)
            .observeOn(observeOn)
            .subscribe({
                successCallback?.invoke(it)
            }, {
                failureCallback?.invoke(it)
            }, {
                onComplete?.invoke()
            })
    }

    companion object {
        fun <T> Maybe<T>.onSuccess(successCallback: ((T) -> Unit)): FireMaybe<T> {
            return FireMaybe(this).apply {
                this.successCallback = successCallback
            }
        }

        fun <T> Maybe<T>.onComplete(onComplete: () -> Unit): FireMaybe<T> {
            return FireMaybe(this).apply {
                this.onComplete = onComplete
            }
        }

        fun <T> Maybe<T>.onFailure(failureCallback: ((Throwable) -> Unit)): FireMaybe<T> {
            return FireMaybe(this).apply {
                this.failureCallback = failureCallback
            }
        }

        fun <T> FireMaybe<T>.onSuccess(successCallback: ((T) -> Unit)): FireMaybe<T> {
            return this.apply {
                this.successCallback = successCallback
            }
        }

        fun <T> FireMaybe<T>.onComplete(onComplete: () -> Unit): FireMaybe<T> {
            return this.apply {
                this.onComplete = onComplete
            }
        }

        fun <T> Maybe<T>.subscribeOnMain(): Maybe<T> {
            return this.subscribeOn(AndroidSchedulers.mainThread())
        }

        fun <T> Maybe<T>.subscribeOnIO(): Maybe<T> {
            return this.subscribeOn(Schedulers.io())
        }

        fun <T> Maybe<T>.observeOnMain(): Maybe<T> {
            return this.observeOn(AndroidSchedulers.mainThread())
        }

        fun <T> Maybe<T>.subscribeOnIOAndObserveOnMain(): Maybe<T> {
            return this.subscribeOnIO()
                .observeOnMain()
        }

        fun <T> Maybe<T>.defaultSubscribe(onCallback: ((T?, Throwable?) -> Unit)): Disposable {
            return this.subscribeOnIOAndObserveOnMain()
                .subscribe({
                    onCallback(it, null)
                }, {
                    onCallback(null, it)
                }, {
                    onCallback(null, null)
                })
        }
    }
}