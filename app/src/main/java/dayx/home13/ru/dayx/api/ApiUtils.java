package dayx.home13.ru.dayx.api;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dayx.home13.ru.dayx.utils.Logger;

class ApiUtils {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final boolean LOG_ENABLED = false;
    private static final String HTTP_PROTOCOL = "http";
    private static final String HTTPS_PROTOCOL = "https";
    private static final String TLS_PROTOCOL = "TLS";
    private static final int HTTP_PORT = 80;
    private static final int HTTPS_PORT = 443;
    private static final String PAYMENT_HOST_PASSWORD = "Bit321";

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    static ResponseResult callGet(final String url, final String authToken,
                                  final boolean useCertificate)
            throws URISyntaxException, IOException {
        return call(new HttpGet(new URI(url)), authToken, useCertificate);
    }

    static ResponseResult callExternalGet(final String url)
            throws URISyntaxException, IOException {
        return call(new HttpGet(new URI(url)), null, false);
    }

    static ResponseResult callDelete(final String url, final String authToken,
                                     final boolean useCertificate)
            throws URISyntaxException, IOException {
        return call(new HttpDelete(new URI(url)), authToken, useCertificate);
    }

    static ResponseResult callPost(final String url, final String authToken,
                                   final JSONObject param, final boolean useCertificate,
                                   final boolean useAccessToken)
            throws URISyntaxException, IOException {
        final HttpPost httpPost = new HttpPost(new URI(url));

        StringEntity inputEntity = new StringEntity(param.toString(), HTTP.UTF_8);
        inputEntity.setContentType("application/json");
        httpPost.setEntity(inputEntity);

        return call(httpPost, authToken, useCertificate, useAccessToken);
    }

    private static ResponseResult call(final HttpRequestBase requestBase, final String authToken,
                                       final boolean useCertificate) throws IOException {
        return call(requestBase, authToken, useCertificate, true);
    }

    private static ResponseResult call(final HttpRequestBase requestBase, final String authToken,
                                       final boolean useCertificate,
                                       final boolean useAccessToken) throws IOException {
        if (LOG_ENABLED)
            Logger.info(requestBase.getMethod() +
                    (useAccessToken ? " X-ACCESS-Token: " : "X-Application-Id: ") + authToken +
                    " url: " + requestBase.getURI() +
                    " useCertificate " + useCertificate +
                    " useAccessToken " + useAccessToken);

        configureDefaultJsonHeader(requestBase, authToken, useAccessToken);

        final HttpClient httpClient = new MyHttpClient(getClientKeyStore(useCertificate));
        final HttpResponse httpResponse = httpClient.execute(requestBase);

        return new ResponseResult(httpResponse);
    }

    private static void configureDefaultJsonHeader(final HttpRequestBase requestBase,
                                                   final String authToken,
                                                   final boolean useAccessToken) {
        requestBase.setHeader("Accept", "application/json");
        requestBase.setHeader("Content-type", "application/json; charset=utf-8");
        requestBase.setHeader("Cache-Control", "no-cache");

        if (authToken != null) {
            if (useAccessToken)
                requestBase.setHeader("X-ACCESS-Token", authToken);
            else
                requestBase.setHeader("X-Application-Id", authToken);
        }
    }

    private static KeyStore getClientKeyStore(final boolean useCertificate) {
        KeyStore trustStore = null;
        try {
            if (useCertificate) {
                trustStore = KeyStore.getInstance("PKCS12");
                final InputStream in =
                        Core.getApplication().getResources().getAssets().open("client_pass.p12");
                try {
                    trustStore.load(in, PAYMENT_HOST_PASSWORD.toCharArray());
                } finally {
                    in.close();
                }
            } else {
                trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null, null);
            }
        } catch (Exception e) {
            Logger.error(e);
        }

        return trustStore;
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    private static class MyHttpClient extends DefaultHttpClient {

        final KeyStore clientKeyStore;

        MyHttpClient(KeyStore clientKeyStore) {
            this.clientKeyStore = clientKeyStore;
        }

        @Override
        protected ClientConnectionManager createClientConnectionManager() {
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(
                    new Scheme(HTTP_PROTOCOL, PlainSocketFactory.getSocketFactory(), HTTP_PORT));
            registry.register(
                    new Scheme(HTTPS_PROTOCOL, createSSLSocketFactory(), HTTPS_PORT));
            return new SingleClientConnManager(getMyParams(), registry);
        }

        HttpParams getMyParams() {
            final HttpParams params = getParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            return params;
        }

        private SSLSocketFactory createSSLSocketFactory() {
            try {
                SSLSocketFactory sf = new MySSLSocketFactory(clientKeyStore);
                sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                return sf;
            } catch (Exception e) {
                throw new AssertionError(e);
            }
        }

        /**
         * Собственная фабрика сертификатов, по умолчанию считает серверные сертификаты верными.
         */
        static class MySSLSocketFactory extends SSLSocketFactory {
            SSLContext sslContext = SSLContext.getInstance(TLS_PROTOCOL);

            MySSLSocketFactory(KeyStore keyStore) throws NoSuchAlgorithmException,
                    KeyManagementException, KeyStoreException, UnrecoverableKeyException {
                super(keyStore);
                try {
                    KeyManagerFactory kmf =
                            KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                    kmf.init(keyStore, "".toCharArray());

                    final TrustManager trustManager = new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain,
                                                       String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain,
                                                       String authType) throws CertificateException {
                            // Сертификат не проверяется. по умолчанию счетаем подтвержденным.
                            // Если потребуется можно реализовать так: http://habrahabr.ru/post/194530/
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                    };

                    sslContext.init(kmf.getKeyManagers(),
                            new TrustManager[]{trustManager},
                            new SecureRandom());
                } catch (Exception e) {
                    Logger.error(e);
                }
            }

            @Override
            public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
                    throws IOException {
                return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
            }

            @Override
            public Socket createSocket() throws IOException {
                return sslContext.getSocketFactory().createSocket();
            }
        }
    }

    static class ResponseResult {
        int statusCode = -1;
        JSONObject response = null;

        ResponseResult(HttpResponse httpResponse) {
            this.statusCode = parseStatusCode(httpResponse);
            this.response = parseJSONHttpResponse(httpResponse);
        }

        private int parseStatusCode(HttpResponse httpResponse) {
            final StatusLine statusLine = httpResponse.getStatusLine();
            if (LOG_ENABLED)
                Logger.info("statusCode " + statusLine.getStatusCode());
            return statusLine.getStatusCode();
        }

        private Object parseJSONHttpResponse(HttpResponse httpResponse) {
            try {
                final HttpEntity entity = httpResponse.getEntity();
                final InputStream content = entity.getContent();
                final BufferedReader reader = new BufferedReader(
                        new InputStreamReader(content), 8192);

                final StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null)
                    builder.append(line);

                if (LOG_ENABLED)
                    Logger.info("Response " + builder);
                if (builder.length() > 0) {
                    if (builder.toString().startsWith("["))
                        return new JSONArray(builder.toString());
                    else
                        return new JSONObject(builder.toString());
                }
            } catch (Exception e) {
                Logger.error(e);
            }
            return null;
        }
    }

}