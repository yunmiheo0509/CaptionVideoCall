package com.cationvideocall.example.captionvideocall;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.captionvideocall.example.captionvideocall.databinding.ActivityOnCallBinding;
import com.google.android.material.snackbar.Snackbar;
import com.captionvideocall.example.captionvideocall.R;
import com.remotemonster.sdk.RemonCall;
import com.remotemonster.sdk.RemonException;
import com.remotemonster.sdk.data.CloseType;

public class OnCallActivity extends AppCompatActivity {
    private int REMON_PERMISSION_REQUEST = 0x0101;
    // RemonCall 객체 정의 - P2P 1:1 통화
    // 1:1 통화는 RemonCall 을 사용합니다.
    private RemonCall remonCall = null;
    // 안드로이드 UI에 관련된 부분
    private ActivityOnCallBinding binding;
    private ConstraintSet constraintSet = null;
    private ConstraintSet defaultConstraintSet = null;

    //기타
    private InputMethodManager inputMethodManager;
    private RemonException latestError = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_call);
        Intent intent = getIntent();
        String user_id = intent.getExtras().getString("user_id");
        String room_num = intent.getExtras().getString("room_num");
//       String user_id = "작은아들";String room_num="5020";
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // bind activity layout
        binding = DataBindingUtil.setContentView(this, R.layout.activity_on_call);

        // 레이아웃 조절을 위해 activity_main.xml의 constraint 정보를 저장해 둡니다.
        constraintSet = new ConstraintSet();

        defaultConstraintSet = new ConstraintSet();

        constraintSet.clone(binding.constraintLayout);
        defaultConstraintSet.clone(binding.constraintLayout);

        updateView(false);
        // RemonCall 초기화
        initRemonCall();

        // RemonCall 연결
        remonCall.connect(room_num);

//        binding.btnConnect.setEnabled(false);
        binding.imvClose.setEnabled(true);

        // 버튼 이벤트 연결
        // 연결, 종료 버튼 클릭이벤트를 정의합니다.
        // 연결 버튼 클릭시 RemonCall 을 초기화하고, connect(채널명) 메쏘드를 호출합니다.
//        binding.btnConnect.setOnClickListener(view -> {
//            if (binding.etChannelName.getText().toString().isEmpty()) {
//                Snackbar.make(binding.rootLayout, "채널명을 입력하세요.", Snackbar.LENGTH_SHORT).show();
//            } else {
//                inputMethodManager.hideSoftInputFromWindow(binding.etChannelName.getWindowToken(), 0);
//                binding.etChannelName.clearFocus();
//
//                // RemonCall 초기화
//                initRemonCall();
//
//                // RemonCall 연결
//                remonCall.connect(binding.etChannelName.getText().toString());
//
//                binding.btnConnect.setEnabled(false);
//                binding.btnClose.setEnabled(true);
//            }
//        });


        // 종료 버튼 클릭시 RemonCall 의 close() 메쏘드를 호출합니다.
        binding.imvClose.setOnClickListener(view -> {
            remonCall.close();
            remonCall = null;
            Intent intentMain = new Intent(OnCallActivity.this, MainActivity.class);
            startActivity(intentMain);
            finish();
        });

        //원래 여기가 메시지 보내는 부분이다 참고하기참고하기!!!!!!!이거 xml에서는 지워버림
        // 메시지 버튼
//        binding.btnSend.setOnClickListener(view -> {
//            String msg = binding.etMessage.getText().toString();
//            binding.etMessage.setText("");
//            inputMethodManager.hideSoftInputFromWindow(binding.etMessage.getWindowToken(), 0);
//            binding.etMessage.clearFocus();
//
//            if (remonCall != null && !msg.isEmpty()) {
//                remonCall.sendMessage(msg);
//            }
//        });
    }

    // RemonCall 초기화
    // Builder 를 사용해 각 설정 정보를 정의
    private void initRemonCall() {
        remonCall = RemonCall.builder()
                .context(this)
                .serviceId("75faedb2-cbb0-4be2-a85b-be24e7212d9c")
//            .serviceId( "SERVICEID1")
//            .key("1234567890")
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
        remonCall.onInit(() -> {
        });

        // 서버 접속 및 채널 생성이 완료된 이후 호출되는 콜백입니다.
        remonCall.onConnect(chid -> {
            Snackbar.make(binding.rootLayout, "채널($id)에 연결되었습니다.", Snackbar.LENGTH_SHORT).show();
            updateView(false);
        });

        // 다른 사용자와 Peer 연결이 완료된 이후 호출되는 콜백입니다.
        remonCall.onComplete(() -> {
            updateView(true);
        });

        // 상대방이 연결을 끊거나, close() 호출후 종료가 완료되면 호출됩니다.
        // CloseType.MINE : 자신이 close() 를 통해 종료한 경우
        // CloseType.OTHER : 상대방이 close() 를 통해 종료한 경우
        // CloseType.OTHER_UNEXPECTED : 상대방이 끊어져서 연결이 종료된 경우
        // CloseType.UNKNOWN : 에러에 의한 연결 종료, 기타 연결 종료 이유 불명확
        remonCall.onClose(closeType -> {
            updateView(false);
//            binding.btnConnect.setEnabled(true);
            binding.imvClose.setEnabled(false);

            // 에러에 의한 종료인지 체크
            if (closeType == CloseType.UNKNOWN && latestError != null) {
                Snackbar.make(
                        binding.rootLayout,
                        "오류로 연결이 종료되었습니다. 에러=" + latestError.getDescription(),
                        Snackbar.LENGTH_SHORT
                ).show();
            }
        });


        // 에러가 발생할 때 호출되는 콜백을 정의합니다.
        // 연결이 종료되는 경우 에러 전달 후 onClose가 호출 되므로,
        // 시나리오에 따른 ux 처리는 onClose에서 진행되어야 합니다.
        remonCall.onError(e -> {
            Log.e("SimpleRemon", "error=" + e.getDescription());
            latestError = e;
        });
        // 연결된 peer 간에 메시지를 전달하는 경우 호출되는 콜백입니다.
        remonCall.onMessage(msg -> {
            Snackbar.make(binding.rootLayout, msg, Snackbar.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onDestroy() {
        remonCall.close();
        super.onDestroy();
    }

    // 연결된 이후 간단하게 레이아웃을 변경하는 예제입니다.
    // SDK가 아닌 서비스와 관련한 부분으로 참고용도로 살펴보시기 바랍니다.
    private void updateView(Boolean isConnected) {

        changeConstraints(isConnected);

        // 로컬뷰의 이미지뷰 해당 이미지 끄기
        for (int j = 0; j < binding.layoutLocal.getChildCount(); j++) {
            View view = binding.layoutLocal.getChildAt(j);
            if (view instanceof ImageView) {
                if (isConnected) {
                    view.setVisibility(View.INVISIBLE);
                } else {
                    view.setVisibility(View.VISIBLE);
                }
            }
        }

//        if (isConnected) {
//            binding.layoutMessage.setVisibility(View.VISIBLE);
//        } else {
//            binding.layoutMessage.setVisibility(View.GONE);
//        }
    }

    // 뷰 크기를 조정하기 위해 constraints 값을 변경합니다.
    private void changeConstraints(Boolean isConnected) {
        constraintSet.clone(defaultConstraintSet);

        // 연결되지 않은 경우에는 로컬을 크게 보여주고, 연결되면 activity_main.xml 설정으로 보여준다.
        if (!isConnected) {
            // 로컬 레이아웃 사이즈 constraint 등록
            constraintSet.constrainWidth(R.id.layoutLocal, ConstraintSet.MATCH_CONSTRAINT);
            constraintSet.connect(R.id.layoutLocal, ConstraintSet.TOP, R.id.constraintLayout, ConstraintSet.TOP, 0);
            constraintSet.connect(R.id.layoutLocal, ConstraintSet.START, R.id.constraintLayout, ConstraintSet.START, 0);
            constraintSet.connect(R.id.layoutLocal, ConstraintSet.END, R.id.constraintLayout, ConstraintSet.END, 0);
            constraintSet.connect(R.id.layoutLocal, ConstraintSet.BOTTOM, R.id.constraintLayout, ConstraintSet.BOTTOM, 0);
        }

        // change bounds transition
        ChangeBounds transition = new ChangeBounds();
        transition.setDuration(500);

        TransitionManager.beginDelayedTransition(binding.constraintLayout, transition);
        constraintSet.applyTo(binding.constraintLayout);

    }

    // 권한을 체크합니다.
    // 사용자에게 필수로 권한을 확인받아야 하는  요소는 CAMERA,RECORD_AUDIO,WRITE_EXTERNAL_STORAGE 입니다
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
}


