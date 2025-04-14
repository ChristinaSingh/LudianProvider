package com.ludianseller.ui.booking.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ludianseller.R
import com.ludianseller.models.PropertyModel


class ImageSliderAdapter2(private val mContext : Context, private val images: List<String>) : RecyclerView.Adapter<ImageSliderAdapter2.ImageViewHolder2>() {

    inner class ImageViewHolder2(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder2 {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image_slider, parent, false)
        return ImageViewHolder2(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder2, position: Int) {
        //holder.imageView.setImageResource(images[position])

        Glide.with(mContext)
            .load(images[position])
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(com.denzcoskun.imageslider.R.drawable.default_loading)
            .error(com.denzcoskun.imageslider.R.drawable.default_error)
            .centerInside()
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return images.size
    }
}