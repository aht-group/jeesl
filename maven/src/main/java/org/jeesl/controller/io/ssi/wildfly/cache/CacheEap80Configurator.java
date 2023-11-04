package org.jeesl.controller.io.ssi.wildfly.cache;

import java.io.IOException;

import org.exlp.util.io.JsonUtil;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.OperationBuilder;
import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.dmr.ModelNode;
import org.jeesl.interfaces.controller.io.ssi.eap.EapCacheConfigurator;
import org.jeesl.model.json.io.ssi.core.JsonSsiCredential;
import org.jeesl.model.json.io.ssi.core.JsonSsiSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheEap80Configurator extends AbstractEapCacheConfigurator implements EapCacheConfigurator
{
	final static Logger logger = LoggerFactory.getLogger(CacheEap80Configurator.class);
	
	public enum DbType {mysql,mariadb,postgresql}
	
	private CacheEap73Configurator cache73Configurator;
	
	public static CacheEap80Configurator instance(ModelControllerClient client) {return new CacheEap80Configurator(client);}
	private CacheEap80Configurator(ModelControllerClient client)
	{
		super(client);
		cache73Configurator = CacheEap73Configurator.instance(client);
	}
	
	@Override public void addCaches(JsonSsiSystem system) throws IOException
	{
		JsonUtil.info(system);
		// More info on how this works can be found here
		// https://stackoverflow.com/questions/76865879/how-to-add-a-cache-programmatically-in-wildfly-jboss-using-the-detyped-java-api

		ModelNode cacheContainer = new ModelNode();		
		cacheContainer.get(ClientConstants.OP_ADDR).add("subsystem","infinispan");
		cacheContainer.get(ClientConstants.OP_ADDR).add("cache-container",system.getCode());
		cacheContainer.get(ClientConstants.OP).set(ClientConstants.ADD);
		cacheContainer.get("marshaller").set("JBOSS");
		
		ModelNode transport = cacheContainer.clone();
		transport.get(ClientConstants.OP_ADDR).add("transport","jgroups");		
		transport.get("lock-timeout").set(60000);
		transport.get(ClientConstants.OP).set(ClientConstants.ADD);
		
		ModelNode result = client.execute(new OperationBuilder(cacheContainer).build());
//		System.out.println(result.toString());
		
		result = client.execute(new OperationBuilder(transport).build());
//		System.out.println(result.toString());
//		
		
		for(JsonSsiCredential cache : system.getCredentials())
		{
			if(cache.getCode().equals("menu")) {this.createCacheReplicated(cacheContainer,cache.getCode());}
			else if(cache.getCode().equals("icon")) {this.createCacheLocal(cacheContainer,cache.getCode());}
			else if(cache.getCode().equals("ofx")) {this.createCacheLocal(cacheContainer,cache.getCode());}
			else if(cache.getCode().equals("report")) {this.createCacheLocal(cacheContainer,cache.getCode());}
			else if(cache.getCode().equals("aom")) {this.createCacheLocal(cacheContainer,cache.getCode());}
		}
//		logger.warn("@HH: Implement result code handling");
	}
	
	public void createCacheReplicated(ModelNode container, String cacheName) throws IOException
	{
		ModelNode replicatedCache = container.clone();
		replicatedCache.get(ClientConstants.OP_ADDR).add("replicated-cache",cacheName);		
		replicatedCache.get(ClientConstants.OP).set(ClientConstants.ADD);
		
		ModelNode replicatedCacheTransaction = replicatedCache.clone();	
		replicatedCacheTransaction.get(ClientConstants.OP_ADDR).add("component","transaction");		
		replicatedCacheTransaction.get("mode").set("BATCH");
		replicatedCacheTransaction.get(ClientConstants.OP).set(ClientConstants.ADD);
		
		ModelNode result = client.execute(new OperationBuilder(replicatedCache).build());
//		System.out.println(result.toString());

		result = client.execute(new OperationBuilder(replicatedCacheTransaction).build());
//		System.out.println(result.toString());
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
//		System.out.println(result.toString());

		result = client.execute(new OperationBuilder(replicatedCacheTransaction).build());
//		System.out.println(result.toString());
	}
}