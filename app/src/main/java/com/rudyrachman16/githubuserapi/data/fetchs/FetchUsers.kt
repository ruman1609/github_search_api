package com.rudyrachman16.githubuserapi.data.fetchs

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.rudyrachman16.githubuserapi.data.GetResult
import com.rudyrachman16.githubuserapi.data.models.SearchUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FetchUsers(private val getResult: GetResult<ArrayList<SearchUser>>){
    fun getUser(context : Context?, call : Call<ArrayList<SearchUser>>){
        val result = ArrayList<SearchUser>()
        call.enqueue(object : Callback<ArrayList<SearchUser>>{
            override fun onResponse(
                call: Call<ArrayList<SearchUser>>,
                response: Response<ArrayList<SearchUser>>
            ) {
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
                result.addAll(body)
                getResult.onSuccess(result)
            }

            override fun onFailure(call: Call<ArrayList<SearchUser>>, t: Throwable) {
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