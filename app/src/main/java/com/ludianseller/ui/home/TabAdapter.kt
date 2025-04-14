package com.ludianseller.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ludianseller.R
import com.ludianseller.databinding.ItemTabBinding



class TabAdapter (
    private val mContext: Context,
    private var arrayList: ArrayList<String>?
    /*, private var listener : OnPropertyListener*/
) : RecyclerView.Adapter<TabAdapter.MyViewHolder>() {



    class MyViewHolder(var binding: ItemTabBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemTabBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext), R.layout.item_tab, parent, false
        )
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arrayList!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.tvName.text = arrayList!![position] + " (0)"



     /*   holder.itemView.setOnClickListener {
            listener.onPropertyClick(arrayList!![position],position,"")
        }*/
    }

   /* interface OnPropertyListener {
        fun onPropertyClick(model: PropertyModel.Property, position: Int, type: String)

    }*/

    fun notifyAdapter(propertyList: ArrayList<String>) {
        arrayList = propertyList
        notifyDataSetChanged()
    }

}