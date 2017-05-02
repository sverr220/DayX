package dayx.home13.ru.dayx.application;

import dayx.home13.ru.dayx.application.MyActivityLifecycleCallbacks;

public class DayApplication extends MyActivityLifecycleCallbacks {

    /**
     * Признак что приложение свернуто.
     */
    public boolean isApplicationBroughtToBackground() {
        return !MyActivityLifecycleCallbacks.isShow();
    }

}
