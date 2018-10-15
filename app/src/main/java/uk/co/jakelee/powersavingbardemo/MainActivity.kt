package uk.co.jakelee.powersavingbardemo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var powerSavingReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupPowerSavingReceiver()
        }
    }

    override fun onStop() {
        super.onStop()
        powerSavingReceiver?.let { unregisterReceiver(it) }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setupPowerSavingReceiver() {
        togglePowerSavingBar((getSystemService(Context.POWER_SERVICE) as PowerManager).isPowerSaveMode)
        powerSavingReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val powerManager: PowerManager =
                        context.getSystemService(Context.POWER_SERVICE) as PowerManager
                togglePowerSavingBar(powerManager.isPowerSaveMode)
                Log.e("PowerSaving", "Enabled: ${powerManager.isPowerSaveMode}")
            }
        }
        registerReceiver(
                powerSavingReceiver,
                IntentFilter("android.os.action.POWER_SAVE_MODE_CHANGED")
        )
    }

    private fun togglePowerSavingBar(display: Boolean) {
        if (display && power_saving_bar.visibility != View.VISIBLE) {
            val enterAnim = AnimationUtils.loadAnimation(this, R.anim.enter_from_bottom)
            power_saving_bar.startAnimation(enterAnim)
        } else if (!display && power_saving_bar.visibility != View.GONE) {
            val exitAnim = AnimationUtils.loadAnimation(this, R.anim.exit_to_bottom)
            power_saving_bar.startAnimation(exitAnim)
        }
        power_saving_bar.visibility = if (display) View.VISIBLE else View.GONE
    }
}
