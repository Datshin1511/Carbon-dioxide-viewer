package universal.appfactory.CarbonDioxideViewer.Settings

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.JSch
import universal.appfactory.CarbonDioxideViewer.R
import java.io.BufferedReader
import java.io.InputStreamReader
import universal.appfactory.CarbonDioxideViewer.Settings.SettingsActivity

class TasksActivity : AppCompatActivity() {

    lateinit var username: String
    lateinit var password: String
    lateinit var ip: String
    lateinit var port: String
    lateinit var machineCount: String
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
        val tag = view.tag.toString()

        // Actual code
        when(tag){
            "1" -> {
                try{
                    val jsch = JSch()
                    val session = jsch.getSession(username, ip, port.toInt())
                    session.setPassword(password)
                    session.setConfig("StrictHostKeyChecking", "no")
                    session.connect()

                    if(session.isConnected){
                        val channel = session.openChannel("exec")

                        for(i in 1..machineCount.toInt()){
                            val relaunchCommand = """RELAUNCH_CMD="\\
if [ -f /etc/init/lxdm.conf ]; then
  export SERVICE=lxdm
elif [ -f /etc/init/lightdm.conf ]; then
  export SERVICE=lightdm
else
  exit 1
fi
if  [[ \\\$(service \\\${'$'}{SERVICE} status) =~ 'stop' ]]; then
  echo $password | sudo -S service \\\${'$'}{SERVICE} start
else
  echo $password | sudo -S service \\\${'$'}{SERVICE} restart
fi
" && sshpass -p $password ssh -x -t lg@lg$i "\${'$'}RELAUNCH_CMD\""""

                            (channel as ChannelExec).setCommand(""""/home/${username}/bin/lg-relaunch" > /home/${username}/log.txt""")
                            channel.setCommand(relaunchCommand)
                            channel.connect()
                        }

                        val input = channel.inputStream
                        val output = BufferedReader(InputStreamReader(input)).readLine()

                        Log.i("Server Output", output)
                        Toast.makeText(this, "Output: $output", Toast.LENGTH_SHORT).show()

                        // Disconnection from the rigs
                        channel.disconnect()
                        session.disconnect()
                    }
                    else{
                        MaterialAlertDialogBuilder(this).setTitle("ERROR")
                            .setMessage("Not connected to LG rigs. Connect and try again.")
                            .setPositiveButton("OK"){ _,_ -> }
                    }
                }
                catch (e: java.lang.Exception){
                    e.printStackTrace()
                }
            }

            "2" -> {
                try{
                    val jsch = JSch()
                    val session = jsch.getSession(username, ip, port.toInt())
                    session.setPassword(password)
                    session.timeout = 10000
                    session.setConfig("StrictHostKeyChecking", "no")
                    session.connect()

                    val channel = session.openChannel("exec")
                    (channel as ChannelExec).setCommand("echo HEY, I'M HERE !")
                    channel.connect()

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
            "3" -> "lg-shutdown"
            "4" -> ""
            "5" -> ""
            "6" -> ""
            "7" -> ""
            "8" -> ""

            else -> Log.i("Task Activity", "No command was executed") // No suitable command
        }


    }

    fun setData(){
        sharedPreferences = getSharedPreferences("LGConnectionData", MODE_PRIVATE)

        username = sharedPreferences.getString("username", "lg").toString()
        password = sharedPreferences.getString("password", "lg").toString()
        port = sharedPreferences.getString("port", "22").toString()
        ip = sharedPreferences.getString("host", "192.168.201.3").toString()
        machineCount = (kotlin.math.floor(
            ((sharedPreferences.getString("machineCount", "0")!!.toDouble()) / 2)
        ) + 2).toInt().toString()

    }

}