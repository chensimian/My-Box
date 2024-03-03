package com.example.btcontroller.entity;

import org.eclipse.paho.android.service.MqttService;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class ClientMQTT {


    public static final String HOST = "tcp://183.230.40.39:6002";
    public static final String TOPIC = "kai";
    private static final String clientid= "688993156";//设备id
    private String userName = "406537";//产品id
    private String passWord = "12345";//设备鉴权信息


    private MqttClient client;
    private MqttConnectOptions options;


    public void start() {
        try {
        // host为主机名，clientid即连接MQTT的客户端ID，一般以唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
        client = new MqttClient(HOST, clientid, new MemoryPersistence());
        // MQTT的连接设置
        options = new MqttConnectOptions();
        // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
        options.setCleanSession(true);
        // 设置连接的用户名
        options.setUserName(userName);
        // 设置连接的密码
        options.setPassword(passWord.toCharArray());
        // 设置超时时间 单位为秒
        options.setConnectionTimeout(10);
        // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
        options.setKeepAliveInterval(20);
        MqttTopic topic = client.getTopic(TOPIC);
        //setWill方法，如果项目中需要知道客户端是否掉线可以调用该方法。设置最终端口的通知消息
        options.setWill(topic, "close".getBytes(), 2, true);
        // 设置回调
        client.setCallback(new PushCallback());
        //连接
        client.connect(options);
        System.out.println("mqtt连接成功");

        //订阅消息
        int[] Qos  = {1};
        String[] topic1 = {TOPIC};
        client.subscribe(topic1, Qos);
        System.out.println("mqtt订阅成功");

    } catch (Exception e) {
        e.printStackTrace();
    }
}

            //订阅消息
//            int[] Qos  = {1};
//            String[] topic1 = {TOPIC};
//            client.subscribe(topic1, Qos);
//
//            MqttMessage msg = new MqttMessage();
//            Map map=new HashMap();
//            map.put("value","1");
//            ObjectMapper mapper = new ObjectMapper();
//            String payload = mapper.writeValueAsString(map);
//
//            msg.setPayload(payload.getBytes());  //发布内容
//            msg.setQos(1);                       //设置发布级别
//
//            client.publish(TOPIC, msg);          //发布




    public void send()  {
        try {

            MqttMessage msg = new MqttMessage();
            msg.setQos(0);  //发布级别
            msg.setPayload(("1").getBytes());  //
            client.publish(TOPIC, msg);
            System.out.println("mqtt发布消息成功");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ClientMQTT clientMQTT = new ClientMQTT();
        clientMQTT.start();
        clientMQTT.send();
    }


}
