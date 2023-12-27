package org.jeesl.factory.xml.system.io.sync;

import org.jeesl.model.xml.io.ssi.sync.Sync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlSyncFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlSyncFactory.class);
	
	public static Sync build(int clientId, int serverId){return build(new Long(clientId),new Long(serverId));}
	public static Sync build(long clientId, long serverId)
	{
		Sync xml = new Sync();
		xml.setClientId(clientId);
		xml.setServerId(serverId);
		return xml;
	}
}