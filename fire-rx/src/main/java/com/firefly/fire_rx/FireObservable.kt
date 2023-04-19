package com.firefly.fire_rx

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class FireObservable<T>(private val observable: Observable<T>) : FireDisposable {
    private var onNext: ((T) -> Unit)? = null
    override var failureCallback: ((Throwable) -> Unit)? = null

    override fun execute(subscribeOn: Scheduler, observeOn: Scheduler): Disposable {
        return observable.subscribeOn(subscribeOn)
            .observeOn(observeOn)
            .subscribe(
                {
                    onNext?.invoke(it)
                }, {
                    failureCallback?.invoke(it)
                }
            )
    }

    companion object {
        fun <T> Observable<T>.onSuccess(onNextCallback: ((T) -> Unit)): FireObservable<T> {
            return FireObservable(this).apply {
                this.onNext = onNextCallback
            }
        }

        fun <T> Observable<T>.onFailure(failureCallback: ((Throwable) -> Unit)): FireObservable<T> {
            return FireObservable(this).apply {
                this.failureCallback = failureCallback
            }
        }

        fun <T> FireObservable<T>.onSuccess(onNextCallback: ((T) -> Unit)): FireObservable<T> {
            return this.apply {
                this.onNext = onNextCallback
            }
        }

        fun <T> Observable<T>.subscribeOnMain(): Observable<T> {
            return this.subscribeOn(AndroidSchedulers.mainThread())
        }

        fun <T> Observable<T>.subscribeOnIO(): Observable<T> {
            return this.subscribeOn(Schedulers.io())
        }

        fun <T> Observable<T>.observeOnMain(): Observable<T> {
            return this.observeOn(AndroidSchedulers.mainThread())
        }

        fun <T> Observable<T>.subscribeOnIOAndObserveOnMain(): Observable<T> {
            return this.subscribeOnIO()
                .observeOnMain()
        }

        fun <T> Observable<T>.defaultSubscribe(onCallback: ((T?, Throwable?) -> Unit)): Disposable {
            return this.subscribeOnIOAndObserveOnMain()
                .subscribe({
                    onCallback.invoke(it, null)
                }, {
                    onCallback.invoke(null, it)
                })
        }
    }
}