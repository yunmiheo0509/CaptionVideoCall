package com.cationvideocall.example.captionvideocall.Activity;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;

import com.captionvideocall.example.captionvideocall.databinding.ActivityOnCallBinding;
import com.cationvideocall.example.captionvideocall.PhItemAnimator;
import com.cationvideocall.example.captionvideocall.captionCreator.CaptionCreator;
import com.cationvideocall.example.captionvideocall.recyclerview.SimpleTextAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.captionvideocall.example.captionvideocall.R;
import com.remotemonster.sdk.RemonCall;
import com.remotemonster.sdk.RemonException;
import com.remotemonster.sdk.data.CloseType;

import java.util.ArrayList;

public class OnCallActivity extends AppCompatActivity {
    private RemonCall remonCall = null;
    private ActivityOnCallBinding binding;

    private RemonException latestError = null;
    ArrayList<String> list;
    SimpleTextAdapter adapter;

    // Caption
    CaptionCreator captionCreator;
    private static final String MSG_KEY = "status";
    private final Handler handler = new Handler() {
        @Override
        public synchronized void handleMessage(Message msg) {

            Bundle bd = msg.getData();
            String v = bd.getString(MSG_KEY);
            if (remonCall != null && !v.isEmpty()) {
                remonCall.sendMessage(v);
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_call);

        // get information
        Intent intent = getIntent();
        String room_num = intent.getExtras().getString("room_num");
        String counter_id = intent.getExtras().getString("counter_id");


        // settings
        checkPermission();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        // layout sources
        binding = DataBindingUtil.setContentView(this, R.layout.activity_on_call);
        binding.imvClose.setOnClickListener(view -> {
            remonCall.close();
        });

        list = new ArrayList<>();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this)) ;
        adapter = new SimpleTextAdapter(list) ;
        binding.recyclerView.setAdapter(adapter) ;

        // Animation 설정

        /* DefaultItemAnimator
        DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setChangeDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        */

        // SimpleItemAnimator
        PhItemAnimator itemAnimator = new PhItemAnimator(this);
        binding.recyclerView.setItemAnimator(itemAnimator);

        // start Remote-monster
        initRemonCall();
        remonCall.connect(room_num);
    }


    private void initRemonCall() {
        remonCall = RemonCall.builder()
                .context(this)
                .serviceId("75faedb2-cbb0-4be2-a85b-be24e7212d9c")
                .key("c95cca523618c8b5b51d7c24a0d72c993fd41254f6a09b790f16c949085f6dd6")
                .videoCodec("VP8")
                .videoWidth(640)
                .videoHeight(480)
                .localView(binding.surfRendererLocal)
                .remoteView(binding.surfRendererRemote)
                .build();

        // SDK 의 이벤트 콜백을 정의합니다.
        initRemonCallback();
    }

    private void initRemonCallback() {
        // RemonCall, RemonCast 의 초기화가 완료된 후 호출되는 콜백입니다.
        remonCall.onInit(() -> {});

        // 서버 접속 및 채널 생성이 완료된 이후 호출되는 콜백입니다.
        remonCall.onConnect(chid -> {});

        // 다른 사용자와 Peer 연결이 완료된 이후 호출되는 콜백입니다.
        remonCall.onComplete(() -> {
            // 영웅 추가 - 캡션을 위한 오디오 녹음 시작
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECORD_AUDIO}, 0);
            }
            captionCreator = new CaptionCreator(handler);
            try {
                new Thread(new Runnable() {
                    public void run() {
                        captionCreator.recordSpeech();
                    }
                }).start();
            } catch (Throwable t) {
                System.out.println("자막 전송을 위한 녹음 에러 발생");
            }
        });

        // 상대방이 연결을 끊거나, close() 호출후 종료가 완료되면 호출됩니다.
        // CloseType.MINE : 자신이 close() 를 통해 종료한 경우
        // CloseType.OTHER : 상대방이 close() 를 통해 종료한 경우
        // CloseType.OTHER_UNEXPECTED : 상대방이 끊어져서 연결이 종료된 경우
        // CloseType.UNKNOWN : 에러에 의한 연결 종료, 기타 연결 종료 이유 불명확
        remonCall.onClose(closeType -> {

            // 에러에 의한 종료인지 체크
            if (closeType == CloseType.UNKNOWN && latestError != null) {
                Snackbar.make(
                        binding.rootLayout,
                        "오류로 연결이 종료되었습니다. 에러=" + latestError.getDescription(),
                        Snackbar.LENGTH_SHORT
                ).show();
            }
            if (closeType == CloseType.OTHER || closeType == closeType.OTHER_UNEXPECTED) {

            }
            finish();
        });


        // 연결이 종료되는 경우 에러 전달 후 onClose가 호출 되므로, 시나리오에 따른 ux 처리는 onClose에서 진행되어야 합니다.
        remonCall.onError(e -> {

        });

        // 연결된 peer 간에 메시지를 전달받았을 때 호출되는 콜백입니다.
        remonCall.onMessage(msg -> {
            addCaption(msg);
        });
    }

    @Override
    protected void onDestroy() {
        if (remonCall != null) {
            remonCall.close();
        }
        super.onDestroy();
        // 영웅 추가 - 녹음 정지
        if (captionCreator != null){
            captionCreator.stopRecording();
        }
    }


    @SuppressLint("NewApi")
    private void checkPermission() {
        String[] MANDATORY_PERMISSIONS = {
                "android.permission.INTERNET",
                "android.permission.CAMERA",
                "android.permission.RECORD_AUDIO",
                "android.permission.MODIFY_AUDIO_SETTINGS",
                "android.permission.ACCESS_NETWORK_STATE",
                "android.permission.CHANGE_WIFI_STATE",
                "android.permission.ACCESS_WIFI_STATE",
                "android.permission.READ_PHONE_STATE",
                "android.permission.BLUETOOTH",
                "android.permission.BLUETOOTH_ADMIN",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(MANDATORY_PERMISSIONS, 100);
        }
    }

    public void addCaption(String str){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!is_English(str)){
                    if (!list.isEmpty() && (list.get(list.size()-1) + str).length() < 14) {
                        list.set(list.size()-1, list.get(list.size()-1) + " " + str);
                    } else {
                        list.add(str) ;
                    }
                    binding.recyclerView.smoothScrollToPosition(binding.recyclerView.getAdapter().getItemCount() - 1);
//                    adapter.notifyDataSetChanged();
                    // List 반영
                    adapter.notifyItemInserted(list.size());
//                    adapter.notifyItemInserted();
                }
            }
        });
    }

    public boolean is_English(String str){
        for (int i=0; i < str.length(); i++){
            int index = str.charAt(i);
            if (index >= 65 && index <= 122){
                return true;
            }
        }
        return false;
    }

    public String getNameById(String id){
        return null;
    }
}


