package com.example.android.smartstreet;

import android.util.Log;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pavanibaradi on 4/28/16.
 */
public class PubNubHelper {
    private Pubnub mPubNub;
    public static final String PUBLISH_KEY = "pub-c-61caa62e-ea5d-4292-88ea-de9f27bf88a9";
    public static final String SUBSCRIBE_KEY = "sub-c-b5e4eabc-0ca1-11e6-8c3e-0619f8945a4f";
    public static final String PHUE_CHANNEL = "phue";
    public static final String START_CHANNEL = "start";
    public static final String STOP_CHANNEL = "stop";
    public static final String PLAY_CHANNEL = "play";
    public static final String PAUSE_CHANNEL = "pause";
    public static final String STOP_MUSIC_CHANNEL = "stopMusic";
    public static final String NEXT_MUSIC_CHANNEL = "next";
    public static final String PREVIOUS_MUSIC_CHANNEL = "previous";
    public static final String PIR_ON_CHANNEL = "pirOn";
    public static final String PIR_OFF_CHANNEL = "pirOff";

    public PubNubHelper() {
        initPubNub();
    }

    public void initPubNub(){
        this.mPubNub = new Pubnub(
                PUBLISH_KEY,
                SUBSCRIBE_KEY
        );
        this.mPubNub.setUUID("AndroidPHue");
        subscribe();
    }

    public void publishColor(int red, int green, int blue){
        JSONObject js = new JSONObject();
        try {
            js.put("RED",   red);
            js.put("GREEN", green);
            js.put("BLUE",  blue);
        } catch (JSONException e) { e.printStackTrace(); }

        publish(PHUE_CHANNEL, js);
    }

    public void publishStart(){
        JSONObject js = new JSONObject();
        try {
            js.put("MESSAGE",   "start LED ");
        } catch (JSONException e) { e.printStackTrace(); }

        publish(START_CHANNEL, js);
    }

    public void publishStop(){
        JSONObject js = new JSONObject();
        try {
            js.put("MESSAGE",   "stop LED ");
        } catch (JSONException e) { e.printStackTrace(); }

        publish(STOP_CHANNEL, js);
    }

    //to add to the git

    public void publishPlayMusic() {
        JSONObject js = new JSONObject();
        try {
            js.put("MESSAGE",   "Play Music ");
        } catch (JSONException e) { e.printStackTrace(); }

        publish(PLAY_CHANNEL, js);
    }

    public void publishPauseMusic() {
        JSONObject js = new JSONObject();
        try {
            js.put("MESSAGE",   "Pause Music ");
        } catch (JSONException e) { e.printStackTrace(); }

        publish(PAUSE_CHANNEL, js);
    }

    public void publishStopMusic() {
        JSONObject js = new JSONObject();
        try {
            js.put("MESSAGE", "Stop Music ");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        publish(STOP_MUSIC_CHANNEL, js);
    }


    public void publishNextMusic() {
        JSONObject js = new JSONObject();
        try {
            js.put("MESSAGE", "Next Music ");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        publish(NEXT_MUSIC_CHANNEL, js);
    }

    public void publishPreviousMusic() {
        JSONObject js = new JSONObject();
        try {
            js.put("MESSAGE", "Previous Music ");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        publish(PREVIOUS_MUSIC_CHANNEL, js);
    }

    public void publishPIROnSensor() {
        JSONObject js = new JSONObject();
        try {
            js.put("MESSAGE", "PIR Sensor On ");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        publish(PIR_ON_CHANNEL, js);
    }

    public void publishPIROffSensor() {
        JSONObject js = new JSONObject();
        try {
            js.put("MESSAGE", "PIR Sensor Off ");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        publish(PIR_OFF_CHANNEL, js);
    }

    public void publish(String channel, JSONObject js){
        Callback callback = new Callback() {
            public void successCallback(String channel, Object response) {
                Log.d("PUBNUB", response.toString());
            }

            public void errorCallback(String channel, PubnubError error) {
                Log.d("PUBNUB", error.toString());
            }
        };
        this.mPubNub.publish(channel, js, callback);

    }

    public void subscribe(){
        subscribe(PHUE_CHANNEL);
        subscribe(START_CHANNEL);
        subscribe(STOP_CHANNEL);
        subscribe(PLAY_CHANNEL);
        subscribe(PAUSE_CHANNEL);
        subscribe(STOP_MUSIC_CHANNEL);
        subscribe(NEXT_MUSIC_CHANNEL);
        subscribe(PREVIOUS_MUSIC_CHANNEL);
        subscribe(PIR_ON_CHANNEL);
        subscribe(PIR_OFF_CHANNEL);

    }

    public void subscribe(String channel){
        try {
            this.mPubNub.subscribe(channel, new Callback() {
                @Override
                public void connectCallback(String channel, Object message) {
                    Log.d("PUBNUB", "SUBSCRIBE : CONNECT on channel:" + channel
                            + " : " + message.getClass() + " : "
                            + message.toString());
                }

                @Override
                public void disconnectCallback(String channel, Object message) {
                    Log.d("PUBNUB", "SUBSCRIBE : DISCONNECT on channel:" + channel
                            + " : " + message.getClass() + " : "
                            + message.toString());
                }

                public void reconnectCallback(String channel, Object message) {
                    Log.d("PUBNUB", "SUBSCRIBE : RECONNECT on channel:" + channel
                            + " : " + message.getClass() + " : "
                            + message.toString());
                }

                @Override
                public void successCallback(String channel, Object message) {
                    Log.d("PUBNUB", "SUBSCRIBE : " + channel + " : "
                            + message.getClass() + " : " + message.toString());
                }

                @Override
                public void errorCallback(String channel, PubnubError error) {
                    Log.d("PUBNUB", "SUBSCRIBE : ERROR on channel " + channel
                            + " : " + error.toString());
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
