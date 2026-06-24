package com.firefly.fire_rx

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class FireSingle<T : Any>(private val single: Single<T>) : FireDisposable {
    private var successCallback: ((T) -> Unit)? = null
    private var onComplete: ((T?, Throwable?) -> Unit)? = null
    override var failureCallback: ((Throwable) -> Unit)? = null

    override fun execute(subscribeOn: Scheduler, observeOn: Scheduler): Disposable {
        return single.subscribeOn(subscribeOn)
            .observeOn(observeOn)
            .subscribe { result: T?, throwable: Throwable? ->
                onComplete?.invoke(result, throwable)
                if (result != null) {
                    successCallback?.invoke(result)
                } else if (throwable != null) {
                    failureCallback?.invoke(throwable)
                }
            }
    }

    companion object {
        fun <T : Any> Single<T>.onSuccess(successCallback: ((T) -> Unit)): FireSingle<T> {
            return FireSingle(this).apply {
                this.successCallback = successCallback
            }
        }

        fun <T : Any> Single<T>.onComplete(onComplete: (T?, Throwable?) -> Unit): FireSingle<T> {
            return FireSingle(this).apply {
                this.onComplete = onComplete
            }
        }

        fun <T : Any> Single<T>.onFailure(failureCallback: ((Throwable) -> Unit)): FireSingle<T> {
            return FireSingle(this).apply {
                this.failureCallback = failureCallback
            }
        }

        fun <T : Any> FireSingle<T>.onSuccess(successCallback: ((T) -> Unit)): FireSingle<T> {
            return this.apply {
                this.successCallback = successCallback
            }
        }

        fun <T : Any> FireSingle<T>.onComplete(onComplete: (T?, Throwable?) -> Unit): FireSingle<T> {
            return this.apply {
                this.onComplete = onComplete
            }
        }

        fun <T : Any> Single<T>.subscribeOnMain(): Single<T> {
            return this.subscribeOn(AndroidSchedulers.mainThread())
        }

        fun <T : Any> Single<T>.subscribeOnIO(): Single<T> {
            return this.subscribeOn(Schedulers.io())
        }

        fun <T : Any> Single<T>.observeOnMain(): Single<T> {
            return this.observeOn(AndroidSchedulers.mainThread())
        }

        fun <T : Any> Single<T>.subscribeOnIOAndObserveOnMain(): Single<T> {
            return this.subscribeOnIO()
                .observeOnMain()
        }

        fun <T : Any> Single<T>.defaultSubscribe(onCallback: ((T?, Throwable?) -> Unit)): Disposable {
            return this.subscribeOnIOAndObserveOnMain()
                .subscribe(onCallback)
        }

        fun <T : Any> Single<T>.onErrorResume(resumeFunction: (Throwable) -> T): Single<T> {
            return this.onErrorResumeNext {
                Single.just(resumeFunction.invoke(it))
            }
        }
    }
}