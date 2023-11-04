package org.jeesl.util.rest;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.exlp.util.io.JsonUtil;
import org.jeesl.model.json.io.ssi.core.JsonSsiCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslRestClient
{
	final static Logger logger = LoggerFactory.getLogger(JeeslRestClient.class);
	
	private HttpClient client;
	private HttpClientContext context;
	
	public JeeslRestClient(JsonSsiCredential credentials)
	{
		this(credentials,10*60*1000);
	}
	public JeeslRestClient(JsonSsiCredential credentials, int timeout)
	{	
		context = HttpClientContext.create();
		
		if(credentials!=null)
		{
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			credsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(credentials.getUser(), credentials.getPassword()));

			HttpHost targetHost = new HttpHost(credentials.getHost(), 443, "https");
			AuthCache authCache = new BasicAuthCache();
			authCache.put(targetHost, new BasicScheme());
			
			context.setCredentialsProvider(credsProvider);
			context.setAuthCache(authCache);
		}
		
		RequestConfig config = RequestConfig.custom()
		  .setConnectTimeout(timeout)
		  .setConnectionRequestTimeout(timeout)
		  .setSocketTimeout(timeout).build();
		
		client = HttpClientBuilder.create()
					.setDefaultRequestConfig(config)
					.build();

	}
	
	public String plain(String url) throws ClientProtocolException, IOException
	{
		HttpGet httpGet = new HttpGet(url);
		logger.info(url);
		HttpResponse httpRespnse = client.execute(httpGet,context);
		return EntityUtils.toString(httpRespnse.getEntity(),"UTF-8");
	}
	
	public <T extends Object> T json(Class<T> c, String url) throws ClientProtocolException, IOException
	{
		HttpGet httpGet = new HttpGet(url);
//		logger.info(url);
		HttpResponse httpRespnse = client.execute(httpGet,context);
//		logger.info("Result: "+EntityUtils.toString(httpRespnse.getEntity()));
		return JsonUtil.read(c,EntityUtils.toString(httpRespnse.getEntity(),"UTF-8"));
	}
	
	private <T extends Object> T json(int counter, Class<T> c, String url) throws ClientProtocolException, IOException
	{
		HttpGet httpGet = new HttpGet(url);
		
		logger.info("Excecuting");
		HttpResponse httpRespnse = client.execute(httpGet,context);
		logger.info("Status Code "+httpRespnse.getStatusLine().getStatusCode()+" Attemp:"+counter);
		if(httpRespnse.getStatusLine().getStatusCode()==502)
		{
			if(counter<=5)
			{
				try {Thread.sleep(1000*3);} catch (InterruptedException e) {e.printStackTrace();}
				logger.info("Slept, now trying again");
				return json(counter+1,c,url);
			}
			else
			{
				logger.info("Exception");
				throw new ClientProtocolException("Getting a HTTP 502 (Even after "+counter+" attempt");
			}
		}
		else
		{
			return JsonUtil.read(c,EntityUtils.toString(httpRespnse.getEntity(),"UTF-8"));
		}		
	}
}