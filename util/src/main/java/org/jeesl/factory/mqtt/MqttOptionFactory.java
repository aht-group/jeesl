package org.jeesl.factory.mqtt;

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
import java.util.Objects;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.apache.commons.lang3.ObjectUtils;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.exlp.interfaces.system.property.Configuration;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.factory.json.io.ssi.core.JsonSsiCredentialFactory;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiCredential;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.model.json.io.ssi.core.JsonSsiCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.io.resourceloader.MultiResourceLoader;

public class MqttOptionFactory
{
	final static Logger logger = LoggerFactory.getLogger(MqttOptionFactory.class);
	
	private Configuration config;
	private JsonSsiCredential credential;
	
	private boolean clean;
	private String type; public <E extends Enum<E>> MqttOptionFactory authentication(E type) {this.type = type.toString(); return this;}
	private String broker; public <E extends Enum<E>> MqttOptionFactory broker(E broker) {this.broker = broker.toString(); return this;}
	
	public static MqttOptionFactory instance(Configuration config) {return new MqttOptionFactory(config);}
	public MqttOptionFactory(Configuration config)
	{
		this.config=config;
		this.clear();
	}
	
	public static <CRED extends JeeslIoSsiCredential<SYSTEM>, SYSTEM extends JeeslIoSsiSystem<?,?>> MqttOptionFactory instance(CRED credential)
	{
		return new MqttOptionFactory(JsonSsiCredentialFactory.build(credential));
	}
	private MqttOptionFactory(JsonSsiCredential credential)
	{
		this.credential = credential;
	}
	
	private void clear()
	{
		clean = false;
		type = null;
	}
	
	public MqttOptionFactory clean() {this.clean = true; return this;}
	public MqttOptionFactory durable() {this.clean = false; return this;}
	
	public MqttConnectionOptions build() throws UtilsConfigurationException
	{
		if(Objects.nonNull(config)) {return this.buildByConfig();}
		else if(Objects.nonNull(credential)) {return this.buildByCredential();}
		throw new UtilsConfigurationException("No configuration available");
	}
	private MqttConnectionOptions buildByConfig()
	{
		MqttConnectionOptions options = new MqttConnectionOptions();
		options.setCleanStart(clean);
		if(Objects.nonNull(type))
		{
			StringBuilder sb = new StringBuilder();
			sb.append("net.mqtt");
			if(Objects.nonNull(broker)) {sb.append(".").append(broker);}
			if(Objects.nonNull(type)) {sb.append(".").append(type.toString());}
			
			logger.info("Configuring with base {}",sb.toString());
			
			options.setUserName(config.getString(sb.toString()+".user"));
			options.setPassword(config.getString(sb.toString()+".password").getBytes());
			
			if(ObjectUtils.allNotNull(broker,type))
			{
				Integer port = config.getInt(String.format("net.mqtt.%s.%s.port", broker, type));
				if(port==8883){applySsl(options);}
			}
			
		}
		return options;
	}
	private MqttConnectionOptions buildByCredential()
	{
		MqttConnectionOptions options = new MqttConnectionOptions();
		options.setCleanStart(clean);

		options.setUserName(credential.getUser());
		options.setPassword(credential.getPassword().getBytes());
		if(credential.getPort()==8883){applySsl(options);}

		return options;
	}
	
	private void applySsl(MqttConnectionOptions options)
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
			
		}
	}
}