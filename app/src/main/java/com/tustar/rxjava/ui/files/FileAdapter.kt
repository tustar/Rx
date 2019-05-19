package com.tustar.rxjava.ui.files

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tustar.rxjava.R
import com.tustar.rxjava.base.OnItemClickListener
import com.tustar.rxjava.util.DateUtils
import com.tustar.rxjava.util.FileType
import com.tustar.rxjava.util.FileUtils
import kotlinx.android.synthetic.main.item_file_list.view.*
import com.tustar.rxjava.base.EmptyViewHolder

class FileAdapter(var files: List<FileModel>,
                  private val listener: OnItemClickListener<FileModel>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == 1) {
            val view = inflater.inflate(R.layout.item_empty_view, parent, false)
            EmptyViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                    .inflate(com.tustar.rxjava.R.layout.item_file_list, parent, false)
            FileViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FileViewHolder) {
            holder.bind(files[position])
        }
    }

    override fun getItemCount(): Int {
        return if (files.isNotEmpty()) files.size else 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (files.size === 0) 1 else super.getItemViewType(position)
    }

    inner class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val icon: ImageView = itemView.file_icon
        private val name: TextView = itemView.file_name
        private val size: TextView = itemView.file_size
        private val lastModified: TextView = itemView.file_lastModified
        private val arrow: ImageView = itemView.file_arrow

        fun bind(item: FileModel) {
            val context = itemView.context
            Glide.with(context).load(item.icon).into(icon)
            name.text = if (item.fileType == FileType.TYPE_APK) {
                "${item.name} ${item.version}"
            } else {
                item.name
            }

            size.text = if (item.isDirectory) {
                if (item.fileCount == 1) {
                    context.getString(R.string.files_contains_file)
                } else {
                    context.getString(R.string.files_contains_files, item.fileCount)
                }
            } else {
                FileUtils.getFileSize(item.size)
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