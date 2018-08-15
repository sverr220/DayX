package dayx.home13.ru.dayx.api;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import dayx.home13.ru.dayx.utils.Logger;


/**
 * Класс обертка для создания GET/POST запросов.
 */
public class ApiWrapper {

    // ===========================================================
    // Constants
    // ===========================================================

    private final static int NETWORK_CODE_SUCCESS_REQ = 200;
    private final static int NETWORK_CODE_SUCCESS_CREATE = 201;

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    public static void registeredDeviceId(final Context context,
                                          final String token,
                                          final JSONObject jsonObject,
                                          final IRegisteredListener listener) {
        try {
            /*ApiSender.registeredTokenRequest(context, token, jsonObject,
                    new ResultListener() {
                        @Override
                        public void onResult(int code, JSONObject data) {
                            listener.onResult(code == NETWORK_CODE_SUCCESS_REQ
                                    || code == NETWORK_CODE_SUCCESS_CREATE);
                        }
                    });*/
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    /**
     * Загрузить настройки с сервера Биржи
     *
     * @param context      контекст
     * @param marketCrewId Ид водителя на бирже
     */
    public static void loadSettings(final Context context, final String marketCrewId) {
        try {
            /*ApiSender.getSettingsRequest(context, marketCrewId,
                    new ResultListener() {
                        @Override
                        public void onResult(int code, JSONObject data) {
                            if (code == NETWORK_CODE_SUCCESS_REQ) {
                                try {
                                    parseSettings(data);
                                } catch (JSONException e) {
                                    Logger.error(e);
                                }
                            }
                        }
                    });*/
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    private static void parseSettings(JSONObject jsonObject) throws JSONException {
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    interface IRegisteredListener {
        void onResult(final boolean result);
    }
}
