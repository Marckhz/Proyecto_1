package com.example.elrobotista.sensor10;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.PrintStreamPrinter;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONArray;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.Arrays;
import android.view.KeyEvent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView.OnEditorActionListener;
import android.view.View.OnKeyListener;
import android.widget.Toast;


public class MainActivity extends Activity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private TextView texto;
    private TextView texto1;
    private TextView texto2;
    private Socket socket;
    private PrintStream out;
    private Boolean connected;
    private EditText addres;


    public MainActivity() {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addres = (EditText) findViewById(R.id.adrrInput);
        connected = false;
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        texto = (TextView) findViewById(R.id.textView);
        texto1 = (TextView) findViewById(R.id.textView2);
        texto2 = (TextView) findViewById(R.id.textView3);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        new Thread(new SocketThread()).start();
    }
    class SocketThread implements Runnable {

        public void run() {

            try {
                String sockInput = addres.toString();
                socket = new Socket(sockInput,5001);
                out = new PrintStream(socket.getOutputStream(),true);
                connected = true;
                } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    @Override
    public void onSensorChanged(final SensorEvent event) {

        texto.setText(Float.toString(event.values[0]));
        texto1.setText(Float.toString(event.values[1]));
        texto2.setText(Float.toString(event.values[2]));

        if(connected == true) {

            String s = String.format("{\"x\": %f, \"y\": %f, \"z\": %f}", event.values[0], event.values[1], event.values[2]);
            out.print(s);
        }
    }
}

