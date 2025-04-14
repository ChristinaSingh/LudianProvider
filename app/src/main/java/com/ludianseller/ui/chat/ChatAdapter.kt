package com.ludianseller.ui.chat

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ludianseller.R
import com.ludianseller.databinding.ItemChatBinding


class ChatAdapter (
    private val mContext: Context/*,
                          var arrayList: ArrayList<String>?,*/
       , val listener : OnChaListener
) : RecyclerView.Adapter<ChatAdapter.MyViewHolder>() {



    class MyViewHolder(var binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemChatBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext), R.layout.item_chat, parent, false
        )
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.binding.chk.text = arrayList!!.get(position)
        holder.itemView.setOnClickListener {
            listener.onChat(position)
        }
    }

    interface   OnChaListener{
        fun onChat(/*model: MessageModel,*/ position: Int)

    }

}