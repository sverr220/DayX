package dayx.home13.ru.dayx.fcm;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

class FCMMessage {

    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    /**
     * Данные сообщения.
     */
    private HashMap<String, String> data = null;

    // ===========================================================
    // Constructors
    // ===========================================================

    private FCMMessage() {
        super();

        this.data = new HashMap<String, String>();
    }

    FCMMessage(final Map<String, String> map) {
        this();

        if (map != null && map.size() > 0) {
            this.data.putAll(map);
        }
    }

    FCMMessage(final Bundle bundle) {
        this();
        if (bundle != null) {
            for (String key : bundle.keySet()) {
                if (bundle.get(key) instanceof String)
                    data.put(key, (String) bundle.get(key));
            }
        }
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    String getTitle() {
        switch (getType()) {
            default:
                return "";
        }
    }

    String getBody() {
        switch (getType()) {
            default:
                return "";
        }
    }

    PendingIntent getPendingIntent(Context context, boolean isBackground) {
        return PendingIntent.getActivity(context, 0, getIntent(context, isBackground), PendingIntent.FLAG_ONE_SHOT);
    }

    Intent getIntent(Context context) {
        return this.getIntent(context, false);
    }

    private Intent getIntent(Context context, boolean isBackground) {
        Intent intent;

        if (isBackground) {
            intent = getMainActIntent(context);

            Bundle bundle = new Bundle();
            for (String key : data.keySet()) {
                if (data.get(key) != null)
                    bundle.putString(key, data.get(key));
            }

            intent.putExtras(bundle);
        } else
            switch (getType()) {
                default:
                    intent = getMainActIntent(context);
                    break;
            }

        return intent;
    }

    private Intent getMainActIntent(Context context) {
        return context.getPackageManager().getLaunchIntentForPackage(context.getApplicationContext().getPackageName());
    }

    Object getObject() {
        switch (getType()) {
            default:
                return null;
        }

    }

    private Type getType() {
        return Type.parse(getValue("type"));
    }

    private String getValue(String key) {
        return data.get(key);
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    private enum Type {
        unknown;

        static Type parse(String value) {
            for (Type type : Type.values()) {
                if (type.toString().equalsIgnoreCase(value))
                    return type;
            }

            return Type.unknown;
        }
    }

}
