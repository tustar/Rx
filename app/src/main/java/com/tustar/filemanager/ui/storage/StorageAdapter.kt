package com.tustar.filemanager.ui.storage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tustar.filemanager.model.StorageItem
import com.tustar.filemanager.utils.SizeUtils
import com.tustar.rxjava.R
import com.tustar.rxjava.base.OnItemClickListener
import org.jetbrains.anko.find

class StorageAdapter : RecyclerView.Adapter<StorageAdapter.ViewHolder>() {

    var onItemClickListener: OnItemClickListener<StorageItem>? = null
    var items: List<StorageItem> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = items?.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_storage, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val icon: ImageView = itemView.find(R.id.storage_icon)
        private val name: TextView = itemView.find(R.id.storage_name)
        private val available: TextView = itemView.find(R.id.storage_available)
        private val progressBar: ProgressBar = itemView.find(R.id.storage_progress)
        private val arrow: ImageView = itemView.find(R.id.storage_arrow)

        fun bindItem(item: StorageItem) {
            icon.setImageResource(item.icon)
            name.text = itemView.context.getString(R.string.storage_name, item.name)
            available.text = itemView.context.getString(R.string.storage_available,
                    SizeUtils.formatFileSize(item.availableBytes))
            val progress = if (item.totalBytes == 0L) {
                0
            } else {
                ((item.totalBytes - item.availableBytes) / item.totalBytes.toFloat() * 100).toInt()
            }
            progressBar.progress = progress

            itemView.setOnClickListener {
                onItemClickListener?.onItemClick(item)
            }
        }

    }
}