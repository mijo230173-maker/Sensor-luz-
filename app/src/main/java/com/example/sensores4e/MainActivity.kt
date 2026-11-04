package com.example.sensores4e


import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity(), SensorEventListener {


    private lateinit var tvValores: TextView
    private lateinit var tvEstado: TextView
    private lateinit var tvTitulo: TextView
    private lateinit var layout: View
    private lateinit var sensorManager: SensorManager


    private var sensorLuz: Sensor? = null
    private var modoOscuro = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        tvValores = findViewById(R.id.tvValores)
        tvEstado = findViewById(R.id.tvEstado)
        tvTitulo = findViewById(R.id.tvTitulo)
        layout = findViewById(R.id.main)


        sensorManager =
            getSystemService(SENSOR_SERVICE) as SensorManager

        sensorLuz =
            sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)


        if (sensorLuz == null) {
            tvEstado.text = "No hay sensor de luz"
        }


        cambiarModo(false)


        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v, insets ->
            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
    }


    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_LIGHT) {

            val luz = event.values[0]
            tvValores.text = "Luz: $luz lx"


            if (luz < 50) {
                cambiarModo(true)
            } else if (luz > 500) {
                cambiarModo(false)
            }
        }
    }


    private fun cambiarModo(oscuro: Boolean) {
        modoOscuro = oscuro


        if (modoOscuro) {
            layout.setBackgroundColor(Color.BLACK)
            tvTitulo.setTextColor(Color.WHITE)
            tvValores.setTextColor(Color.WHITE)
            tvEstado.setTextColor(Color.WHITE)
            tvEstado.text = "Modo oscuro"
        } else {
            layout.setBackgroundColor(Color.WHITE)
            tvTitulo.setTextColor(Color.BLACK)
            tvValores.setTextColor(Color.BLACK)
            tvEstado.setTextColor(Color.BLACK)
            tvEstado.text = "Modo claro"
        }
    }


    override fun onAccuracyChanged(
        sensor: Sensor?,
        accuracy: Int
    ) {
    }

    override fun onResume() {
        super.onResume()


        sensorLuz?.let {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }


    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}