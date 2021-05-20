package com.jacode.sendemail.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.jacode.sendemail.MainActivity
import com.jacode.sendemail.databinding.ActivityUiconfigBinding
import com.jacode.sendemail.model.HostConfig
import com.jacode.sendemail.provider.ConfigurationProvider
//import com.tommasoberlose.progressdialog.ProgressDialogFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UIConfig : AppCompatActivity() {

    private lateinit var binding: ActivityUiconfigBinding

    //provider
    private val provider= ConfigurationProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityUiconfigBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val host=provider.readConfiguration(this)

        if (host.port!=0){
            binding.hostName.setText(host.host)
            binding.hostPort.setText(host.port.toString())
            binding.password.setText(host.password)
            binding.username.setText(host.username)
        }

        binding.btnSave.setOnClickListener {
            lifecycleScope.launch {
//                ProgressDialogFragment.showProgressBar(this@UIConfig)
                saveConfig()
            }
        }
    }

    private suspend fun saveConfig(){

        val host=binding.hostName.text.toString()
        val port=binding.hostPort.text.toString().toInt()
        val username=binding.username.text.toString()
        val password=binding.password.text.toString()
        val config=HostConfig(host, port, username, password)

        provider.writeConfiguration(this@UIConfig,config)
        delay(1200)
//        ProgressDialogFragment.hideProgressBar(this)

        delay(500)
        val intent=Intent(this@UIConfig,MainActivity::class.java)
        startActivity(intent)
    }
}