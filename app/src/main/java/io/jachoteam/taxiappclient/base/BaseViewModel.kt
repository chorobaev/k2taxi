package io.jachoteam.taxiappclient.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

abstract class BaseViewModel : ViewModel() {

    protected var errorListener: OnErrorListener? = null
        private set
    private var compositeDisposable: CompositeDisposable? = CompositeDisposable()
    protected val mLoading = MutableLiveData<Boolean>()

    val loading: LiveData<Boolean> get() = mLoading

    fun setOnErrorListener(onErrorListener: OnErrorListener?) {
        this.errorListener = onErrorListener
    }

    protected fun <T> Single<T>.request(
        delay: Long = 0,
        shouldShowLoading: Boolean = true,
        block: (T) -> Unit = {}
    ): Single<T> {
        if (shouldShowLoading) {
            mLoading.value = true
        }
        compositeDisposable!!.add(
            this
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .delaySubscription(delay, TimeUnit.MILLISECONDS)
                .subscribeWith(object : DisposableSingleObserver<T>() {
                    override fun onSuccess(t: T) {
                        block(t)
                        mLoading.value = false
                    }

                    override fun onError(e: Throwable) {
                        errorListener?.onError(e)
                        mLoading.value = false
                        e.printStackTrace()
                    }
                })
        )
        return this
    }

    protected fun cancelRequests() {
        compositeDisposable?.clear()
    }

    override fun onCleared() {
        compositeDisposable?.clear()
        compositeDisposable = null
        super.onCleared()
    }
}