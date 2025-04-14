package com.ludianseller.ui.post.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ludianseller.R
import com.ludianseller.databinding.ItemImageBinding



class ImageAdapter (private val mContext: Context) : RecyclerView.Adapter<ImageAdapter.MyViewHolder>() {

    private var itemCount: Int = 0

    fun setItemCount(count: Int) {
        itemCount = count
        notifyItemChanged(itemCount)
    }

    class MyViewHolder(var binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemImageBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext), R.layout.item_image, parent, false
        )
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemCount
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
              fadeInImage(holder.binding.imageView)
    }

    private fun fadeInImage(imageView : ImageView) {
        imageView.apply {
            // Make sure image view is initially invisible
            visibility = ImageView.VISIBLE
            imageView.setImageResource(R.drawable.ic_mensss)

            // Fade in animation
            alpha = 0f
            animate()
                .alpha(1f)
                .setDuration(1000) // Animation duration in milliseconds
                .setListener(null) // Optional listener for animation events
        }
    }


}
