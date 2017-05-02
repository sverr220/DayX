package dayx.home13.ru.dayx.api;

import android.app.Activity;
import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dayx.home13.ru.dayx.utils.Logger;
import dayx.home13.ru.dayx.api.ApiUtils.ResponseResult;

/**
 * Класс GET/POST запросов.
 */
class ApiSender {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String SERVER_URI = "http://dayx.bitmaster.ru";

    private static final String REGISTERED_TOKEN_URI = "/api/v1/token";
    private static final String SETTINGS_URI = "/api/v1/settings";


    // ===========================================================
    // Methods
    // ===========================================================

    static void registeredTokenRequest(final Context context,
                                             final String authToken,
                                             final JSONObject params,
                                             final ResultListener resultListener)
            throws UnsupportedEncodingException {
        new HttpPostApiTask(context, authToken, params, resultListener)
                .execute(SERVER_URI + REGISTERED_TOKEN_URI);
    }

    static void getSettingsRequest(final Context context,
                                          final String marketCrewId,
                                          final ResultListener resultListener)
            throws UnsupportedEncodingException {
        new HttpGetApiTask(context, resultListener, marketCrewId, true)
                .execute(SERVER_URI + SETTINGS_URI);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    /**
     * Http Delete асинхронный запрос.
     */
    private static class HttpDeleteApiTask extends ApiTask {

        HttpDeleteApiTask(final Context context, final ResultListener resultListener,
                          final String authToken, final boolean useCertificate) {
            super(context, authToken, useCertificate, resultListener);
        }

        @Override
        protected ResponseResult internalDoInBackground(String param)
                throws URISyntaxException, IOException, JSONException {
            return ApiUtils.callDelete(param, authToken, useCertificate);
        }
    }

    /**
     * Http Post асинхронный запрос.
     */
    private static class HttpPostApiTask extends ApiTask {
        private JSONObject mParam;

        HttpPostApiTask(final Context context, final String authToken, final JSONObject param,
                        final boolean useCertificate, final ResultListener resultListener) {
            super(context, authToken, useCertificate, resultListener);
            mParam = param;
        }

        HttpPostApiTask(final Context context, final String authToken, final JSONObject param,
                        final ResultListener resultListener) {
            super(context, authToken, false, resultListener);
            useAccessToken = false;
            mParam = param;
        }

        @Override
        protected ResponseResult internalDoInBackground(String param)
                throws URISyntaxException, IOException, JSONException {
            return ApiUtils.callPost(param, authToken, mParam, useCertificate, useAccessToken);
        }
    }

    /**
     * Http Get асинхронный расширенный запрос. запрос.
     */
    private static class HttpExternalGetApiTask extends ApiTask {

        HttpExternalGetApiTask(Context context, ResultListener resultListener) {
            super(context, resultListener);
        }

        @Override
        protected ResponseResult internalDoInBackground(String param)
                throws URISyntaxException, IOException, JSONException {
            return ApiUtils.callExternalGet(param);
        }
    }

    /**
     * Http Get асинхронный запрос.
     */
    private static class HttpGetApiTask extends ApiTask {

        HttpGetApiTask(final Context context, final ResultListener resultListener,
                       final String authToken, final boolean useCertificate) {
            super(context, authToken, useCertificate, resultListener);
        }

        @Override
        protected ResponseResult internalDoInBackground(String param)
                throws URISyntaxException, IOException, JSONException {
            return ApiUtils.callGet(param, authToken, useCertificate);
        }
    }

    /**
     * Http Get асинхронный запрос.
     */
    private static abstract class ApiTask implements Runnable {
        Context mContext;
        Activity mActivity = null;
        ResultListener mResultListener;
        String authToken = null;
        boolean useCertificate = false;
        boolean useAccessToken = true;
        private static ExecutorService mExecutorService = null;
        private String mParam = null;

        ApiTask(final Context context,
                final ResultListener resultListener) {
            mContext = context;
            if (mContext instanceof Activity) {
                mActivity = (Activity) mContext;
            }
            mResultListener = resultListener;
            if (mExecutorService == null)
                synchronized (this.getClass()) {
                    if (mExecutorService == null)
                        mExecutorService = Executors.newCachedThreadPool();
                }
        }

        ApiTask(final Context context, final String authToken,
                final boolean useCertificate, final ResultListener resultListener) {
            this(context, resultListener);
            this.authToken = authToken;
            this.useCertificate = useCertificate;
        }

        ResponseResult doInBackground(final String param) {
            try {
                return internalDoInBackground(param);
            } catch (URISyntaxException | IOException | JSONException e) {
                Logger.error(e);
                e.printStackTrace();
            }
            return null;
        }

        protected abstract ResponseResult internalDoInBackground(String param)
                throws URISyntaxException, IOException, JSONException;

        void onPostExecute(int resultCode, JSONObject result) {
            if (mResultListener != null)
                mResultListener.onResult(resultCode, result);
        }

        void execute(String param) {
            mParam = param;
            mExecutorService.execute(this);
        }

        @Override
        public void run() {
            ResponseResult result = doInBackground(mParam);
            if (result != null)
                onPostExecute(result.statusCode, result.response);
            else
                onPostExecute(-1, null);
        }
    }
}
