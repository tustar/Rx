package com.tustar.rxjava.ui


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.tustar.rxjava.MainActivity
import com.tustar.rxjava.R
import com.tustar.rxjava.util.Logger
import com.tustar.rxjava.util.plus
import com.tustar.view.clicks
import com.tustar.view.tap
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_notification.*
import java.util.concurrent.TimeUnit

class NotificationActivity : AppCompatActivity() {

    private var mDisposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        mDisposables + normal_bar_notification.tap().subscribe {
            showNormalBarNotification(it.hashCode())
        }

        mDisposables + high_notification.tap().subscribe {
            showHighNotification(it.hashCode())
        }

        mDisposables + high_fullscreen_notification.tap().subscribe {
            showHighFullscreenNotification(it.hashCode())
        }

        val maxCountTime = 4L
        val text = "高优先级全屏通知(悬浮通知栏通知．不会自动消失), 延迟%d秒发送"
        mDisposables + high_fullscreen_notification_4.clicks()
                .throttleFirst(maxCountTime, TimeUnit.SECONDS)
                .flatMap {
                    high_fullscreen_notification_4.isEnabled = false
                    high_fullscreen_notification_4.text = String.format(text, maxCountTime)
                    Observable.interval(1, TimeUnit.SECONDS, Schedulers.io())
                            .take(maxCountTime)
                }
                .map {
                    maxCountTime - (it + 1)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it == 0L) {
                        high_fullscreen_notification_4.isEnabled = true
                        high_fullscreen_notification_4.text = String.format(text, maxCountTime)
                        showHighFullscreenNotification(it.hashCode())
                    } else {
                        high_fullscreen_notification_4.text = String.format(text, it)
                    }
                }
    }

    override fun onResume() {
        super.onResume()
        Logger.i()
    }

    override fun onPause() {
        super.onPause()
        Logger.i()
    }

    override fun onStop() {
        super.onStop()
        Logger.i()
    }

    private fun showNormalBarNotification(notificationId: Int) {
        val channelId = "普通channelId"
        val context = applicationContext

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = "普通channel_name"
            val descriptionText = "普通channel_description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(channelId, name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }


        // Create an explicit intent for an Activity in your app
        val intent = Intent(context, NotificationDetailActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(MainActivity.INTENT_KEY_FROM_WHICH, "普通")
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, notificationId,
                intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("普通Title")
                .setContentText("普通Content[$notificationId]")
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setColor(ContextCompat.getColor(this, R.color.abc_btn_colored_text_material))
                .addAction(R.drawable.navigation_empty_icon, "关闭",
                        getClosePendingIntent(context, notificationId))


        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(notificationId, builder.build())
        }
    }

    private fun showHighNotification(notificationId: Int) {
        val channelId = "高channelId"
        val context = applicationContext

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = "高channel_name"
            val descriptionText = "高channel_description"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(channelId, name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }


        // Create an explicit intent for an Activity in your app
        val intent = Intent(context, NotificationDetailActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(MainActivity.INTENT_KEY_FROM_WHICH, "高")
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, notificationId,
                intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("高Title")
                .setContentText("高Content[$notificationId]")
                // Set the intent that will fire when the user taps the notification
//                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setColor(ContextCompat.getColor(this, R.color.abc_btn_colored_text_material))
                .addAction(R.drawable.navigation_empty_icon, "关闭",
                        getClosePendingIntent(context, notificationId))


        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(notificationId, builder.build())
        }
    }

    private fun showHighFullscreenNotification(notificationId: Int) {
        val channelId = "高全屏channelId"
        val context = applicationContext

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = "高全屏channel_name"
            val descriptionText = "高全屏channel_description"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(channelId, name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }


        // Create an explicit intent for an Activity in your app
        val intent = Intent(context, NotificationDetailActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(MainActivity.INTENT_KEY_FROM_WHICH, "高全屏")
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, notificationId,
                intent, PendingIntent.FLAG_CANCEL_CURRENT)

        val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("高全屏Title")
                .setContentText("高全屏Content[$notificationId]")
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(true)
                .setFullScreenIntent(pendingIntent, true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setColor(ContextCompat.getColor(this, R.color.abc_btn_colored_text_material))
                .addAction(R.drawable.navigation_empty_icon, "关闭",
                        getClosePendingIntent(context, notificationId))



        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(notificationId, builder.build())
        }
    }

    private fun getClosePendingIntent(context: Context, notificationId: Int): PendingIntent {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            action = MainActivity.ACTION_CLOSE_NOTIFICATION
            putExtra(MainActivity.INTENT_KEY_NOTIFICATION_ID, notificationId)
        }
        return PendingIntent.getBroadcast(context, notificationId,
                intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }


    override fun onDestroy() {
        super.onDestroy()
        mDisposables.clear()
    }
}
