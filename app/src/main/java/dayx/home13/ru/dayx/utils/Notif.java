package dayx.home13.ru.dayx.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import dayx.home13.ru.dayx.R;

public class Notif {

    // ===========================================================
    // Constants
    // ===========================================================

    /**
     * Код для сообщения о программе.
     */
    private static final int NOTIFY_ID_MAIN = 199;
    private static final int NOTIF_ID_PUSH = 384;

    // ===========================================================
    // Fields
    // ===========================================================

    private Context context;
    private int id;
    private boolean canDelete;
    private boolean useSound;
    private NotificationCompat.Builder notificationBuilder;

    // ===========================================================
    // Constructors
    // ===========================================================

    private Notif(Context context, final int idNotification) {
        super();

        this.context = context;
        this.id = idNotification;
        this.canDelete = true;
        notificationBuilder = new NotificationCompat.Builder(context);

        setData(context.getString(R.string.app_name), "");
        setIcon(R.drawable.ic_menu_gallery);
        setAutoCancel(true);
        setPIntent(createPendingIntent(context));
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    private Notif setData(final String title, final String body) {
        notificationBuilder.setContentTitle(title).setContentText(body);
        return this;
    }

    private Notif setCanDelete(final boolean canDelete) {
        this.canDelete = canDelete;
        return this;
    }

    private Notif setIcon(final int icon) {
        notificationBuilder.setSmallIcon(icon);
        return this;
    }

    private Notif setPIntent(final PendingIntent pendingIntent){
        notificationBuilder.setContentIntent(pendingIntent);
        return this;
    }

    private Notif setAutoCancel(final boolean autoCancel){
        notificationBuilder.setAutoCancel(autoCancel);
        return this;
    }

    private Notif setSound() {
        useSound = true;
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder.setSound(defaultSoundUri);
        Logger.info ("uri " + defaultSoundUri);

        return this;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    public static void showMain(Context context, final int icon) {
        new Notif(context, NOTIFY_ID_MAIN).setIcon(icon).setCanDelete(false)
                .show();
    }

    public static void showPush(Context context, final String title, final String body,
                                final PendingIntent pIntent) {
        new Notif(context, NOTIF_ID_PUSH).setData(title, body).setPIntent(pIntent)
                .setSound().show();
    }

    private void show() {
        Notification notification = notificationBuilder.build();
        if (!canDelete) {
            notification.flags = notification.flags | Notification.FLAG_NO_CLEAR;
            setAutoCancel(false);
        }

        if (useSound)
            notification.defaults = Notification.DEFAULT_SOUND;

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
        notificationManager.notify(id, notification);
    }

    private PendingIntent createPendingIntent(Context context) {
        String packageName = context.getApplicationContext().getPackageName();
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void closeAll(Context context) {
        try {
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
        } catch (NullPointerException e) {
            Logger.error(e);
        }
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
