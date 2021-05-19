package com.jacode.sendemail.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.motion.widget.MotionLayout
import com.jacode.sendemail.MainActivity
import com.jacode.sendemail.databinding.ActivitySplashBinding
import com.jacode.sendemail.provider.ConfigurationProvider

class Splash : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    private val provider= ConfigurationProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.motionLayout.addTransitionListener(object:MotionLayout.TransitionListener{
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {

            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {

            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                if (checkConfiguration()){
                    val intent=Intent(this@Splash,MainActivity::class.java)
                    startActivity(intent)
                }else{
                    val intent=Intent(this@Splash,UIConfig::class.java)
                    startActivity(intent)
                }
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {

            }

        })
    }

    private fun checkConfiguration():Boolean{
        val host=provider.readConfiguration(this)
        return host.port!=0
    }
}