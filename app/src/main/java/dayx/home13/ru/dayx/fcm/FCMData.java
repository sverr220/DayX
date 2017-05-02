package dayx.home13.ru.dayx.fcm;

import android.content.Context;
import android.text.TextUtils;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import dayx.home13.ru.dayx.utils.Preferences;
import dayx.home13.ru.dayx.R;

class FCMData {

    // ===========================================================
    // Constants
    // ===========================================================

    private final static String API_PUSH_TOKEN = "push_token";
    private final static String API_APP_KEY = "app_key";
    private final static String API_PLATFORM = "platform";
    private final static String API_ANDROID = "android";
    private final static String API_RELEASE = "release";

    // ===========================================================
    // Fields
    // ===========================================================

    private Context context;

    // ===========================================================
    // Constructors
    // ===========================================================

    FCMData(Context context) {
        super();
        this.context = context;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    /**
     * Необходимо обновить данные на сервере.
     *
     * @return возвращает true если остуствует deviceId <br>
     * или сохраненый pushToken отличается от текущего <br>
     * или прошло больше суток с последнего обновления
     */
    boolean isNeedSend() {
        String token = getToken();
        String saveToken = Preferences.getToken();
        return TextUtils.isEmpty(saveToken)
                || !saveToken.equalsIgnoreCase(token);
    }

    public JSONObject getJSONParam() throws JSONException {
        final JSONObject result = new JSONObject();
        result.put(API_PUSH_TOKEN, getToken());
        result.put(API_APP_KEY, getAppId());
        result.put(API_PLATFORM, API_ANDROID);
        result.put(API_RELEASE, getAppVersion());
        return result;
    }

    private String getToken() {
        return FirebaseInstanceId.getInstance().getToken();
    }

    private String getAppId() {
        return context.getApplicationContext().getPackageName();
    }

    private String getAppVersion() {
        return context.getString(R.string.app_version);
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================


    // ===========================================================
    // Methods
    // ===========================================================

}
