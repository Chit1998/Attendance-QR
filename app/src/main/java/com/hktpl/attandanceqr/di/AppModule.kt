package com.hktpl.attandanceqr.di

import android.util.Base64
import com.hktpl.attandanceqr.objects.TAG.getApi
import com.hktpl.attandanceqr.services.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val interceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient()
        .newBuilder()
        .addInterceptor(interceptor)
        .build()

    @Singleton
    @Provides
    fun getRetrofitServiceInstance(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(String(Base64.decode(getApi(),Base64.DEFAULT)))
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}