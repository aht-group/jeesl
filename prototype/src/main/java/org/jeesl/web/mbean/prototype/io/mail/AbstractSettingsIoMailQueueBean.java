package org.jeesl.web.mbean.prototype.io.mail;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoMailFacade;
import org.jeesl.api.handler.sb.SbDateIntervalSelection;
import org.jeesl.controller.handler.sb.SbDateIntervalHandler;
import org.jeesl.controller.handler.tuple.JsonTuple1Handler;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoMailFactoryBuilder;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.mail.core.JeeslIoMail;
import org.jeesl.interfaces.model.io.mail.core.JeeslMailRetention;
import org.jeesl.interfaces.model.io.mail.core.JeeslMailStatus;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;
import net.sf.exlp.util.DateUtil;

public class AbstractSettingsIoMailQueueBean <L extends JeeslLang,D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											CATEGORY extends JeeslStatus<L,D,CATEGORY>,
											MAIL extends JeeslIoMail<L,D,CATEGORY,STATUS,RETENTION,FRC>,
											STATUS extends JeeslMailStatus<L,D,STATUS,?>,
											RETENTION extends JeeslMailRetention<L,D,RETENTION,?>,
											FRC extends JeeslFileContainer<?,?>>
					extends AbstractAdminBean<L,D,LOC>
					implements Serializable,SbToggleBean,SbDateIntervalSelection
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractSettingsIoMailQueueBean.class);
	
	private enum Statistic{today,day30}
	
	protected JeeslIoMailFacade<L,D,CATEGORY,MAIL,STATUS,RETENTION,FRC> fMail;
	private final IoMailFactoryBuilder<L,D,CATEGORY,MAIL,STATUS,RETENTION,FRC> fbMail;
	
	private Class<MAIL> cMail;
	private Class<STATUS> cStatus;

	private List<CATEGORY> categories; public List<CATEGORY> getCategories() {return categories;}
	private List<MAIL> mails; public List<MAIL> getMails() {return mails;}
	private final List<String> statistics; public List<String> getStatistics() {return statistics;}
	private MAIL mail; public MAIL getMail() {return mail;} public void setMail(MAIL mail) {this.mail = mail;}
	
	protected final SbMultiHandler<CATEGORY> sbhCategory; public SbMultiHandler<CATEGORY> getSbhCategory() {return sbhCategory;}
	protected final SbMultiHandler<STATUS> sbhStatus; public SbMultiHandler<STATUS> getSbhStatus() {return sbhStatus;}
	protected final SbMultiHandler<RETENTION> sbhRetention; public SbMultiHandler<RETENTION> getSbhRetention() {return sbhRetention;}
	private final SbDateIntervalHandler sbhDate; public SbDateIntervalHandler getSbhDate() {return sbhDate;}
	
	private final Map<String,JsonTuple1Handler<STATUS>> mapTh; public Map<String, JsonTuple1Handler<STATUS>> getMapTh() {return mapTh;}
	private final JsonTuple1Handler<STATUS> thToday,thDay30;
	
	public AbstractSettingsIoMailQueueBean(IoMailFactoryBuilder<L,D,CATEGORY,MAIL,STATUS,RETENTION,FRC> fbMail)
	{
		super(fbMail.getClassL(),fbMail.getClassD());
		this.fbMail=fbMail;
		sbhDate = new SbDateIntervalHandler(this);
		sbhDate.setEnforceStartOfDay(true);
		sbhDate.initWeeksToNow(2);
		
		sbhCategory = new SbMultiHandler<CATEGORY>(fbMail.getClassCategory(),this);
		sbhStatus = new SbMultiHandler<STATUS>(fbMail.getClassStatus(),this);
		sbhRetention = new SbMultiHandler<RETENTION>(fbMail.getClassRetention(),this);
		
		mapTh = new HashMap<>();
		thToday = new JsonTuple1Handler<>(fbMail.getClassStatus());
		thDay30 = new JsonTuple1Handler<>(fbMail.getClassStatus());
		
		statistics = new ArrayList<>();
		statistics.add(Statistic.today.toString());
		statistics.add(Statistic.day30.toString());
	}
	
	protected void postConstructMailQueue(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage, JeeslIoMailFacade<L,D,CATEGORY,MAIL,STATUS,RETENTION,FRC> fMail, final Class<L> cLang, final Class<D> cDescription, Class<MAIL> cMail, Class<STATUS> cStatus)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.fMail=fMail;
		
		this.cMail=cMail;
		this.cStatus=cStatus;
		
		categories = fMail.allOrderedPositionVisible(fbMail.getClassCategory());
		if(debugOnInfo){logger.info(AbstractLogMessage.reloaded(fbMail.getClassCategory(),categories));}

		try
		{
			sbhRetention.setList(fMail.allOrderedPositionVisible(fbMail.getClassRetention()));
			sbhRetention.selectAll();
			
			sbhStatus.setList(fMail.allOrderedPositionVisible(cStatus));
			sbhStatus.select(fMail.fByCode(cStatus,JeeslMailStatus.Code.queue));
			sbhStatus.select(fMail.fByCode(cStatus,JeeslMailStatus.Code.spooling));
		}
		catch (JeeslNotFoundException e) {logger.error(e.getMessage());}
		initPageConfiguration();
		reloadMails();
		reloadStatistic();
	}
	
	protected void initPageConfiguration()
	{
		sbhCategory.setList(categories);
		sbhCategory.selectAll();
	}
	
	@Override public void toggled(Class<?> c)
	{
		logger.info(AbstractLogMessage.toggled(c));
		if(fbMail.getClassCategory().isAssignableFrom(c)){logger.info(fbMail.getClassCategory().getName());reloadStatistic();}
		else if(cStatus.isAssignableFrom(c)){logger.info(cStatus.getName());}
		reloadMails();
		clear(true);
	}
	
	private void reloadStatistic()
	{
		thToday.init(fMail.tpcIoMailByStatus(LocalDate.now(),LocalDate.now(),sbhCategory.getSelected()));
		thDay30.init(fMail.tpcIoMailByStatus(LocalDate.now().minusDays(30),LocalDate.now(),sbhCategory.getSelected()));
		
		mapTh.put(Statistic.today.toString(),thToday);
		mapTh.put(Statistic.day30.toString(),thDay30);
		
		for(STATUS s : thToday.getListA())
		{
			logger.info(s.getCode()+" "+thToday.getMapA().get(s).getCount1());
		}
	}
	
	@Override
	public void callbackDateChanged()
	{
		reloadMails();
		clear(true);
	}
	
	private void clear(boolean clearMail)
	{
		if(clearMail){mail=null;}
	}
	
	//*************************************************************************************
	protected void reloadMails()
	{
		LocalDateTime ldt = DateUtil.toLocalDateTime(sbhDate.getDate2());
		
		mails = fMail.fMails(sbhCategory.getSelected(),sbhStatus.getSelected(),sbhRetention.getSelected(),sbhDate.getDate1(),DateUtil.toDate(ldt.plusDays(1)),null);
		if(debugOnInfo){logger.info(AbstractLogMessage.reloaded(cMail,mails));}
//		Collections.sort(templates, comparatorTemplate);
	}
		
	public void selectMail()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(mail));}
		
	}
}