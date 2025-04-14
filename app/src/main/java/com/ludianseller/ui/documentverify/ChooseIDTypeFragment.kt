package com.ludianseller.ui.documentverify

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.gson.Gson
import com.ludianseller.R
import com.ludianseller.databinding.FragmentChooseIdTypeBinding
import com.ludianseller.models.CalenderPropertyModel
import com.ludianseller.models.CountryModel
import com.ludianseller.models.DocumentTypeModel
import com.ludianseller.models.PlaceTypeModel
import com.ludianseller.ui.home.SellerDataViewModel
import com.ludianseller.utils.Constants
import com.ludianseller.utils.Helper
import com.ludianseller.utils.NetworkResult
import com.ludianseller.utils.SharedPrf
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class ChooseIDTypeFragment : Fragment(), DocumentListAdapter.OnDocumentTypeListener {

    private var _binding: FragmentChooseIdTypeBinding? = null
    private val binding get() = _binding!!
    private lateinit var sellerDataViewModel: SellerDataViewModel
    private var countryArrayList: ArrayList<CountryModel.Country>? = null

    private lateinit var documentListAdapter: DocumentListAdapter
    private var documentArrayList: ArrayList<DocumentTypeModel.Documents>? = null
    private var countryId: String?=null
    private var documentTypeId: String?=null
    private var documentName: String?=null
    private val sharedPrf by lazy { SharedPrf(requireActivity()) }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChooseIdTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sellerDataViewModel = ViewModelProvider(this).get(SellerDataViewModel::class.java)

        initViews()
    }

    private fun initViews() {
        countryArrayList = ArrayList()
        documentArrayList = ArrayList()


       documentListAdapter = DocumentListAdapter(requireActivity(),documentArrayList,this@ChooseIDTypeFragment)
       binding.rvDocumentType.adapter = documentListAdapter


        binding.ivBack.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }


        binding.rlMain2.setOnClickListener {
            showDropDownMenu(it,binding.tvName,countryArrayList!!)
        }



        binding.btnContinue.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("countryId",countryId)
            bundle.putString("documentTypeId",documentTypeId)
            bundle.putString("documentName",documentName)

            Navigation.findNavController(binding.root)
                .navigate(R.id.action_chooseIDTypeFragment_to_uploadDocumentFragment,bundle)

        }


        bindObservers()

        Helper.showProgressMessage(requireActivity(), getString(R.string.please_wait))
        sellerDataViewModel.getCountry(sharedPrf.getStoredTag(Constants.USER_TOKEN))


    }



    private fun bindObservers() {
        sellerDataViewModel.countryLiveData.observe(viewLifecycleOwner, Observer { it ->
            Helper.hideProgressMessage()
            when (it) {
                is NetworkResult.Success -> {
                    it.data?.let {
                        val jsonObject = JSONObject(it.string())
                        Log.e("get Country List Response===", jsonObject.toString())
                        if (jsonObject.getInt("status") == 1) {
                            val countryModel: CountryModel = Gson().fromJson(
                                jsonObject.toString(),
                                CountryModel::class.java
                            )
                            countryArrayList!!.clear()
                            countryArrayList!!.addAll(countryModel.result)
                            countryId = countryArrayList!![98].id
                            binding.tvName.text = countryArrayList!![98].nicename

                            Helper.showProgressMessage(requireActivity(), getString(R.string.please_wait))
                            sellerDataViewModel.getDocumentRepo(sharedPrf.getStoredTag(Constants.USER_TOKEN),countryArrayList!![98].iso)

                            sellerDataViewModel.clearCountryData()
                        } else {
                            countryArrayList!!.clear()
                            Toast.makeText(
                                requireActivity(),
                                "" + jsonObject.getString("message"),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        //  Log.e("TAG", "observers: $it.")
                    }


                }

                is NetworkResult.Error -> {
                    showValidationErrors(it.message.toString())
                }

                is NetworkResult.Loading -> {
                    Helper.showProgressMessage(requireActivity(), getString(R.string.please_wait))
                }

                else -> {}
            }
        })


        sellerDataViewModel.documentLiveData.observe(viewLifecycleOwner, Observer { it ->
            Helper.hideProgressMessage()
            when (it) {
                is NetworkResult.Success -> {
                    it.data?.let {
                        val jsonObject = JSONObject(it.string())
                        Log.e("document type Response===", jsonObject.toString())
                        if (jsonObject.getInt("status") == 1) {
                            val documentTypeModel: DocumentTypeModel = Gson().fromJson(
                                jsonObject.toString(),
                                DocumentTypeModel::class.java
                            )
                            documentArrayList!!.clear()
                            documentArrayList!!.addAll(documentTypeModel.result)
                            documentListAdapter.notifyAdapter(documentArrayList!!)


                            sellerDataViewModel.clearDocumentData()
                        } else {
                            documentArrayList!!.clear()
                            documentListAdapter.notifyAdapter(documentArrayList!!)
                            //   binding.shimmerLayout.stopShimmer();
                            //   binding.shimmerLayout.visibility = View.GONE;
                            Toast.makeText(
                                requireActivity(),
                                "" + jsonObject.getString("message"),
                                Toast.LENGTH_SHORT
                            ).show()
                        }                        //  Log.e("TAG", "observers: $it.")
                    }


                }

                is NetworkResult.Error -> {
                    showValidationErrors(it.message.toString())
                }

                is NetworkResult.Loading -> {
                    Helper.showProgressMessage(requireActivity(), getString(R.string.please_wait))
                }

                else -> {}
            }
        })




    }

    private fun showValidationErrors(error: String) {
        val text = String.format(resources.getString(R.string.txt_error_message, error))
        Toast.makeText(requireActivity(), text, Toast.LENGTH_SHORT).show()
    }


    private fun showDropDownMenu(
        v: View,
        textView: TextView,
        stringList: List<CountryModel.Country>,
    ) {
        val popupMenu = PopupMenu(requireActivity(), v)
        for (i in stringList.indices) {
            popupMenu.menu.add(stringList[i].nicename)
        }

        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            for (i in stringList.indices) {
                if (stringList[i].nicename == menuItem.title.toString()) {
                    // l1 =stringList[i]
                    textView.text = menuItem.title
                    //stateString = menuItem.title.toString()
                   // arrayList!![position].state = stateString
                    countryId = stringList[i].id
                    documentName = stringList[i].nicename

                    Log.e("select country====",i.toString()+"====" +countryArrayList!![i].iso)

                    Helper.showProgressMessage(requireActivity(), getString(R.string.please_wait))
                    sellerDataViewModel.getDocumentRepo(sharedPrf.getStoredTag(Constants.USER_TOKEN),countryArrayList!![i].iso)
                }
            }
            true
        }
        popupMenu.show()
    }

    override fun onDocumentType(
        list: ArrayList<DocumentTypeModel.Documents>,
        position: Int
    ) {
        documentArrayList = list
        documentListAdapter.notifyAdapter(documentArrayList!!)
        documentTypeId = list[position].id
    }

}