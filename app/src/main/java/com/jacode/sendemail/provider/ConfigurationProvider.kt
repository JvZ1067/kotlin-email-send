package com.jacode.sendemail.provider

import android.content.Context
import com.jacode.sendemail.R
import com.jacode.sendemail.model.HostConfig

class ConfigurationProvider {

    fun readConfiguration(context: Context): HostConfig {
        val sharedPref = context.getSharedPreferences(
            context.getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        val host = sharedPref?.getString(context.getString(R.string.HOST_NAME), "null")!!
        val port = sharedPref.getInt(context.getString(R.string.PUERTO_VALUE), 0)
        val username = sharedPref.getString(context.getString(R.string.USERNAME_VALUE), "null")!!
        val password = sharedPref.getString(context.getString(R.string.PASSWORD_VALUE), "null")!!
        return HostConfig(host, port, username, password)
    }
    fun writeConfiguration(context:Context, config:HostConfig) {
        val sharedPref=context.getSharedPreferences(
            context.getString(R.string.preference_file_key), Context.MODE_PRIVATE)?:return
        with(sharedPref.edit()){
            putString(context.getString(R.string.HOST_NAME), config.host)
            putInt(context.getString(R.string.PUERTO_VALUE), config.port)
            putString(context.getString(R.string.USERNAME_VALUE), config.username)
            putString(context.getString(R.string.PASSWORD_VALUE), config.password)
            commit()
        }

    }

}