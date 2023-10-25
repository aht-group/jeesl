package org.jeesl.controller.processor.system.io.mail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.jeesl.api.facade.io.JeeslIoMailFacade;
import org.jeesl.controller.monitoring.counter.BucketSizeCounter;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoMailFactoryBuilder;
import org.jeesl.interfaces.model.io.mail.core.JeeslIoMail;
import org.jeesl.interfaces.model.io.mail.core.JeeslIoMailRetention;
import org.jeesl.interfaces.model.io.mail.core.JeeslIoMailStatus;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.system.io.mail.Attachment;
import org.jeesl.model.xml.system.io.mail.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.xml.JaxbUtil;

public class MailSplitter<L extends JeeslLang,D extends JeeslDescription,
						CATEGORY extends JeeslStatus<L,D,CATEGORY>,
						MAIL extends JeeslIoMail<L,D,CATEGORY,STATUS,RETENTION,?>,
						STATUS extends JeeslIoMailStatus<L,D,STATUS,?>,
						RETENTION extends JeeslIoMailRetention<L,D,RETENTION,?>>
{
	final static Logger logger = LoggerFactory.getLogger(MailSplitter.class);
	
	private final IoMailFactoryBuilder<L,D,CATEGORY,MAIL,STATUS,RETENTION> fbMail;
	private final JeeslIoMailFacade<L,D,CATEGORY,MAIL,STATUS,RETENTION,?> fMail;
	
	private final BucketSizeCounter bsc;
	
	public MailSplitter(IoMailFactoryBuilder<L,D,CATEGORY,MAIL,STATUS,RETENTION> fbMail,
						JeeslIoMailFacade<L,D,CATEGORY,MAIL,STATUS,RETENTION,?> fMail)
	{		
		this.fbMail=fbMail;
		this.fMail=fMail;
		bsc = new BucketSizeCounter(this.getClass().getSimpleName()); 
	}
	
	public void split(List<CATEGORY> categories, List<STATUS> status, Date upToDate)
	{
		bsc.clear();
		List<RETENTION> retentions = new ArrayList<>();
		
		try
		{
			retentions.add(fMail.fByCode(fbMail.getClassRetention(), JeeslIoMailRetention.Code.fully));
		}
		catch (JeeslNotFoundException e) {e.printStackTrace();}
		
		
		int size=1;
		while(size>0)
		{
			List<MAIL> mails = fMail.fMails(categories,status,retentions, new Date(0), upToDate, 5);
			size=mails.size();
			for(MAIL eMail : mails)
			{
				bsc.debugLoop(100);
				bsc.add("e-mails",1);
				try
				{
					Mail xMail = JaxbUtil.loadJAXB(IOUtils.toInputStream(eMail.getXml(), "UTF-8"), Mail.class);
					
					bsc.add("attachments",xMail.getAttachment().size());
					for(Attachment att : xMail.getAttachment())
					{
						bsc.add("size",att.getData().length);
					}
//					logger.info(eMail.getRecipient()+" "+eMail.getXml().length()+" "+xMail.getAttachment().size());
					
					eMail.setRetention(fMail.fByCode(fbMail.getClassRetention(), JeeslIoMailRetention.Code.split));
					fMail.save(eMail);
				}
				catch (JeeslNotFoundException | JeeslConstraintViolationException | JeeslLockingException e) {e.printStackTrace();}
			}
		}
		
		bsc.ofx(System.out);
	}
}