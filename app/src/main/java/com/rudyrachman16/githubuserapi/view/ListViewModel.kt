package com.rudyrachman16.githubuserapi.view

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rudyrachman16.githubuserapi.data.APICall
import com.rudyrachman16.githubuserapi.data.models.SearchUser
import com.rudyrachman16.githubuserapi.data.models.Searches
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListViewModel : ViewModel() {
    private val list = MutableLiveData<ArrayList<SearchUser>>()
    private var callTemp: Call<Searches>? = null
    private var tempText: String? = null

    fun setList(context: Context, newText: String) {
        val call = APICall.apiReq.getSearch(newText)
        tempText = newText
        if (callTemp != null) callTemp?.cancel()
        if (newText != "") {
            callTemp = call
            call.enqueue(object : Callback<Searches> {
                override fun onResponse(
                    call: Call<Searches>,
                    response: Response<Searches>
                ) {
                    if (!response.isSuccessful) {
                        Toast.makeText(
                            context,
                            "Error on Fetch Repos\nHTML${response.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                        return
                    }
                    val results = response.body()!!.items
                    list.postValue(results)
                }

                override fun onFailure(call: Call<Searches>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
    }

    fun getList(): LiveData<ArrayList<SearchUser>> = list
    fun getTemp() = tempText
}