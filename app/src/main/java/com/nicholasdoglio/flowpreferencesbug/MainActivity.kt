package com.nicholasdoglio.flowpreferencesbug

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.tfcporciuncula.flow.FlowSharedPreferences
import com.tfcporciuncula.flow.Preference
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val tag by lazy { this.javaClass.simpleName }

    private val preferences by lazy {
        FlowSharedPreferences(PreferenceManager.getDefaultSharedPreferences(this.applicationContext))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nightModePref: Preference<Int> = preferences.getInt(NIGHT_MODE_KEY)

        night_mode_button.setOnClickListener {
            val selection = (0..2).random()

            Log.d(tag, "SELECTION $selection")

            GlobalScope.launch { nightModePref.setAndCommit(selection) }
        }

        nightModePref.asFlow()
            .onEach { Log.d(tag, "NIGHT MODE CHANGE: $it") }
            .launchIn(GlobalScope)
    }

    companion object {
        private const val NIGHT_MODE_KEY = "NIGHT_MODE_KEY"
    }
}
