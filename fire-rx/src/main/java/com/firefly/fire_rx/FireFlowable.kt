package com.firefly.fire_rx

import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class FireFlowable<T>(private val flowable: Flowable<T>) : FireDisposable {
    private var successCallback: ((T) -> Unit)? = null
    private var onComplete: ((T?, Throwable?) -> Unit)? = null

    override var failureCallback: ((Throwable) -> Unit)? = null

    override fun execute(subscribeOn: Scheduler, observeOn: Scheduler): Disposable {
        return flowable.subscribeOn(subscribeOn)
            .observeOn(observeOn)
            .subscribeOn(subscribeOn)
            .subscribe({ result ->
                onComplete?.invoke(result, null)
                successCallback?.invoke(result)
            }, { t ->
                onComplete?.invoke(null, t)
                failureCallback?.invoke(t)
            })
    }

    companion object {
        fun <T> Flowable<T>.onSuccess(successCallback: ((T) -> Unit)): FireFlowable<T> {
            return FireFlowable(this).apply {
                this.successCallback = successCallback
            }
        }

        fun <T> Flowable<T>.onComplete(onComplete: (T?, Throwable?) -> Unit): FireFlowable<T> {
            return FireFlowable(this).apply {
                this.onComplete = onComplete
            }
        }

        fun <T> Flowable<T>.onFailure(failureCallback: ((Throwable) -> Unit)): FireFlowable<T> {
            return FireFlowable(this).apply {
                this.failureCallback = failureCallback
            }
        }

        fun <T> FireFlowable<T>.onSuccess(successCallback: ((T) -> Unit)): FireFlowable<T> {
            return this.apply {
                this.successCallback = successCallback
            }
        }

        fun <T> FireFlowable<T>.onComplete(onComplete: (T?, Throwable?) -> Unit): FireFlowable<T> {
            return this.apply {
                this.onComplete = onComplete
            }
        }

        fun <T> Flowable<T>.subscribeOnMain(): Flowable<T> {
            return this.subscribeOn(AndroidSchedulers.mainThread())
        }

        fun <T> Flowable<T>.subscribeOnIO(): Flowable<T> {
            return this.subscribeOn(Schedulers.io())
        }

        fun <T> Flowable<T>.observeOnMain(): Flowable<T> {
            return this.observeOn(AndroidSchedulers.mainThread())
        }

        fun <T> Flowable<T>.subscribeOnIOAndObserveOnMain(): Flowable<T> {
            return this.subscribeOnIO()
                .observeOnMain()
        }

        fun <T> Flowable<T>.defaultSubscribe(onNextCallback: ((T) -> Unit)): Disposable {
            return this.subscribeOnIOAndObserveOnMain()
                .subscribe(onNextCallback)
        }
    }
}