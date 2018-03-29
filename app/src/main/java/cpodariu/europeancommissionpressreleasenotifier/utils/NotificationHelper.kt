package cpodariu.europeancommissionpressreleasenotifier.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.view.View
import cpodariu.europeancommissionpressreleasenotifier.R
import android.os.Build
import android.provider.Settings.Global.getString
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import cpodariu.europeancommissionpressreleasenotifier.model.Article
import android.content.Intent
import android.net.Uri
import android.app.PendingIntent




class NotificationHelper(val ctx: Context) {
    private final val CHANNEL_ID = "PRESSRELEASENOTIFICATIONPRESSRELEASE"

    companion object {
        var notificationId = 1
    }

    @SuppressLint("WrongConstant")
    fun sendNotification(article: Article) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            val name = ctx.getString(R.string.channel_name)
            val description = ctx.getString(R.string.channel_description)
            val importance = NotificationManagerCompat.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            // Register the channel with the system
            val notificationManager = NotificationManagerCompat.from(ctx) as NotificationManager
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

}