package dayx.home13.ru.dayx.fcm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.Map;

import dayx.home13.ru.dayx.application.DayApplication;

public class FCMManager {

    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    private static volatile FCMManager instance;

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public static FCMManager getInstance() {
        FCMManager localInstance = instance;
        if (localInstance == null) {
            synchronized (FCMManager.class) {
                localInstance = instance;
                if (localInstance == null)
                    instance = localInstance = new FCMManager();
            }
        }
        return localInstance;
    }

    /**
     * @return Признак состояния свернутого приложения
     */
    private boolean isBackground(Context context) {
        DayApplication application = (DayApplication) context.getApplicationContext();
        return application.isApplicationBroughtToBackground();
    }

    /**
     * @param bundle бундле в котором ищем сообщение от FCM
     * @return Признак того что в bundle есть сообщение от FCM
     */
    public boolean isContainsMessage(Bundle bundle) {
        try {
            return bundle != null
                    && (bundle.containsKey("google.message_id") || bundle.containsKey("type"));
        } catch (Exception e) {
        }
        return false;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    /**
     * Обновить данные на Push сервере. С проверкой, необходимости обновления данных
     *
     * @param context Контекст
     */
    public void registeredDeviceId(Context context) {
        final FCMData fcmData = new FCMData(context);
        if (fcmData.isNeedSend()) {
        }
    }

    /**
     * Обработать пришедшее сообщение, либо вывести в трей, либо открыти акивити.
     *
     * @param context Контекст
     * @param bundle  данные сообщения в виде Bundle
     */
    public void messageSpecialReceived(Context context, Bundle bundle) {
        // Парсим сообщение если есть.
        FCMMessage message = new FCMMessage(bundle);
        startActivity(context, message);

        saveMessage(message);
    }

    /**
     * Обработать пришедшее сообщение, либо вывести в трей, либо открыти акивити.
     *
     * @param context Контекст
     * @param bundle  данные сообщения в виде Bundle
     */
    public void messageReceived(Context context, Bundle bundle) {
        // Парсим сообщение если есть.
        FCMMessage message = new FCMMessage(bundle);
        startActivity(context, message);

        saveMessage(message);
    }

    /**
     * Обработать пришедшее сообщение, либо вывести в трей, либо открыти акивити.
     *
     * @param context Контекст
     * @param data    данные сообщения в виде MAP
     */
    void messageReceived(Context context, Map<String, String> data) {
        // Парсим сообщение.
        FCMMessage message = new FCMMessage(data);

        if (!isBackground(context)) {
            startActivity(context, message);
        } else
            showNotification(context, message);

        saveMessage(message);
    }

    /**
     * Показать активити с обработанным сообщением.
     *
     * @param context Контекст активити
     * @param message Распарсеное сообщение
     */
    private void startActivity(final Context context, final FCMMessage message) {
        Intent intent = message.getIntent(context);
        context.getApplicationContext().startActivity(intent);
    }

    /**
     * Вывести нотификейшен в трей с обработанным сообщением.
     *
     * @param context Контекст
     * @param message Распарсеное сообщение
     */
    private void showNotification(final Context context, final FCMMessage message) {
        /*String title = message.getTitle(context);
        String body = message.getNotificationBody();
        PendingIntent pendingIntent = message.getPendingIntent(context, isBackground());

        TMNotification.showPush(context, title, body, pendingIntent);*/
    }

    private void saveMessage(FCMMessage message) {
        //Core.saveFCMObject(message.getObject());
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

}
