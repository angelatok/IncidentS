package com.cms.incident.mqtt;

import java.util.Date;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class TestPubSub implements IMqttMessageListener{

	@Autowired
	GcMqttClient gcMqttClient;

	boolean subflag = false;
	

	
	@Scheduled(fixedDelay=3000, initialDelay=30000) // 1 sec after 40 sec
	public void pubtask() {
		
		if(gcMqttClient.getClient().isConnected()) {
			System.out.println(" pub t1.... " + new Date());
			gcMqttClient.publish(1, false, "t1", "hello world");

			System.out.println(" pub t2.... " + new Date());
			gcMqttClient.publish(1, false, "t2", "Good bye world");


			if(subflag == false) {

				gcMqttClient.subscribe("t1", this);
				gcMqttClient.subscribe("t2", this);
				gcMqttClient.subscribe("incident", this);

				subflag = true;
			}
		}else {
			System.out.println(" mqtt NOT CONNECTED !!!!!");
		}


	}
	
	
	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		
		System.out.println(" rx  @ " + new Date() + "  " + topic + " = "+ message  );

	}
}