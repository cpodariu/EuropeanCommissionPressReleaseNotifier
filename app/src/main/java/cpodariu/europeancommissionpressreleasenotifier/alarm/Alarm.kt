package cpodariu.europeancommissionpressreleasenotifier.alarm

import android.R.string.cancel
import android.annotation.SuppressLint
import android.content.Context.ALARM_SERVICE
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.widget.Toast
import android.os.PowerManager
import android.content.Context.POWER_SERVICE
import android.content.BroadcastReceiver
import android.content.Context
import android.util.Log
import cpodariu.europeancommissionpressreleasenotifier.data.db.database
import cpodariu.europeancommissionpressreleasenotifier.model.Article
import cpodariu.europeancommissionpressreleasenotifier.model.KeyWord
import cpodariu.europeancommissionpressreleasenotifier.network_helpers.RequestHelper
import cpodariu.europeancommissionpressreleasenotifier.utils.NotificationHelper
import kotlinx.coroutines.experimental.selects.select
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.update
import org.jetbrains.anko.doAsync
import kotlin.math.log


class Alarm : BroadcastReceiver() {
    @SuppressLint("InvalidWakeLockTag", "UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "")
        wl.acquire(10 * 60 * 1000L)

        doAsync {
            sendNotifications(context)

        if (intent.action != null)
            if (intent.action.contains("BOOT_COMPLETED"))
                setAlarm(context)
        }
        wl.release()

        Log.e("wakelog ahaha", "released")
    }

    fun sendNotifications(context: Context) {
        Log.e("CoolAlarm: ","sendNotifications called")
        val rowParser = classParser<KeyWord>()
        lateinit var querryResult: List<KeyWord>
        context.database.use {
            querryResult = select("KeyWord").parseList(rowParser)
        }
        for (i in querryResult) {
            val articles = Article.getAllArticles(i.word.replace(" ", "+"))

            if (articles.size == 0)
                return

            if (i.lastId == "") {
                context.database.use {
                    update("KeyWord", "lastID" to articles[0].id)
                            .whereSimple("_id = ?", i.id.toString())
                            .exec()
                }
            } else {
                var j = 0
                while (articles[j].id != i.lastId && j < articles.size){
                    NotificationHelper(context).sendNotification(articles[j])
                    j ++
                }
                context.database.use {
                    update("KeyWord", "lastID" to articles[0].id)
                            .whereSimple("_id = ?", i.id.toString())
                            .exec()
                }
            }
        }
    }

    fun setAlarm(context: Context) {
        Log.e("Alarm", "setAlarm called")
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val i = Intent(context, Alarm::class.java)
        val pi = PendingIntent.getBroadcast(context, 0, i, 0)
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), (1000 * 6).toLong(), pi) // Millisec * Second * Minute
    }

    fun cancelAlarm(context: Context) {
        val intent = Intent(context, Alarm::class.java)
        val sender = PendingIntent.getBroadcast(context, 0, intent, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(sender)
    }
}