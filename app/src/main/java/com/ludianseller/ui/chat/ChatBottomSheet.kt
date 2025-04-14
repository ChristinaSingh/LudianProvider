package com.ludianseller.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ludianseller.R

class ChatBottomSheet :  BottomSheetDialogFragment() {
    private lateinit var bottomSheetView : View
    private lateinit var messageAdapter: MessageAdapter
    private var msgArrayList : ArrayList<MessageModel>? =null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bottomSheetView = inflater.inflate(R.layout.bottomsheet_chat, container, false)

        return  bottomSheetView


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvChatList = bottomSheetView.findViewById<RecyclerView>(R.id.rvChatList)
       // val ivClose = bottomSheetView.findViewById<ImageView>(R.id.ivClose)


        msgArrayList = ArrayList()
        msgArrayList!!.add(MessageModel("Hi Jake, how are you? I saw on the app that we’ve crossed paths several times this week","02:55 PM"))
        msgArrayList!!.add(MessageModel("Haha truly! Nice to meet you Grace! What about a cup of coffee today evening?","03:02 PM"))
        msgArrayList!!.add(MessageModel("Sure, let’s do it!","03:10 PM"))
        msgArrayList!!.add(MessageModel("Great I will write later the exact time and place. See you soon!","03:12 PM"))


        messageAdapter = MessageAdapter(requireActivity(),msgArrayList!!)
        rvChatList.adapter = messageAdapter




    }


}