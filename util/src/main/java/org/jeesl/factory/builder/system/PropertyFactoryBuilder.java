package org.jeesl.factory.builder.system;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.property.JeeslProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertyFactoryBuilder<L extends JeeslLang, D extends JeeslDescription,
									C extends JeeslStatus<L,D,C>,
									P extends JeeslProperty<L,D,C,P>>
				extends AbstractFactoryBuilder<L,D>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(PropertyFactoryBuilder.class);
	
	private final Class<C> cCategory; public Class<C> getClassCategory() {return cCategory;}
	private final Class<P> cProperty; public Class<P> getClassProperty() {return cProperty;}
	
	public PropertyFactoryBuilder(final Class<L> cL, final Class<D> cD, final Class<C> cCategory, final Class<P> cProperty)
	{
		super(cL,cD);
		this.cCategory=cCategory;
		this.cProperty=cProperty;
	}
}