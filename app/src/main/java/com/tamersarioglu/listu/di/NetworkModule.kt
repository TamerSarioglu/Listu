package com.tamersarioglu.listu.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.tamersarioglu.listu.data.remote.api.animedetailservice.AnimeDetailService
import com.tamersarioglu.listu.data.remote.api.searchanimeservice.SearchAnimeService
import com.tamersarioglu.listu.data.remote.api.topanimeservice.TopAnimeService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json { ignoreUnknownKeys = true }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()


    @Provides
    @Singleton
    fun provideRetrofit(json: Json, client: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl("https://api.jikan.moe/v4/")
            .addConverterFactory(
                json.asConverterFactory(contentType)
            )
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideTopAnimeService(retrofit: Retrofit): TopAnimeService =
        retrofit.create(TopAnimeService::class.java)

    @Provides
    @Singleton
    fun provideAnimeDetailService(retrofit: Retrofit): AnimeDetailService =
        retrofit.create(AnimeDetailService::class.java)

    @Provides
    @Singleton
    fun provideSearchAnimeService(retrofit: Retrofit): SearchAnimeService =
        retrofit.create(SearchAnimeService::class.java)
}