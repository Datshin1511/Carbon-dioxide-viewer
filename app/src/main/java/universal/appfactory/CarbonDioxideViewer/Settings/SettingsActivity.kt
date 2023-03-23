package universal.appfactory.CarbonDioxideViewer.Settings

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.jcraft.jsch.*
import universal.appfactory.CarbonDioxideViewer.R
import java.util.*


class SettingsActivity : AppCompatActivity() {

    lateinit var username: String
    lateinit var password: String
    lateinit var ip: String
    lateinit var port: String
    lateinit var machineCount: String

    var connectionFlag: Boolean = false

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
                if(!connectionFlag){
                    appendData()
                    setupConnection()
                }
                else{

                }

            }
        }

    }

    fun setupConnection(){

        try{
            val jsch = JSch()
            val session = jsch.getSession(username, ip, port.toInt())
            session.setPassword(password)
            session.timeout = 10000
            session.setConfig("StrictHostKeyChecking", "no")
            session.connect()

            val channel = session.openChannel("exec")
            (channel as ChannelExec).setCommand("lg-relaunch")
            channel.connect()

            if(channel.isConnected) {
                Toast.makeText(this, "Connection Success", Toast.LENGTH_SHORT).show()
                findViewById<TextView>(R.id.connection_status).text = "CONNECTED"
                findViewById<TextView>(R.id.connection_status).setTextColor(Color.GREEN)
                findViewById<Button>(R.id.connectButton).text = "DISCONNECT"
            }
            else{
                findViewById<Button>(R.id.connectButton).text = "CONNECT TO LG"
                Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show()
            }


            val input = channel.inputStream.bufferedReader().readText()
            val error = channel.errStream.bufferedReader().readText()

            Log.i("Server Output", input)
            Log.i("Error", error)
        }
        catch (e: Exception){
            e.printStackTrace()
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
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