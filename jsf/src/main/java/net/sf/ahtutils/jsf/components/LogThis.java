package net.sf.ahtutils.jsf.components;

import java.io.IOException;

import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;

import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.jsf.jx.util.ComponentAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FacesComponent(value="net.sf.ahtutils.jsf.components.LogThis")
@ListenerFor(systemEventClass=PostAddToViewEvent.class)
public class LogThis extends UINamingContainer
{
	static Logger logger = LoggerFactory.getLogger(LogThis.class);
	
	private static enum Attribute {value}
	
	@Override
	public void processEvent(ComponentSystemEvent event) throws AbortProcessingException
	{
		if(event instanceof PostAddToViewEvent)
		{
			logger.info("LogThis successfully loaded for " +this.getParent().getClientId() +"!");
		}
		super.processEvent(event);
	}
	
	@Override
	public void encodeBegin(FacesContext ctx) throws IOException
	{
		logger = LoggerFactory.getLogger("JSF-LOG " +this.getParent().getClientId());
		logger.info("Logging requested..");
		String value = null;
		try {
			value = ComponentAttribute.get(Attribute.value.toString(), ctx, this);
		} catch (JeeslNotFoundException e) {
			e.printStackTrace();
		}
		logger.info(value);
	}
}