package com.example.mynewsapp.Packages.DashBoard.Adapter_package

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mynewsapp.Packages.DashBoard.Data_Classes_package.All_sources_dataclass
import com.example.mynewsapp.R

class All_sources_adapter( val dataset: ArrayList<All_sources_dataclass>, val context: Context): RecyclerView.Adapter<All_sources_adapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): All_sources_adapter.ViewHolder {

        val view=LayoutInflater.from(parent.context).inflate(R.layout.allsources_adapter_layout,parent,false)

        return ViewHolder(view)
            }

    override fun onBindViewHolder(holder: All_sources_adapter.ViewHolder, position: Int) {

val all_sources_data: All_sources_dataclass =dataset[position]

        holder.news_name.text=all_sources_data.name
        holder.news_description.text=all_sources_data.description
        holder.news_categorys.text=all_sources_data.category


        holder.itemview.setOnClickListener {

            onBrowseClick(context,all_sources_data.url)
        }
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
        val news_description=itemview.findViewById<TextView>(R.id.news_description)

        val news_categorys=itemview.findViewById<TextView>(R.id.news_category_value)

        val news_image=itemview.findViewById<ImageView>(R.id.image)

    }

    private fun onBrowseClick(context: Context,url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)

       // context.startActivity(intent)
        context.startActivity(Intent.createChooser(intent, "Browse with"));

        // Verify that the intent will resolve to an activity

    }


}