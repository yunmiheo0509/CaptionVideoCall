package com.remotemonster.example.captionvideocall;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingIDService extends FirebaseMessagingService {


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("Firebase", "FirebaseInstanceIDService : " + s);
    }


    /**
     * 메세지를 받았을 경우 그 메세지에 대하여 구현하는 부분입니다.
     * **/
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage != null && remoteMessage.getData().size() > 0) {
            sendNotification(remoteMessage);
        }
    }

    /**
     * remoteMessage 메세지 안애 getData와 getNotification이 있습니다.
     *
     * **/
    private void sendNotification(RemoteMessage remoteMessage) {

        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("message");

    }

}
