package com.example.bishal.testapplication;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.MqttTopic;

import java.util.Properties;


public class Activity1 extends Activity {
    double latitude = 0;
    double longitude = 0;
    TextView txtgps ;
    MqttClient client = null;
    MqttMessage message;
    MqttTopic topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity1);
        txtgps = (TextView) findViewById(R.id.tv_gps);
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        MyLocationListener mlocListener = new MyLocationListener();
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);

        try {
            client = new MqttClient(
                    IoTDeviceProperties.TCPAddress,
                    IoTDeviceProperties.clientId, null);
            topic = client.getTopic(IoTDeviceProperties.topicStringSub);


            MqttConnectOptions opts = new MqttConnectOptions();

            //uncomment this for registered flow
            opts.setPassword(IoTDeviceProperties.password);
            opts.setUserName(IoTDeviceProperties.username);

            Properties securityProps = new Properties();
            securityProps.setProperty("com.ibm.ssl.protocol", "TLSv1.2");

            opts.setSSLProperties(securityProps);

            client.connect(opts);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {}
        }



        public class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {

            latitude = loc.getLatitude();
            longitude = loc.getLongitude();
            String Text = "Latitude = " + loc.getLatitude() + "Longitude = " + loc.getLongitude();
            txtgps.setText(Text);

            try {
                    JsonObject deviceEventObj = new JsonObject();
                    JsonObject finalObj = new JsonObject();

                    deviceEventObj.addProperty("myName", "JasmeetDevice");
                    deviceEventObj.addProperty("assets", "asset_jasmeet");
                    deviceEventObj.addProperty("latitude", latitude);
                    deviceEventObj.addProperty("longitude", longitude);
                    deviceEventObj.addProperty("altitude",  "0");

                    finalObj.add("d", deviceEventObj);

                    message = new MqttMessage(finalObj.toString().getBytes());

                    message.setQos(IoTDeviceProperties.QoS);
                    System.out.println("Waiting for up to "
                            + IoTDeviceProperties.sleepTimeout / 1000
                            + " seconds for publication of \"" + message.toString()
                            + "\" with QoS = " + message.getQos());
                    System.out.println("On topic  \"" + topic.getName()
                            + "\" for client instance: \"" + client.getClientId()
                            + "\" on address " + client.getServerURI() + "\"");

                    MqttDeliveryToken token = topic.publish(message);

                    token.waitForCompletion(IoTDeviceProperties.sleepTimeout);
                    System.out.println("Delivery token \"" + token.hashCode()
                            + "\" has been received: " + token.isComplete());
                    //Thread.sleep(2000);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(), "Disable",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(), "Enable",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }

}

