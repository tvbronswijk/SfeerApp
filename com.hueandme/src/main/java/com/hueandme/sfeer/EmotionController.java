package com.hueandme.sfeer;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.hueandme.R;
import com.hueandme.SfeerSettingsActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Tobi on 16-Dec-16.
 */

public class EmotionController {

    public enum Emotion {
        Happy,
        Comfort,
        Peaceful,
        Optimistic,
        Inspired
    }

    private static EmotionController controller = new EmotionController();

    private EmotionController(){

    }

    public static EmotionController getInstance(){
        return controller;
    }

    public static Emotion getEmotion(Context context){
        return Emotion.values()[context.getSharedPreferences("emoticons", 0).getInt("emotion", 0)];
    }

    public static void setCurrentEmotion(Context context, Emotion emotion){
        context.getSharedPreferences("emoticons", 0).edit().putInt("emotion", emotion.ordinal()).apply();
    }

    /**
     * Creates custom notification with 5 buttons. Change the android Manifest in case the layout changes.
     * @param context
     */
    public static void fireEmotionQuery(Context context){
        NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);

        PendingIntent pendingHappyIntent = PendingIntent.getBroadcast(context, 0, new Intent("Happy"), 0);
        PendingIntent pendingComfortIntent = PendingIntent.getBroadcast(context, 0, new Intent("Comfort"), 0);
        PendingIntent pendingPeacefulIntent = PendingIntent.getBroadcast(context, 0, new Intent("Peaceful"), 0);
        PendingIntent pendingOptimistcIntent = PendingIntent.getBroadcast(context, 0, new Intent("Optimistic"), 0);
        PendingIntent pendingInspiredIntent = PendingIntent.getBroadcast(context, 0, new Intent("Inspired"), 0);

        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.custom_notification);
        contentView.setImageViewResource(R.id.image, R.mipmap.ic_launcher);
        contentView.setOnClickPendingIntent(R.id.happybutton, pendingHappyIntent);
        contentView.setOnClickPendingIntent(R.id.sadbutton, pendingComfortIntent);
        contentView.setOnClickPendingIntent(R.id.angrybutton, pendingPeacefulIntent);
        contentView.setOnClickPendingIntent(R.id.phlegmaticbutton, pendingOptimistcIntent);
        contentView.setOnClickPendingIntent(R.id.comfortbutton, pendingInspiredIntent);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setCustomBigContentView(contentView)
                .setContentTitle(context.getString(R.string.notification_emotion_title))
                .setContentText(context.getString(R.string.notification_emotion_content))
                .setWhen(System.currentTimeMillis());

        Intent notificationIntent = new Intent(context, SfeerSettingsActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        mNotificationManager.notify(3, notificationBuilder.build());
    }

    public static class EmotionReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            setCurrentEmotion(context, Emotion.valueOf(intent.getAction()));
            System.out.println("Your current emotion is: " + getEmotion(context).toString());
            ((NotificationManager)context.getSystemService(NOTIFICATION_SERVICE)).cancel(1);
        }
    }
}


