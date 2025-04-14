package com.ludianseller.ui.post.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.ludianseller.R
import com.ludianseller.databinding.FragmentStartBinding

class StartFragment : Fragment() {

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {

        binding.ivBack.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }



        binding.btnStart.setOnClickListener {
               Navigation.findNavController(binding.root)
                   .navigate(R.id.action_startFragment_to_describePlaceFragment)

        }
    }



}