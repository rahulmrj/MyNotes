package com.example.mynotes.Api

import com.example.mynotes.models.UserRequest
import com.example.mynotes.models.UserResponce
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response as Retrofit2Response

interface UserAPI {

    @POST("/users/signup")
    suspend fun signup(@Body userRequest: UserRequest): Retrofit2Response<UserResponce>

    @POST("/users/signin")
    suspend fun signin(@Body userRequest: UserRequest): Retrofit2Response<UserResponce>
}