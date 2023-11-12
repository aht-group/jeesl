package org.jeesl.controller.io.ssi.wildfly.cache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.plugin.MojoExecutionException;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.OperationBuilder;
import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.dmr.ModelNode;
import org.jeesl.interfaces.controller.io.ssi.eap.EapCacheConfigurator;
import org.jeesl.model.json.io.ssi.core.JsonSsiCredential;
import org.jeesl.model.json.io.ssi.core.JsonSsiSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheEap73Configurator extends AbstractEapCacheConfigurator implements EapCacheConfigurator
{
	final static Logger logger = LoggerFactory.getLogger(CacheEap73Configurator.class);
	
	public enum DbType {mysql,mariadb,postgresql}
	
	public static CacheEap73Configurator instance(ModelControllerClient client) {return new CacheEap73Configurator(client);}
	private CacheEap73Configurator(ModelControllerClient client)
	{
		super(client);
	}
	
	@Override public void addCaches(JsonSsiSystem system) throws IOException, MojoExecutionException
	{
		// More info on how this works can be found here
		// https://stackoverflow.com/questions/76865879/how-to-add-a-cache-programmatically-in-wildfly-jboss-using-the-detyped-java-api
		
		ModelNode cacheContainer = new ModelNode();		
		cacheContainer.get(ClientConstants.OP_ADDR).add("subsystem","infinispan");
		cacheContainer.get(ClientConstants.OP_ADDR).add("cache-container",system.getCode());
		cacheContainer.get(ClientConstants.OP).set(ClientConstants.ADD);
		
		ModelNode transport = cacheContainer.clone();
		transport.get(ClientConstants.OP_ADDR).add("transport","jgroups");		
		transport.get("lock-timeout").set(60000);
		transport.get(ClientConstants.OP).set(ClientConstants.ADD);
		
		ModelNode result = client.execute(new OperationBuilder(cacheContainer).build());
		evaluateResult(system.getCode(), result);
		
		result = client.execute(new OperationBuilder(transport).build());
		evaluateResult(system.getCode(), result);
		
		List<String> cacheNames = new ArrayList<>();
		for(JsonSsiCredential cache : system.getCredentials())
		{
			if(cache.getCode().equals("menu")) {this.createCacheReplicated(cacheContainer,cache.getCode()); cacheNames.add(cache.getCode());}
			else if(cache.getCode().equals("icon")) {this.createCacheLocal(cacheContainer,cache.getCode()); cacheNames.add(cache.getCode());}
			else if(cache.getCode().equals("ofx")) {this.createCacheLocal(cacheContainer,cache.getCode()); cacheNames.add(cache.getCode());}
			else if(cache.getCode().equals("report")) {this.createCacheLocal(cacheContainer,cache.getCode()); cacheNames.add(cache.getCode());}
			else if(cache.getCode().equals("aom")) {this.createCacheLocal(cacheContainer,cache.getCode()); cacheNames.add(cache.getCode());}
			else if(cache.getCode().equals("ts")) {this.createCacheLocal(cacheContainer,cache.getCode()); cacheNames.add(cache.getCode());}
		}
		
//		logger.warn("@HH: Implement result code handling");
		logger.info("Added caches for " +system.getCode() +": " +cacheNames.toString());
	}
	
	public void evaluateResult(String key, ModelNode result) throws MojoExecutionException
	{
		if (result.get("outcome").toString().equals("\"failed\""))
		{
			// Check if it is only "duplicate resource" (WFLYCTL0212)
			// https://docs.wildfly.org/19/wildscribe/log-message-reference.html
			if (result.get("failure-description").toString().contains("WFLYCTL0212"))
			{
				// Thats ok, continue
				// More codes can be added here as elseif clauses
			} 
			else
			{
				throw new MojoExecutionException(result.get("failure-description").toString());
			}
		}
	}

	public void createCacheReplicated(ModelNode container, String cacheName) throws IOException, MojoExecutionException
	{
		ModelNode replicatedCache = container.clone();
		replicatedCache.get(ClientConstants.OP_ADDR).add("replicated-cache",cacheName);		
		replicatedCache.get(ClientConstants.OP).set(ClientConstants.ADD);
		
		ModelNode replicatedCacheTransaction = replicatedCache.clone();	
		replicatedCacheTransaction.get(ClientConstants.OP_ADDR).add("component","transaction");		
		replicatedCacheTransaction.get("mode").set("BATCH");
		replicatedCacheTransaction.get(ClientConstants.OP).set(ClientConstants.ADD);
		
		ModelNode result = client.execute(new OperationBuilder(replicatedCache).build());
		evaluateResult(cacheName, result);

		result = client.execute(new OperationBuilder(replicatedCacheTransaction).build());
		evaluateResult(cacheName, result);
	}
	
	public void createCacheLocal(ModelNode container, String cacheName) throws IOException
	{
		ModelNode replicatedCache = container.clone();
		replicatedCache.get(ClientConstants.OP_ADDR).add("local-cache",cacheName);		
		replicatedCache.get(ClientConstants.OP).set(ClientConstants.ADD);
		
		ModelNode replicatedCacheTransaction = replicatedCache.clone();	
		replicatedCacheTransaction.get(ClientConstants.OP_ADDR).add("component","transaction");		
		replicatedCacheTransaction.get("mode").set("BATCH");
		replicatedCacheTransaction.get(ClientConstants.OP).set(ClientConstants.ADD);
		
		ModelNode result = client.execute(new OperationBuilder(replicatedCache).build());

		result = client.execute(new OperationBuilder(replicatedCacheTransaction).build());
		System.out.println(result.get("outcome").toString());
	}
}