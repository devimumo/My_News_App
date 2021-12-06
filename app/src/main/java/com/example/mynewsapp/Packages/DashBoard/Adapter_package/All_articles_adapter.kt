package com.example.mynewsapp.Packages.DashBoard.Adapter_package

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mynewsapp.Packages.DashBoard.Data_Classes_package.All_articles_dataclass
import com.example.mynewsapp.R
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat

import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.ContextCompat.startActivity







class All_articles_adapter(val dataset: ArrayList<All_articles_dataclass>, val context: Context): RecyclerView.Adapter<All_articles_adapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): ViewHolder {


        val view=LayoutInflater.from(parent.context).inflate(R.layout.allarticles_adapter_layout,parent,false)

        return ViewHolder(view)
            }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

val all_articles_data: All_articles_dataclass=dataset[position]

        holder.news_name.text=all_articles_data.name+" : "
        holder.news_title.text=all_articles_data.title
        holder.news_description.text=all_articles_data.description

        holder.itemview.setOnClickListener {
            onBrowseClick(context,all_articles_data.url)
        }

        Glide.with(context)
            .load(all_articles_data.urlToImage)
            .placeholder(R.drawable.images)
            .into(holder.news_image);

    }

    override fun getItemCount(): Int {

return dataset.size
    }

    class ViewHolder(val itemview: View): RecyclerView.ViewHolder(itemview)
    {

        val news_name=itemview.findViewById<TextView>(R.id.news_name)
        val news_title=itemview.findViewById<TextView>(R.id.news_title)
        val news_description=itemview.findViewById<TextView>(R.id.news_description)
        val news_image=itemview.findViewById<ImageView>(R.id.image)

    }

    private fun onBrowseClick(context: Context,url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)

        context.startActivity(intent)

        // Verify that the intent will resolve to an activity

    }


}