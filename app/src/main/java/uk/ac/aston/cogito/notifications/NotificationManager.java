package uk.ac.aston.cogito.notifications;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import uk.ac.aston.cogito.R;
import uk.ac.aston.cogito.model.TimeManager;
import uk.ac.aston.cogito.model.entities.Settings;

public class NotificationManager {

    private static final int[] NOTIFICATION_IDS_INFORMAL = {71, 72, 73, 74, 75};
    private static final String[] CHANNEL_IDS_INFORMAL = {"cogito-i71", "cogito-i72", "cogito-i73", "cogito-i74", "cogito-i75"};

    private static final int[] NOTIFICATION_IDS_FORMAL = {81, 82, 83, 84, 85};
    private static final String[] CHANNEL_IDS_FORMAL = {"cogito-i81", "cogito-i82", "cogito-i83", "cogito-i84", "cogito-i85"};

    private static final List<NotificationProps> INFORMAL_NOTIFICATIONS_LIST = initiInformalNotifList();

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
                intent.putExtra("is_formal", true);

                NotificationReceiver.scheduleFormalNotification(context, intent);
            }
        }

        // TODO Similarly, if informal notifications are on, re(schedule) them.
        if (settings.isInformalEnabled()) {
            int numInformal = settings.getNumInformal();

            // Shuffle a few numbers
            ArrayList<Integer> list = new ArrayList<>();
            for (int i = 0; i < INFORMAL_NOTIFICATIONS_LIST.size(); i++) {
                list.add(i);
            }
            Collections.shuffle(list);

            // Pick three random notifications
            for (int i = 0; i < numInformal ; i++) {
                int rdmIndex = list.get(i);
                NotificationProps rdmNotification = INFORMAL_NOTIFICATIONS_LIST.get(rdmIndex);

                Intent intent = new Intent(context, NotificationReceiver.class);
                intent.putExtra("title", context.getString(rdmNotification.titleId));
                intent.putExtra("text", context.getString(rdmNotification.textId));
                intent.putExtra("time", TimeManager.randomTime24());
                intent.putExtra("notification_id", NOTIFICATION_IDS_INFORMAL[i]);
                intent.putExtra("channel_id", CHANNEL_IDS_INFORMAL[i]);
                intent.putExtra("is_formal", false);

                NotificationReceiver.scheduleInformalNotification(context, intent);
            }
        }
    }


    private static List<NotificationProps> initiInformalNotifList() {
        List<NotificationProps> tmpList = new ArrayList<>();

        tmpList.add(new NotificationProps
                (R.string.notification_informal_1_title,R.string.notification_informal_1_text));
        tmpList.add(new NotificationProps
                (R.string.notification_informal_2_title,R.string.notification_informal_2_text));
        tmpList.add(new NotificationProps
                (R.string.notification_informal_3_title,R.string.notification_informal_3_text));
        tmpList.add(new NotificationProps
                (R.string.notification_informal_4_title,R.string.notification_informal_4_text));
        tmpList.add(new NotificationProps
                (R.string.notification_informal_5_title,R.string.notification_informal_5_text));
        tmpList.add(new NotificationProps
                (R.string.notification_informal_6_title,R.string.notification_informal_6_text));
        tmpList.add(new NotificationProps
                (R.string.notification_informal_7_title,R.string.notification_informal_7_text));
        tmpList.add(new NotificationProps
                (R.string.notification_informal_8_title,R.string.notification_informal_8_text));
        tmpList.add(new NotificationProps
                (R.string.notification_informal_9_title,R.string.notification_informal_9_text));
        tmpList.add(new NotificationProps
                (R.string.notification_informal_10_title,R.string.notification_informal_10_text));

        return tmpList;
    }


    public static NotificationProps getRandomInformal() {
        int rdmIndex = new Random().nextInt(INFORMAL_NOTIFICATIONS_LIST.size());
        return INFORMAL_NOTIFICATIONS_LIST.get(rdmIndex);
    }



    public static class NotificationProps {
        int titleId;
        int textId;

        public NotificationProps(int titleId, int textId) {
            this.titleId = titleId;
            this.textId = textId;
        }
    }
}
