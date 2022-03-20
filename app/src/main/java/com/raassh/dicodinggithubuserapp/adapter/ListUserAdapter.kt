package com.raassh.dicodinggithubuserapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raassh.dicodinggithubuserapp.databinding.ItemUserDetailBinding
import com.raassh.dicodinggithubuserapp.data.UserItem
import com.raassh.dicodinggithubuserapp.data.UserRepository

class ListUserAdapter(private val listUser: ArrayList<UserItem>) :
    RecyclerView.Adapter<ListUserAdapter.ViewHolder>() {
    private var onItemClickCallback: OnItemClickCallback? = null

    interface OnItemClickCallback {
        fun onItemClicked(user: UserItem)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ViewHolder(val binding: ItemUserDetailBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemUserDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (username, avatar) = listUser[position]

        holder.binding.apply {
            Glide.with(holder.itemView.context)
                .load(avatar)
                .circleCrop()
                .into(photo)
            tvItemUsername.text = username
        }

        holder.itemView.setOnClickListener {
            onItemClickCallback?.onItemClicked(listUser[holder.adapterPosition])
        }
    }

    override fun getItemCount() = listUser.size

}