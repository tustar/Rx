package com.tustar.filemanager.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tustar.filemanager.model.DetailNaviItem
import com.tustar.rxjava.R
import kotlinx.android.synthetic.main.item_detail_navi.view.*
import java.util.*

class DetailNaviAdapter(private val listener: OnNaviItemClickListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var naviEntries = LinkedList<DetailNaviItem>()
    var isInEditModel: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_detail_navi, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(naviEntries[position])
        }
    }

    fun pushNaviItem(item: DetailNaviItem) {
        naviEntries.add(item)
        notifyDataSetChanged()
    }

    fun popToNaviItem(item: DetailNaviItem) {
        val index = naviEntries.indexOf(item)
        naviEntries.take(index)
        notifyDataSetChanged()
    }

    fun pop() {
        naviEntries.removeLast()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = naviEntries.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.navi_text

        fun bind(item: DetailNaviItem) {
            name.text = item.name

            itemView.setOnClickListener {
                listener?.onNaviItemClick(item)
            }

            itemView.isEnabled = !isInEditModel
        }
    }

    interface OnNaviItemClickListener {
        fun onNaviItemClick(item: DetailNaviItem)
    }
}