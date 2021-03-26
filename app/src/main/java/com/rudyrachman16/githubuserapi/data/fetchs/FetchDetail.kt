package com.rudyrachman16.githubuserapi.data.fetchs

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.rudyrachman16.githubuserapi.data.GetResult
import com.rudyrachman16.githubuserapi.data.models.DetailUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FetchDetail (private val getResult: GetResult<DetailUser>){
    fun getDetail(context : Context?, call : Call<DetailUser>){
        var result :DetailUser
        call.enqueue(object :Callback<DetailUser>{
            override fun onResponse(call: Call<DetailUser>, response: Response<DetailUser>) {
                if (!response.isSuccessful) {
                    Log.d("ERROR 404", "ERROR NOT SUCCESS")
                    Toast.makeText(
                        context,
                        "Error on Fetch Repos\nHTML${response.code()}",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }
                val body = response.body()!!
                result = body
                getResult.onSuccess(result)
            }

            override fun onFailure(call: Call<DetailUser>, t: Throwable) {
                Toast.makeText(
                    context,
                    "Error on Fetch Repos\n${t.message}",
                    Toast.LENGTH_LONG
                ).show()
                Log.d("ONFAILURE", "GAGAL TOTAL")
                t.printStackTrace()
            }
        })
    }
}