package org.jeesl.web.rest.system.io;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.exlp.util.jx.JaxbUtil;
import org.exlp.util.system.DateUtil;
import org.jeesl.api.facade.io.JeeslIoMailFacade;
import org.jeesl.api.rest.i.io.JeeslIoMailRestInterface;
import org.jeesl.api.rest.rs.jx.io.mail.JeeslIoMailRestExport;
import org.jeesl.api.rest.rs.jx.io.mail.JeeslIoMailRestImport;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoMailFactoryBuilder;
import org.jeesl.factory.xml.io.mail.XmlMailFactory;
import org.jeesl.factory.xml.io.mail.XmlMailsFactory;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.mail.core.JeeslIoMail;
import org.jeesl.interfaces.model.io.mail.core.JeeslIoMailRetention;
import org.jeesl.interfaces.model.io.mail.core.JeeslIoMailStatus;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.mail.Mail;
import org.jeesl.model.xml.io.mail.Mails;
import org.jeesl.model.xml.io.ssi.sync.DataUpdate;
import org.jeesl.model.xml.xsd.Container;
import org.jeesl.web.rest.AbstractJeeslRestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoMailRestHandler <L extends JeeslLang,D extends JeeslDescription,
								CATEGORY extends JeeslStatus<L,D,CATEGORY>,
								MAIL extends JeeslIoMail<L,D,CATEGORY,STATUS,RETENTION,FRC>,
								STATUS extends JeeslIoMailStatus<L,D,STATUS,?>,
								RETENTION extends JeeslIoMailRetention<L,D,RETENTION,?>,
								FRC extends JeeslFileContainer<?,?>>
					extends AbstractJeeslRestHandler<L,D>
					implements JeeslIoMailRestInterface,JeeslIoMailRestExport,JeeslIoMailRestImport
{
	final static Logger logger = LoggerFactory.getLogger(IoMailRestHandler.class);
	
	private final IoMailFactoryBuilder<?,?,CATEGORY,MAIL,STATUS,RETENTION> fbMail;
	private JeeslIoMailFacade<CATEGORY,MAIL,STATUS,RETENTION,FRC> fMail;
	
	private IoMailRestHandler(IoMailFactoryBuilder<L,D,CATEGORY,MAIL,STATUS,RETENTION> fbMail, JeeslIoMailFacade<CATEGORY,MAIL,STATUS,RETENTION,FRC> fMail)
	{
		super(fMail,fbMail.getClassL(),fbMail.getClassD());
		this.fbMail=fbMail;
		this.fMail=fMail;
	}
	
	public static <L extends JeeslLang,D extends JeeslDescription,
					CATEGORY extends JeeslStatus<L,D,CATEGORY>,
					MAIL extends JeeslIoMail<L,D,CATEGORY,STATUS,RETENTION,FRC>,
					STATUS extends JeeslIoMailStatus<L,D,STATUS,?>,
					RETENTION extends JeeslIoMailRetention<L,D,RETENTION,?>,
					FRC extends JeeslFileContainer<?,?>>
		IoMailRestHandler<L,D,CATEGORY,MAIL,STATUS,RETENTION,FRC>
		instance(IoMailFactoryBuilder<L,D,CATEGORY,MAIL,STATUS,RETENTION> fbMail, JeeslIoMailFacade<CATEGORY,MAIL,STATUS,RETENTION,FRC> fMail)
	{
		return new IoMailRestHandler<L,D,CATEGORY,MAIL,STATUS,RETENTION,FRC>(fbMail,fMail);
	}
	
	@Override public Container exportSystemIoMailCategories() {return xfContainer.build(fMail.allOrderedPosition(fbMail.getClassCategory()));}
	@Override public Container exportSystemIoMailStatus() {return xfContainer.build(fMail.allOrderedPosition(fbMail.getClassStatus()));}
	@Override public Container exportSystemIoMailRetention() {return xfContainer.build(fMail.allOrderedPosition(fbMail.getClassRetention()));}
	
	@Override public DataUpdate importSystemIoMailCategories(Container categories) {return importStatus(fbMail.getClassCategory(),categories,null);}
	@Override public DataUpdate importSystemIoMailStatus(Container categories) {return importStatus(fbMail.getClassStatus(),categories,null);}
	@Override public DataUpdate importSystemIoMailRetention(Container categories) {return importStatus(fbMail.getClassRetention(),categories,null);}

	@Override public Mails spool()
	{
		List<MAIL> eMails = fMail.fSpoolMails(1);
		Mails xml = XmlMailsFactory.build();
		xml.setQueue(fMail.cQueue());

		for(MAIL eMail : eMails)
		{
			try
			{
				eMail = fMail.find(fbMail.getClassMail(),eMail);
				eMail.setRecordSpool(new Date());
				eMail.setCounter(eMail.getCounter()+1);
				if(eMail.getCounter()>5)
				{
					eMail.setStatus(fMail.fByCode(fbMail.getClassStatus(), JeeslIoMailStatus.Code.failed));
					eMail = fMail.save(eMail);
				}
				else
				{
					eMail.setStatus(fMail.fByCode(fbMail.getClassStatus(), JeeslIoMailStatus.Code.spooling));
					eMail = fMail.save(eMail);
					Mail xMail = JaxbUtil.loadJAXB(IOUtils.toInputStream(eMail.getXml(), "UTF-8"), Mail.class);
					xMail.setId(eMail.getId());
					xml.getMail().add(xMail);
				}
			}
			catch (JeeslConstraintViolationException e) {e.printStackTrace();}
			catch (JeeslLockingException e) {logger.warn(e.getMessage());}
			catch (JeeslNotFoundException e) {logger.error(e.getMessage());}
		}		
		return xml;
	}

	@Override public Mail confirm(long id)
	{
		Mail xMail = XmlMailFactory.build("pending");
		try
		{
			MAIL eMail = fMail.find(fbMail.getClassMail(),id);
			eMail.setStatus(fMail.fByCode(fbMail.getClassStatus(), JeeslIoMailStatus.Code.sent));
			eMail.setRecordSent(new Date());
			eMail = fMail.update(eMail);
			xMail.setId(eMail.getId());
			xMail.setCode("confirmed");
		}
		catch (JeeslNotFoundException e) {xMail.setCode("error");}
		catch (JeeslConstraintViolationException e) {xMail.setCode("error");}
		catch (JeeslLockingException e) {xMail.setCode("error");}
		return xMail;
	}

	@Override public Mails discard(int days)
	{
		LocalDate ld = LocalDate.now().minusDays(days);
//		DateTime dt = new DateTime().minusDays(days);
		
		List<CATEGORY> categories = fMail.all(fbMail.getClassCategory());
		List<RETENTION> retentions = fMail.all(fbMail.getClassRetention());
		
		List<STATUS> status = new ArrayList<>();
		try {status.add(fMail.fByCode(fbMail.getClassStatus(),JeeslIoMailStatus.Code.queue));}
		catch (JeeslNotFoundException e) {e.printStackTrace();}
		
		List<MAIL> mails = fMail.fMails(categories, status, retentions, null, DateUtil.toDate(ld.atStartOfDay()), null);
		
		if(!mails.isEmpty())
		{
			try
			{
				STATUS discard = fMail.fByCode(fbMail.getClassStatus(),JeeslIoMailStatus.Code.discarded);
				for(MAIL mail : mails)
				{
					mail.setStatus(discard);
				}
				fMail.save(mails);
			}
			catch (JeeslNotFoundException | JeeslConstraintViolationException | JeeslLockingException e) {e.printStackTrace();}
		}
		
		Mails xml = new Mails();
		xml.setQueue(mails.size());
		return xml;
	}
}