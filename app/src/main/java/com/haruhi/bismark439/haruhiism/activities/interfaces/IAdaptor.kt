package com.haruhi.bismark439.haruhiism.activities.interfaces

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kotlin.collections.ArrayList

//https://stackoverflow.com/questions/27845069/add-a-new-item-to-recyclerview-programmatically
@Suppress("UNCHECKED_CAST")
abstract class ItemsAdaptor<T, U : ViewBinding>(
    protected val context: Context,
    var items: ArrayList<T>,
    protected val viewBinder: AdapterViewBinder
) : RecyclerView.Adapter<ItemsAdaptor.MyViewHolder>() {
    override fun getItemCount(): Int {
        return items.size
    }

    class MyViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            viewBinder(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items[position]
        onBindView(holder.binding as U, position, item)
    }

    abstract fun onBindView(binding: U, position: Int, model: T)
}

typealias AdapterViewBinder = (li: LayoutInflater, vg: ViewGroup, attach: Boolean) -> ViewBinding