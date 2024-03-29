package org.jeesl.factory.builder.io;

import org.jeesl.api.facade.io.JeeslIoMailFacade;
import org.jeesl.controller.processor.system.io.mail.MailSplitter;
import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.io.mail.core.EjbIoMailFactory;
import org.jeesl.interfaces.model.io.mail.core.JeeslIoMail;
import org.jeesl.interfaces.model.io.mail.core.JeeslIoMailRetention;
import org.jeesl.interfaces.model.io.mail.core.JeeslIoMailStatus;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoMailFactoryBuilder<L extends JeeslLang,D extends JeeslDescription,
								CATEGORY extends JeeslStatus<L,D,CATEGORY>,
								MAIL extends JeeslIoMail<L,D,CATEGORY,STATUS,RETENTION,?>,
								STATUS extends JeeslIoMailStatus<L,D,STATUS,?>,
								RETENTION extends JeeslIoMailRetention<L,D,RETENTION,?>>
		extends AbstractFactoryBuilder<L,D>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(IoMailFactoryBuilder.class);
	
	private final Class<CATEGORY> cCategory; public Class<CATEGORY> getClassCategory(){return cCategory;}
	private final Class<MAIL> cMail; public Class<MAIL> getClassMail(){return cMail;}
	private final Class<STATUS> cStatus; public Class<STATUS> getClassStatus(){return cStatus;}
	private final Class<RETENTION> cRetention;  public Class<RETENTION> getClassRetention(){return cRetention;}
	
	public IoMailFactoryBuilder(final Class<L> cL, final Class<D> cD,
								final Class<CATEGORY> cCategory,
								final Class<MAIL> cMail,
								final Class<STATUS> cStatus,
								final Class<RETENTION> cRetention)
	{
		super(cL,cD);
		this.cCategory=cCategory;
		this.cMail = cMail;
		this.cStatus=cStatus;
		this.cRetention=cRetention;
	}
	
	public EjbIoMailFactory<CATEGORY,MAIL,STATUS,RETENTION> mail() {return new EjbIoMailFactory<>(cMail);}
	public MailSplitter<L,D,CATEGORY,MAIL,STATUS,RETENTION> splitter(JeeslIoMailFacade<CATEGORY,MAIL,STATUS,RETENTION,?> fMail) {return new MailSplitter<L,D,CATEGORY,MAIL,STATUS,RETENTION>(this,fMail);}
}