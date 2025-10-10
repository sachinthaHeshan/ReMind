package com.example.remind

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.example.remind.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MainActivity", "=== onCreate START ===")
        super.onCreate(savedInstanceState)
        
        try {
            Log.d("MainActivity", "Step 1: Inflating layout")
            binding = ActivityMainBinding.inflate(layoutInflater)
            
            Log.d("MainActivity", "Step 2: Setting content view")
            setContentView(binding.root)
            
            Log.d("MainActivity", "Step 3: Finding NavController")
            // Get NavController from the NavHostFragment
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment_content_main) as androidx.navigation.fragment.NavHostFragment
            val navController = navHostFragment.navController
            
            Log.d("MainActivity", "Step 4: Setting up AppBarConfiguration")
            // Set up AppBarConfiguration with top-level destinations
            appBarConfiguration = AppBarConfiguration(
                setOf(R.id.DailyHabitsFragment, R.id.MoodJournalFragment)
            )
            
            Log.d("MainActivity", "Step 5: Setting up bottom navigation")
            // Setup bottom navigation with NavController
            binding.bottomNavigation.setupWithNavController(navController)
            
            Log.d("MainActivity", "Step 6: Adding destination change listener")
            // Hide bottom navigation for certain destinations
            navController.addOnDestinationChangedListener { _, destination, _ ->
                Log.d("MainActivity", "Navigation: Changed to ${destination.id} - ${destination.label}")
                try {
                    when (destination.id) {
                        R.id.DailyHabitsFragment, R.id.MoodJournalFragment -> {
                            binding.bottomNavigation.visibility = View.VISIBLE
                            Log.d("MainActivity", "Bottom nav: VISIBLE")
                        }
                        else -> {
                            binding.bottomNavigation.visibility = View.GONE
                            Log.d("MainActivity", "Bottom nav: GONE")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("MainActivity", "Error in destination listener", e)
                }
            }
            
            Log.d("MainActivity", "=== onCreate COMPLETED SUCCESSFULLY ===")
            Toast.makeText(this, "App started successfully", Toast.LENGTH_SHORT).show()
            
        } catch (e: Exception) {
            Log.e("MainActivity", "=== CRITICAL ERROR in onCreate ===", e)
            Log.e("MainActivity", "Error message: ${e.message}")
            Log.e("MainActivity", "Error cause: ${e.cause}")
            e.printStackTrace()
            
            // Show error to user
            Toast.makeText(this, "Error starting app: ${e.message}", Toast.LENGTH_LONG).show()
            
            // Don't throw - let's try to recover
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_content_main) as androidx.navigation.fragment.NavHostFragment
        return navHostFragment.navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}