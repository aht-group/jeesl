package org.jeesl.client.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.naming.NamingException;

import org.exlp.util.io.JsonUtil;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jeesl.JeeslBootstrap;
import org.jeesl.api.rest.rs.io.ssi.JeeslIoDockerRest;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.factory.json.io.ssi.docker.JsonDockerFactory;
import org.jeesl.factory.xml.system.io.ssi.XmlSystemFactory;
import org.jeesl.factory.xml.system.io.ssi.docker.XmlContainerFactory;
import org.jeesl.factory.xml.system.io.ssi.docker.XmlDockerFactory;
import org.jeesl.model.json.io.ssi.docker.JsonDocker;
import org.jeesl.model.json.io.ssi.docker.JsonDockerContainer;
import org.jeesl.model.xml.io.ssi.core.Docker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.exlp.exception.ExlpConfigurationException;
import net.sf.exlp.util.xml.JaxbUtil;

public class JeeslDocker
{
	final static Logger logger = LoggerFactory.getLogger(JeeslDocker.class);
	
	public JeeslDocker()
	{

	}

	private String[] xGet(String url) 
	{
		String[] stringGet = {"curl", "--unix-socket", "/var/run/docker.sock", "-X", "GET", url, "-H", "accept: application/json", "-H", "content-type: application/json"};
		return stringGet;
	}
	
	@SuppressWarnings("unused")
	private String[] xPut(String url, String postSubmission)
	{
		String[] stringPut = {"curl", "--unix-socket", "/var/run/docker.sock", "-X", "GET", url, "-H", "accept: application/json", "-H", "content-type: application/json"};
		return stringPut;
	}

	private String runProcess(String[] strings) throws IOException, InterruptedException 
	{
		ProcessBuilder ps = new ProcessBuilder(strings);
		Process pr = ps.start();
		pr.waitFor();

		BufferedReader reader2 = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String line2 = "";	
		
		while ((line2 = reader2.readLine()) != null) {sb.append(line2 + "\n");}
		
		return sb.toString();
	}
	
	public JsonDocker read() throws JsonParseException, JsonMappingException, IOException, InterruptedException
	{
		String response = this.runProcess(this.xGet("http://v1.40/containers/json?all"));
		
		ObjectMapper objectMapper = new ObjectMapper();
		JsonDocker json = JsonDockerFactory.build(objectMapper.readValue(response,new TypeReference<List<JsonDockerContainer>>(){}));
		JsonUtil.info(json);
		return json;
	}
	
	public Docker transform() throws JsonParseException, JsonMappingException, IOException, InterruptedException
	{
		JsonDocker json = read();
		Docker xml = XmlDockerFactory.build();
		xml.setSystem(XmlSystemFactory.build("jeesl"));
		xml.getContainer().addAll(XmlContainerFactory.build(json.getContainers()));
		JaxbUtil.info(xml);
		return xml;
	}
	
	public void rest() throws JsonParseException, JsonMappingException, IOException, InterruptedException
	{
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget restTarget = client.target("http://localhost:8080/erp");
		JeeslIoDockerRest rest = restTarget.proxy(JeeslIoDockerRest.class);
		rest.update(transform());
	}
	
	public static void main(String args[]) throws UtilsConfigurationException, NamingException, ExlpConfigurationException, JsonParseException, JsonMappingException, IOException, InterruptedException
	{
		JeeslBootstrap.init();
		JeeslDocker cli = new JeeslDocker();
		
//		cli.read();
//		cli.transform();
		cli.rest();
	}
}