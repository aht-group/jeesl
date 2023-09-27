package org.jeesl.factory.builder.module;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.module.mdc.EjbMdcCollectionFactory;
import org.jeesl.factory.ejb.module.mdc.EjbMdcDataFactory;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeContainer;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeSet;
import org.jeesl.interfaces.model.module.mdc.collection.JeeslMdcCollection;
import org.jeesl.interfaces.model.module.mdc.collection.JeeslMdcScope;
import org.jeesl.interfaces.model.module.mdc.collection.JeeslMdcStatus;
import org.jeesl.interfaces.model.module.mdc.data.JeeslMdcData;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MdcFactoryBuilder<L extends JeeslLang,D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
							R extends JeeslTenantRealm<L,D,R,?>,
							COLLECTION extends JeeslMdcCollection<R,SCOPE,STATUS,ASET>,
							SCOPE extends JeeslMdcScope<L,D,R,SCOPE,?>,
							STATUS extends JeeslMdcStatus<L,D,STATUS,?>,
							
							CDATA extends JeeslMdcData<COLLECTION,ACON>,
							
							ASET extends JeeslAttributeSet<?,?,R,?,?>,
							ACON extends JeeslAttributeContainer<ASET,?>
							>
		extends AbstractFactoryBuilder<L,D>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(MdcFactoryBuilder.class);
	
	private final Class<COLLECTION> cActivity; public Class<COLLECTION> getClassActivity() {return cActivity;}
	private final Class<SCOPE> cScope; public Class<SCOPE> getClassScope() {return cScope;}
	private final Class<STATUS> cStatus; public Class<STATUS> getClassStatus() {return cStatus;}
	private final Class<CDATA> cData; public Class<CDATA> getClassData() {return cData;}
	
	private final Class<ASET> cAttributeSet; public Class<ASET> getClassAttributeSet() {return cAttributeSet;}

	public MdcFactoryBuilder(final Class<L> cL,final Class<D> cD,
							final Class<COLLECTION> cActivity,
							final Class<SCOPE> cScope,
							final Class<STATUS> cStatus,
							final Class<CDATA> cData,
							final Class<ASET> cAttributeSet)
	{       
		super(cL,cD);
		this.cActivity=cActivity;
		this.cScope=cScope;
		this.cStatus=cStatus;
		this.cData=cData;
		this.cAttributeSet=cAttributeSet;
	}

	public EjbMdcCollectionFactory<R,COLLECTION,SCOPE,STATUS,ASET> ejbActivity() {return new EjbMdcCollectionFactory<>(this);}
	public EjbMdcDataFactory<COLLECTION,CDATA,ACON> ejbData() {return new EjbMdcDataFactory<>(cData);}
}