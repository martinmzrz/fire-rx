package com.firefly.fire_rx

import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class FireCompletable(private val completable: Completable): FireDisposable {
    private var successCallback: (() -> Unit)? = null
    private var onComplete: ((Throwable?) -> Unit)? = null
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

    companion object{
        fun Completable.onSuccess(successCallback: () -> Unit): FireCompletable{
            return FireCompletable(this).apply {
                this.successCallback = successCallback
            }
        }

        fun Completable.onFailure(failureCallback: (Throwable) -> Unit): FireCompletable {
            return FireCompletable(this).apply {
                this.failureCallback = failureCallback
            }
        }

        fun Completable.onComplete(onComplete: (Throwable?) -> Unit): FireCompletable {
            return FireCompletable(this).apply {
                this.onComplete = onComplete
            }
        }

        fun FireCompletable.onSuccess(successCallback: () -> Unit): FireCompletable{
            return this.apply {
                this.successCallback = successCallback
            }
        }

        fun FireCompletable.onComplete(onComplete: (Throwable?) -> Unit): FireCompletable {
            return this.apply {
                this.onComplete = onComplete
            }
        }

        fun Completable.subscribeOnMain(): Completable {
            return this.subscribeOn(AndroidSchedulers.mainThread())
        }

        fun Completable.subscribeOnIO(): Completable {
            return this.subscribeOn(Schedulers.io())
        }

        fun Completable.observeOnMain(): Completable {
            return this.observeOn(AndroidSchedulers.mainThread())
        }

        fun Completable.subscribeOnIOAndObserveOnMain(): Completable {
            return subscribeOnIO()
                .observeOnMain()
        }

        fun Completable.defaultSubscribe(onCallback: (Throwable?) -> Unit): Disposable {
            return this.subscribeOnIOAndObserveOnMain().subscribe({
                onCallback(null)
            }, { throwable ->
                onCallback(throwable)
            })
        }
    }
}