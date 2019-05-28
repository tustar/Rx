package com.tustar.filemanager.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.tustar.filemanager.model.CategoryItem
import com.tustar.rxjava.R
import com.tustar.rxjava.base.OnItemClickListener
import org.jetbrains.anko.find

class CategoryAdapter(var items: List<CategoryItem>) : BaseAdapter() {

    var onItemClickListener: OnItemClickListener<CategoryItem>? = null

    override fun getCount() = items.size

    override fun getItem(position: Int) = items[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var view: View
        val holder: ViewHolder
        if (convertView == null) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_category,
                    parent, false)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }
        holder.bindItem(getItem(position))

        return view
    }

    inner class ViewHolder(private val itemView: View) {

        private val icon: ImageView = itemView.find(R.id.category_icon)
        private val name: TextView = itemView.find(R.id.category_name)
        private val count: TextView = itemView.find(R.id.category_count)

        fun bindItem(item: CategoryItem) {
            icon.setImageResource(item.icon)
            name.setText(item.name)
            count.text = item.count.toString()

            itemView.setOnClickListener {
                onItemClickListener?.onItemClick(item)
            }
        }

    }
}