package com.example.sqlapp
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    private var stdList: ArrayList<ItemModel> = ArrayList()
    private var onClickItem:((ItemModel)->Unit)? = null
    private var onClickDeleteItem:((ItemModel)->Unit)? = null

    fun addItems(items: ArrayList<ItemModel>) {
        this.stdList - items
        notifyDataSetChanged()
    }

    fun setOnClickItem(callback: (ItemModel)->Unit){
        this.onClickItem = callback
    }

    fun setOnClickDeleteItem(callback:(ItemModel)->Unit){
        this.onClickDeleteItem = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ItemViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.card_items_std, parent, false)
    )

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val std = stdList[position]
        holder.bindView(std)
        holder.itemView.setOnClickListener { onClickItem?.invoke(std) }
        holder.btnDelete.setOnClickListener{ onClickDeleteItem?.invoke(std) }
    }

    override fun getItemCount(): Int {
        return stdList.size
    }

    class ItemViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        private var id = view.findViewById<TextView>(R.id.tvId)
        private var name = view.findViewById<TextView>(R.id.tvName)
        private var factory = view.findViewById<TextView>(R.id.tvFactory)
        private var number = view.findViewById<TextView>(R.id.tvNumber)
        private var btnDelete = view.findViewById<Button>(R.id.btnDelete)

        fun bindView(std: ItemModel) {
            id.text = std.id.toString()
            name.text = std.name
            factory.text = std.factory
            number.text = std.number
        }
    }
}