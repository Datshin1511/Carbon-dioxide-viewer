package universal.appfactory.CarbonDioxideViewer.home

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.AsyncTask
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
import androidx.loader.content.AsyncTaskLoader
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import universal.appfactory.CarbonDioxideViewer.API.ApiInterface
import universal.appfactory.CarbonDioxideViewer.API.ServiceBuilder
import universal.appfactory.CarbonDioxideViewer.R
import universal.appfactory.CarbonDioxideViewer.Settings.CreditsActivity
import universal.appfactory.CarbonDioxideViewer.Settings.HelpActivity
import universal.appfactory.CarbonDioxideViewer.Settings.SettingsActivity
import universal.appfactory.CarbonDioxideViewer.Settings.TasksActivity
import universal.appfactory.CarbonDioxideViewer.data.*


@Suppress("UNUSED_PARAMETER")
class HomepageActivity : AppCompatActivity() {

    lateinit var navigableIntent: Intent
    var backpress: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        findViewById<Button>(R.id.carbonDataButton).setOnClickListener {
            CO2AsyncTask().execute()
        }

    }

    @SuppressLint("StaticFieldLeak")
    inner class CO2AsyncTask: AsyncTask<String, String, String>() {

        @Deprecated("Deprecated in Java")
        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<ProgressBar>(R.id.progressbar).visibility = View.VISIBLE
        }

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg p0: String?): String {
            //Accessing API Interface for obtaining CO2 data
            val response = ServiceBuilder.buildService1(ApiInterface::class.java)

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
                        }

                        override fun onFailure(call: Call<DataResponseModel>, t: Throwable) {
                            Toast.makeText(this@HomepageActivity, "Error", Toast.LENGTH_SHORT).show()
                            Log.e("Error message", t.toString())
                        }

                    }
                )
            }
            return "API Response text"
        }

        @Deprecated("Deprecated in Java")
        override fun onPostExecute(result: String?) {
            Log.i("CO2 data", "Data is fetched")
            findViewById<ProgressBar>(R.id.progressbar).visibility = View.GONE
        }

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

            val cycleView = TextView(this)
            cycleView.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            cycleView.gravity = Gravity.START
            cycleView.text = "Cycle: ${datum.cycle}"

            val trendView = TextView(this)
            trendView.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            trendView.gravity = Gravity.START
            trendView.text = "Trend: ${datum.trend}"

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

    private fun spToPx(sp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp.toFloat(), applicationContext.resources.displayMetrics)
            .toInt()
    }

}