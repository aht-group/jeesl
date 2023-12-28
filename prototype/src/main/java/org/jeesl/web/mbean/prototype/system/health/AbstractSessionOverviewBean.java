package org.jeesl.web.mbean.prototype.system.health;

import java.io.Serializable;
import java.util.List;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.factory.builder.io.IoLocaleFactoryBuilder;
import org.jeesl.interfaces.bean.system.JeeslSessionRegistryBean;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.metachart.model.xml.chart.Chart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSessionOverviewBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
													USER extends JeeslUser<?>>
					extends AbstractAdminBean<L,D,LOC>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractSessionOverviewBean.class);
	
	@SuppressWarnings("unused")
	private JeeslSessionRegistryBean<USER> bSession;
	
	private List<USER> users; public List<USER> getUsers() {return users;}

	private USER user; public USER getUser() {return user;} public void setUser(USER user) {this.user = user;}
	protected Chart chart; public Chart getChart() {return chart;}
	
	public AbstractSessionOverviewBean(IoLocaleFactoryBuilder<L,D,LOC> fbStatus)
	{
		super(fbStatus.getClassL(),fbStatus.getClassD());
	}
	
	protected void postConstructOverview(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage, JeeslSessionRegistryBean<USER> bSession)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		users = bSession.getUsers();
	}
	
}