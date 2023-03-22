package universal.appfactory.CarbonDioxideViewer.Settings

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.JSch
import net.schmizz.sshj.SSHClient
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

        findViewById<Button>(R.id.connectButton).setOnClickListener {
            appendData()
            setupConnection()
        }

    }

    fun populateFields(){
        sharedPreferences = getSharedPreferences("ConnectionData", MODE_PRIVATE)

        findViewById<EditText>(R.id.master_username).setText(sharedPreferences.getString("username", "lg").toString())
        findViewById<EditText>(R.id.master_password).setText(sharedPreferences.getString("password", "lg").toString())
        findViewById<EditText>(R.id.ip_address).setText(sharedPreferences.getString("host", "192.168.201.3").toString())
        findViewById<EditText>(R.id.port).setText(sharedPreferences.getString("port", "22").toString())
        findViewById<EditText>(R.id.machine_count).setText(sharedPreferences.getString("machineCount", "3").toString())

        Log.i("Shared Preferences", "Fields are populated")

    }

    fun appendData(){

        sharedPreferences = getSharedPreferences("ConnectionData", MODE_PRIVATE)
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

    fun setupConnection(){

    }

}