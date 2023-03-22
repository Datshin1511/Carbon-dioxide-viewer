package universal.appfactory.CarbonDioxideViewer.Settings

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.JSch
import universal.appfactory.CarbonDioxideViewer.R

class TasksActivity : AppCompatActivity() {

    lateinit var username: String
    lateinit var password: String
    lateinit var ip: String
    lateinit var port: String
    lateinit var sharedPreferences: SharedPreferences
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks)

        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setCustomView(R.layout.action_bar_layout)

        setData()
        findViewById<ImageView>(R.id.backpress).setOnClickListener {
            super.onBackPressed()
        }

    }

    fun task(view: View) {

        try{
            val jsch = JSch()
            val session = jsch.getSession(username, ip, port.toInt())
            session.setPassword(password)
            session.setConfig("StrictHostKeyChecking", "no")
            session.connect()

            val channel = session.openChannel("exec")
            (channel as ChannelExec).setCommand(returnCommand(view.tag as String))
            channel.connect()

            val input = channel.inputStream.bufferedReader().readText()
            val error = channel.errStream.bufferedReader().readText()

            Log.i("Server Output", input)
            Log.i("Error", error)

            channel.disconnect()
            session.disconnect()
            Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show()
        }
        catch (e: Exception){
            e.printStackTrace()
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }


    }

    fun returnCommand(tag: String): String{
        return when(tag){
            "1" -> "lg-relaunch"
            "2" -> "lg-reboot"
            "3" -> "lg-shutdown"
            "4" -> ""
            "5" -> ""
            "6" -> ""
            "7" -> ""
            "8" -> ""

            else -> "" // No suitable command
        }
    }

    fun setData(){
        sharedPreferences = getSharedPreferences("LGConnectionData", MODE_PRIVATE)

        username = sharedPreferences.getString("username", "lg").toString()
        password = sharedPreferences.getString("password", "lg").toString()
        port = sharedPreferences.getString("port", "22").toString()
        ip = sharedPreferences.getString("host", "192.168.201.3").toString()
    }

}