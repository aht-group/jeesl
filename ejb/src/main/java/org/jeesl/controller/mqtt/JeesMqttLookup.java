package org.jeesl.controller.mqtt;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Collection;

import javax.naming.Context;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.exlp.interfaces.system.property.Configuration;
import org.exlp.util.io.JsonUtil;
import org.jeesl.factory.json.io.ssi.core.JsonSsiCredentialFactory;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiCredential;
import org.jeesl.model.json.io.ssi.core.JsonSsiCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.io.resourceloader.MultiResourceLoader;

/**
 * The MqttLookup is for MQTT v3, for v5 use MqttClientFactory
 */
@Deprecated
public class JeesMqttLookup
{
	final static Logger logger = LoggerFactory.getLogger(JeesMqttLookup.class);

	public static <MQTT extends Enum<MQTT>> MqttClient mqtt(Configuration config, MQTT mqtt, String clientId, boolean cleanSession) throws MqttException
	{
		JsonSsiCredential credential = JsonSsiCredentialFactory.build(config, mqtt);
		JsonUtil.info(credential);
		
		MemoryPersistence persistence = new MemoryPersistence();
		MqttClient client = new MqttClient(credential.getUrl(), clientId, persistence);
		 
		MqttConnectOptions options = new MqttConnectOptions();
		options.setCleanSession(cleanSession);
		options.setUserName(credential.getUser());
		options.setPassword(credential.getPassword().toCharArray());
		
		Integer port = config.getInt("net.mqtt."+mqtt.toString()+".port");
		if(port==8883)
		{
			try
			{
				InputStream certInput = MultiResourceLoader.instance().searchIs("jeesl/io/ssi/mqtt/isrg-root-x1.pem");
				CertificateFactory cf = CertificateFactory.getInstance("X.509");
				Collection<? extends Certificate> certs = cf.generateCertificates(certInput);
				KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
				ks.load(null, null);
				int index = 0;
				for (Certificate cert : certs)
				{
					ks.setCertificateEntry("server_ca_" + index++, cert);
				}
				TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
				tmf.init(ks);
				SSLContext ctx = SSLContext.getInstance("TLS");
				ctx.init(null, tmf.getTrustManagers(), null);
				SSLSocketFactory sslSocketFactory = ctx.getSocketFactory();
				options.setSocketFactory(sslSocketFactory);
			}
			catch (IOException | CertificateException | KeyStoreException | NoSuchAlgorithmException | KeyManagementException e)
			{
				e.printStackTrace();
				throw new MqttException(e);
			}
		}
         
		client.connect(options);
		
		return client;
	}
}