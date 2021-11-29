package com.example.mynewsapp.Packages.DashBoard

import android.util.Log
import android.view.View
import com.android.volley.*
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONException
import java.util.HashMap
import kotlin.jvm.Throws
import com.android.volley.DefaultRetryPolicy

import android.R.string.no
import android.content.Context
import android.R.string.no
import android.net.Uri

private  var responsevalue: String=""

class All_sources_networkfetch {

    fun network_fetch(view: View): String
    {

        val context=view.context
        val cache: Cache =  DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        val network: Network =  BasicNetwork( HurlStack());

        val requestQueue: RequestQueue =  RequestQueue(cache, network);
        requestQueue.start();

        val builder: Uri.Builder = Uri.Builder()
        builder.scheme("https")
            .authority("newsapi.org")
            .appendPath("v2")
            .appendPath("top-headlines")
            .appendPath("sources?")
                .appendQueryParameter("apiKey", "581ecd6a0ec4430fb1cb37f97ff1a26e")
        val myUrl: String = builder.build().toString()

        var url="https://newsapi.org/v2/top-headlines/sources?apiKey=581ecd6a0ec4430fb1cb37f97ff1a26e"

        val jsonObjectRequest = object :JsonObjectRequest(Request.Method.GET, url, null,{ response ->
                try {
                    Log.d("response_value",response.toString())

                    responsevalue=response.toString()

                }catch (e: JSONException)
                {
                    responsevalue=e.toString()
                    Log.d("JSONEXCEPTION",e.toString())

                }
                //textView.text = "Response: %s".format(response.toString())
            },{
                           Log.d("volley_error",it.toString())

                responsevalue=it.toString()
                // TODO: Handle error
            }
        )
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"]="Mozilla/5.0"
                return headers
            }
        }

requestQueue.add(jsonObjectRequest)

// Access the RequestQueue through your singleton class.
//    MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

        jsonObjectRequest.setRetryPolicy(
            DefaultRetryPolicy(
                10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT            )
        )


        return responsevalue

}

}
