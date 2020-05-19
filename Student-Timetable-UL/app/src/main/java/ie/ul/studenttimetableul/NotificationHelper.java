package ie.ul.studenttimetableul;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class NotificationHelper extends ContextWrapper {

    public static final String cID = "cID";
    public static final String cName = "cName";

    private NotificationManager mManager;

    public NotificationHelper(Context base) {
        super(base);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void createChannel()
    {
        NotificationChannel channel = new NotificationChannel(cID, cName, NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(Color.MAGENTA);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager()
    {
        if(mManager == null)
        {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification(String title, String message)
    {
        return new NotificationCompat.Builder(getApplicationContext(), cID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_menu_school)
                .setVibrate(new long[]{500, 1000, 500})
                .setLights(Color.MAGENTA, 1000, 1000);
    }
}
