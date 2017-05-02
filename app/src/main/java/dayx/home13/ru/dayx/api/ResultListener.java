package dayx.home13.ru.dayx.api;

import org.json.JSONObject;

interface ResultListener {

    void onResult(int code, JSONObject data);
}
