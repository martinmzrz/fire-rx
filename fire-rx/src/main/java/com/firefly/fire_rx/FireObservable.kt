package com.firefly.fire_rx

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class FireObservable<T : Any>(private val observable: Observable<T>) : FireDisposable {
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
        fun <T : Any> Observable<T>.onSuccess(onNextCallback: ((T) -> Unit)): FireObservable<T> {
            return FireObservable(this).apply {
                this.onNext = onNextCallback
            }
        }

        fun <T : Any> Observable<T>.onFailure(failureCallback: ((Throwable) -> Unit)): FireObservable<T> {
            return FireObservable(this).apply {
                this.failureCallback = failureCallback
            }
        }

        fun <T : Any> FireObservable<T>.onSuccess(onNextCallback: ((T) -> Unit)): FireObservable<T> {
            return this.apply {
                this.onNext = onNextCallback
            }
        }

        fun <T : Any> Observable<T>.subscribeOnMain(): Observable<T> {
            return this.subscribeOn(AndroidSchedulers.mainThread())
        }

        fun <T : Any> Observable<T>.subscribeOnIO(): Observable<T> {
            return this.subscribeOn(Schedulers.io())
        }

        fun <T : Any> Observable<T>.observeOnMain(): Observable<T> {
            return this.observeOn(AndroidSchedulers.mainThread())
        }

        fun <T : Any> Observable<T>.subscribeOnIOAndObserveOnMain(): Observable<T> {
            return this.subscribeOnIO()
                .observeOnMain()
        }

        fun <T : Any> Observable<T>.defaultSubscribe(onCallback: ((T?, Throwable?) -> Unit)): Disposable {
            return this.subscribeOnIOAndObserveOnMain()
                .subscribe({
                    onCallback.invoke(it, null)
                }, {
                    onCallback.invoke(null, it)
                })
        }
    }
}