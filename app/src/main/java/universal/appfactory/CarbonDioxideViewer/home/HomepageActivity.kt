package universal.appfactory.CarbonDioxideViewer.home

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import androidx.viewpager.widget.ViewPager
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.appcompat.app.ActionBar
import universal.appfactory.CarbonDioxideViewer.API.ApiInterface
import universal.appfactory.CarbonDioxideViewer.API.ServiceBuilder
import universal.appfactory.CarbonDioxideViewer.R
import universal.appfactory.CarbonDioxideViewer.Settings.CreditsActivity
import universal.appfactory.CarbonDioxideViewer.Settings.HelpActivity
import universal.appfactory.CarbonDioxideViewer.Settings.SettingsActivity
import universal.appfactory.CarbonDioxideViewer.Settings.TasksActivity
import universal.appfactory.CarbonDioxideViewer.requestModels.EmissionFactors
import universal.appfactory.CarbonDioxideViewer.requestModels.EmissionParameters
import universal.appfactory.CarbonDioxideViewer.requestModels.EmissionRequestModel
import universal.appfactory.CarbonDioxideViewer.responseModels.*
import universal.appfactory.CarbonDioxideViewer.adapters.PageAdapter

@Suppress("UNUSED_PARAMETER")
class HomepageActivity : AppCompatActivity() {

    private lateinit var navigableIntent: Intent
    private var backpress: Long = 0
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setCustomView(R.layout.action_bar_layout_homepage)

        viewPager = findViewById(R.id.pager)
        tabLayout = findViewById(R.id.tabLayout)
        viewPager.adapter = PageAdapter(supportFragmentManager, 3)

        tabLayout.setSelectedTabIndicatorColor(Color.GREEN)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if(tabLayout.selectedTabPosition == 1){
                    startActivity(Intent(this@HomepageActivity, MapsActivity::class.java))
                    tabLayout.getTabAt(0)?.select()
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        tabLayout.setupWithViewPager(viewPager)


    }

    fun getCarbonData(view: View){
        findViewById<ProgressBar>(R.id.progressbar).visibility = View.GONE
        val response = ServiceBuilder.buildServiceURL1(ApiInterface::class.java)

        @OptIn(DelicateCoroutinesApi::class)
        GlobalScope.launch(Dispatchers.IO) {
            response.getData()?.enqueue(
                object : Callback<DataResponseModel> {
                    override fun onResponse(
                        call: Call<DataResponseModel>,
                        response: Response<DataResponseModel>
                    ) {
                        val data = response.body()?.co2 as ArrayList<Statistics>
                        setDataLayout(data)

                        for(i in 0 until data.size){
                            Log.i("Data ${i+1}", "Date: ${data[i].day}-${data[i].month}-${data[i].year}\n" +
                                    "Cycle: ${data[i].cycle}\n" +
                                    "Trend: ${data[i].trend}")
                        }

                        findViewById<ProgressBar>(R.id.progressbar).visibility = View.GONE
                    }

                    override fun onFailure(call: Call<DataResponseModel>, t: Throwable) {
                        Log.i("Error", t.toString())
                        findViewById<ProgressBar>(R.id.progressbar).visibility = View.GONE
                    }
                }
            )

        }
        findViewById<ProgressBar>(R.id.progressbar).visibility = View.VISIBLE
    }

    fun estimateCarbon(view: View){

        val emissionRequestModel = EmissionRequestModel(EmissionFactors(), EmissionParameters())
        val jsonRequest = modelToJSON(emissionRequestModel)
        val response = ServiceBuilder.buildServiceURL2(ApiInterface::class.java)

        @OptIn(DelicateCoroutinesApi::class)
        GlobalScope.launch(Dispatchers.IO) {
            response.getEstimate(body = jsonRequest)?.enqueue(
                object : Callback<EstimateResponseModel> {
                    override fun onResponse(
                        call: Call<EstimateResponseModel>,
                        response: Response<EstimateResponseModel>
                    ) {

                        val co2Emission = "${response.body()?.co2e.toString()} ${response.body()?.co2e_unit.toString()}"
                        val emissionFactor = response.body()?.emission_factor
                        val constituentModel = response.body()?.constituent_gases

                        Log.i("Emission Characteristics", "Carbon dioxide (CO2): $co2Emission\n" +
                                "Methane (CH4): ${constituentModel?.ch4} ${response.body()?.co2e_unit}\n" +
                                "Nitrous oxide (N2O): ${constituentModel?.n2o} ${response.body()?.co2e_unit}\n" +
                                "Calculation method: ${response.body()?.co2e_calculation_method}\n" +
                                "Calculation origin: ${response.body()?.co2e_calculation_origin}\n" +
                                "Name: ${emissionFactor?.name}\n" +
                                "Activity ID: ${emissionFactor?.activity_id}\n" +
                                "Source: ${emissionFactor?.source}\n" +
                                "Year: ${emissionFactor?.year}\n" +
                                "Region: ${emissionFactor?.region}\n" +
                                "Category: ${emissionFactor?.category}")

                        findViewById<ProgressBar>(R.id.progressbar).visibility = View.GONE
                    }

                    override fun onFailure(call: Call<EstimateResponseModel>, t: Throwable) {
                        Log.e("Error message", t.toString())
                        findViewById<ProgressBar>(R.id.progressbar).visibility = View.GONE
                    }

                }
            )
        }
        findViewById<ProgressBar>(R.id.progressbar).visibility = View.VISIBLE
    }

    @SuppressLint("SetTextI18n", "CutPasteId")
    fun setDataLayout(carbonData: ArrayList<Statistics>){

        findViewById<LinearLayout>(R.id.homepageLL)?.removeAllViews()
        findViewById<TextView>(R.id.headerText).visibility = View.VISIBLE

        var count = 0
        for(datum in carbonData) {

            count += 1

            val hll = LinearLayout(this)
            hll.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            hll.gravity = Gravity.START
            hll.orientation = LinearLayout.VERTICAL
            hll.setPadding(spToPx(15))

           val dateView = TextView(this)
           dateView.layoutParams = LinearLayout.LayoutParams(
               ViewGroup.LayoutParams.MATCH_PARENT,
               ViewGroup.LayoutParams.WRAP_CONTENT
           )
           dateView.gravity = Gravity.START
           dateView.text = "Date: ${datum.day}-${datum.month}-${datum.year}"
           dateView.setTextColor(Color.WHITE)

            val cycleView = TextView(this)
            cycleView.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            cycleView.gravity = Gravity.START
            cycleView.text = "Cycle: ${datum.cycle}"
            cycleView.setTextColor(Color.WHITE)

            val trendView = TextView(this)
            trendView.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            trendView.gravity = Gravity.START
            trendView.text = "Trend: ${datum.trend}"
            trendView.setTextColor(Color.WHITE)

           hll.addView(dateView)
           hll.addView(cycleView)
           hll.addView(trendView)

           findViewById<LinearLayout>(R.id.homepageLL)?.addView(hll)

       }

        findViewById<TextView>(R.id.headerText).text = "CO2 Data: $count entries"
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
       if(backpress + 2000 > System.currentTimeMillis())
           this.finishAffinity()
       else
           Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()

        backpress = System.currentTimeMillis()
        Log.i("System current Time in millis", backpress.toString())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun menu(item: MenuItem){

        navigableIntent = when(item.titleCondensed){
            "help" -> Intent(this@HomepageActivity, HelpActivity::class.java)
            "connect" -> Intent(this@HomepageActivity, SettingsActivity::class.java)
            "tasks" -> Intent(this@HomepageActivity, TasksActivity::class.java)
            "credits" -> Intent(this@HomepageActivity, CreditsActivity::class.java)
            else -> Intent(this@HomepageActivity, HomepageActivity::class.java)
        }

        startActivity(navigableIntent)
    }

    fun modelToJSON(requestModel: Any): String {
        val mapper = jacksonObjectMapper()
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestModel)
    }

    private fun spToPx(sp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp.toFloat(), applicationContext.resources.displayMetrics)
            .toInt()
    }

}