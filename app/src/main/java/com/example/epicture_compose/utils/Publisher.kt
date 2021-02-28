package com.example.epicture_compose.utils

import android.annotation.SuppressLint
import io.reactivex.subjects.PublishSubject


/**
 * Class to track state of T (success, error, loading)
 */
class Publisher<Output, Failure> {
    val successSubject: PublishSubject<Output> = PublishSubject.create()
    val errorSubject: PublishSubject<Failure> = PublishSubject.create()
    val loaderSubject: PublishSubject<Boolean> = PublishSubject.create()

    init {
        this.checkLoader()
    }

    /**
     * Check if the state of T is loading
     */
    @SuppressLint("CheckResult")
    private fun checkLoader() {
        errorSubject.subscribe(
                {
                    this.loaderSubject.onNext(false)
                },
                {
                    this.loaderSubject.onNext(false)
                }
        )

        successSubject.subscribe(
                {
                    this.loaderSubject.onNext(false)
                },
                {
                    this.loaderSubject.onNext(false)
                }
        )
    }
}