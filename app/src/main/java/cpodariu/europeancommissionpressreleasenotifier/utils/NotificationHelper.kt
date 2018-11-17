package cpodariu.europeancommissionpressreleasenotifier.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import cpodariu.europeancommissionpressreleasenotifier.R
import android.os.Build
import android.support.v4.app.NotificationCompat
import cpodariu.europeancommissionpressreleasenotifier.model.Article
import android.content.Intent
import android.net.Uri
import android.app.PendingIntent
import android.support.v4.app.NotificationManagerCompat
import cpodariu.europeancommissionpressreleasenotifier.data.db.database
import org.jetbrains.anko.db.*


class NotificationHelper(val ctx: Context) {
    private final val CHANNEL_ID = "PRESSRELEASENOTIFICATIONPRESSRELEASE"

    companion object {
        var notificationId = 1
    }

    @SuppressLint("WrongConstant")
    fun sendNotification(article: Article) {
        if (checkIfArticleSent(article))
            return
        else

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            val name = ctx.getString(R.string.channel_name)
            val description = ctx.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            // Register the channel with the system
            val notificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
        val browserPendingIntent = PendingIntent.getActivity(ctx, 0, browserIntent, 0)

        val mBuilder = NotificationCompat.Builder(ctx, CHANNEL_ID)
                .setSmallIcon(R.drawable.press_release_notification)
                .setContentTitle(article.title)
                .setContentText(article.articleDescription)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(browserPendingIntent)
                .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(ctx)

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(NotificationHelper.notificationId, mBuilder.build());
        NotificationHelper.notificationId++

    }

    fun checkIfArticleSent(a: Article): Boolean {
        val id = a.id
        var result = true
        ctx.database.use {
            val rowParser = StringParser

            ctx.database.use {
                val querryResult = select("SentNotifications", "_id").parseList(rowParser)
                if (querryResult.isEmpty())
                    result = false
            }
        }
        return result
    }

    fun saveArticle(a:Article){
        val id = a.id
        ctx.database.use{
            ctx.database.use{
                insert("SentNotifications", "_id" to id)
            }
        }
    }
}