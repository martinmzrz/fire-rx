# fire-rx  [![](https://jitpack.io/v/martinmzrz/fire-rx.svg)](https://jitpack.io/#martinmzrz/fire-rx)

Library for android to simplify the execution of RxJava2 observables.

```kotlin
class MainActivity : AppCompatActivity() {
    private val fireRx = FireRx()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Single.just("Hello Single").onSuccess { result ->
            Log.d(LOG_TAG, result)
        }.onFailure {
            Log.d(LOG_TAG, "code not executed")
        }.defaultSubscribe(fireRx)

        Single.error<String>(Exception("Your circuit's dead")).onSuccess {
            Log.d(LOG_TAG, "code not executed")
        }.onFailure {
            Log.e(LOG_TAG, "code executed", it)
        }.defaultSubscribe(fireRx)

        Completable.complete().onSuccess {
            Log.d(LOG_TAG, "Task completed")
        }.defaultSubscribe(fireRx)

        Flowable.interval(2, TimeUnit.SECONDS).onSuccess {
            Log.d(LOG_TAG, "Flowable -> $it")
        }.defaultSubscribe(fireRx)

        Observable.interval(2, TimeUnit.SECONDS).onSuccess {
            Log.d(LOG_TAG, "Observable -> $it")
        }.defaultSubscribe(fireRx)

        Maybe.just("Hello Maybe").onSuccess { result ->
            Log.d(LOG_TAG, result)
        }.defaultSubscribe(fireRx)
    }

    override fun onDestroy() {
        super.onDestroy()
        fireRx.dispose()
    }
}
```

The ```defaultSubscribe``` method subscribe on the IO thread and observe on the main thread.

## How to use
Add Jitpack in your root build.gradle

```groovy
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```
Add to your dependencies:
```groovy
dependencies {
    implementation 'com.github.martinmzrz:fire-rx:{Tag}'
}
```
