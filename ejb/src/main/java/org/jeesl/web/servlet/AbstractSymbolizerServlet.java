package org.jeesl.web.servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicFigure;
import org.openfuxml.content.media.Image;
import org.openfuxml.factory.xml.media.XmlImageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;
import net.sf.exlp.util.io.StringUtil;

public abstract class AbstractSymbolizerServlet<L extends UtilsLang, D extends UtilsDescription,
												G extends JeeslGraphic<L,D,GT,F,FS>, GT extends UtilsStatus<GT,L,D>,
												F extends JeeslGraphicFigure<L,D,G,GT,F,FS>, FS extends UtilsStatus<FS,L,D>>
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
		
	  	IOUtils.copy(bais,response.getOutputStream());
	}
}