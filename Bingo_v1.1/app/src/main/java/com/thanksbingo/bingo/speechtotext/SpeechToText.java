package com.thanksbingo.bingo.speechtotext;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;


import com.thanksbingo.db.BingoDB;
import com.thanksbingo.db.OtherClasses;

import java.util.ArrayList;


/**
 * Created by yoonsun on 2015. 1. 30..
 */

public class SpeechToText {

    public interface OnCallbackSTT {
        void onCallbackSTT(String result);
    }

    private SpeechRecognizer speechRecognizer;
    private Intent intent;
    private Context context;
    private OnCallbackSTT callbackSTT;
    private BingoDB bingoDB;
    ArrayList<OtherClasses.SomeFood> listDB;

    public SpeechToText(Context _context) {

        context = _context;
    }

    public void setUpSTT() {
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        speechRecognizer.setRecognitionListener(listener);
        Log.i("aaa", "setup");
    }

    public void startSTT(OnCallbackSTT callback) {
        speechRecognizer.startListening(intent);
        callbackSTT = callback;
        Log.i("aaa", "start");
    }

    public void processSTT(String[] arr){
        String result=""; //보여줄 결과 String
        ArrayList<String> temp= new ArrayList<>();
        String temp_s = "";

        Log.i("aaa", "process");

        bingoDB = new BingoDB(context);
        listDB = bingoDB.getFoodList();

        for(int i=0; i<arr.length; i++){
            temp_s = arr[i].replace(" ", "");
            boolean signal = true;
            for(int j=0; j<temp.size(); j++){
                if(temp_s.compareTo(temp.get(j))==0) {
                    signal = false;
                    break;
                }
            }
            if(signal==true)
                temp.add(temp_s);
        }

        for(int i=0; i<temp.size(); i++){
            for(int j=0; j<listDB.size(); j++){
                if (listDB.get(j).frequency > 1||(listDB.get(j).frequency==1&&j<=2)){
                    if(listDB.get(j).food_name.compareTo(arr[i])==0){
                        result = arr[i];
                        returnSTT(result);
                        return;
                    }
                }
            }
        }
        returnSTT(temp.get(0));
    }

    public void returnSTT(String result){
        callbackSTT.onCallbackSTT(result);
    }



    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {

        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int error) {
            Log.i("aaa", "on error");
        }

        @Override
        public void onResults(Bundle results) {

            Log.i("aaa", "on result");
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = results.getStringArrayList(key);
            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);
            processSTT(rs);
        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    };
}
