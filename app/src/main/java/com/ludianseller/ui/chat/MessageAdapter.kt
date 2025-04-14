package com.ludianseller.ui.chat

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ludianseller.R


class MessageAdapter(
    private val mContext: Context,
    private var msgList: ArrayList<MessageModel>?
) : RecyclerView.Adapter<MessageAdapter.TransViewHolder>() {
  //  lateinit var sharedPref: SharedPref

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransViewHolder {
       // sharedPref = SharedPref(mContext)
        val layoutResId = if (viewType == 0) R.layout.incoming_message_layout else R.layout.outgoing_message_layout
        val view = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        Log.e("TAG", "getItemViewType:  viewType-- $viewType")
        return TransViewHolder(view)
    }
    override fun getItemViewType(position: Int): Int {
       //val message = msgList?.get(position)
      //  sharedPref = SharedPref(mContext)
       // Log.e("TAG", "getItemViewType: senderId-- "+message?.senderId )
     //   Log.e("TAG", "getItemViewType:  USER_ID -- "+sharedPref.getStringValue(Constant.USER_ID))
     //   return if (message?.senderId==sharedPref.getStringValue(Constant.USER_ID)) 1 else 0
        return if(position %2 ==0) 0 else 1
    }
    override fun onBindViewHolder(holder: TransViewHolder, position: Int) {

        var data: MessageModel = msgList!!.get(position)
        try {
            holder.bind(data)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return if (msgList == null) 0 else msgList!!.size
    }

    class TransViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView){
        val messageTextView = if (itemViewType == 0) itemView.findViewById<TextView>(R.id.userTextSHow) else itemView.findViewById<TextView>(R.id.userTextSHow)
        val timeTextView = if (itemViewType == 0) itemView.findViewById<TextView>(R.id.usertime) else itemView.findViewById<TextView>(R.id.usertime)
        fun bind(message: MessageModel) {
            messageTextView.text = message.chatMsg
            timeTextView.text = message.timeAgo
            // bind other views as necessary
        }
    }
}