package dayx.home13.ru.dayx.fcm;

import com.google.firebase.iid.FirebaseInstanceIdService;

public class FCMIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        FCMManager.getInstance().registeredDeviceId(this);
    }

}
