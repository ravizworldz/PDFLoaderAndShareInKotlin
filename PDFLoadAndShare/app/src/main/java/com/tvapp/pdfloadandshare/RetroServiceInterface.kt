package com.tvapp.pdfloadandshare

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface RetroServiceInterface {

    @GET
    fun downloadPdfFile(@Url pdfUrl: String): Call<ResponseBody>
}