package com.jacode.sendemail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.jacode.sendemail.databinding.ActivityMainBinding
import com.jacode.sendemail.model.HostConfig
import com.jacode.sendemail.provider.ConfigurationProvider
import com.jacode.sendemail.ui.UIConfig
import kotlinx.coroutines.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import kotlin.coroutines.coroutineContext

class MainActivity : AppCompatActivity() {
    // injection view by view binding
    private lateinit var binding: ActivityMainBinding

    private val provider =ConfigurationProvider()
    private var host: HostConfig ?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        host=provider.readConfiguration(this@MainActivity)
        Log.e("host",host!!.toString())

        binding.emailFrom.setText(host!!.username)
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {

                R.id.action_send -> {
                    sendEmail()
                    true
                }

                R.id.action_config -> {
                    val intent = Intent(this@MainActivity,UIConfig::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
    }

    private fun sendEmail(){

        lifecycleScope.launch {
            withContext(Dispatchers.IO +Job()){
               try {
                   Transport.send(plainMail())
               }catch (e:MessagingException ) {
                   Log.e("Error",e.toString())
               }
            }
        }


    }

    private fun plainMail(): MimeMessage {
        val tos = binding.emailTo.text.toString()
        val from =host!!.username //Sender email

        val properties = System.getProperties()

        with (properties) {
            put("mail.smtp.host", host!!.host)
            put("mail.smtp.socketFactory.port", host!!.port)
            put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            put("mail.smtp.auth", "true")
            put("mail.smtp.port", host!!.port)
        }

        val auth = object: Authenticator() {
            override fun getPasswordAuthentication() =
                PasswordAuthentication(from, host!!.password) //Credentials of the sender email
        }

        val session = Session.getDefaultInstance(properties, auth)

        val message = MimeMessage(session)
        message.setFrom(from)
        message.setRecipient(Message.RecipientType.TO,InternetAddress(tos))
        message.subject=binding.subjectEmail.text.toString()
        message.setText(binding.emailBody.text.toString())

        return message
    }


}