package com.cms.incident.mqtt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties("mqtt")
@Getter
@Setter
public class GcMqttConfig {
	
//	@Autowired
//	private GcMqttClient gcMqttClient;
	
	private String username;
	private String password;
	private String hostUrl;
	private String clientID;
	private String defaultTopic;
	private int timeout;
	private int keepalive;
	
	
//	@Bean 
//	public GcMqttClient getMqttPushCient() {
//		System.out.println(" hostUrl : " + hostUrl);
//		System.out.println(" username: " + username);
//		System.out.println(" password: " + password);
//		System.out.println(" clientId: " + clientID);
//		System.out.println(" timeout: " + timeout);
//		System.out.println(" keepalive: " + keepalive);
//		
//		gcMqttClient.connect(hostUrl, clientID, username, password, timeout, keepalive);
//		return gcMqttClient;
//	}
}
