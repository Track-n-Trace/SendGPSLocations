package com.example.bishal.testapplication;
import java.util.Properties;

public final class IoTDeviceProperties {

    public static final String TCPAddress = System.getProperty("TCPAddress",
            "tcp://5m95lr.messaging.internetofthings.ibmcloud.com:1883");

    public static final String username = "use-token-auth";
    //
    public static final char[] password = "AdAmh8WmSaZm+DkM-K".toCharArray();

    //	public static String clientId = "d:quickstart:javadevice:001122334455";
    public static String clientId = "d:5m95lr:Pi:gateway_jasmeet";

    public static final String topicStringSub = "iot-2/evt/bishal/fmt/json";

    public static final int QoS =0;

    public static final long sleepTimeout = 4000;


    public static final Properties getSSLSettings() {
        final Properties properties = new Properties();
        properties.setProperty("com.ibm.ssl.keyStore",
                "C:\\IBM\\MQ\\Data\\ClientKeyStore.jks");
        properties.setProperty("com.ibm.ssl.keyStoreType", "JKS");
        properties.setProperty("com.ibm.ssl.keyStorePassword", "password");
        properties.setProperty("com.ibm.ssl.trustStore",
                "C:\\IBM\\MQ\\Data\\ClientTrustStore.jks");
        properties.setProperty("com.ibm.ssl.trustStoreType", "JKS");
        properties.setProperty("com.ibm.ssl.trustStorePassword", "password");
        return properties;
    }
}