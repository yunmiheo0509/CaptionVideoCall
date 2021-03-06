package com.cationvideocall.example.captionvideocall.captionCreator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.audiofx.NoiseSuppressor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.cationvideocall.example.captionvideocall.Activity.MainActivity;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CaptionCreator {
    private static final String MSG_KEY = "status";
    int startingIndex, endIndex;
    byte[] speechData;
    Handler handler;
    boolean isRecording;
    AudioRecord audio;
    int bufferSize;

    @SuppressLint("MissingPermission")
    public CaptionCreator(Handler handler_) {
        this.handler = handler_;

        bufferSize = AudioRecord.getMinBufferSize(
                16000, // sampling frequency
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        audio = new AudioRecord(
                MediaRecorder.AudioSource.VOICE_RECOGNITION,
                16000, // sampling frequency
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize);
    }

    // record
    public void recordSpeech() throws RuntimeException {
        int lenSpeech = 0;
        isRecording = true;
        speechData = new byte[16000 * 60 * 2];
        try {
//            if (audio.getState() != AudioRecord.STATE_INITIALIZED) {
            if (false) {
                throw new RuntimeException("ERROR: Failed to initialize audio device. Allow app to access microphone");
            } else {
                short[] inBuffer = new short[bufferSize];
                audio.startRecording();
                int total;
                int level;
                int cnt = 0;
                startingIndex = 0;
                endIndex = 0;
                boolean voiceReconize = false;

                // ?????? ?????? ??????
                int init_level = 0;
//                for (int t = 0; t< 10; t++){
//                    int ret = audio.read(inBuffer, 0, bufferSize);
//                    total = 0;
//                    for (int i = 0; i < ret; i++) {
//                        total += Math.abs(inBuffer[i]);
//                    }
//                    init_level += total / (ret+1);
//                }
//                init_level = init_level / 10;

                System.out.println("?????? Level check: " + init_level);
                while (isRecording) {
                    int ret = audio.read(inBuffer, 0, bufferSize);

                    for (int i = 0; i < ret; i++) {
                        try {
                            speechData[lenSpeech * 2] = (byte) (inBuffer[i] & 0x00FF);
                            speechData[lenSpeech * 2 + 1] = (byte) ((inBuffer[i] & 0xFF00) >> 8);
                            lenSpeech++;
                        } catch (ArrayIndexOutOfBoundsException e){
                            speechData = new byte[16000 * 60 * 2];
                        }

                    }

                    total = 0;

                    for (int i = 0; i < ret; i++) {
                        total += Math.abs(inBuffer[i]);
                    }
                    level = total / (ret+1);

                    // level ??? ??????..
                    // level ?????? 2000??? ?????? ?????? ???????????? ????????? ??????
                    // 2000??? ?????? ???????????? cnt ??? ???????????? 10??? ?????? ???????????? ???????????? ?????? ????????? ?????????
                    // voiceReconize ??? ????????? ?????? ?????? ?????????
                    if (!voiceReconize) {
                        if (level > 1000 + init_level) {
                            if (cnt == 0)
                                startingIndex = lenSpeech;
                            cnt++;
                        }

                        if (cnt > 5) {
                            cnt = 0;
                            voiceReconize = true;
                            System.out.println("level ?????? ???????????? 1000 ?????? ???????????????????????? 15 ?????? ???????????? ????????? ?????? ??????");
                            // level ?????? ???????????? 1000 ?????? ???????????????????????? 15 ?????? ???????????? ????????? ?????? ??????
                            // ???????????? ???????????? ?????? ????????? ?????? ?????? ????????? -15
                            startingIndex -= 60;
                            if (startingIndex < 0)
                                startingIndex = 0;
                        }
                    }

                    if (voiceReconize) {
                        // ???????????? ????????? 500????????? ????????? ????????? 5?????? ????????? ??????
                        // ????????? ????????? ??????????????? ??????.. ?????? ?????? ??????
                        if (level < 500 + init_level) {
                            System.out.println("???????????? ????????? 500????????? ????????? ????????? 5?????? ????????? ??????");
                            cnt++;
                        }
                        // ????????? ?????? ????????? ????????? ?????? ?????? ???????????? ?????? ????????? ??????????????? cnt ?????? 0
                        if (level > 1000 + init_level) {
                            System.out.println("????????? ?????? ????????? ????????? ?????? ?????? ???????????? ?????? ????????? ??????????????? cnt ?????? 0");
                            cnt = 0;
                        }
                        // endIndex ??? ???????????? ??????????????? ??????
                        if (cnt > 5) {
                            System.out.println("endIndex ??? ???????????? ??????????????? ??????");
//                            System.out.println("startingIndex" + startingIndex);
                            endIndex = lenSpeech;

                            String response = sendAndGetData(makeRequest(Arrays.copyOfRange(speechData, 2 * startingIndex, 2 * endIndex + 1), endIndex-startingIndex));
                            SendMessage(response);

                            speechData = new byte[16000 * 60 * 2];
                            lenSpeech = 0;
                            endIndex = 0;
                            startingIndex = 0;
                            voiceReconize = false;
                            cnt = 0;
                        }
                    }
                }

                audio.stop();
                audio.release();
            }
        } catch (Throwable t) {
            throw new RuntimeException(t.toString());
        }

    }

    public void SendMessage(String str) {
        Message msg = handler.obtainMessage();
        Bundle bd = new Bundle();
        bd.putString(MSG_KEY, str);
        msg.setData(bd);
        Log.d("??????:recordspeach?????? e!","?????? ????????? ?????????.sendmesag :"+str);
        handler.sendMessage(msg);
    }

    public void stopRecording(){
        isRecording = false;
    }


    // send
    public Map<String, Object> makeRequest (byte[] speechData, int lenSpeech) {
        String accessKey = "154f164e-34ca-4b52-88b3-8108edd22849";
        String languageCode = "korean";
        String audioContents;


        Map<String, Object> request = new HashMap<>();
        Map<String, String> argument = new HashMap<>();
        audioContents = Base64.encodeToString(
                speechData, 0, lenSpeech*2, Base64.NO_WRAP);

        argument.put("language_code", languageCode);
        argument.put("audio", audioContents);

        request.put("access_key", accessKey);
        request.put("argument", argument);
        return request;
    }

    public String sendAndGetData (Map<String, Object> request) {
        URL url;
        Integer responseCode;
        String responBody;
        String openApiURL = "http://aiopen.etri.re.kr:8000/WiseASR/Recognition";
        Gson gson = new Gson();

        try {
            url = new URL(openApiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(gson.toJson(request).getBytes("UTF-8"));
            wr.flush();
            wr.close();

            responseCode = con.getResponseCode();
            if ( responseCode == 200 ) {
                InputStream is = new BufferedInputStream(con.getInputStream());
                responBody = readStream(is);
                JSONObject json = new JSONObject(responBody);
                return json.getJSONObject("return_object").getString("recognized");
//                return responBody;
            }
            else
                return "ERROR: " + Integer.toString(responseCode);
        } catch (Throwable t) {
            return "ERROR: " + t.toString();
        }
    }

    public static String readStream(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(in),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        in.close();
        return sb.toString();
    }

}
