package net.sf.ahtutils.jsf.components.layout;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.FacesComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;

import org.jeesl.jsf.components.layout.grid.AbstractGrid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FacesComponent("net.sf.ahtutils.jsf.components.layout.Grid")
@ListenerFor(systemEventClass=PostAddToViewEvent.class)
public class Grid extends AbstractGrid
{	
	//j:grid
	final static Logger logger = LoggerFactory.getLogger(Grid.class);
	private static enum Properties {width,gutter,styleClass}
	
	 @Override
	 public void processEvent(ComponentSystemEvent event) throws AbortProcessingException
	 {
//		 logger.info("processEvent "+event.getClass().getName());
		 if(event instanceof PostAddToViewEvent)
		 {
			Map<String,Object> map = getAttributes();	
			if(map.containsKey(Properties.width.toString())) {slot = new Integer(map.get(Properties.width.toString()).toString());}
			if(map.containsKey(Properties.gutter.toString())){gutter = new Integer(map.get(Properties.gutter.toString()).toString());}
			super.pushCssToHead();
//			super.pushCssToHead("auGrid.css");
		 }
		super.processEvent(event);
	}
	
	@Override
	public void encodeBegin(FacesContext context) throws IOException
	{
		Map<String,Object> map = getAttributes();
		
		ResponseWriter responseWriter = context.getResponseWriter();
		super.writeGridBegin(context, responseWriter, map);
	}

	@Override
	public void encodeEnd(FacesContext context) throws IOException
	{
		ResponseWriter responseWriter = context.getResponseWriter();
		super.writeGridEnd(responseWriter);
	}
}
