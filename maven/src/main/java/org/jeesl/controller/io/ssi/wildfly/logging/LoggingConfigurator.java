package org.jeesl.controller.io.ssi.wildfly.logging;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.maven.plugin.MojoExecutionException;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.OperationBuilder;
import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.dmr.ModelNode;
import org.jeesl.interfaces.controller.io.ssi.eap.EapLoggingConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingConfigurator extends AbstractEapLoggingConfigurator implements EapLoggingConfigurator
{
	final static Logger logger = LoggerFactory.getLogger(LoggingConfigurator.class);
	
	public static LoggingConfigurator instance(ModelControllerClient client) {return new LoggingConfigurator(client);}
	private LoggingConfigurator(ModelControllerClient client)
	{
		super(client);
	}
	
	@Override public void addLogger(String name, String level, String formatter) throws IOException
	{
		// More info on how this works can be found here
		// https://stackoverflow.com/questions/76865879/how-to-add-a-cache-programmatically-in-wildfly-jboss-using-the-detyped-java-api
		
		ModelNode handlerContainer = new ModelNode();		
		handlerContainer.get(ClientConstants.OP_ADDR).add("subsystem","logging");
		handlerContainer.get(ClientConstants.OP_ADDR).add("console-handler",name);
		handlerContainer.get("level").set("INFO");
		handlerContainer.get("named-formatter").set("COLOR-PATTERN");
		handlerContainer.get(ClientConstants.OP).set(ClientConstants.ADD);
		
		ModelNode result = client.execute(new OperationBuilder(handlerContainer).build());
		logger.info("result " +result.toString());
	}
	
	public void evaluateResult(String key, ModelNode result, Map<String, List<String>> failures) throws MojoExecutionException
	{
		if (result.get("outcome").toString().equals("\"failed\""))
		{
			// Check if it is only "duplicate resource" (WFLYCTL0212)
			// https://docs.wildfly.org/19/wildscribe/log-message-reference.html
			if (result.get("failure-description").toString().contains("WFLYCTL0212"))
			{
				// Thats ok, continue
			} 
			else
			{
				throw new MojoExecutionException(result.get("failure-description").toString());
			}
		}
	}
}