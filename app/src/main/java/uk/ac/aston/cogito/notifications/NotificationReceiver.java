package uk.ac.aston.cogito.notifications;

import static uk.ac.aston.cogito.model.TimeManager.TIME_FORMAT_24;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.ac.aston.cogito.R;
import uk.ac.aston.cogito.model.TimeManager;

public class NotificationReceiver extends BroadcastReceiver {

    private final static List<Intent> intentList = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        String text = intent.getStringExtra("text");
        int notification_id = intent.getIntExtra("notification_id", 0);
        String channel_id = intent.getStringExtra("channel_id");

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channel_id,
                    "Cogito Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }

        // Build notification based on Intent
        Notification notification = new NotificationCompat.Builder(context, channel_id)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(title)
                .setContentText(text)
                .build();

        // Show notification
        manager.notify(notification_id, notification);

        // Schedule the next notification
        boolean isFormalReminder = intent.getBooleanExtra("is_formal", true);
        if (isFormalReminder) {
            scheduleFormalNotification(context, intent);

        } else {
            String tomorrowTime = TimeManager.randomTime24();
            intent.putExtra("time", tomorrowTime);
            scheduleInformalNotification(context, intent);
        }
    }

    protected static void scheduleFormalNotification(Context context, Intent intent) {
        if (!intentList.contains(intent)) {
            intentList.add(intent);
        }

        int notification_id = intent.getIntExtra("notification_id", 0);
        String time24 = intent.getStringExtra("time");
        long nextDateTime = TimeManager.date24toMillisNext(time24);

        PendingIntent pending = PendingIntent.getBroadcast(
                context,
                notification_id,
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        // Schedule notification
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, nextDateTime, pending);
    }

    protected static void scheduleInformalNotification(Context context, Intent intent) {
        int notification_id = intent.getIntExtra("notification_id", 0);
        String time24 = intent.getStringExtra("time");

        long tomorrowDateTime = TimeManager.date24toMillisTomorrow(time24);

        Date date = new Date(tomorrowDateTime);
        String timeString = TIME_FORMAT_24.format(date);
        intent.putExtra("time", timeString);
        if (!intentList.contains(intent)) {
            intentList.add(intent);
        }


        // TODO remove
        Log.i("ParmiTime", "Next schedule informal:" + date);

        PendingIntent pending = PendingIntent.getBroadcast(
                context,
                notification_id,
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        // Schedule notification
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, tomorrowDateTime, pending);
    }


    protected static void cancelAll(Context context) {
        for (Intent intent : intentList) {
            cancelNotification(context, intent);
        }

        intentList.clear();
    }


    private static void cancelNotification(Context context, Intent intent) {
        int notification_id = intent.getIntExtra("notification_id", 0);

        PendingIntent pending = PendingIntent.getBroadcast(
                context,
                notification_id,
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        // Cancel notification
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pending);
    }
}

















