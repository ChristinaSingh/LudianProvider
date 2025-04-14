package com.ludianseller.ui.post.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ludianseller.R
import com.ludianseller.databinding.ItemPhotosBinding
import com.ludianseller.models.AmenitiesModel


class PhotosAdapter (
    private val mContext: Context,
                          var arrayList: ArrayList<Bitmap>?
    , val listener : OnPhotoListener
) : RecyclerView.Adapter<PhotosAdapter.MyViewHolder>() {



    class MyViewHolder(var binding: ItemPhotosBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemPhotosBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext), R.layout.item_photos, parent, false
        )
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arrayList!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.ivImg.setImageBitmap(arrayList!![position])

        holder.binding.imageOptions.setOnClickListener {
            showPopupMenu(holder.binding.imageOptions,position)
        }
    }


    fun notifyAdapter(photoArrayList: ArrayList<Bitmap>) {
        arrayList = photoArrayList
        notifyDataSetChanged()
    }



    interface   OnPhotoListener{
          fun onPhoto( position: Int,type:String)

    }


    private fun showPopupMenu(view: View,position: Int) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.inflate(R.menu.popup_menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_edit -> {
                    // Handle edit click
                   listener.onPhoto(position,"Edit")
                    true
                }
                R.id.menu_delete -> {
                    // Handle delete click
                    listener.onPhoto(position,"Delete")

                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }



}