package org.jeesl.factory.mqtt;

import java.util.Objects;

import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.exlp.interfaces.system.property.ConfigKey;
import org.exlp.interfaces.system.property.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttOptionFactory
{
	final static Logger logger = LoggerFactory.getLogger(MqttOptionFactory.class);
	
	private final Configuration config;
	
	private boolean clean;
	private ConfigKey.ConnnectionType type;
	private String scope; public <E extends Enum<E>> MqttOptionFactory  scope(E scope) {this.scope = scope.toString(); return this;}
	
	public static MqttOptionFactory instance(Configuration config) {return new MqttOptionFactory(config);}
	public MqttOptionFactory(Configuration config)
	{
		this.config=config;
		this.clear();
	}
	
	private void clear()
	{
		clean = false;
		type = null;
	}
	
	public MqttOptionFactory clean() {this.clean = true; return this;}
	public MqttOptionFactory durable() {this.clean = false; return this;}
	
	public MqttOptionFactory authentication(ConfigKey.ConnnectionType type) {this.type = type; return this;}
	
	public MqttConnectionOptions build()
	{
		MqttConnectionOptions options = new MqttConnectionOptions();
		options.setCleanStart(clean);
		if(Objects.nonNull(type))
		{
			StringBuilder sb = new StringBuilder();
			sb.append("net.mqtt");
			sb.append(".").append(type.toString());
			if(Objects.nonNull(scope)) {sb.append(".").append(scope);}
			
			options.setUserName(config.getString(sb.toString()+".user"));
			options.setPassword(config.getString(sb.toString()+".password").getBytes());
		}
		return options;
	}
}