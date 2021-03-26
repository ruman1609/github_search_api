package com.rudyrachman16.githubuserapi.data.models

import android.content.Context
import android.os.Parcelable
import com.rudyrachman16.githubuserapi.data.APICall
import com.rudyrachman16.githubuserapi.data.GetResult
import com.rudyrachman16.githubuserapi.data.fetchs.FetchDetail
import com.rudyrachman16.githubuserapi.data.fetchs.FetchUsers
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Int,
    val username: String,
    val picUrl: String
) : Parcelable {
    @IgnoredOnParcel
    private val followers: ArrayList<SearchUser> = ArrayList()

    @IgnoredOnParcel
    private val following: ArrayList<SearchUser> = ArrayList()

    @IgnoredOnParcel
    private var detailUser: DetailUser? = null

    fun getFollowers(context: Context?, callback :(data :ArrayList<SearchUser>) -> Unit) {
        if(followers.isEmpty()){
            FetchUsers(object : GetResult<ArrayList<SearchUser>> {
                override fun onSuccess(result: ArrayList<SearchUser>) {
                    followers.addAll(result)
                    callback(result)
                }
            }).getUser(context, APICall.apiReq.getFollowers(username))
        } else callback(followers)
    }

    fun getFollowing(context: Context?, callback :(data :ArrayList<SearchUser>) -> Unit) {
        if (following.isEmpty()) {
            FetchUsers(object : GetResult<ArrayList<SearchUser>> {
                override fun onSuccess(result: ArrayList<SearchUser>) {
                    following.addAll(result)
                    callback(result)
                }
            }).getUser(context, APICall.apiReq.getFollowing(username))
        } else callback(following)
    }

    fun getDetail(context: Context?, callback :(data :DetailUser) -> Unit) {
        if(detailUser == null){
            FetchDetail(object : GetResult<DetailUser> {
                override fun onSuccess(result: DetailUser) {
                    detailUser = result
                    callback(detailUser!!)
                }
            }).getDetail(context, APICall.apiReq.getDetail(username))
        } else callback(detailUser!!)

    }
}