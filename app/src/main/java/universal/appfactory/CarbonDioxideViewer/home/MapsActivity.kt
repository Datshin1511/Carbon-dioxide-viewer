package universal.appfactory.CarbonDioxideViewer.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import universal.appfactory.CarbonDioxideViewer.R
import universal.appfactory.CarbonDioxideViewer.Settings.CreditsActivity
import universal.appfactory.CarbonDioxideViewer.Settings.HelpActivity
import universal.appfactory.CarbonDioxideViewer.Settings.SettingsActivity
import universal.appfactory.CarbonDioxideViewer.Settings.TasksActivity
import universal.appfactory.CarbonDioxideViewer.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var navigableIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun menu(item: MenuItem){

        navigableIntent = when(item.titleCondensed){
            "help" -> Intent(this@MapsActivity, HelpActivity::class.java)
            "connect" -> Intent(this@MapsActivity, SettingsActivity::class.java)
            "tasks" -> Intent(this@MapsActivity, TasksActivity::class.java)
            "credits" -> Intent(this@MapsActivity, CreditsActivity::class.java)
            else -> Intent(this@MapsActivity, HomepageActivity::class.java)
        }

        startActivity(navigableIntent)
    }

}