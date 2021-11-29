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
import com.android.volley.DefaultRetryPolicy

import android.net.Uri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynewsapp.Packages.DashBoard.Adapter_package.All_articles_adapter
import com.example.mynewsapp.Packages.DashBoard.Data_Classes_package.All_articles_dataclass
import com.example.mynewsapp.Packages.DashBoard.Fragments.All_articles
import com.example.mynewsapp.Packages.DashBoard.Fragments.all_articles_data_set
import com.example.mynewsapp.R
import org.json.JSONObject

private  var responsevalue: String=""
private lateinit var  adaps: All_articles_adapter

class All_articles_networkfetch (){

    fun network_fetch(view: View,keyword: String,adapter: All_articles_adapter,arraylist: ArrayList<All_articles>): String
    {

     //   Log.d("keywords_jsondata :",keyword.toString())

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
            .appendPath("everything")
            .appendQueryParameter("q", keyword)
            .appendQueryParameter("apiKey", "91fcb5c401984c25bdc839bbfbe34c57")
        val myUrl: String = builder.build().toString()


        val jsonObjectRequest = object :JsonObjectRequest(Request.Method.GET, myUrl, null,{ response ->
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
        jsonObjectRequest.setRetryPolicy(
            DefaultRetryPolicy(
                10000,3, 1F
            )
        )


        return responsevalue

}



    private fun iterate_data(view: View,keyword: String,json_data: String,adapter: All_articles_adapter) {

        //progress_bar.visibility=View.VISIBLE

        all_articles_data_set.clear()
      //  adap.notifyDataSetChanged()

        //  Log.i("response", response.toString())
      //  fetch_data = All_articles_networkfetch()
            //  val json_data = fetch_data.network_fetch(view, keyword)

       // Log.d("JSONDATA :",json_data)

        if (json_data.equals("error")) {

            Log.d("Volley_error :",json_data)

        } else if (json_data.equals("volley_error")) {
            Log.d("Volley_error :",json_data)

        }
        else if (json_data.equals("")) {
            Log.d("Volley_error :",json_data)

        }else {


            //   "{\"status\":\"ok\",\"totalResults\":25071,\"articles\":[{\"source\":{\"id\":\"engadget\",\"name\":\"Engadget\"},\"author\":\"Mariella Moon\",\"title\":\"Xiaomi's upcoming EV factory will make up to 300,000 cars per year\",\"description\":\"Xiaomi only announced its electric car plans in March, but it already has grand ambitions. According to Reuters, the economic development agency Beijing E-Town has confirmed that Xiaomi will build an EV factory in the city capable of producing up to 300,000 v…\",\"url\":\"https://www.engadget.com/xiaomi-ev-factory-beijing-150108230.html\",\"urlToImage\":\"https://s.yimg.com/os/creatr-uploaded-images/2021-08/fc043120-0592-11ec-bdad-7c2fd9bfb433\",\"publishedAt\":\"2021-11-27T15:01:08Z\",\"content\":\"Xiaomi only announced its electric car plans in March, but it already has grand ambitions. According to Reuters, the economic development agency Beijing E-Town has confirmed that Xiaomi will build an… [+824 chars]\"},{\"source\":{\"id\":\"engadget\",\"name\":\"Engadget\"},\"author\":\"Jon Fingas\",\"title\":\"Mercedes cars will have optional Dolby Atmos audio starting in 2022\",\"description\":\"You won't have to buy a Lucid Air if you want a car with more immersive Dolby Atmos. Mercedes will equip its cars with Atmos audio on all models that use both its latest MBUX interface (introduced with the latest S-Class) and an optional Burmester 3D or 4D so…\",\"url\":\"https://www.engadget.com/mercedes-benz-dolby-atmos-music-204555703.html\",\"urlToImage\":\"https://s.yimg.com/os/creatr-uploaded-images/2021-10/2204b7d0-3826-11ec-befb-aac911ca15fe\",\"publishedAt\":\"2021-10-28T20:45:55Z\",\"content\":\"You won't have to buy a Lucid Air if you want a car with more immersive Dolby Atmos. Mercedes will equip its cars with Atmos audio on all models that use both its latest MBUX interface (introduced wi… [+892 chars]\"},{\"source\":{\"id\":\"the-verge\",\"name\":\"The Verge\"},\"author\":\"Andrew J. Hawkins\",\"title\":\"Hyundai goes really big with its latest electric concept SUV\",\"description\":\"Hyundai debuted its latest concept, the electric Seven SUV, at the 2021 LA Auto Show. The extra-large SUV shares an EV platform with the automaker’s other electric cars.\",\"url\":\"https://www.theverge.com/2021/11/17/22787672/hyundai-seven-electric-suv-concept-la-auto-show-2021\",\"urlToImage\":\"https://cdn.vox-cdn.com/thumbor/Do3ViXgMCD54uk1p8p-vaihApRc=/0x186:3395x1963/fit-in/1200x630/cdn.vox-cdn.com/uploads/chorus_asset/file/23022270/48639_HyundaiMotorUnveilsSEVENConceptSegment_bustingSUEVfortheIONIQBrand.jpg\",\"publishedAt\":\"2021-11-17T18:46:57Z\",\"content\":\"The extra-large Hyundai Seven made its debut at the 2021 LA Auto Show\\r\\nHyundai revealed a new electric vehicle concept at the 2021 Los Angeles Auto Show, showing how its scalable EV platform can be s… [+2757 chars]\"},{\"source\":{\"id\":\"bbc-news\",\"name\":\"BBC News\"},\"author\":\"https://www.facebook.com/bbcnews\",\"title\":\"Tesla drivers left unable to start their cars after outage\",\"description\":\"The carmaker's CEO Elon Musk says its app is coming back online after many owners reported an error.\",\"url\":\"https://www.bbc.co.uk/news/technology-59357306\",\"urlToImage\":\"https://ichef.bbci.co.uk/news/1024/branded_news/F19D/production/_121735816_070674677.jpg\",\"publishedAt\":\"2021-11-20T04:57:24Z\",\"content\":\"Image source, Getty Images\\r\\nImage caption, The Tesla app is used as a key by drivers to unlock their cars\\r\\nTesla drivers say they have been locked out of their cars after an outage struck the carmake… [+989 chars]\"},{\"source\":{\"id\":\"bbc-news\",\"name\":\"BBC News\"},\"author\":null,\"title\":\"Dramatic footage shows tornado cut across Texas highway\",\"description\":\"The twister flung trees and cars into the air and affected 100 homes, officials said.\",\"url\":\"https://www.bbc.co.uk/news/av/world-us-canada-59082976\",\"urlToImage\":\"https://ichef.bbci.co.uk/news/1024/branded_news/14D07/production/_121255258_p0b0ybkq.jpg\",\"publishedAt\":\"2021-10-28T15:39:05Z\",\"content\":\"A twister struck a highway near Orange County on Wednesday, lifting nearby trees and cars into the air.\\r\\nThe storm decimated the nearby town of Mauriceville, with officials estimating that 100 homes … [+23 chars]\"},{\"source\":{\"id\":null,\"name\":\"New York Times\"},\"author\":\"Neil Vigdor\",\"title\":\"Drunken-Driving Warning Systems Would Be Required for New Cars Under U.S. Bill\",\"description\":\"Congress attached the mandate to the \$1 trillion infrastructure package that President Biden is expected to sign soon. It would take effect as early as 2026.\",\"url\":\"https://www.nytimes.com/2021/11/10/us/drunk-driving-system-mandate.html\",\"urlToImage\":\"https://static01.nyt.com/images/2021/11/10/multimedia/10xp-alcohol/10xp-alcohol-facebookJumbo.jpg\",\"publishedAt\":\"2021-11-10T22:40:32Z\",\"content\":\"John Bozzella, the president and chief executive of the Alliance for Automotive Innovation, said in a statement this week that the industry group appreciated that lawmakers gave safety regulators fle… [+1331 chars]\"},{\"source\":{\"id\":\"cnn\",\"name\":\"CNN\"},\"author\":\"Sherry Liang, CNN\",\"title\":\"Hawaiian artist's enormous murals are ambitious in scale and message\",\"description\":\"Kamea Hadar paints murals across the world -- his canvas ranging from 15-story buildings to luxury cars. His latest artwork of surfing champions Carissa Moore and Duke Kahanamoku will be his largest and most detailed yet.\",\"url\":\"https://www.cnn.com/style/article/kamea-hadar-mural-art-hawaii-building/index.html\",\"urlToImage\":\"https://cdn.cnn.com/cnnnext/dam/assets/211125021844-kamea-hadar-hawaii-building-mural-super-tease.jpg\",\"publishedAt\":\"2021-11-25T08:28:24Z\",\"content\":\"artsUpdated 25th November 2021\\r\\nHonolulu, Hawaii-based artist Kamea Hadar has lost count of how many murals he's painted in his career -- his best guess is at least 50 over the past decade. \\r\\nHe's pa… [+5501 chars]\"},{\"source\":{\"id\":null,\"name\":\"CNET\"},\"author\":\"Sean Szymkowski\",\"title\":\"The best Black Friday deals for new cars - Roadshow\",\"description\":\"New cars are more expensive this year, but some cars without inflated price tags make for smarter buys.\",\"url\":\"https://www.cnet.com/roadshow/news/best-black-friday-deals-new-cars/\",\"urlToImage\":\"https://www.cnet.com/a/img/C4RQeL6N4UzlKdskjow02P3IkpE=/1200x630/2020/04/15/bc21b3c0-77b0-431e-ab30-c4801c5966f5/mazda6-ogi.jpg\",\"publishedAt\":\"2021-11-24T16:44:31Z\",\"content\":\"The Mazda6 dies next year, so dealers may want to clear their stock out.\\r\\nAndrew Krok/Roadshow\\r\\nBlack Friday car deals are going to be few and far between this year due to supply chain bottlenecks an… [+3075 chars]\"},{\"source\":{\"id\":null,\"name\":\"Slashdot.org\"},\"author\":\"EditorDavid\",\"title\":\"Could Electric Cars Save the Coal Industry?\",\"description\":\"North Dakota has just 266 electric cars, the fewest of any state in America, reports the Washington Post. But the state's biggest booster for electric cars may be: the coal industry:\\n\\nThe thinking is straightforward: More electric cars would mean more of a ma…\",\"url\":\"https://hardware.slashdot.org/story/21/11/14/0253249/could-electric-cars-save-the-coal-industry\",\"urlToImage\":\"https://a.fsdn.com/sd/topics/power_64.png\",\"publishedAt\":\"2021-11-14T08:34:00Z\",\"content\":\"The thinking is straightforward: More electric cars would mean more of a market for the [lower carbon] lignite coal that produces most of North Dakota's electricity, and if a long-shot project to sto… [+2674 chars]\"},{\"source\":{\"id\":\"reuters\",\"name\":\"Reuters\"},\"author\":\"Reuters Staff\",\"title\":\"Volvo Cars IPO oversubscribed ahead of trading debut - Reuters\",\"description\":\"Geely-owned automaker Volvo Cars said its initial public offering (IPO) was substantially oversubscribed as it geared up for its bourse debut in Stockholm later on Friday.\",\"url\":\"https://www.reuters.com/article/volvo-cars-shares-idUSL8N2RP14K\",\"urlToImage\":\"https://s1.reutersmedia.net/resources_v2/images/rcom-default.png?w=800\",\"publishedAt\":\"2021-10-29T05:34:00Z\",\"content\":\"By Reuters Staff\\r\\nSTOCKHOLM, Oct 29 (Reuters) - Geely-owned automaker Volvo Cars said its initial public offering (IPO) was substantially oversubscribed as it geared up for its bourse debut in Stockh… [+669 chars]\"},{\"source\":{\"id\":\"reuters\",\"name\":\"Reuters\"},\"author\":\"Reuters Editorial\",\"title\":\"Much of world not ready for electric cars: Toyota - Reuters\",\"description\":\"Large parts of the world are not ready for zero-emission vehicles, which is why Toyota says it couldn't sign a pledge this week to phase out fossil-fuel cars by 2040. Francis Maguire reports.\",\"url\":\"https://www.reuters.com/video/watch/idOVF35T56Z\",\"urlToImage\":\"https://static.reuters.com/resources/r/?d=20211111&i=OVF35T56Z&r=OVF35T56Z&t=2\",\"publishedAt\":\"2021-11-11T16:02:54Z\",\"content\":\"Posted \\r\\nLarge parts of the world are not ready for zero-emission vehicles, which is why Toyota says it couldn't sign a pledge this week to phase out fossil-fuel cars by 2040. Francis Maguire reports.\"},{\"source\":{\"id\":\"reuters\",\"name\":\"Reuters\"},\"author\":null,\"title\":\"From beers to cars, German consumers face higher prices - Reuters\",\"description\":\"German consumers face higher prices for goods across the board as more and more companies in Europe's largest economy decide to pass on increased production costs, driven by widespread supply shortages and a spike in energy prices.\",\"url\":\"https://www.reuters.com/world/europe/beers-cars-german-consumers-face-higher-prices-2021-10-28/\",\"urlToImage\":\"https://www.reuters.com/resizer/no7v9bDbetybu3lYVafO9iWxhSE=/1200x628/smart/filters:quality(80)/cloudfront-us-east-2.images.arcpublishing.com/reuters/N6S67AL3W5IS5P6YAASXPMBH3A.jpg\",\"publishedAt\":\"2021-10-28T11:19:00Z\",\"content\":\"Shoppers wear mask and fill Cologne's main shopping street Hohe Strasse (High Street) in Cologne, Germany, 12, December, 2020. REUTERS/Wolfgang Rattay/File PhotoBERLIN, Oct 28 (Reuters) - German cons… [+4286 chars]\"},{\"source\":{\"id\":\"reuters\",\"name\":\"Reuters\"},\"author\":\"Michael Nienaber\",\"title\":\"From beers to cars, German consumers face higher prices - Reuters\",\"description\":\"German consumers face higher prices for goods across the board as more and more companies in Europe's largest economy decide to pass on increased production costs, driven by widespread supply shortages and a spike in energy prices.\",\"url\":\"https://www.reuters.com/article/germany-inflation-idUSKBN2HI1IW\",\"urlToImage\":\"https://static.reuters.com/resources/r/?m=02&d=20211028&t=2&i=1579402853&r=LYNXMPEH9R0OL&w=800\",\"publishedAt\":\"2021-10-28T11:08:00Z\",\"content\":\"BERLIN (Reuters) - German consumers face higher prices for goods across the board as more and more companies in Europes largest economy decide to pass on increased production costs, driven by widespr… [+4097 chars]\"},{\"source\":{\"id\":\"reuters\",\"name\":\"Reuters\"},\"author\":null,\"title\":\"Electric cars charge up \$1.8 bln BYD share sale - Reuters\",\"description\":\"Investors snapped up <a href=\\\"https://www1.hkexnews.hk/listedco/listconews/sehk/2021/1101/2021110100033.pdf\\\" target=\\\"_blank\\\">new shares</a> in the \$128 billion Chinese electric-car maker BYD <a href=\\\"https://www.reuters.com/companies/002594.SZ\\\" target=\\\"_blank…\",\"url\":\"https://www.reuters.com/breakingviews/electric-cars-charge-up-18-bln-byd-share-sale-2021-11-01/\",\"urlToImage\":\"https://www.reuters.com/resizer/djiXQ2jmkDDbWK8SPWx3dXuMv2k=/1200x628/smart/filters:quality(80)/cloudfront-us-east-2.images.arcpublishing.com/reuters/KW4L732FRVL4PHDDJPWJ6XTN3I.jpg\",\"publishedAt\":\"2021-11-01T07:46:00Z\",\"content\":\"People stand near a BYD X Dream electric vehicle (EV) displayed during a media day for the Auto Shanghai show in Shanghai, China April 19, 2021. REUTERS/Aly SongHONG KONG, Nov 1 (Reuters Breakingview… [+2026 chars]\"},{\"source\":{\"id\":null,\"name\":\"The Guardian\"},\"author\":\"Reuters\",\"title\":\"App outage locks hundreds of Tesla drivers out of cars\",\"description\":\"Dozen of motorists report error as company’s CEO, Elon Musk, apologises on TwitterHundreds of Tesla drivers were locked out of their cars at the start of the weekend after the manufacturer’s mobile app suffered an outage – and dozens voiced their complaints o…\",\"url\":\"https://amp.theguardian.com/technology/2021/nov/20/tesla-app-outage-elon-musk-apologises\",\"urlToImage\":\"https://i.guim.co.uk/img/media/751f8c31f73c051d9b4d788d36d4d9dd62a475cc/375_922_3799_2280/master/3799.jpg?width=1200&height=630&quality=85&auto=format&fit=crop&overlay-align=bottom%2Cleft&overlay-width=100p&overlay-base64=L2ltZy9zdGF0aWMvb3ZlcmxheXMvdGctZGVmYXVsdC5wbmc&enable=upscale&s=a10f750d90f8f677b7bf22a20dead4d6\",\"publishedAt\":\"2021-11-20T17:38:16Z\",\"content\":\"Hundreds of Tesla drivers were locked out of their cars at the start of the weekend after the manufacturers mobile app suffered an outage and dozens voiced their complaints on social media.\\r\\nElon Mus… [+1724 chars]\"},{\"source\":{\"id\":null,\"name\":\"Kotaku\"},\"author\":\"Zack Zwiezen\",\"title\":\"In Forza Horizon 5, Cars Have Stolen The World From Humans\",\"description\":\"Sure, cars are cool. Yes, they go fast. But cars have also fundamentally altered our cities and civilizations in ways that we still haven’t recovered from. Large sections of our world are just permanently off-limits because cars need that space. It sucks. And…\",\"url\":\"https://kotaku.com/in-forza-horizon-5-cars-have-stolen-the-world-from-hum-1848090276\",\"urlToImage\":\"https://i.kinja-img.com/gawker-media/image/upload/c_fill,f_auto,fl_progressive,g_center,h_675,pg_1,q_80,w_1200/3f47c2632ea3d1989712744848e86cb4.jpg\",\"publishedAt\":\"2021-11-19T18:45:00Z\",\"content\":\"Sure, cars are cool. Yes, they go fast. But cars have also fundamentally altered our cities and civilizations in ways that we still havent recovered from. Large sections of our world are just permane… [+3673 chars]\"},{\"source\":{\"id\":null,\"name\":\"New York Times\"},\"author\":\"Brad Plumer and Hiroko Tabuchi\",\"title\":\"6 Automakers and 31 Countries Say They’ll Phase Out Gasoline Car Sales\",\"description\":\"Ford, G.M. and Mercedes agreed to work toward selling only zero-emissions vehicles by 2040. But Toyota, Volkswagen and Nissan-Renault did not join the pledge.\",\"url\":\"https://www.nytimes.com/2021/11/09/climate/cars-zero-emissions-cop26.html\",\"urlToImage\":\"https://static01.nyt.com/images/2021/11/09/multimedia/09CLI-CARS/09CLI-CARS-facebookJumbo.jpg\",\"publishedAt\":\"2021-11-10T01:32:22Z\",\"content\":\"Two dozen vehicle fleet operators, including Uber and LeasePlan, also joined the coalition, vowing to operate only zero-emissions vehicles by 2030, or earlier where markets allow.\\r\\nWorldwide, transpo… [+1749 chars]\"},{\"source\":{\"id\":\"the-verge\",\"name\":\"The Verge\"},\"author\":\"Chaim Gartenberg\",\"title\":\"Heated seats for 2022 Chevy trucks are the latest victim of the chip shortage\",\"description\":\"GM has warned dealers that it won’t be offering heated or ventilated seats on most of its 2022 models due to chip shortages. The feature is the latest issue to arise from supply chain shortages for semiconductors.\",\"url\":\"https://www.theverge.com/2021/11/17/22787339/chevrolet-trucks-heated-seats-2022-models-chip-shortage-supply-chain-issues\",\"urlToImage\":\"https://cdn.vox-cdn.com/thumbor/BaG_uyNLRjRrBb8PxnNcN93WvVs=/0x144:2040x1212/fit-in/1200x630/cdn.vox-cdn.com/uploads/chorus_asset/file/8814039/akrales_170313_1481_A_0013.jpg\",\"publishedAt\":\"2021-11-17T19:51:39Z\",\"content\":\"Photo by Amelia Holowaty Krales / The Verge\\r\\n\\n \\n\\n If you’re looking to buy a 2022 Chevrolet (or nearly any GM vehicle) this winter, you might want to invest in a warmer pair of sweatpants: the compan… [+1994 chars]\"},{\"source\":{\"id\":null,\"name\":\"Slashdot.org\"},\"author\":\"EditorDavid\",\"title\":\"Will Self-Driving Cars Be Able to Handle... Bears?\",\"description\":\"A wild bear broke into a parked car looking for food. This set AI pundit Lance Eliot a-thinking...\\n\\nThe AI driving system of a self-driving car is always intact. A parked self-driving car is immediately able to become a moving car.... If the self-driving car …\",\"url\":\"https://tech.slashdot.org/story/21/11/07/1853246/will-self-driving-cars-be-able-to-handle-bears\",\"urlToImage\":\"https://a.fsdn.com/sd/topics/ai_64.png\",\"publishedAt\":\"2021-11-07T18:56:00Z\",\"content\":\"The odds are that self-driving cars will be designed differently on the interior than are conventional human-driven cars. For example, there is no need for a steering wheel and nor any need for the p… [+1004 chars]\"},{\"source\":{\"id\":\"reuters\",\"name\":\"Reuters\"},\"author\":null,\"title\":\"Tax breaks kick Pakistan's electric car shift into higher gear - Reuters\",\"description\":\"Pakistani businessman Nawabzada Kalam Ullah Khan had been planning to swap his family's petrol-powered cars for electric models for years.\",\"url\":\"https://www.reuters.com/business/cop/tax-breaks-kick-pakistans-electric-car-shift-into-higher-gear-2021-11-22/\",\"urlToImage\":\"https://www.reuters.com/pf/resources/images/reuters/reuters-default.png?d=59\",\"publishedAt\":\"2021-11-22T02:00:00Z\",\"content\":\"ISLAMABAD, Nov 22 (Thomson Reuters Foundation) - Pakistani businessman Nawabzada Kalam Ullah Khan had been planning to swap his family's petrol-powered cars for electric models for years.\\r\\nBut it was… [+5974 chars]\"}]}"

            val jsonObject = JSONObject(json_data)

            val status = jsonObject.getString("status")
            if (status.equals("error")) {

            } else {

                val data = jsonObject.getJSONArray("articles")

                for (i in 0..data.length() - 1) {

                    val all_articles_data = data.getJSONObject(i)


                    val source_data = all_articles_data.getJSONObject("source")
                    val source_name = source_data.getString("name")
                    val source_id = source_data.getString("id",)

                    val articles_data = All_articles_dataclass(
                        source_id,
                        source_name,
                        all_articles_data.getString("author"),
                        all_articles_data.getString("title"),
                        all_articles_data.getString("description"),
                        all_articles_data.getString("url"),
                        all_articles_data.getString("urlToImage"),
                        all_articles_data.getString("publishedAt"),
                        all_articles_data.getString("content")

                    )

                    all_articles_data_set.add(articles_data)
                    Log.d("all_articles_data_sets", all_articles_data_set.toString())


                    if(i == (data.length()-1)){
                        set_to_adapter(view,adapter)                    }

                }





            }
        }
    }

    private fun set_to_adapter(view: View, adap: All_articles_adapter)
    {
        var recycler_view = view.findViewById<RecyclerView>(R.id.recycler_view)

        recycler_view.layoutManager = LinearLayoutManager(view.context)

      //  view.
       // adap = All_articles_adapter(all_articles_data_set, view.context)
      //  adap.notifyDataSetChanged()
      //  recycler_view.adapter = adap

        //progress_bar.visibility=View.GONE
    }

}
