package com.cms.incident.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GcMqttClient {

	private static MqttClient mqttClient;
	
	GcMqttConfig config;
		
	@Autowired
	public GcMqttClient(GcMqttConfig mqttconfig) {
		config = mqttconfig;
		connect();
	}
//	public MqttClient getClient() {
//		
//		return mqttClient;
//	}
//	
//	private static void setClient(MqttClient client) {
//		mqttClient = client;
//	}
	
//	public void connect(
//					String host, 
//					String clientID, 
//					String userName, 
//					String password, 
//					int timeout,
//					int keepalive) {
		public void connect() {
			
		
		String host = config.getHostUrl();
		String clientID = config.getClientID();
		String userName = config.getUsername();
		String password = config.getPassword();
		int timeout = config.getTimeout();
		int keepalive = config.getKeepalive();
		
		try {
			mqttClient = new MqttClient(host, clientID, new MemoryPersistence());
			MqttConnectOptions options = new MqttConnectOptions();
			options.setCleanSession(true);
			options.setUserName(userName);
			options.setPassword(password.toCharArray());
			options.setConnectionTimeout(timeout);
			options.setKeepAliveInterval(keepalive);
			
			try {
				mqttClient.connect(options);
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void publish(int qos, boolean retained, String topic, String pushMessage) {
		MqttMessage message = new MqttMessage();
		message.setQos(qos);
		message.setRetained(retained);
		message.setPayload(pushMessage.getBytes());
		
		MqttTopic mTopic = mqttClient.getTopic(topic);
		if(null == mTopic) {
			System.out.println(" topic not exist");
			
		}
		
		MqttDeliveryToken token;
		try {
			token = mTopic.publish(message);
			token.waitForCompletion();
		}catch (MqttPersistenceException e) {
			e.printStackTrace();
		}catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void subscribe(String topic, IMqttMessageListener messageListener) {
		try {
			mqttClient.subscribe(topic, messageListener);
		}catch(MqttException e) {
			e.printStackTrace();
		}
	}
	
}
