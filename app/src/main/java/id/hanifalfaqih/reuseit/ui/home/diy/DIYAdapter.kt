package id.hanifalfaqih.reuseit.ui.home.diy

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.hanifalfaqih.reuseit.data.model.Content
import id.hanifalfaqih.reuseit.databinding.ItemListDiyBinding

class DIYAdapter(private val diyContentId: (Int) -> Unit): ListAdapter<Content, DIYAdapter.DIYViewHolder>(DIYComparator()) {

    inner class DIYViewHolder(private val binding: ItemListDiyBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Content) {
            binding.also {
                it.tvTitleItemDiy.text = data.title
                it.tvSubtitleItemDiy.text = data.header

                Glide.with(this.itemView)
                    .load(data.imageThumbnail)
                    .into(it.ivItemDiy)

                itemView.setOnClickListener {
                    diyContentId.invoke(data.id)
                }
            }
        }
    }

    class DIYComparator: DiffUtil.ItemCallback<Content>() {
        override fun areItemsTheSame(oldItem: Content, newItem: Content): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Content, newItem: Content): Boolean {
            return oldItem.id == newItem.id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DIYAdapter.DIYViewHolder {
        val binding = ItemListDiyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DIYViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DIYAdapter.DIYViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}