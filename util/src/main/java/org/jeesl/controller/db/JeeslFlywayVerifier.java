package org.jeesl.controller.db;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URLDecoder;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ResourceList;
import io.github.classgraph.ScanResult;

public class JeeslFlywayVerifier
{
	final static Logger logger = LoggerFactory.getLogger(JeeslFlywayVerifier.class);
	
	private static final String jeeslPath = "jeesl/system/io/db/migration";
	private static final String versionSeparator = "-";
	private final String pathFlyway;
	
	private MessageDigest sha256;
	
	public JeeslFlywayVerifier(String pathFlyway)
	{
		this.pathFlyway=pathFlyway;
		try {sha256 = MessageDigest.getInstance("SHA-256");}
		catch (NoSuchAlgorithmException e) {throw new RuntimeException(e);}
	}
	
	public void test(int i, String pathLibrary)
	{
		List<URI> listFlyway = JeeslFlywayVerifier.sqlScripts(pathFlyway);
		List<URI> listLib = JeeslFlywayVerifier.sqlScriptsSince(pathLibrary,i);
		logger.info("Checking "+listLib.size()+" library migrations with "+listFlyway.size()+" flyway migrations");
		
		
		try
		{
			Map<String,URI> mapFly = this.toMapHashUri(listFlyway);
			Map<URI,String> mapLib = this.toMapUriHash(listLib);
			
			for(URI uri : listLib)
			{
				String hash = mapLib.get(uri);
				if(!mapFly.containsKey(hash))
				{
					logger.warn("Not applied: "+toLibName(uri));
				}
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	private static List<URI> sqlScriptsSince(String path, int i)
	{
		Map<Integer,URI> map = new HashMap<>();
		try (ScanResult result = new ClassGraph().acceptPaths(path).scan())
		{
		    ResourceList resources = null;
		    resources = result.getResourcesWithExtension("sql");
//		    resources = result.getResourcesWithPath("jeesl");
		    List<URI> uris = resources.getURIs();
		    for(URI url : uris)
		    {
		    	String name = URLDecoder.decode(url.getRawPath(), "UTF-8");
		    	int lastIndex = name.lastIndexOf(File.separator);
		    	name = name.substring(lastIndex+1,name.length()-1);
		    	int versionIndex = name.indexOf(versionSeparator);
		    	String sVersion = name.substring(0,versionIndex).trim();
		    	if(logger.isTraceEnabled())
		    	{
		    		logger.info(url.toString());
		    		logger.info(url.getScheme());
		    		logger.info(url.getSchemeSpecificPart());
		    		logger.info("Last ("+File.separator+"): "+lastIndex);
			    	logger.info("Version ("+versionSeparator+"): "+versionIndex);
			    	logger.info("Version: "+sVersion);
		    	}
		    	
		    	Integer iVersion = Integer.valueOf(sVersion);
		    	if(iVersion>=i) {map.put(iVersion,url);}
		    }
		}
		catch (UnsupportedEncodingException e) {throw new RuntimeException(e);}
		
		List<Integer> versions = new ArrayList<>(map.keySet());
		Collections.sort(versions);
		
		List<URI> list = new ArrayList<>();
		for(Integer v : versions) {list.add(map.get(v));}
		
		return list;
	}
	public static List<URI> sqlScripts(String path)
	{
		try (ScanResult result = new ClassGraph().acceptPaths(path).scan())
		{
		    ResourceList resources = result.getResourcesWithExtension("sql");
		    return resources.getURIs();
		}
	}
	
	private Map<URI,String> toMapUriHash(List<URI> list) throws MalformedURLException, IOException
	{
		Map<URI,String> map = new HashMap<>();
		for(URI uri : list){map.put(uri,toHash(uri));}
		return map;
	}
	private Map<String,URI> toMapHashUri(List<URI> list) throws MalformedURLException, IOException
	{
		Map<String,URI> map = new HashMap<>();
		for(URI uri : list){map.put(toHash(uri),uri);}
		return map;
	}
	
	private String toHash(URI uri) throws MalformedURLException, IOException
	{
		if(uri.getScheme().equals("file"))
		{
			InputStream is = uri.toURL().openStream();
			DigestInputStream dis = new DigestInputStream(is, sha256);
			byte[] buffer = new byte[1024 * 8]; 
	        while(dis.read(buffer) != -1)
	        {
	            ;
	        }
	        dis.close();
	        byte[] raw = sha256.digest();
	        
			String hash = Base64.encodeBase64String(raw);
			return hash;
		}
		else
		{
			logger.warn("NYI");
			throw new RuntimeException();
		}
	}
	
	private String toLibName(URI uri)
	{
		String ssp = uri.getSchemeSpecificPart();
		int index = ssp.indexOf(jeeslPath);
		String name = ssp.substring(index+jeeslPath.length()+1,ssp.length());
		return name;
	}
}