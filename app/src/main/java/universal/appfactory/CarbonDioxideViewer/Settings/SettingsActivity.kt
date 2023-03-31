package universal.appfactory.CarbonDioxideViewer.Settings

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Color
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.jcraft.jsch.*
import universal.appfactory.CarbonDioxideViewer.R
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*


class SettingsActivity : AppCompatActivity() {

    lateinit var username: String
    lateinit var password: String
    lateinit var ip: String
    lateinit var port: String
    lateinit var machineCount: String

    lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setCustomView(R.layout.action_bar_layout)

        populateFields()

        findViewById<ImageView>(R.id.backpress).setOnClickListener { super.onBackPressed() }
        findViewById<TextView>(R.id.connection_status).text = "DISCONNECTED"
        findViewById<TextView>(R.id.connection_status).setTextColor(Color.RED)

        val button = findViewById<Button>(R.id.connectButton)


        val SDK_INT = Build.VERSION.SDK_INT
        if (SDK_INT > 8) {
            val policy = StrictMode.ThreadPolicy.Builder()
                .permitAll().build()
            StrictMode.setThreadPolicy(policy)
            //your codes here
            button.setOnClickListener {
                appendData()
                LGConnectionAsyncTask().execute()
            }
        }

    }

    @SuppressLint("StaticFieldLeak")
    inner class LGConnectionAsyncTask: AsyncTask<String, String, String>() {

        @Deprecated("Deprecated in Java")
        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<ProgressBar>(R.id.progressbar).visibility = View.VISIBLE
            findViewById<Button>(R.id.connectButton).text = "CONNECTING..."
        }

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg p0: String?): String {
            try{
                val jsch = JSch()
                val session = jsch.getSession(username, ip, port.toInt())
                session.setPassword(password)
                session.timeout=20000
                session.setConfig("StrictHostKeyChecking", "no")
                session.connect()

                val channel = session.openChannel("exec")
                (channel as ChannelExec).setCommand("""echo "Connection set !"""")
                channel.connect()

                val output = BufferedReader(InputStreamReader(channel.inputStream)).readLine().toString()
                Log.i("Output", output)

                return if(channel.isConnected) {
                    "Success"
                } else{
                    "Failure"
                }

            }
            catch (e: Exception){
                e.printStackTrace()
                return "Timeout"
            }

        }

        @Deprecated("Deprecated in Java")
        override fun onPostExecute(result: String?) {
            Log.i("CO2 data", result.toString())

            if(result == "Success"){
                findViewById<TextView>(R.id.connection_status).text = "CONNECTED"
                findViewById<TextView>(R.id.connection_status).setTextColor(Color.GREEN)
                findViewById<Button>(R.id.connectButton).text = "DISCONNECT"
            }
            else{
                findViewById<Button>(R.id.connectButton).text = "CONNECT TO LG"
                findViewById<TextView>(R.id.connection_status).text = "DISCONNECTED"
                findViewById<TextView>(R.id.connection_status).setTextColor(Color.RED)
                runOnUiThread {
                    Toast.makeText(this@SettingsActivity, "Error", Toast.LENGTH_SHORT).show()
                }
            }
            findViewById<ProgressBar>(R.id.progressbar).visibility = View.GONE

        }

    }

    fun populateFields(){
        sharedPreferences = getSharedPreferences("LGConnectionData", MODE_PRIVATE)

        findViewById<EditText>(R.id.master_username).setText(sharedPreferences.getString("username", "lg").toString())
        findViewById<EditText>(R.id.master_password).setText(sharedPreferences.getString("password", "lg").toString())
        findViewById<EditText>(R.id.ip_address).setText(sharedPreferences.getString("host", "192.168.201.3").toString())
        findViewById<EditText>(R.id.port).setText(sharedPreferences.getString("port", "22").toString())
        findViewById<EditText>(R.id.machine_count).setText(sharedPreferences.getString("machineCount", "3").toString())

        Log.i("Shared Preferences", "Fields are populated")

    }

    fun appendData(){

        val editPreferences: SharedPreferences.Editor = sharedPreferences.edit()

        username = findViewById<EditText>(R.id.master_username).text.toString()
        password = findViewById<EditText>(R.id.master_password).text.toString()
        ip = findViewById<EditText>(R.id.ip_address).text.toString()
        port = findViewById<EditText>(R.id.port).text.toString()
        machineCount = findViewById<EditText>(R.id.machine_count).text.toString()

        editPreferences.putString("username", username)
        editPreferences.putString("password", password)
        editPreferences.putString("host", ip)
        editPreferences.putString("port", port)
        editPreferences.putString("machineCount", machineCount)

        editPreferences.apply()
        Log.i("Shared Preferences", "Data is appended")
    }

}