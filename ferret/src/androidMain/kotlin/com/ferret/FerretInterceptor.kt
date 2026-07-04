package com.ferret

import okhttp3.Interceptor
import okhttp3.Response

class FerretInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        return response
    }
}