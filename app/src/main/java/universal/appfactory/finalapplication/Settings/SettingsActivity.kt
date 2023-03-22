package universal.appfactory.finalapplication.Settings

import android.annotation.SuppressLint
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
import universal.appfactory.finalapplication.R
import java.util.*
import java.io.BufferedReader
import java.io.InputStreamReader


class SettingsActivity : AppCompatActivity() {

    lateinit var username: String
    lateinit var password: String
    lateinit var ip: String
    lateinit var port: String
    lateinit var machineCount: String

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setCustomView(R.layout.action_bar_layout)

        val backpress = findViewById<ImageView>(R.id.backpress)
        val button = findViewById<Button>(R.id.connectButton)

        username = findViewById<EditText>(R.id.master_username).text.toString()
        password = findViewById<EditText>(R.id.master_password).text.toString()
        ip = findViewById<EditText>(R.id.ip_address).text.toString()
        port = findViewById<EditText>(R.id.port).text.toString()
        machineCount = findViewById<EditText>(R.id.machine_count).text.toString()

        backpress.setOnClickListener {
            super.onBackPressed()
        }

        button.setOnClickListener {
            val jsch = JSch()
            val session = jsch.getSession(username, ip, port.toInt())
            session.setPassword(password)

            val config = Properties()
            config["StrictHostKeyChecking"] = "no"
            session.setConfig(config)
            session.connect()

            val channel = session.openChannel("exec") as ChannelExec
            channel.setCommand("lg-relaunch")
            channel.connect()
            val input = BufferedReader(InputStreamReader(channel.inputStream))
            while (true) {
                val line = input.readLine() ?: break
                Log.i("SSH Message", line)
            }

            Toast.makeText(this, "Connected successfully", Toast.LENGTH_SHORT).show()
            channel.disconnect()
        }

    }
}