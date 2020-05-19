package ie.ul.studenttimetableul;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {

    private NotificationHelper mNotificationHelper;
    static final String EXTRAS_N_DETAILS = "ie.ul.studenttimetableul";

    @Override
    public void onReceive(Context context, Intent intent) {
        //String extras [] = intent.getStringArrayExtra(EXTRAS_N_DETAILS);
        //String title = extras[0];
        //String text = extras[1];

        String title = intent.getExtras().getString("N_TITLE");
        String text = intent.getExtras().getString("N_TEXT");
        //System.out.println("AR TITLE = " + title);
        //System.out.println("AR TEXT = "+ text);
        mNotificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = mNotificationHelper.getChannelNotification(title, text);
        mNotificationHelper.getManager().notify(1, nb.build());
    }
}
