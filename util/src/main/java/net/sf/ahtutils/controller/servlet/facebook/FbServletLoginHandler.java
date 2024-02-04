package net.sf.ahtutils.controller.servlet.facebook;

import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.ahtutils.controller.factory.xml.cloud.facebook.SignedRequestFactory;
import net.sf.ahtutils.xml.cloud.facebook.App;
import net.sf.ahtutils.xml.cloud.facebook.SignedRequest;

import org.exlp.util.jx.JaxbUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FbServletLoginHandler
{	
	final static Logger logger = LoggerFactory.getLogger(FbServletLoginHandler.class);
	
	private static Map<String,App> mapAppDef;
	private App app;
	private static final String sessionKeyPermissionRequest = "de.hekit.util.cloud.fb.util.servlet.PermissionRequest";
	
	public FbServletLoginHandler(String fbAppDefXml)
	{
		if(mapAppDef==null){mapAppDef = new Hashtable<String,App>();}
		logger.debug(".. with fbAppDefXml="+fbAppDefXml);
		try
		{
			loadXml(fbAppDefXml);
		}
		catch (FileNotFoundException e) {logger.error("",e);}
	}
	
	private void loadXml(String fbAppDefXml) throws FileNotFoundException
	{
		synchronized(mapAppDef)
		{
			if(!mapAppDef.containsKey(fbAppDefXml))
			{
				App app = (App)JaxbUtil.loadJAXB(fbAppDefXml, App.class);
				mapAppDef.put(fbAppDefXml, app);
				logger.debug("XML Loaded: "+JaxbUtil.toString(app));
			}
			app = mapAppDef.get(fbAppDefXml);
		}
	}
	
	public void login(FbLoginCallback calback, HttpServletRequest request , HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		Map<String,String[]> mapParameter = request.getParameterMap();

		StringBuffer sb = new StringBuffer();
/*		for(String key : mapParameter.keySet())
		{
			sb.append(key).append(", ");
		}
		logger.debug("Got Keys: "+sb);
*/		
		FbRedirector fbRedirector = new FbRedirector(app);	
		if(request.getParameterMap().containsKey("ref"))
		{
			String[] para = (String[])mapParameter.get("ref");
			String ref = para[0];
			logger.debug("ref: "+ref);
		}
		boolean isSetSessionKeyPermissionRequest = !(session.getAttribute(sessionKeyPermissionRequest)==null);
		
		
		if(request.getParameterMap().containsKey("signed_request"))
		{
			SignedRequestFactory srf = new SignedRequestFactory();
			String[] para = (String[])request.getParameterMap().get("signed_request");
			srf.decode(para[0]);
			
			SignedRequest sr = srf.getSignedRequest();
			sb = new StringBuffer();
			sb.append("SignedRequest.");
			sb.append(" isSetSessionKeyPermissionRequest=").append(isSetSessionKeyPermissionRequest).append(".");
			if(sr.isSetOauth())
			{
				sb.append(" With OAuth.");
				if(!isSetSessionKeyPermissionRequest){session.setAttribute(sessionKeyPermissionRequest, false);}
				calback.receivedOauth(sr,session);
			}
			else
			{
				sb.append(" No OAuth.");
				if(!isSetSessionKeyPermissionRequest){session.setAttribute(sessionKeyPermissionRequest, true);}
				
			}
			logger.debug(sb.toString());
			fbRedirector.toFbOauth(response);
		}
		else if(request.getParameterMap().containsKey("code"))
		{
			String[] para = (String[])request.getParameterMap().get("code");
			String code = para[0];
			calback.receivedAppAuthCode(app,code,session);
			
			boolean prevRequest = false;
			if(session.getAttribute(sessionKeyPermissionRequest)!=null)
			{
				prevRequest = (Boolean)session.getAttribute(sessionKeyPermissionRequest);
				session.removeAttribute(sessionKeyPermissionRequest);
			}
			
			logger.debug("Got code, forwarding APP prevRequest="+prevRequest);
			if(prevRequest)
			{
				fbRedirector.toCanvas(response);
			}
			else
			{
				fbRedirector.toApp(response);
			}
		}
		else
		{
			logger.warn("No signed_request received ... ignoring request");
			for(String key : mapParameter.keySet())
			{
				String v[] =  mapParameter.get(key);
				sb = new StringBuffer();
				for(int i=0;i<v.length;i++){sb.append(v[i]).append(", ");}
				logger.debug("key: "+key+" "+sb);
			}
		}
	}
}