package universal.appfactory.CarbonDioxideViewer

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import universal.appfactory.CarbonDioxideViewer.home.HomepageActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()
        val navigableIntent = Intent(this@MainActivity, HomepageActivity::class.java)

        val timer = object: CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                if(isOnline(this@MainActivity)){
                    Log.i("Internet connections", "Valid and fine to start")

                    // Starts Homepage Activity
                    startActivity(navigableIntent)
                    this@MainActivity.finish()
                }
                else{
                    MaterialAlertDialogBuilder(this@MainActivity).setTitle("ERROR !")
                        .setMessage("You need an internet connection to get started. Kindly connect to an internet connection.")
                        .setPositiveButton("OK"){ dialog, which ->
                            this@MainActivity.finishAffinity()
                        }
                        .create().show()

                    Log.i("Internet connections", "Connections invalid. Need an internet connection to proceed")
                }
            }
        }
        timer.start()
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }
}