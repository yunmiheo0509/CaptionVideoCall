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
import android.util.Log;
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
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SimpleTextAdapter(list);
        binding.recyclerView.setAdapter(adapter);

        // Animation ??????
        // SimpleItemAnimator
//        PhItemAnimator itemAnimator = new PhItemAnimator(this);
//        binding.recyclerView.setItemAnimator(itemAnimator);

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

        // SDK ??? ????????? ????????? ???????????????.
        initRemonCallback();
    }

    private void initRemonCallback() {
        // RemonCall, RemonCast ??? ???????????? ????????? ??? ???????????? ???????????????.
        remonCall.onInit(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // ?????? ?????? ??? ?????? ????????? ????????? ?????? ???????????? ???????????????.
        remonCall.onConnect(chid -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // ?????? ???????????? Peer ????????? ????????? ?????? ???????????? ???????????????.
        remonCall.onComplete(() -> {
            // ?????? ?????? - ????????? ?????? ????????? ?????? ??????
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 0);
                Log.d("??????: ??????????????????!", "start");
            }
            captionCreator = new CaptionCreator(handler);
            try {
                new Thread(new Runnable() {
                    public void run() {
                        captionCreator.recordSpeech();
                    }
                }).start();
            } catch (Throwable t) {
                System.out.println("?????? ????????? ?????? ?????? ?????? ??????");
            }
        });

        // ???????????? ????????? ?????????, close() ????????? ????????? ???????????? ???????????????.
        // CloseType.MINE : ????????? close() ??? ?????? ????????? ??????
        // CloseType.OTHER : ???????????? close() ??? ?????? ????????? ??????
        // CloseType.OTHER_UNEXPECTED : ???????????? ???????????? ????????? ????????? ??????
        // CloseType.UNKNOWN : ????????? ?????? ?????? ??????, ?????? ?????? ?????? ?????? ?????????
        remonCall.onClose(closeType -> {

            // ????????? ?????? ???????????? ??????
            if (closeType == CloseType.UNKNOWN && latestError != null) {
                Snackbar.make(
                        binding.rootLayout,
                        "????????? ????????? ?????????????????????. ??????=" + latestError.getDescription(),
                        Snackbar.LENGTH_SHORT
                ).show();
            }
            if (closeType == CloseType.OTHER || closeType == closeType.OTHER_UNEXPECTED) {

            }
            finish();
        });


        // ????????? ???????????? ?????? ?????? ?????? ??? onClose??? ?????? ?????????, ??????????????? ?????? ux ????????? onClose?????? ??????????????? ?????????.
        remonCall.onError(e -> {

        });

        // ????????? peer ?????? ???????????? ??????????????? ??? ???????????? ???????????????.
        remonCall.onMessage(this::addCaption);
    }

    @Override
    protected void onDestroy() {
        if (remonCall != null) {
            remonCall.close();
        }
        super.onDestroy();
        // ?????? ?????? - ?????? ??????
        if (captionCreator != null) {
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

    public void addCaption(String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!is_English(str)) {
//                    if (!list.isEmpty() && (list.get(list.size()-1) + str).length() < 14) {
//                        list.set(list.size()-1, list.get(list.size()-1) + " " + str);
//                    } else {
                    list.add(str);
//                    }
//                    adapter.notifyDataSetChanged();
                    // List ??????
                    adapter.notifyItemInserted(list.size());
                    binding.recyclerView.smoothScrollToPosition(binding.recyclerView.getAdapter().getItemCount() - 1);
//                    adapter.notifyItemInserted();
                }
            }
        });
    }

    public boolean is_English(String str) {
        for (int i = 0; i < str.length(); i++) {
            int index = str.charAt(i);
            if (index >= 65 && index <= 122) {
                return true;
            }
        }
        return false;
    }

    public String getNameById(String id) {
        return null;
    }
}


