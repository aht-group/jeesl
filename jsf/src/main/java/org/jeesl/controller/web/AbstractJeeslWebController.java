package org.jeesl.controller.web;

import java.io.Serializable;
import java.util.Objects;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.controller.handler.io.log.DebugJeeslLogger;
import org.jeesl.interfaces.controller.handler.system.io.JeeslLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractJeeslWebController implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractJeeslWebController.class);
	
	protected JeeslFacesMessageBean bMessage;
	
	protected boolean debugOnInfo;
	protected JeeslLogger jogger;
	
	public enum SecurityActionSuffix {developer}
	public enum SecurityActionSuffixDeprecated {Developer}

	public AbstractJeeslWebController()
	{
		debugOnInfo = false;
		jogger = DebugJeeslLogger.instance(this.getClass());
	}
	
	public void debugOnInfo(boolean value)
	{
		this.debugOnInfo=value;
	}
	
	public void jogger(JeeslLogger jogger) {this.jogger=jogger;}
	public void activateDebuggingOnInfo(JeeslLogger jogger)
	{
		this.debugOnInfo=true;
		this.jogger=jogger;
	}
	
	protected void postConstructWebController(JeeslFacesMessageBean bMessage)
	{
		this.bMessage=bMessage;
		
		if(Objects.isNull(bMessage)) {logger.warn(JeeslFacesMessageBean.class.getSimpleName()+" is NULL");}
	}
}