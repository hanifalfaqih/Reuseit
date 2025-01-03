package id.hanifalfaqih.reuseit.ui.home.course

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.hanifalfaqih.reuseit.data.model.Content
import id.hanifalfaqih.reuseit.databinding.ItemListCourseBinding

class CourseAdapter(private val courseContentId: (Int) -> Unit): ListAdapter<Content, CourseAdapter.CourseViewHolder>(CourseComparator()) {

    inner class CourseViewHolder(private val binding: ItemListCourseBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Content) {
            binding.also {
                it.tvTitleItemCourse.text = data.title
                it.tvSubtitleItemCourse.text = data.header

                Glide.with(this.itemView)
                    .load(data.imageThumbnail)
                    .into(it.ivItemCourse)

                itemView.setOnClickListener {
                    courseContentId.invoke(data.id)
                }
            }
        }
    }

    class CourseComparator: DiffUtil.ItemCallback<Content>() {
        override fun areItemsTheSame(oldItem: Content, newItem: Content): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Content, newItem: Content): Boolean {
            return oldItem.id == newItem.id
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CourseAdapter.CourseViewHolder {
        val binding = ItemListCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseAdapter.CourseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}