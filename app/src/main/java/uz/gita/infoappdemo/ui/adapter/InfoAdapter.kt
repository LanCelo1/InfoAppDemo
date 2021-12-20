package uz.gita.infoappdemo.ui.adapter

import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.gita.infoappdemo.data.model.Data
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import uz.gita.infoappdemo.R
import uz.gita.infoappdemo.databinding.ItemLayout2Binding
import uz.gita.infoappdemo.databinding.ItemLayoutBinding


class InfoAdapter(val metric : DisplayMetrics? = null) : PagingDataAdapter<Data, InfoAdapter.VH>(diffUtilCallback) {

    private var onClickItemListener : ((Data) -> Unit)? = null

    fun onClickItemListener(block : ((Data) -> Unit)){
        onClickItemListener = block
    }

    private var haveElementListener : (() -> Unit)? = null

    fun haveElementListener(block : (() -> Unit)){
        haveElementListener = block
    }

    inner class VH(val binding : ItemLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: Data){
            binding.apply {
                val item = getItem(adapterPosition)


                val llImg = itemView.findViewById<View>(R.id.container) as CardView

//                llImg.getLayoutParams().width = (getScreenWidth(itemView.context) )
//                llImg.getLayoutParams().height = (getScreenHeight(itemView.context) / 3)

                llImg.layoutParams.width = (metric?.widthPixels!! * 0.95f).toInt()
                llImg.layoutParams.height = (metric?.heightPixels!! / 3)
                var imgView = itemView.findViewById<View>(R.id.ivArticleImage) as ImageView
                tvTitle.text = data.name
                tvDescription.text = data.objType
                tvSource.text = data.startDate
                tvPublishedAt.text = data.endDate
                Glide.with(binding.root).load(data.icon).into(imgView)
            }

        }
        init {
            binding.clickableLayer.setOnClickListener {
                onClickItemListener?.invoke(getItem(adapterPosition)!!)
            }
            haveElementListener?.invoke()
        }
    }
    companion object {
        val diffUtilCallback = object : DiffUtil.ItemCallback<Data>() {
            override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
                return oldItem.id == newItem.id && oldItem.name == newItem.name
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        return holder.bind(getItem(position)!!)
    }
}