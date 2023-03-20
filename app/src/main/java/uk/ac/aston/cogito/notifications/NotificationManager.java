package uk.ac.aston.cogito.notifications;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Date;

import uk.ac.aston.cogito.R;
import uk.ac.aston.cogito.model.TimeManager;
import uk.ac.aston.cogito.model.entities.Settings;

public class NotificationManager {

    private static final int NOTIFICATION_ID_INFORMAL = 70;
    private static final String CHANNEL_ID_INFORMAL = "cogito-i70";
    
    private static final int[] NOTIFICATION_IDS_FORMAL = {81, 82, 83, 84, 85};
    private static final String[] CHANNEL_IDS_FORMAL = {"cogito-i81", "cogito-i82", "cogito-i83", "cogito-i84", "cogito-i85"};


    public static void schedule(Context context, Settings settings) {
        NotificationReceiver.cancelAll(context);

        // If formal notification are on, (re)schedule them.
        if (settings.isFormalEnabled()) {
            int numFormal = settings.getNumFormal();
            for (int idx = 0; idx < numFormal; idx++) {
                String time24 = settings.getFormalTimes()[idx];

                Intent intent = new Intent(context, NotificationReceiver.class);
                intent.putExtra("title", context.getString(R.string.notification_formal_title));
                intent.putExtra("text", context.getString(R.string.notification_formal_text));
                intent.putExtra("time", time24);
                intent.putExtra("notification_id", NOTIFICATION_IDS_FORMAL[idx]);
                intent.putExtra("channel_id", CHANNEL_IDS_FORMAL[idx]);

                NotificationReceiver.scheduleNotification(context, intent);
            }
        }

        // TODO Similarly, if informal notifications are on, re(schedule) them.
        if (settings.isInformalEnabled()) {

        }
    }
}
