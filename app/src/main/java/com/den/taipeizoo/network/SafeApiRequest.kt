package com.den.taipeizoo.network

import retrofit2.Response
import java.io.IOException

abstract class SafeApiRequest {

    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): T {
        val response = call.invoke()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            return body
        } else {
            throw ApiException(response.code().toString())
        }
    }
}

class ApiException(message: String) : IOException(message)