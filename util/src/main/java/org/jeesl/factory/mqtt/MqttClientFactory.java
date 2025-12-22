package org.jeesl.factory.mqtt;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.eclipse.paho.mqttv5.client.MqttAsyncClient;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.exlp.interfaces.system.property.Configuration;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.factory.json.io.ssi.core.JsonSsiCredentialFactory;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiCredential;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.model.json.io.ssi.core.JsonSsiCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttClientFactory
{
	final static Logger logger = LoggerFactory.getLogger(MqttClientFactory.class);
	
	private Configuration config;
	private JsonSsiCredential credential;
	
	private String brokerCode; public <E extends Enum<E>> MqttClientFactory  broker(E code) {brokerCode = code.toString(); return this;}

	private Class<?> clientClass;
	private String clientProfile;
	
	private MqttCallback callback; public <E extends Enum<E>> MqttClientFactory callback(MqttCallback callback) {this.callback=callback; return this;}
	
	public static MqttClientFactory instance(Configuration config) {return new MqttClientFactory(config);}
	private MqttClientFactory(Configuration config)
	{
		this.config=config;
	}
	
	
	public static <CRED extends JeeslIoSsiCredential<SYSTEM>, SYSTEM extends JeeslIoSsiSystem<?,?>> MqttClientFactory instance(CRED credential)
	{
		return new MqttClientFactory(JsonSsiCredentialFactory.build(credential));
	}
	private MqttClientFactory(JsonSsiCredential credential)
	{
		this.credential = credential;
	}

	
	public <E extends Enum<E>> MqttClientFactory client(Class<?> c, E profile)
	{
		if(Objects.isNull(config)) {throw new IllegalStateException("You need to use config!");}
		if(Objects.nonNull(credential)) {throw new IllegalStateException("You need to use config, not credential!");}
		this.clientClass=c;
		this.clientProfile = profile.toString();
		return this;
	}
	public <E extends Enum<E>> MqttClientFactory client(Class<?> c)
	{
		if(Objects.nonNull(config)) {throw new IllegalStateException("You need to use credential!");}
		if(Objects.isNull(credential)) {throw new IllegalStateException("You need to use credential, not config!");}
		this.clientClass=c;
		if(ObjectUtils.isEmpty(credential.getToken())) {throw new IllegalStateException("Credential needs a tolen!!");}
		this.clientProfile = credential.getToken();
		return this;
	}

	public MqttAsyncClient build() throws UtilsConfigurationException, MqttException
	{
		if(Objects.nonNull(config)) {return this.buildByConfig();}
		else if(Objects.nonNull(credential)) {return this.buildByCredential();}
		throw new UtilsConfigurationException("No configuration available");
	}
	
	private MqttAsyncClient buildByConfig() throws MqttException
	{
		Integer port = config.getInt(String.format("net.mqtt.%s.%s.port", brokerCode, clientProfile));
		
		StringBuilder sbBroker = new StringBuilder();
		if(port==1883) {sbBroker.append("tcp://");}
		else if(port==8883) {sbBroker.append("ssl://");}
		sbBroker.append(config.getString(String.format("net.mqtt.%s.%s.host", brokerCode, clientProfile)));
		sbBroker.append(":").append(port);

		StringBuilder sbIdentifier = new StringBuilder();
		sbIdentifier.append(clientClass.getSimpleName());
		sbIdentifier.append(":").append(clientProfile);
		
		logger.info("Broker: {} with {}",sbBroker.toString(),sbIdentifier.toString());
		
		MemoryPersistence persistence = new MemoryPersistence();
		MqttAsyncClient client = new MqttAsyncClient(sbBroker.toString(),sbIdentifier.toString(),persistence);
		
		if(Objects.nonNull(callback)) {client.setCallback(callback);}
		return client;
	}
	private MqttAsyncClient buildByCredential() throws MqttException
	{
		Integer port = credential.getPort();
		
		StringBuilder sbBroker = new StringBuilder();
		if(port==1883) {sbBroker.append("tcp://");}
		else if(port==8883) {sbBroker.append("ssl://");}
		sbBroker.append(credential.getHost());
		sbBroker.append(":").append(port);

		StringBuilder sbIdentifier = new StringBuilder();
		sbIdentifier.append(clientClass.getSimpleName());
		sbIdentifier.append(":").append(credential.getToken());
		
		logger.info("Broker: {} with {}",sbBroker.toString(),sbIdentifier.toString());
		
		MemoryPersistence persistence = new MemoryPersistence();
		MqttAsyncClient client = new MqttAsyncClient(sbBroker.toString(),sbIdentifier.toString(),persistence);
		
		if(Objects.nonNull(callback)) {client.setCallback(callback);}
		return client;
	}
}