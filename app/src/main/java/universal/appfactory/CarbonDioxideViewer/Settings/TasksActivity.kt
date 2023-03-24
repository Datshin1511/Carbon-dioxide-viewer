package universal.appfactory.CarbonDioxideViewer.Settings

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
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
import com.jcraft.jsch.Session
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

        // Actual code
        when(view.tag.toString()){
            "1" -> {
                try{
                    val jsch = JSch()
                    val session = jsch.getSession(username, ip, port.toInt())
                    session.setPassword(password)
                    session.setConfig("StrictHostKeyChecking", "no")
                    session.connect()

                    val channel = session.openChannel("exec")

                    for(i in 1..machineCount.toInt()){
                        val relaunchCommand = """RELAUNCH_CMD='if [ -f /etc/init/lxdm.conf ]; then export SERVICE=lxdm; elif [ -f /etc/init/lightdm.conf ]; then export SERVICE=lightdm; else exit 1; fi; if [[ $(service ${'$'}{SERVICE} status) =~ "stop" ]]; then echo $password | sudo -S service ${'$'}{SERVICE} start; else echo $password | sudo -S service ${'$'}{SERVICE} restart; fi' && sshpass -p $password ssh -x -t lg@lg$i "${'$'}RELAUNCH_CMD""""

                        (channel as ChannelExec).setCommand(relaunchCommand)
                        channel.connect()
                        Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show()
                    }

                    val input = channel.inputStream
                    val output = BufferedReader(InputStreamReader(input)).readLine()

                    Log.i("Server Output", output)
                    Toast.makeText(this, "Output: $output", Toast.LENGTH_SHORT).show()

                    // Disconnection from the rigs
                    channel.disconnect()
                    session.disconnect()
                }
                    catch (e: java.lang.Exception){
                        e.printStackTrace()
                    }
            }

            "2" -> {
                try{
                    val session = setSession()
                    session.connect()

                    val channel = session.openChannel("exec")

                    for(i in 1..machineCount.toInt()){
                        val rebootCommand = """sshpass -p $password ssh -t lg$i "echo $password | sudo -S reboot""""

                        (channel as ChannelExec).setCommand(rebootCommand)
                        channel.connect()
                        Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show()
                    }

                    val output = BufferedReader(InputStreamReader(channel.inputStream)).readLine()
                    Log.i("Output", output)

                    // Disconnection from the rigs
                    channel.disconnect()
                    session.disconnect()
                }
                catch (e: Exception){
                    e.printStackTrace()
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
            }

            "3" -> {
                try{
                    val session = setSession()
                    session.connect()

                    val channel = session.openChannel("exec")

                    for(i in 1..machineCount.toInt()){
                        val shutdownCommand = """sshpass -p $password ssh -t lg$i "echo $password | sudo -S poweroff""""

                        (channel as ChannelExec).setCommand(shutdownCommand)
                        channel.connect()
                        Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show()
                    }

                    val output = BufferedReader(InputStreamReader(channel.inputStream)).readLine()
                    Log.i("Output", output)

                    // Disconnection from the rigs
                    channel.disconnect()
                    session.disconnect()
                }
                catch (e: Exception){
                    e.printStackTrace()
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
            }
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

    fun setSession(): Session {
        val jsch = JSch()
        val session = jsch.getSession(username, ip, port.toInt())
        session.setPassword(password)
        session.timeout = 10000
        session.setConfig("StrictHostKeyChecking", "no")

        return session
    }

    // Old Relaunch command
//    val relaunchCommand = """RELAUNCH_CMD="\\
//if [ -f /etc/init/lxdm.conf ]; then
//  export SERVICE=lxdm
//elif [ -f /etc/init/lightdm.conf ]; then
//  export SERVICE=lightdm
//else
//  exit 1
//fi
//if  [[ \\\$(service \\\${'$'}{SERVICE} status) =~ 'stop' ]]; then
//  echo $password | sudo -S service \\\${'$'}{SERVICE} start
//else
//  echo $password | sudo -S service \\\${'$'}{SERVICE} restart
//fi
//" && sshpass -p $password ssh -x -t lg@lg$i "\${'$'}RELAUNCH_CMD\""""


}