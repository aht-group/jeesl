package net.sf.ahtutils.jsf.listener;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.spi.BeanManager;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpSession;

import net.sf.ahtutils.model.event.GlobalCdiEvent;

import org.jeesl.jsf.jx.util.FacesContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobalCdiEventPhaseListener  implements PhaseListener
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(GlobalCdiEventPhaseListener.class);
	
	@Override
	public void beforePhase(final PhaseEvent event)
	{
//		logger.info("beforePhase");
		
	    final FacesContext facesContext = event.getFacesContext();
	    final HttpSession httpSession = FacesContextUtil.getHttpSession(facesContext);

	    if (httpSession != null)
	    {
	    	final List<GlobalCdiEvent> globalEvents = getGlobalEvents(httpSession);
//	    	logger.info("Got Global Events: "+globalEvents.size());
	    	if (!globalEvents.isEmpty()) {fireEvents(globalEvents);}
	    }
	    else {logger.warn("httpSession==null");}
	}
	
	@Override public void afterPhase(PhaseEvent event){}

	@Override
	public PhaseId getPhaseId()
	{
		return PhaseId.RENDER_RESPONSE; // RESTORE_VIEW;
	}
	
	private void fireEvents(final List<GlobalCdiEvent> globalEvents)
	{
//		logger.info("fireEvents "+globalEvents.size());
	    final BeanManager beanManager = FacesContextUtil.lookBeanManager();
	    if (beanManager!=null)
	    {
	    	try
	    	{
	    		for (final GlobalCdiEvent devaGlobalEvent : globalEvents)
	    		{
	    			beanManager.fireEvent(devaGlobalEvent);
//	    			logger.info("Event fired into BeanManager "+ devaGlobalEvent.getClass().getName());
	    		}
	    	}
	    	catch (final Exception e)
	    	{
	    		e.printStackTrace();
	    		throw new IllegalStateException("fireEvents", e);
	    	}
	    }
	    else {logger.warn("beanManager==null");}
	}

	@SuppressWarnings("unchecked")
	private synchronized List<GlobalCdiEvent> getGlobalEvents(final HttpSession httpSession)
	{
		final List<GlobalCdiEvent> events = (List<GlobalCdiEvent>) httpSession.getAttribute(GlobalCdiEvent.httpSessionAttributeName);
	    final List<GlobalCdiEvent> result = new ArrayList<GlobalCdiEvent>();

	    if (events != null)
	    {
	      result.addAll(events);
	      events.clear();
	    }

	    return result;
	}
}