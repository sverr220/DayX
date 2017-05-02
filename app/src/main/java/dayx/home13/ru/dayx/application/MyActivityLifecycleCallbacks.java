package dayx.home13.ru.dayx.application;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

public class MyActivityLifecycleCallbacks extends Application implements Application.ActivityLifecycleCallbacks {

    private static int startStatus = 0;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        startStatus++;
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        startStatus--;
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    public static boolean isShow() {
        return startStatus > 0;
    }
}
