package it.unito.billsplitter

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.parse.ParseObject
import com.parse.ParsePushBroadcastReceiver
import it.unito.billsplitter.activity.DetailActivity
import it.unito.billsplitter.model.Model
import org.json.JSONException
import org.json.JSONObject


class NotificationReceiver: ParsePushBroadcastReceiver() {

    val PARSE_DATA_KEY = "com.parse.Data"

    override fun onPushReceive(context: Context?, intent: Intent?) {
        //val data = getDataFromIntent(intent!!)

        val pushData: JSONObject
        var alert: String? = null
        var title: String? = null
        var split: String? = null
        try {
            pushData = JSONObject(intent?.extras?.getString(PARSE_DATA_KEY))
            alert = pushData.getString("alert")
            title = pushData.getString("title")
            split = pushData.getString("split")

        } catch (e: JSONException) {
            println(e.message)
        }

        Log.i("PN", "alert is $alert")
        Log.i("PN", "title is $title")
        Log.i("PN", "split is $split")

        if(split!=null){
            val split_obj = Model.instance.getSplit(split)
            createNotification(context, alert!!, title!!,split_obj!!)
        }
        else{
            println("Split is null!")
        }

    }

    @SuppressLint("WrongConstant")
    private fun createNotification(context: Context?, alert: String, title: String, data: ParseObject){
        createNotificationChannel(context)

        val myIntent = Intent(context, DetailActivity::class.java)
        myIntent.putExtra("split", data)

        val pendingIntent = PendingIntent.getActivity(
                context,
                3,
                myIntent,
                Intent.FLAG_ACTIVITY_NEW_TASK)

        var builder = NotificationCompat.Builder(context!!, "AA")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(alert)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)


        with(NotificationManagerCompat.from(context!!)) {
            // notificationId is a unique int for each notification that you must define
            notify(1, builder.build())
        }
    }

    private fun createNotificationChannel(context: Context?) {
 
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