package net.sf.ahtutils.web.mbean.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.jeesl.api.facade.system.JeeslSystemPropertyFacade;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.bean.JiraBean;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.property.JeeslProperty;
import org.jeesl.model.json.ssi.jira.Issue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractJiraBean <L extends JeeslLang,D extends JeeslDescription,
								C extends JeeslStatus<L,D,C>,
								P extends JeeslProperty<L,D,C,P>> 
				implements Serializable,JiraBean
{
	final static Logger logger = LoggerFactory.getLogger(AbstractJiraBean.class);
	private static final long serialVersionUID = 1L;
	
	protected String jiraHost;
	protected String jiraScriptPath;
	
	protected final Map<String,String> collectorId; @Override public Map<String, String> getCollectorId() {return collectorId;}
	protected final Map<String,Issue> mapIssue; public Map<String,Issue> getMapIssue() {return mapIssue;}

	public AbstractJiraBean()
	{
		collectorId = new Hashtable<String,String>();
		mapIssue = new HashMap<String,Issue>();
	}
	
    public void init(JeeslSystemPropertyFacade<L,D,C,P> fUtils, Class<P> cl, String[] collectorKeys) throws JeeslNotFoundException
    {
    	jiraHost = fUtils.valueStringForKey(JiraBean.Code.jiraHost.toString(), null);
    	jiraScriptPath = fUtils.valueStringForKey(JiraBean.Code.jiraScriptPath.toString(), null);
    	
    	for(String key : collectorKeys)
    	{
    		collectorId.put(key, fUtils.valueStringForKey(JiraBean.Code.jiraCollector.toString()+key, null));
    	}
    }

	@Override public String getJiraHost() {return jiraHost;}
	@Override public String getJiraScriptPath() {return jiraScriptPath;}
}