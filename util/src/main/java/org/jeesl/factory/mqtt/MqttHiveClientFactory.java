package org.jeesl.factory.mqtt;

import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.ObjectUtils;
import org.exlp.interfaces.system.property.Configuration;
import org.jeesl.factory.json.io.ssi.core.JsonSsiCredentialFactory;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiCredential;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.model.json.io.ssi.core.JsonSsiCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hivemq.client.mqtt.datatypes.MqttClientIdentifier;
import com.hivemq.client.mqtt.lifecycle.MqttClientAutoReconnect;
import com.hivemq.client.mqtt.lifecycle.MqttClientDisconnectedListener;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5ClientBuilder;
import com.hivemq.client.mqtt.mqtt5.message.auth.Mqtt5SimpleAuth;
import com.hivemq.client.mqtt.mqtt5.message.auth.Mqtt5SimpleAuthBuilder;

public class MqttHiveClientFactory
{
	final static Logger logger = LoggerFactory.getLogger(MqttHiveClientFactory.class);
	
	private Configuration config;
	private JsonSsiCredential credential;
	
	private String brokerCode; public <E extends Enum<E>> MqttHiveClientFactory  broker(E broker) {brokerCode = broker.toString(); return this;}
	private String authCode; public <E extends Enum<E>> MqttHiveClientFactory authentication(E profile) {this.authCode = profile.toString(); return this;}
	
	private Class<?> clientClass;
	private Class<?> clientScope;
	private String clientProfile;
		
	public static MqttHiveClientFactory instance() {return new MqttHiveClientFactory();}
	private MqttHiveClientFactory()
	{
		
	}
	
	public MqttHiveClientFactory config(Configuration config) {this.config=config; return this;}
	public MqttHiveClientFactory config(JsonSsiCredential credential) {this.credential=credential; return this;}
	
	public static <CRED extends JeeslIoSsiCredential<SYSTEM>, SYSTEM extends JeeslIoSsiSystem<?,?>> MqttHiveClientFactory instance(CRED credential)
	{
		return new MqttHiveClientFactory(JsonSsiCredentialFactory.build(credential));
	}
	private MqttHiveClientFactory(JsonSsiCredential credential)
	{
		this.credential = credential;
	}
	
	public <E extends Enum<E>> MqttHiveClientFactory client(Class<?> cClient, E profile) {return this.client(null,cClient,profile);}
	public <E extends Enum<E>> MqttHiveClientFactory client(Class<?> cScope, Class<?> cClient, E profile)
	{
		if(ObjectUtils.allNull(config,credential)) {throw new IllegalStateException("You need to configure with config or credential");}
		this.clientScope=cScope;
		this.clientClass=cClient;
		this.clientProfile = profile.toString();
		
		return this;
	}
	
	private boolean clean = true;
	public MqttHiveClientFactory resumeSession(boolean value) {this.clean = !value; return this;}
	public MqttHiveClientFactory cleanSession() {this.clean = true; return this;}
	public MqttHiveClientFactory resumeSession() {this.clean = false; return this;}

	
	public InetSocketAddress toServerAddress()
	{
		InetSocketAddress isa = null;
		
		if(Objects.nonNull(config))
		{
			String host = config.getString(String.format("net.mqtt.%s.host",brokerCode));
			Integer port = config.getInteger(String.format("net.mqtt.%s.port",brokerCode),1883);
			
			logger.info("Connecting to {}:{}",host,port);
			isa = new InetSocketAddress(host,port);
		}
		else if(Objects.nonNull(credential))
		{
			isa = new InetSocketAddress(credential.getHost(),credential.getPort());
		}
		
		return isa;
	}
	
	public String toIdentifier()
	{
		StringBuilder sbIdentifier = new StringBuilder();
		
		if(Objects.nonNull(clientScope) && !clientScope.equals(clientClass)) {sbIdentifier.append(clientScope.getSimpleName()).append(":");}
		sbIdentifier.append(clientClass.getSimpleName());
		sbIdentifier.append(":").append(clientProfile);
		
		logger.info("Broker: {} with {}",brokerCode,sbIdentifier.toString());
		
		return sbIdentifier.toString();
	}
	
	public Mqtt5SimpleAuth toAuth()
	{
		Mqtt5SimpleAuthBuilder builder = Mqtt5SimpleAuth.builder();
		Mqtt5SimpleAuthBuilder.Complete complete = null;
		
		if(Objects.nonNull(config))
		{
			String userKey = String.format("net.mqtt.%s.%s.user", brokerCode, authCode);
			String passwordKey = String.format("net.mqtt.%s.%s.password", brokerCode, authCode);
			
			try
			{
				complete = builder.username(config.getString(userKey))
								.password(config.getString(passwordKey).getBytes());
			}
			catch(NullPointerException e)
			{
				logger.warn("Check your config for {} and {}",userKey,passwordKey);
				throw e;
			}
		}
		else if(Objects.nonNull(credential))
		{
			complete = builder.username(credential.getUser())
								.password(credential.getPassword().getBytes());
		}
		
		return complete.build();
	}
	
	public void ssl(Mqtt5ClientBuilder builder)
	{
		Integer port = null;
		
		
		if(Objects.nonNull(config))
		{
			port = config.getInteger(String.format("net.mqtt.%s.port",brokerCode),1883);
		}
		else if(Objects.nonNull(credential))
		{
			port = credential.getPort();
		}
		
		if(port==8883)
		{
			builder.sslWithDefaultConfig();
		}
	}
	
	public MqttClientAutoReconnect toAutoReconnect()
	{
		MqttClientAutoReconnect autoReconnect = MqttClientAutoReconnect.builder()
		        .initialDelay(1, TimeUnit.SECONDS)
		        .maxDelay(30, TimeUnit.SECONDS).build();
		return autoReconnect;
	}
	
	public MqttClientDisconnectedListener toDisconnectedListener()
	{
		MqttClientDisconnectedListener disconnectedListener = context ->
		{
			int attempt = context.getReconnector().getAttempts();

		    if(attempt==1)
		    {
		    	logger.error("Disconnected from broker. Cause: {}", context.getCause().getMessage(), context.getCause());
		    	logger.error("Source: {}", context.getSource());
		    	logger.error("Reconnector active: {}", context.getReconnector());
		    	
		    }
		    else {logger.info("Reconnect attempt #{}", attempt);}
		};
		return disconnectedListener;
	}
	
	public void toConnect(Mqtt5AsyncClient client)
	{
		String identifier = client.getConfig().getClientIdentifier().map(MqttClientIdentifier::toString).orElse("<unknown>");
		
		if(clean) {MqttHiveClientFactory.connectCleanSession(client,identifier);}
		else {MqttHiveClientFactory.connectResumeSession(client,identifier);}
	}
	
	private static void connectCleanSession(Mqtt5AsyncClient client, String identifier)
	{
        client.connectWith().cleanStart(true).sessionExpiryInterval(0)
                .keepAlive(60)
                .send()
//                .orTimeout(5, TimeUnit.SECONDS)
                .whenComplete((ack, throwable) -> {
                    if (throwable != null) {logger.error("Clean session connect failed", throwable);}
                    else {logger.info("Connected with clean session (cleanStart=true, sessionExpiry=0) as {}",identifier);}
                });
    }

    /**
     * Connects with an existing session (cleanStart=false).
     * The session stays active across reconnects.
     */
	private static void connectResumeSession(Mqtt5AsyncClient client, String identifier)
    {
        client.connectWith()
                .cleanStart(false)
                .sessionExpiryInterval(TimeUnit.DAYS.toSeconds(30))
                .keepAlive(60)
                .send()
//                .orTimeout(5, TimeUnit.SECONDS)
                .whenComplete((ack, throwable) -> {
                	 if (throwable != null) {logger.error("Clean session connect failed", throwable);}
                	 else {logger.info("Connected with clean session (cleanStart=true, sessionExpiry=0) as {}",identifier);}
                });
    }
}