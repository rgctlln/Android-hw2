package com.example.apigrabber

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("random")
    suspend fun getRandomGif(
        @Query("key") apiKey: String,
        @Query("q") query: String,
        @Query("limit") limit: Int
    ): TenorApiResponse
}

data class TenorApiResponse(
    val results: List<TenorGif>
)

data class TenorGif(
    val media: List<TenorMedia>
)

data class TenorMedia(
    val gif: TenorGifUrl
)

data class TenorGifUrl(
    val url: String
)