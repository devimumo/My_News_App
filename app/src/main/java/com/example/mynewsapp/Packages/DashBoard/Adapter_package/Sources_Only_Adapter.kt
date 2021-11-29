package com.example.mynewsapp.Packages.DashBoard.Adapter_package

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mynewsapp.Packages.DashBoard.Data_Classes_package.All_sources_dataclass
import com.example.mynewsapp.R

class Sources_Only_Adapter(val dataset: ArrayList<All_sources_dataclass>, val context: Context): RecyclerView.Adapter<Sources_Only_Adapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): Sources_Only_Adapter.ViewHolder {

        val view=LayoutInflater.from(parent.context).inflate(R.layout.sources_only_adapter_layout,parent,false)

        return ViewHolder(view)
            }

    override fun onBindViewHolder(holder: Sources_Only_Adapter.ViewHolder, position: Int) {

   val all_sources_data: All_sources_dataclass =dataset[position]

        holder.news_name.text=all_sources_data.name
        holder.category.text=all_sources_data.category

        /*Glide.with(context)
            .load(all_sources_data.urlToImage)
            .placeholder(R.drawable.images)
            .into(holder.news_image);*/

    }

    override fun getItemCount(): Int {

return dataset.size
    }
    class ViewHolder(val itemview: View): RecyclerView.ViewHolder(itemview)
    {

        val news_name=itemview.findViewById<TextView>(R.id.news_name)
        val category=itemview.findViewById<TextView>(R.id.category_value)


    }


}