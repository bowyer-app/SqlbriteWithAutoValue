package com.bowyer.app.playermanage.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import com.bowyer.app.playermanage.MainActivity;
import com.bowyer.app.playermanage.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import org.xml.sax.helpers.DefaultHandler;

public class GrowthPushReceiver extends BroadcastReceiver {

  private static final String EXTRA_GROWTH_PUSH_LAUNCH = "launch_from_growth_push";

  @Override public void onReceive(Context context, Intent intent) {

    if (intent.getAction().equals("com.google.android.c2dm.intent.REGISTRATION")) {
      GrowthPushRegistrar.handleRegistration(context, intent);
    } else if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) {
      String messageType = GoogleCloudMessaging.getInstance(context).getMessageType(intent);
      if (messageType != null && messageType.equals(GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE)) {
        handleReceive(context, intent);
      }
    }
  }

  protected void handleReceive(final Context context, final Intent intent) {
    showNotification(context, intent);
  }

  protected void showNotification(final Context context, final Intent intent) {
    new DefaultHandler() {
      public void addNotification(final Context context, final Intent intent) {
        if (context == null || intent == null || intent.getExtras() == null) {
          return;
        }

        NotificationManager notificationManager =
            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify("GrowthPush" + context.getPackageName(), 1,
            generateNotification(context, intent.getExtras()).build());
      }

      private NotificationCompat.Builder generateNotification(final Context context,
          final Bundle extras) {

        GrowthPush growthPush = GrowthPush.of(extras);

        String title = growthPush.title();
        if (TextUtils.isEmpty(title)) {
          title = context.getString(R.string.app_name);
        }
        final String message = growthPush.message();

        Intent intent = new Intent();
        intent.putExtra(EXTRA_GROWTH_PUSH_LAUNCH, true);
        intent.setClass(context, MainActivity.class);

        intent.putExtra(GrowthPush.KEY_GROWTH_PUSH, growthPush);

        PendingIntent pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setTicker(title);
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setContentText(message);
        builder.setContentTitle(title);
        builder.setContentIntent(pendingIntent);
        builder.setWhen(System.currentTimeMillis());
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setColor(ContextCompat.getColor(context, R.color.colorPrimary));

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle(title);
        bigTextStyle.bigText(message);
        builder.setStyle(bigTextStyle);
        return builder;
      }
    }.addNotification(context, intent);
  }

  public static boolean isLaunchFromGrowthPush(final Intent intent) {
    return intent.getBooleanExtra(EXTRA_GROWTH_PUSH_LAUNCH, false);
  }
}
