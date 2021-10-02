package com.cationvideocall.example.captionvideocall;

import android.content.Intent;
import android.util.Log;

import com.cationvideocall.example.captionvideocall.Activity.NotifyCallActivity;
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
//            String messageBody = remoteMessage.getNotification().getBody();
//            String messageTitle = remoteMessage.getNotification().getTitle();
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
//            Intent intent = new Intent(this, MainActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//            String channelId = "Chanel ID";
//            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            NotificationCompat.Builder notificationBuilder =
//                    new NotificationCompat.Builder(this, channelId)
//                            .setSmallIcon(R.mipmap.ic_launcher)
//                            .setContentTitle(messageTitle)
//                            .setContentText(messageBody)
//                            .setAutoCancel(true)
//                            .setSound(defaultSoundUri)
//                            .setContentIntent(pendingIntent);
//            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                String channelName = "Channel Name";
//                NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
//                notificationManager.createNotificationChannel(channel);
//            }
//            notificationManager.notify(0, notificationBuilder.build());
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
