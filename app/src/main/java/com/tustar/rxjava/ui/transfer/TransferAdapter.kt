package com.tustar.rxjava.ui.transfer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tustar.rxjava.R
import com.tustar.rxjava.util.FileType
import com.tustar.rxjava.util.FileUtils
import kotlinx.android.synthetic.main.item_file_list.view.*
import java.io.File

class TransferAdapter(var files: List<FileModel>)
    : RecyclerView.Adapter<TransferAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_file_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(files[position])
    }

    override fun getItemCount(): Int = files.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val icon: ImageView = itemView.file_icon
        private val name: TextView = itemView.file_name
        private val path: TextView = itemView.file_path
        private val size: TextView = itemView.file_size

        fun bind(item: FileModel) {
            if (item.fileType == FileType.TYPE_IMAGE) {
                Glide.with(itemView.context).load(File(item.path)).into(icon)
            } else {
                icon.setImageDrawable(item.icon)
            }
            name.text = if (item.fileType == FileType.TYPE_APK) {
                "${item.name} ${item.version}"
            } else {
                item.name
            }
            path.text = item.path
            size.text = FileUtils.getFileSize(item.size)
        }
    }
}