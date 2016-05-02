package com.example.android.smartstreet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

public class LedInteract extends AppCompatActivity {

    public static final int COLOR_RED   = 0;
    public static final int COLOR_BLUE  = 1;
    public static final int COLOR_GREEN = 2;
    public static final int COLOR_INTENSITY = 2;

    SeekBar mRedSeek, mGreenSeek, mBlueSeek, mIntensitySeek;
    LinearLayout mRGBValueHolder;

    private Switch mySwitch;


    private long lastUpdate = System.currentTimeMillis();
    private boolean pHueOn = false;

    PubNubHelper helper = new PubNubHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led_interact);



        mySwitch = (Switch) findViewById(R.id.mySwitch);
        // Get Seek Bars
        mRedSeek   = (SeekBar) findViewById(R.id.red_seek);
        mGreenSeek = (SeekBar) findViewById(R.id.green_seek);
        mBlueSeek  = (SeekBar) findViewById(R.id.blue_seek);
        mIntensitySeek  = (SeekBar) findViewById(R.id.intensity_seek);
        mRGBValueHolder = (LinearLayout) findViewById(R.id.rgb_value_holder);

        // Setup Seek Bars
        setupSeekBar(mRedSeek, COLOR_RED);
        setupSeekBar(mGreenSeek, COLOR_GREEN);
        setupSeekBar(mBlueSeek, COLOR_BLUE);
        setupIntensity(mIntensitySeek,COLOR_INTENSITY);


        mySwitch.setChecked(false);
        //attach a listener to check for changes in state
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    helper.publishStart();
                    setRGBSeeks(255, 255, 255);

                }else{
                    helper.publishStop();
                    setRGBSeeks(0, 0, 0);
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setupSeekBar(SeekBar seekBar, final int colorID){
        seekBar.setMax(255);        // Seek bar values goes 0-255
        seekBar.setProgress(0);   // Set the knob to 255 to start
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                helper.publishColor(mRedSeek.getProgress(), mGreenSeek.getProgress(), mBlueSeek.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                helper.publishColor(mRedSeek.getProgress(), mGreenSeek.getProgress(), mBlueSeek.getProgress());
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                TextView colorValueText;
                switch (colorID) {  // Get the TextView identified by the colorID
                    case COLOR_RED:
                        colorValueText = (TextView) findViewById(R.id.red_value);
                        break;
                    case COLOR_GREEN:
                        colorValueText = (TextView) findViewById(R.id.green_value);
                        break;
                    case COLOR_BLUE:
                        colorValueText = (TextView) findViewById(R.id.blue_value);
                        break;
                    default:
                        Log.e("SetupSeek", "Invalid color.");
                        return;
                }
                colorValueText.setText(String.valueOf(progress));  // Update the 0-255 text
                int red = mRedSeek.getProgress();     // Get Red value 0-255
                int green = mGreenSeek.getProgress();   // Get Grn value 0-255
                int blue = mBlueSeek.getProgress();    // Get Blu value 0-255
                updateRGBViewHolderColor(red, green, blue); // Change the background of the viewholder

                long now = System.currentTimeMillis();    // Only allow 1 pub every 100 milliseconds
                if (now - lastUpdate > 100 && fromUser) { // Threshold and only send when user sliding
                    lastUpdate = now;
                    helper.publishColor(red, green, blue);          // Stream RGB values to the Pi
                }
            }
        });
    }

    public void setupIntensity(SeekBar seekBar, final int colorID){
        seekBar.setMax(255);        // Seek bar values goes 0-255
        seekBar.setProgress(0);   // Set the knob to 255 to start
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                helper.publishColor(mRedSeek.getProgress(), 0, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                helper.publishColor(mRedSeek.getProgress(), 0, 0);
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

                int red = mIntensitySeek.getProgress();     // Get Red value 0-255
                int green = 0;   // Get Grn value 0-255
                int blue = 0;   // Get Blu value 0-255
                //updateRGBViewHolderColor(red, green, blue); // Change the background of the viewholder

                long now = System.currentTimeMillis();    // Only allow 1 pub every 100 milliseconds
                if (now - lastUpdate > 100 && fromUser) { // Threshold and only send when user sliding
                    lastUpdate = now;
                    helper.publishColor(red, green, blue);          // Stream RGB values to the Pi
                }
            }
        });
    }
    //to add to the git
    public void lightOff(View view){
        helper.publishStop();
        setRGBSeeks(0, 0, 0);
    }

    public void lightOn(View view) {
        helper.publishStart();
        setRGBSeeks(255, 255, 255);
    }


    public void pHueOn(View view){
        this.pHueOn = true;

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    for (int i = 0; i < 720; i+=3) {
                        if (!pHueOn) return;
                        int r = posSinWave(50, i, 0.5);
                        int g = posSinWave(50, i, 1);
                        int b = posSinWave(50, i, 2);
                        helper.publishColor(r, g, b);
                        setRGBSeeks(r, g, b);
                        try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
                    }
                }
            }
        });
        t.start();

    }

    public int posSinWave(int amplitude, int angle, double frequency) {
        return (int)((amplitude + (amplitude * Math.sin(Math.toRadians(angle) * frequency)))/100.0*255);
    }

    public void setRGBSeeks(int red, int green, int blue){
        mRedSeek.setProgress(red);
        mGreenSeek.setProgress(green);
        mBlueSeek.setProgress(blue);
    }

    private void updateRGBViewHolderColor(int red, int green, int blue){
        int alpha = 255; // No opacity
        int color = (alpha << 24) | (red << 16) | (green << 8) | blue;
        mRGBValueHolder.setBackgroundColor(color);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        this.pHueOn = false;
        return super.dispatchTouchEvent(ev);
    }


}
