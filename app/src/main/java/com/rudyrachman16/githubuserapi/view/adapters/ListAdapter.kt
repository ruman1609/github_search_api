package com.rudyrachman16.githubuserapi.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rudyrachman16.githubuserapi.R
import com.rudyrachman16.githubuserapi.data.models.SearchUser
import com.rudyrachman16.githubuserapi.data.models.User
import com.rudyrachman16.githubuserapi.databinding.PerViewBinding

class ListAdapter(
    private val context: Context,
    private val clickListener: (user: User, binding: PerViewBinding) -> Unit
) : RecyclerView.Adapter<ListAdapter.ListHolder>() {
    class ListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val bind = PerViewBinding.bind(itemView)
        fun binding(user: SearchUser) {
            bind.username.text = user.username
            bind.id.text = user.id.toString()
            Glide.with(itemView.context)
                .load(user.picUrl)
                .apply(RequestOptions().override(100))
                .into(bind.imageView)
        }

        fun setLoading() {
            bind.loading.visibility = View.VISIBLE
        }
    }

    private val results = ArrayList<SearchUser>()
    fun setList(result: ArrayList<SearchUser>) {
        results.clear()
        results.addAll(result)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.per_view, parent, false)
        return ListHolder(view)
    }

    override fun onBindViewHolder(holder: ListHolder, position: Int) {
        val user = results[position]
        holder.binding(user)
        val userItem = User(user.id, user.username, user.picUrl)
        holder.itemView.setOnClickListener {
            holder.setLoading()
            clickListener(userItem, PerViewBinding.bind(holder.itemView))
        }
    }

    override fun getItemCount(): Int = results.size
}