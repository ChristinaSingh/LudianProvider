package com.ludianseller.ui.documentverify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.ludianseller.R
import com.ludianseller.databinding.FragmentKeyStoreBinding

class KeyStoreFragment : Fragment() {

    private var _binding: FragmentKeyStoreBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentKeyStoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
      // binding.tvName.text = arguments?.getString("name")
    //   binding.tvAddress.text = arguments?.getString("address")
   //     Glide.with(requireActivity()).load(arguments?.getString("image")).override(50,50).into(binding.ivImg2)


        binding.ivBack.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }


        binding.btnPublish.setOnClickListener {
           /* Navigation.findNavController(binding.root)
                .navigate(R.id.action_ketStoreFragment_to_chooseIDTypeFragment)*/

            Navigation.findNavController(binding.root)
                .navigate(R.id.action_ketStoreFragment_to_uploadDocumentFragment)

        }

    }
}