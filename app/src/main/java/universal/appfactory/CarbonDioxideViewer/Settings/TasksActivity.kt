package universal.appfactory.CarbonDioxideViewer.Settings

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import universal.appfactory.CarbonDioxideViewer.R
import java.io.BufferedReader
import java.io.InputStreamReader

class TasksActivity : AppCompatActivity() {

    lateinit var username: String
    lateinit var password: String
    lateinit var ip: String
    lateinit var port: String
    lateinit var machineCount: String
    lateinit var sharedPreferences: SharedPreferences
    lateinit var tag: String

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

    fun task(view: View){
        tag = view.tag.toString()
        LGTaskAsyncTask().execute()
    }
    @SuppressLint("StaticFieldLeak")
    inner class LGTaskAsyncTask: AsyncTask<String, String, String>() {

        @Deprecated("Deprecated in Java")
        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<ProgressBar>(R.id.progressbar).visibility = View.VISIBLE
        }

        @Deprecated("Deprecated in Java", ReplaceWith("task(tag)"))
        override fun doInBackground(vararg p0: String?): String {
            return LGTask(tag)
        }

        @Deprecated("Deprecated in Java")
        override fun onPostExecute(result: String?) {

            if(result == "Success"){
                runOnUiThread {
                    Toast.makeText(this@TasksActivity, "Done", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                runOnUiThread {
                    Toast.makeText(this@TasksActivity, "Error", Toast.LENGTH_SHORT).show()
                }
            }
            findViewById<ProgressBar>(R.id.progressbar).visibility = View.GONE
        }

    }
    fun LGTask(tag: String): String {

        // Actual code
        when(tag){
            "1" -> {

                var message = "Success"

                try{
                    val session = setSession()
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
                        message = "Failure"
                        e.printStackTrace()
                    }

                return message
            }

            "2" -> {
                var message = "Success"
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
                    message = "Failure"
                }

                return message
            }

            "3" -> {
                var message = "Success"
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
                    message = "Failure"
                }

                return message
            }

            "4" -> {
                var message = "Success"
                val kmlText: String?
                val localPath: String?
                val projectName: String?
                val isOpen: Boolean = false
                val isSuccess: Boolean = false
                val blackAndWhite: Boolean = false

                try{
                    val kmlname = "Sample name"
                    val directory = applicationContext.filesDir
                    localPath = """${directory.path}/$kmlname.txt"""
                }
                catch(e: Exception){
                    message = "Failure"
                    e.printStackTrace()
                }
                return message
            }

            "5" -> {
                val message = "Success"
                return message
            }

            "6" -> {
                var message = "Success"
                try{
                    val session = setSession()
                    session.connect()

                    val blank = """<?xml version="1.0" encoding="UTF-8"?>
                                    <kml xmlns="http://www.opengis.net/kml/2.2" xmlns:gx="http://www.google.com/kml/ext/2.2" xmlns:kml="http://www.opengis.net/kml/2.2" xmlns:atom="http://www.w3.org/2005/Atom">
                                    <Document>
                                    </Document>
                                    </kml>"""
                    val channel = session.openChannel("exec")

                    val command = """echo '$blank' > /var/www/html/kml/slave_$machineCount.kml"""
                    (channel as ChannelExec).setCommand(command)
                    channel.connect()

                    // Disconnection from the rigs
                    channel.disconnect()
                    session.connect()

                }
                catch(e: Exception){
                    message = "Failure"
                    e.printStackTrace()
                }

                return message
            }

            "7" -> {
                var message = "Success"
                try{
                    val session = setSession()
                    session.connect()

                    val search = """<href>##LG_PHPIFACE##kml\\/slave_{{slave}}.kml<\\/href>"""
                    val replace = """<href>##LG_PHPIFACE##kml\\/slave_{{slave}}.kml<\\/href><refreshMode>onInterval<\\/refreshMode><refreshInterval>2<\\/refreshInterval>"""
                    val command = """echo $password | sudo -S sed -i "s/$search/$replace/" ~/earth/kml/slave/myplaces.kml"""
                    val clear = """echo $password | sudo -S sed -i "s/$replace/$search/" ~/earth/kml/slave/myplaces.kml"""

                    for(i in 1..machineCount.toInt()){
                        val channel = session.openChannel("exec")

                        val clearCmd = clear.replace("{{slave}}", i.toString())
                        val cmd = command.replace("{{slave}}", i.toString())
                        val query = """sshpass -p $password ssh -t lg$i \'{{cmd}}\'"""

                        query.replace("{{cmd}}", clearCmd)
                        (channel as ChannelExec).setCommand(query.replace("{{cmd}}", clearCmd))
                        channel.setCommand(query.replace("{{cmd}}", cmd))
                        channel.connect()

                        // Disconnection from the rigs
                        channel.disconnect()
                        session.disconnect()
                    }
                }
                catch(e: Exception){
                    message = "Failure"
                    e.printStackTrace()
                }
                return message
            }

            "8" -> {
                var message = "Success"
                try{
                    val session = setSession()
                    session.connect()

                    val channel = session.openChannel("exec")
                    val search = """<href>##LG_PHPIFACE##kml\\/slave_{{slave}}.kml<\\/href><refreshMode>onInterval<\\/refreshMode><refreshInterval>2<\\/refreshInterval>"""
                    val replace = """<href>##LG_PHPIFACE##kml\\/slave_{{slave}}.kml<\\/href>"""
                    val clear = """echo $password | sudo -S sed -i "s/$search/$replace/" ~/earth/kml/slave/myplaces.kml"""

                    for(i in 1..machineCount.toInt()){
                        val cmd = clear.replace("{{slave}}", i.toString())
                        val query = """sshpass -p $password ssh -t lg$i \'$cmd\'"""
                        (channel as ChannelExec).setCommand(query)
                        channel.connect()
                    }

                    // Disconnection from rigs
                    channel.disconnect()
                    session.disconnect()
                }
                catch(e: Exception){
                    message = "Failure"
                    e.printStackTrace()
                }
                return message
            }

            else -> {
                Log.i("Task Activity", "No command was executed")
                return "Failure"} // No suitable command
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

}