package it.unito.billsplitter

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.parse.ParsePushBroadcastReceiver
import org.json.JSONException

import org.json.JSONObject




class NotificationReceiver: ParsePushBroadcastReceiver() {

    val PARSE_DATA_KEY = "com.parse.Data"

    override fun onPushReceive(context: Context?, intent: Intent?) {
        //val data = getDataFromIntent(intent!!)

        val pushData: JSONObject
        var alert: String? = null
        var title: String? = null
        try {
            pushData = JSONObject(intent?.extras?.getString(PARSE_DATA_KEY))
            alert = pushData.getString("alert")
            title = pushData.getString("title")
        } catch (e: JSONException) {
            println(e.message)
        }

        Log.i("PN", "alert is $alert")
        Log.i("PN", "title is $title")


        createNotificationChannel(context)
        var builder = NotificationCompat.Builder(context!!, "AA")
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(title)
            .setContentText(alert)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)


        with(NotificationManagerCompat.from(context!!)) {
            // notificationId is a unique int for each notification that you must define
            notify(1, builder.build())
        }
    }

    private fun createNotificationChannel(context: Context?) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "All"
            val descriptionText = "All notify"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("AA", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun getDataFromIntent(intent: Intent): JSONObject? {
        var data: JSONObject? = null
        try {
            data = JSONObject(intent.extras!!.getString(PARSE_DATA_KEY))
        } catch (e: JSONException) {
            println(e.message)
        }
        return data
    }

}