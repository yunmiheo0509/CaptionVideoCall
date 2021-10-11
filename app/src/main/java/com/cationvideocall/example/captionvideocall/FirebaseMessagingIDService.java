package com.cationvideocall.example.captionvideocall;

import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.cationvideocall.example.captionvideocall.Activity.NotifyCallActivity;
import com.cationvideocall.example.captionvideocall.Activity.WaitActivity;
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
            String info = remoteMessage.getData().get("info");

            if (info != null && info.equals("propose_call")) {
                String user_id = remoteMessage.getData().get("user_id");
                String room_num = remoteMessage.getData().get("room_num");
                Log.d("파이어베이스나온", user_id + room_num);
                Intent intent = new Intent(this, NotifyCallActivity.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("room_num", room_num);

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                getApplicationContext().startActivity(intent);
                Log.d("스타트 액티비티", user_id + room_num);
            }
            else if (info != null && info.equals("cancel_call")){
                Log.d("전화 끊겼다는 FCM 받음", "전화 끊겼다는 FCM 받음");
                NotifyCallActivity.notifyActivity.finish();
            }
        }
        else{
            Log.d("파이어베이스나온 오류오류오류","엘즈문 ");
        }

    }

//    /**
//     * remoteMessage 메세지 안애 getData와 getNotification이 있습니다.
//     *
//     * **/
//    private void sendNotification(RemoteMessage remoteMessage) {
//
//        String title = remoteMessage.getData().get("title");
//        String message = remoteMessage.getData().get("message");
//
//    }

}
