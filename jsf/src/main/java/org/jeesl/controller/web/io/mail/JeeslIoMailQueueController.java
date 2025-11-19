package org.jeesl.controller.web.io.mail;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.exlp.util.system.DateUtil;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoMailFacade;
import org.jeesl.controller.handler.tuple.JsonTuple1Handler;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.factory.builder.io.IoMailFactoryBuilder;
import org.jeesl.interfaces.bean.sb.bean.SbDateSelectionBean;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.bean.sb.handler.SbDateSelection;
import org.jeesl.interfaces.bean.sb.handler.SbToggleSelection;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.mail.core.JeeslIoMail;
import org.jeesl.interfaces.model.io.mail.core.JeeslIoMailRetention;
import org.jeesl.interfaces.model.io.mail.core.JeeslIoMailStatus;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.jsf.handler.sb.SbDateHandler;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslIoMailQueueController <L extends JeeslLang,D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											CATEGORY extends JeeslStatus<L,D,CATEGORY>,
											MAIL extends JeeslIoMail<L,D,CATEGORY,STATUS,RETENTION,FRC>,
											STATUS extends JeeslIoMailStatus<L,D,STATUS,?>,
											RETENTION extends JeeslIoMailRetention<L,D,RETENTION,?>,
											FRC extends JeeslFileContainer<?,?>>
					extends AbstractJeeslLocaleWebController<L,D,LOC>
					implements Serializable,SbToggleBean,SbDateSelectionBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslIoMailQueueController.class);
	
	private enum Statistic{today,day30}
	
	protected JeeslIoMailFacade<CATEGORY,MAIL,STATUS,RETENTION,FRC> fMail;
	private final IoMailFactoryBuilder<L,D,CATEGORY,MAIL,STATUS,RETENTION> fbMail;

	private List<CATEGORY> categories; public List<CATEGORY> getCategories() {return categories;}
	private List<MAIL> mails; public List<MAIL> getMails() {return mails;}
	private final List<String> statistics; public List<String> getStatistics() {return statistics;}
	private MAIL mail; public MAIL getMail() {return mail;} public void setMail(MAIL mail) {this.mail = mail;}
	
	protected final SbMultiHandler<CATEGORY> sbhCategory; public SbMultiHandler<CATEGORY> getSbhCategory() {return sbhCategory;}
	protected final SbMultiHandler<STATUS> sbhStatus; public SbMultiHandler<STATUS> getSbhStatus() {return sbhStatus;}
	protected final SbMultiHandler<RETENTION> sbhRetention; public SbMultiHandler<RETENTION> getSbhRetention() {return sbhRetention;}
	private final SbDateHandler sbhDate; public SbDateHandler getSbhDate() {return sbhDate;}
	
	private final Map<String,JsonTuple1Handler<STATUS>> mapTh; public Map<String, JsonTuple1Handler<STATUS>> getMapTh() {return mapTh;}
	private final JsonTuple1Handler<STATUS> thToday,thDay30;
	
	public JeeslIoMailQueueController(IoMailFactoryBuilder<L,D,CATEGORY,MAIL,STATUS,RETENTION> fbMail)
	{
		super(fbMail.getClassL(),fbMail.getClassD());
		this.fbMail=fbMail;
		sbhDate = SbDateHandler.instance(this);
		sbhDate.initMonths(2,0);
		
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
	
	public void postConstructMailQueue(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
											JeeslIoMailFacade<CATEGORY,MAIL,STATUS,RETENTION,FRC> fMail)
	{
		super.postConstructLocaleWebController(lp,bMessage);
		this.fMail=fMail;
		
		categories = fMail.allOrderedPositionVisible(fbMail.getClassCategory());
		if(debugOnInfo){logger.info(AbstractLogMessage.reloaded(fbMail.getClassCategory(),categories));}

		sbhRetention.setList(fMail.allOrderedPositionVisible(fbMail.getClassRetention()));
		sbhRetention.selectAll();
		
		sbhStatus.setList(fMail.allOrderedPositionVisible(fbMail.getClassStatus()));
		sbhStatus.select(fMail.fByEnum(fbMail.getClassStatus(),JeeslIoMailStatus.Code.queue));
		sbhStatus.select(fMail.fByEnum(fbMail.getClassStatus(),JeeslIoMailStatus.Code.spooling));
		
		initPageConfiguration();
		reloadMails();
		reloadStatistic();
	}
	
	protected void initPageConfiguration()
	{
		sbhCategory.setList(categories);
		sbhCategory.selectAll();
	}
	
	@Override public void toggled(SbToggleSelection handler, Class<?> c)
	{
		logger.info(AbstractLogMessage.toggled(c));
		if(fbMail.getClassCategory().isAssignableFrom(c)){logger.info(fbMail.getClassCategory().getName());reloadStatistic();}
		else if(fbMail.getClassStatus().isAssignableFrom(c)){logger.info(fbMail.getClassStatus().getName());}
		reloadMails();
		clear(true);
	}
	
	private void reloadStatistic()
	{
		thToday.load(fMail.tpcIoMailByStatus(LocalDate.now(),LocalDate.now(),sbhCategory.getSelected()));
		thDay30.load(fMail.tpcIoMailByStatus(LocalDate.now().minusDays(30),LocalDate.now(),sbhCategory.getSelected()));
		
		mapTh.put(Statistic.today.toString(),thToday);
		mapTh.put(Statistic.day30.toString(),thDay30);
		
		for(STATUS s : thToday.getListA())
		{
			logger.info(s.getCode()+" "+thToday.getMapA().get(s).getCount1());
		}
	}
	
	@Override
	public void callbackDateChanged(SbDateSelection handler)
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
		mails = fMail.fMails(sbhCategory.getSelected(),sbhStatus.getSelected(),sbhRetention.getSelected(),DateUtil.toDate(sbhDate.getDateFrom()),DateUtil.toDate(sbhDate.getDateTo()),null);
		if(debugOnInfo){logger.info(AbstractLogMessage.reloaded(fbMail.getClassMail(),mails));}
//		Collections.sort(templates, comparatorTemplate);
	}
		
	public void selectMail()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(mail));}
		
	}
}