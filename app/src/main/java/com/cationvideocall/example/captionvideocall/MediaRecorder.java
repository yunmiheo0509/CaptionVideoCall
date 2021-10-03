package com.cationvideocall.example.captionvideocall;

import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MediaRecorder extends android.media.MediaRecorder {
    MediaRecorder recorder;
    MediaPlayer player;
    int position = 0; // 다시 시작 기능을 위한 현재 재생 위치 확인 변수

    File sdcard = Environment.getExternalStorageDirectory();
    File file = new File(sdcard, "recorded.mp4");
    String filename = file.getAbsolutePath();


    private void recordAudio() {

        recorder = new MediaRecorder();

        /* 그대로 저장하면 용량이 크다.
         * 프레임 : 한 순간의 음성이 들어오면, 음성을 바이트 단위로 전부 저장하는 것
         * 초당 15프레임 이라면 보통 8K(8000바이트) 정도가 한순간에 저장됨
         * 따라서 용량이 크므로, 압축할 필요가 있음 */
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 어디에서 음성 데이터를 받을 것인지, 인터넷..마이크,,,드읃ㅇ
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); // 압축 형식 설정
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);//바이트로 처리하는 기본적인 인코더

        recorder.setOutputFile(filename);

        try {
            recorder.prepare();
            recorder.start();
            Log.d("녹음 시작됨.", "recordAudio()"+"   파일명: "+filename);
//            Toast.makeText(, "녹음 시작됨.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
            Log.d("녹음 중지", "stopRecording()");
//            Toast.makeText(this, "녹음 중지됨.", Toast.LENGTH_SHORT).show();
        }
    }

    private void playAudio() {
        try {
            closePlayer();

            player = new MediaPlayer();
            player.setDataSource(filename);//재생하려는 파일
            player.prepare();//start하기전에 무조건 적어야함.
            player.start();
            Log.d("재생 시작됨.", "playAudio()");
//            Toast.makeText(this, "재생 시작됨.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void pauseAudio() {
        if (player != null) {
            position = player.getCurrentPosition();
            player.pause();
            Log.d("일시정지됨.", "pauseAudio()");
//            Toast.makeText(this, "일시정지됨.", Toast.LENGTH_SHORT).show();
        }
    }

    private void resumeAudio() {
        if (player != null && !player.isPlaying()) {
            player.seekTo(position);
            player.start();
            Log.d("재시작됨.", "resumeAudio()");
//            Toast.makeText(this, "재시작됨.", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopAudio() {
        if (player != null && player.isPlaying()) {
            player.stop();
            Log.d("중지됨.", "stopAudio()");
//            Toast.makeText(this, "중지됨.", Toast.LENGTH_SHORT).show();
        }
    }

    public void closePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }
}
