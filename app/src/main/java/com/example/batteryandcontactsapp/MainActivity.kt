package com.example.batteryandcontactsapp

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.batteryandcontactsapp.contacts.ContactsFragment
import com.example.batteryandcontactsapp.databinding.ActivityMainBinding
import com.example.batteryandcontactsapp.services.LoggerService.Companion.log

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var batteryChart: ChartBatteryChargeHistory
    private val batteryViewModel: BatteryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListener()
        setupBatteryInfo()

    }
    private fun setupListener() {
        binding.btnShowContacts.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainer.id, ContactsFragment())
                .addToBackStack(null)
                .commit()
            log(this,"button <show contacts> pressed")
        }

    }
    private fun setupBatteryInfo() {
        batteryChart = ChartBatteryChargeHistory(binding.chartBatteryChartHistory)
        batteryViewModel.batteryLevel.observe(this) { level ->
            binding.pbBatteryLevel.progress = level
            binding.tvBatteryLevel.text = level.toString() + "%"
            batteryChart.updateChart(level.toFloat())
        }
        log(this,"setup battery chart and progress bar")
    }
    fun showMainLayout(isVisible: Boolean) {
        binding.mainLayout.visibility = if (isVisible) View.VISIBLE else View.GONE
        binding.fragmentContainer.visibility = if (isVisible) View.GONE else View.VISIBLE

        val not = if(isVisible){""} else{" not"}
        log(this,"main layout is$not visible")
    }
}


