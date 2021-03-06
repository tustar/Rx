package com.tustar.filemanager.ui.detail

import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tustar.filemanager.model.DetailItem
import com.tustar.filemanager.model.ImageItem
import com.tustar.filemanager.utils.DateUtils
import com.tustar.filemanager.utils.FileType
import com.tustar.rxjava.R
import com.tustar.rxjava.base.OnItemClickListener
import com.tustar.rxjava.base.OnLongItemClickListener
import kotlinx.android.synthetic.main.item_detail_content.view.*

class DetailContentAdapter(private val clickListener: OnItemClickListener<DetailItem>,
                           private val longClickListener: OnLongItemClickListener<DetailItem>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var isInEditModel: Boolean = false
        set(value) {
            field = value
            if (!value) {
                directoryEntries.forEach {
                    it.isChecked = false
                }
                notifyDataSetChanged()
            }
        }
    private val directoryEntries = mutableListOf<DetailItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_detail_content, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(directoryEntries[position])
        }
    }

    fun setEntries(newList: List<DetailItem>) {
        synchronized(directoryEntries) {
            directoryEntries.clear()
            directoryEntries.addAll(newList)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = directoryEntries.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val icon: ImageView = itemView.file_icon
        private val name: TextView = itemView.file_name
        private val size: TextView = itemView.file_size
        private val lastModified: TextView = itemView.file_lastModified
        private val arrow: ImageView = itemView.file_arrow
        private val checkBox: CheckBox = itemView.file_check

        fun bind(item: DetailItem) {
            val context = itemView.context
            val drawable = FileType.getDrawable(context, item)
            if (drawable != null) {
                icon.setImageDrawable(drawable)
            } else {
                Glide.with(context)
                        .applyDefaultRequestOptions(RequestOptions
                                .placeholderOf(R.drawable.format_picture)
                                .error(R.drawable.format_unkown))
                        .load(item.uri)
                        .into(icon)
            }

            name.text = when {
                item is ImageItem && item.isBucket -> item.bucketName
                else -> item.name
            }

            size.text = when {
                item.isDirectory || (item is ImageItem && item.isBucket) ->
                    if (item.length == 1L) {
                        context.getString(R.string.files_contains_file)
                    } else {
                        context.getString(R.string.files_contains_files, item.length)
                    }
                else -> Formatter.formatFileSize(context, item.length)
            }

            lastModified.text = DateUtils.millisToUTCDate(item.lastModified)

            arrow.visibility = if (item.isDirectory && !isInEditModel) {
                View.VISIBLE
            } else {
                View.GONE
            }

            checkBox.visibility = if (isInEditModel) {
                checkBox.isChecked = item.isChecked
                View.VISIBLE
            } else {
                View.GONE
            }

            itemView.setOnClickListener {
                if (isInEditModel) {
                    item.isChecked = !item.isChecked
                    notifyDataSetChanged()
                }

                //
                clickListener?.onItemClick(item)
            }
            itemView.setOnLongClickListener {
                //
                isInEditModel = true
                item.isChecked = true
                //

                longClickListener?.onLongItemClick(item)
                return@setOnLongClickListener true
            }
        }
    }
}