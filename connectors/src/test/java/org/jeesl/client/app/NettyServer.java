package org.jeesl.client.app;

import org.jboss.resteasy.core.ResteasyDeploymentImpl;
import org.jboss.resteasy.plugins.server.netty.NettyJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.jeesl.connectors.tools.ServerTrayIcon;
import org.jeesl.connectors.tools.WeapRequestService;

public class NettyServer 
{
	public NettyServer() throws Exception
	{
//		ResteasyDeployment deployment = new ResteasyDeployment();
	    ResteasyDeployment deployment = new ResteasyDeploymentImpl();
	    
	    deployment.getActualResourceClasses().add(WeapRequestService.class);

	    NettyJaxrsServer netty = new NettyJaxrsServer();
	    netty.setDeployment(deployment);
	    netty.setPort(8090);
	    netty.setRootResourcePath("");
	    netty.setSecurityDomain(null);
	    netty.start();
	}
	
	public static void main(String args[]) throws Exception
	{		
	    JeeslRengineBootstrap.init();
	    ServerTrayIcon.renderTrayControl();
	    new NettyServer();
	}
}