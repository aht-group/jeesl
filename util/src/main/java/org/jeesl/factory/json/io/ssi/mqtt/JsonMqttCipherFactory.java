package org.jeesl.factory.json.io.ssi.mqtt;

import java.util.ArrayList;

import org.jeesl.model.json.io.ssi.mqtt.JsonMqttCipher;
import org.jeesl.model.json.io.ssi.mqtt.JsonMqttMessage;

public class JsonMqttCipherFactory
{
	public static JsonMqttCipher build(JsonMqttMessage message)
	{
		JsonMqttCipher json = new JsonMqttCipher();
		json.setMessages(new ArrayList<>());
		json.getMessages().add(message);
		return json;
	}
}