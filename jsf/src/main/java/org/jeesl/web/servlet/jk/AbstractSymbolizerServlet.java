package org.jeesl.web.servlet.jk;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URLDecoder;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.exlp.util.io.StringUtil;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicComponent;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicShape;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.openfuxml.factory.xml.media.XmlImageFactory;
import org.openfuxml.model.xml.core.media.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSymbolizerServlet<G extends JeeslGraphic<GT,GC,GS>, GT extends JeeslGraphicType<?,?,GT,G>,
												GC extends JeeslGraphicComponent<G,GC,GS>, GS extends JeeslGraphicShape<?,?,GS,G>>
	extends HttpServlet
	implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractSymbolizerServlet.class);
	
	protected boolean debugOnInfo; protected void setDebugOnInfo(boolean debugOnInfo) {this.debugOnInfo=debugOnInfo;}
	
	protected AbstractSymbolizerServlet()
	{
		debugOnInfo = false;
	}
	
	protected Image getPathInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if (request.getPathInfo() == null)
		{
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

        String path = URLDecoder.decode(request.getPathInfo(), "UTF-8");
        if(path.length()<1)
        {
	        	response.sendError(HttpServletResponse.SC_NOT_FOUND);
	        	return null;
        }
        
        if(debugOnInfo){logger.info("Path " +path);}
        
        String[] pathElements = path.split("/");
        Integer size = new Integer(pathElements[1]);
        Long id = new Long(pathElements[2]);
        
        if(debugOnInfo){logger.info("Requested size " +size+" id:"+id);}
        
        return XmlImageFactory.idHeight(id,size);
	}
	
	protected Image getGraphicInfo(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		if(request.getPathInfo() == null)
		{
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

        String path = URLDecoder.decode(request.getPathInfo(), "UTF-8");
        if(path.length()<1)
        {
	        	response.sendError(HttpServletResponse.SC_NOT_FOUND);
	        	return null;
        }
        
        if(logger.isTraceEnabled()){logger.trace("Path " +path);}
        
        String[] pathElements = path.split("/");
        String className = pathElements[1];
        Integer size = new Integer(pathElements[2]);
        Long id = new Long(pathElements[3]);
        
        if(logger.isTraceEnabled())
        {
        		logger.trace(StringUtil.stars());
        		logger.trace("\tclass:"+className);
        		logger.trace("\tsize:"+size);
        		logger.trace("\tid:"+id);
        }
        Image image = XmlImageFactory.idHeight(id,size);
        image.setVersion(className);
        
        return image;
	}
	
	protected void respond(HttpServletRequest request, HttpServletResponse response,byte[] bytes, String suffix) throws IOException
    {
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		
		response.reset();
		response.setContentType(getServletContext().getMimeType("x."+suffix));
		response.setHeader("Content-Length", String.valueOf(bytes.length));
		response.setHeader("Expires", DateTimeFormatter.RFC_1123_DATE_TIME.format(OffsetDateTime.now(ZoneOffset.UTC).plus(Duration.ofMinutes(10))));
		
	  	IOUtils.copy(bais,response.getOutputStream());
	}
}