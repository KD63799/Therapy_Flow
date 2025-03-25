package com.example.therapy_flow.di


import android.content.Context
import android.content.SharedPreferences
import com.example.therapy_flow.network.ApiRoutes
import com.example.therapy_flow.network.ApiService
import com.example.therapy_flow.utils.PREFS_FILE
import com.example.therapy_flow.utils.PreferencesManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAuthInterceptor(sharedPreferences: PreferencesManager): Interceptor {
        return Interceptor { chain ->
            val token = sharedPreferences.authToken ?: ""
            val requestBuilder = chain.request().newBuilder()
                .addHeader("apikey", ApiRoutes.API_KEY)
                .addHeader("Content-Type", "application/json")

            if (token.isNotEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }

            val request = requestBuilder.build()
            println(" Headers envoyés: ${request.headers}")

            chain.proceed(request)
        }
    }


    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    // Configure OkHttpClient avec les Interceptors
    @Singleton
    @Provides
    fun provideOkHttpClient(
        authInterceptor: Interceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    // Configure Moshi
    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    // Configure Retrofit
    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(ApiRoutes.BASE_URL)
            .client(okHttpClient)
            .build()
    }

    // Fournit l'instance de l'API
    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    // Fournit l'accès au sharedPreferences
    @Singleton
    @Provides
    fun provideSharedPref(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE)
}