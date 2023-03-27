# fire-rx  [![](https://jitpack.io/v/martinmzrz/fire-rx.svg)](https://jitpack.io/#martinmzrz/fire-rx)

Library for android to simplify the execution of RxJava2 observables.

```kotlin
class MainActivity : AppCompatActivity() {
    private val fireRx = FireRx()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Single.just("Hello World").onSuccess { result ->
            Log.d("Fire-Rx", result)
        }.onFailure {
            Log.d("Fire-Rx", "code not executed")
        }.defaultSubscribe(fireRx)

        Single.error<String>(Exception("Your circuit's dead")).onSuccess {
            Log.d("Fire-Rx", "code not executed")
        }.onFailure {
            Log.e("Fire-Rx", "code executed", it)
        }.defaultSubscribe(fireRx)

        Completable.complete().onSuccess {
            Log.d("Fire-Rx", "Task completed")
        }.defaultSubscribe(fireRx)
    }

    override fun onDestroy() {
        super.onDestroy()
        fireRx.dispose()
    }
}
```

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
