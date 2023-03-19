package com.example.mynotes.di

import com.example.mynotes.Api.AuthInterceptor
import com.example.mynotes.Api.NotesAPI
import com.example.mynotes.Api.UserAPI
import com.example.mynotes.Utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofitBuilder(): Retrofit.Builder{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)

    }

    @Singleton
    @Provides
    fun provideOkhttpClient(authInterceptor: AuthInterceptor): OkHttpClient{
        return OkHttpClient.Builder().addInterceptor(authInterceptor).build()
    }

    @Singleton
    @Provides
    fun provideUserAPI(retrofitBuilder : Retrofit.Builder): UserAPI{
        return retrofitBuilder.build().create(UserAPI::class.java)
    }

    @Singleton
    @Provides
    fun providesNotesAPI(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient):NotesAPI{
        return retrofitBuilder.client(okHttpClient).build().create(NotesAPI::class.java)
    }
}


