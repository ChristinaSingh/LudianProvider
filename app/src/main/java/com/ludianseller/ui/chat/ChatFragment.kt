package com.ludianseller.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ludianseller.databinding.FragmentChatBinding

class ChatFragment : Fragment(), ChatAdapter.OnChaListener {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    private fun initViews() {

        chatAdapter = ChatAdapter(requireActivity(),this@ChatFragment)
        binding.rvChat.adapter = chatAdapter
    }

    override fun onChat(position: Int) {
        val chatBottomSheet = ChatBottomSheet()
        chatBottomSheet.show(childFragmentManager, "")
    }


}