package org.jeesl.factory.mqtt;

import java.util.Objects;

import org.eclipse.paho.mqttv5.client.MqttAsyncClient;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.exlp.interfaces.system.property.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttClientFactory
{
	final static Logger logger = LoggerFactory.getLogger(MqttClientFactory.class);
	
	private final Configuration config;
	
	private String brokerCode; public <E extends Enum<E>> MqttClientFactory  broker(E code) {brokerCode = code.toString(); return this;}
	
	private Class<?> clientClass;
	private String clientProfile;
	
	private MqttCallback callback; public <E extends Enum<E>> MqttClientFactory callback(MqttCallback callback) {this.callback=callback; return this;}
	
	public static MqttClientFactory instance(Configuration config) {return new MqttClientFactory(config);}
	private MqttClientFactory(Configuration config)
	{
		this.config=config;
	}
	
	public <E extends Enum<E>> MqttClientFactory client(Class<?> c, E profile)
	{
		this.clientClass=c;
		this.clientProfile = profile.toString();
		return this;
	}

	public MqttAsyncClient build() throws MqttException
	{
		StringBuilder sbBroker = new StringBuilder();
		sbBroker.append("tcp://");
		sbBroker.append(config.getString(String.format("net.mqtt.%s.host", brokerCode)));
		sbBroker.append(":").append(config.getString(String.format("net.mqtt.%s.port", brokerCode)));

		StringBuilder sbIdentifier = new StringBuilder();
		sbIdentifier.append(clientClass.getSimpleName());
		sbIdentifier.append(":").append(clientProfile);
		
		logger.info("Broker: {} with {}",sbBroker.toString(),sbIdentifier.toString());
		
		MemoryPersistence persistence = new MemoryPersistence();
		MqttAsyncClient client = new MqttAsyncClient(sbBroker.toString(),sbIdentifier.toString(),persistence);
		
		if(Objects.nonNull(callback)) {client.setCallback(callback);}
		return client;
	}
}