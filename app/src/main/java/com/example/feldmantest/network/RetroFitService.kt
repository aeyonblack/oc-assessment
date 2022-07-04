package com.example.feldmantest.network

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object RetroFitService {
    // create the retrofit builder object with null default (so as to not recreate it every time getService is called)
    private var retrofit: Retrofit? = null

    // create the okHttpClient object with the connection settings
    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .readTimeout(120, TimeUnit.SECONDS)
        .connectTimeout(240, TimeUnit.SECONDS)
        .connectionPool(ConnectionPool(10,2, TimeUnit.MINUTES))
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        // .retryOnConnectionFailure(false)
        .build()

    // create the retrofit builder object
    private val builder: Retrofit.Builder = Retrofit.Builder()
        .baseUrl("https://jsonplaceholder.typicode.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .addConverterFactory(ScalarsConverterFactory.create())
        //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())   ////// for supporting rx java in api call

    // make an observable subscribe on the IO thread and observe on the main thread
    fun <T> useNetwork(observable: Observable<T>) : Observable<T> = observable
        .subscribeOn(Schedulers.io()) // Retrofit2 works on the main thread by default
        .observeOn(AndroidSchedulers.mainThread())

    // get the service object from the retrofit build
    fun getService(): AppApi {
        if (retrofit == null) {
            okHttpClient.dispatcher.maxRequestsPerHost = 16
            retrofit = builder.client(okHttpClient).build()
        }

        return retrofit!!.create(AppApi::class.java)
    }
}