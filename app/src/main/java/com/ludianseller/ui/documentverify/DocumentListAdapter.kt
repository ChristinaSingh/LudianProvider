package com.ludianseller.ui.documentverify

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ludianseller.R
import com.ludianseller.databinding.ItemDocumentTypeBinding
import com.ludianseller.models.DocumentTypeModel



class DocumentListAdapter (
    private val mContext: Context,
    var arrayList: ArrayList<DocumentTypeModel.Documents>?
    , val listener : OnDocumentTypeListener
) : RecyclerView.Adapter<DocumentListAdapter.MyViewHolder>() {



    class MyViewHolder(var binding: ItemDocumentTypeBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemDocumentTypeBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext), R.layout.item_document_type, parent, false
        )
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arrayList!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.tvNameType.text = arrayList!![position].name
        if(arrayList!![position].check) holder.binding.ivSelect.setImageResource(R.drawable.ic_select_radio)
        else holder.binding.ivSelect.setImageResource(R.drawable.ic_unselect_radio)

        holder.itemView.setOnClickListener {
            for (i in 0 until arrayList!!.size) {
                arrayList!![i].check = false
            }
            arrayList!![position].check = true
            listener.onDocumentType(arrayList!!,position)
        }
    }

    interface   OnDocumentTypeListener{
        fun onDocumentType(propertyList: ArrayList<DocumentTypeModel.Documents>, position: Int)

    }


    fun notifyAdapter(propertyList: ArrayList<DocumentTypeModel.Documents>) {
        arrayList = propertyList
        notifyDataSetChanged()
    }


}