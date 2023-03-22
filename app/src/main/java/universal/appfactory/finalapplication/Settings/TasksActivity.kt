package universal.appfactory.finalapplication.Settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import universal.appfactory.finalapplication.R

class TasksActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks)

        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setCustomView(R.layout.action_bar_layout)

        val backpress = findViewById<ImageView>(R.id.backpress)
        backpress.setOnClickListener {
            super.onBackPressed()
        }

    }

    fun task(view: View) {
        Toast.makeText(this, "Button ${view.tag} is clicked", Toast.LENGTH_SHORT).show()
    }
}