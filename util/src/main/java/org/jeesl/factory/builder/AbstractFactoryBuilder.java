package org.jeesl.factory.builder;

import java.io.Serializable;

import org.jeesl.factory.ejb.system.status.EjbDescriptionFactory;
import org.jeesl.factory.ejb.system.status.EjbLangFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractFactoryBuilder<L extends JeeslLang, D extends JeeslDescription> implements Serializable 
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(AbstractFactoryBuilder.class);
	
	protected final Class<L> cL; public Class<L> getClassL() {return cL;}
	protected final Class<D> cD; public Class<D> getClassD() {return cD;}
	
	public AbstractFactoryBuilder(final Class<L> cL, final Class<D> cD)
	{       
		this.cL=cL;
		this.cD=cD;
	}
	
	public EjbLangFactory<L> ejbLang() {return EjbLangFactory.instance(cL);}
	public EjbDescriptionFactory<D> ejbDescription() {return EjbDescriptionFactory.instance(cD);}
}