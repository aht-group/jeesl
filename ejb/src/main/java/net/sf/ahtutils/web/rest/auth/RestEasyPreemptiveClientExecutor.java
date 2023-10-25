package net.sf.ahtutils.web.rest.auth;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;
import org.jboss.resteasy.client.ClientExecutor;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;
import org.jboss.resteasy.spi.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestEasyPreemptiveClientExecutor
{
	final static Logger logger = LoggerFactory.getLogger(RestEasyPreemptiveClientExecutor.class);

	private RestEasyPreemptiveClientExecutor()
	{
		
	}
	
	public static ClientExecutor factory(String username, String password) throws UnauthorizedException
	{
		RestEasyPreemptiveClientExecutor f = new RestEasyPreemptiveClientExecutor();
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
		httpClient.addRequestInterceptor(f.new PreemptiveAuthInterceptor(username,password), 0);

	    ClientExecutor clientExecutor = new ApacheHttpClient4Executor(httpClient);
	    return clientExecutor;
	}
	
	public class PreemptiveAuthInterceptor implements HttpRequestInterceptor
	{
		private String username;
		private String password;
		
		public PreemptiveAuthInterceptor(String username, String password)
		{
			this.username=username;
			this.password=password;
		}
		
		@Override
		public void process(org.apache.http.HttpRequest request, HttpContext context) throws HttpException, IOException
		{
			AuthState authState = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);

	        authState.setAuthScope(org.apache.http.auth.AuthScope.ANY);
	        authState.setCredentials(new UsernamePasswordCredentials(username,password));
	        authState.setAuthScheme(new BasicScheme());
			
		}
	}
}