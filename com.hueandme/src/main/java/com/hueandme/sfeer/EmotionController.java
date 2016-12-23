package com.hueandme.sfeer;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.hueandme.R;

/**
 * Created by Tobi on 16-Dec-16.
 */

public class EmotionController {

    private Emotion currentEmotion;

    public enum Emotion {
        Happiness,
        Sadness,
        Anger,
        Phlegmatic,
        Tranquil,
        Comfortable
    }

    public Emotion getEmotion(){
        return currentEmotion;
    }

    public void setCurrentEmotion(Emotion emotion){
        currentEmotion = emotion;
    }

    public void fireEmotionQuery(Context context){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Hue & Me")
                        .setContentText("Hi. How are you feeling today?");

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent GoodReceive = new Intent();
        PendingIntent pendingIntentGood = PendingIntent.getBroadcast(context, 12345, GoodReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.addAction(R.mipmap.ic_launcher, "Good", pendingIntentGood);

        Intent BadReceive = new Intent();
        PendingIntent pendingIntentBad = PendingIntent.getBroadcast(context, 12345, BadReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.addAction(R.mipmap.ic_launcher, "Bad", pendingIntentBad);

        mNotificationManager.notify(0, mBuilder.build());


    }
}


