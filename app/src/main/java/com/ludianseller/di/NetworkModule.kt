package com.ludianseller.di


import com.ludianseller.api.AuthInterceptor
import com.ludianseller.api.SellerAPI
import com.ludianseller.api.SellerDataApi
import com.ludianseller.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(interceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    @Singleton
    @Provides
    fun providesUserAPI(retrofitBuilder: Retrofit.Builder): SellerAPI {
        return retrofitBuilder.build().create(SellerAPI::class.java)
    }


    @Singleton
    @Provides
    fun providesUserDataAPI(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): SellerDataApi {
        return retrofitBuilder.client(okHttpClient).build().create(SellerDataApi::class.java)
    }


}