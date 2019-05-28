package com.tustar.filemanager.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tustar.filemanager.model.CachingDocumentFile
import com.tustar.filemanager.utils.DateUtils
import com.tustar.filemanager.utils.FileUtils
import com.tustar.rxjava.R
import com.tustar.rxjava.base.OnItemClickListener
import kotlinx.android.synthetic.main.item_file_list.view.*
import java.lang.Exception

class DetailAdapter(private val listener: OnItemClickListener<CachingDocumentFile>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val directoryEntries = mutableListOf<CachingDocumentFile>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_file_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(directoryEntries[position])
        }
    }

    fun setEntries(newList: List<CachingDocumentFile>) {
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

        fun bind(item: CachingDocumentFile) {
            val context = itemView.context
            if (item.isDirectory) {
                icon.setImageResource(R.drawable.format_folder)
            } else {
                item.name?.let {
                    icon.setImageDrawable(FileUtils.getFileTypeDrawableByName(itemView.context, it))
                }
                Glide.with(context).load(item.uri).into(icon)
            }

            name.text = item.name

            size.text = if (item.isDirectory) {
                if (item.length == 1L) {
                    context.getString(R.string.files_contains_file)
                } else {
                    context.getString(R.string.files_contains_files, item.length)
                }
            } else {
                FileUtils.getFileSize(item.length)
            }

            lastModified.text = DateUtils.millisToUTCDate(item.lastModified)

            arrow.visibility = if (item.isDirectory) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }

            itemView.setOnClickListener {
                listener?.onItemClick(item)
            }
        }
    }
}