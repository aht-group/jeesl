package org.jeesl.factory.mqtt;

import java.util.Objects;

import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;

public class Mqtt5PublishToBroker
{
	
	private final String brokerId; public String getBrokerId() {return brokerId;}
	private final Mqtt5Publish mqtt5Publish;  public Mqtt5Publish getMqtt5Publish() {return mqtt5Publish;}
    
	public static <E extends Enum<E>> Mqtt5PublishToBroker of(E broker, Mqtt5Publish mqtt5Publish)
	{
		return new Mqtt5PublishToBroker(broker.toString(),mqtt5Publish);
	}
	
    private Mqtt5PublishToBroker(String brokerId, Mqtt5Publish mqtt5Publish)
    {
        this.brokerId = Objects.requireNonNull(brokerId, "brokerId must not be null");
        this.mqtt5Publish = Objects.requireNonNull(mqtt5Publish, "mqtt5Publish must not be null");
    }
}