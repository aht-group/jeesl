package org.jeesl.controller.provider;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.jeesl.api.facade.io.JeeslIoRevisionFacade;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoRevisionFactoryBuilder;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslTranslationProvider;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FacadeTranslationProvider <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
									RE extends JeeslRevisionEntity<L,D,?,?,RA,?>, RA extends JeeslRevisionAttribute<L,D,RE,?,?>>
							implements JeeslTranslationProvider<LOC>
{
	final static Logger logger = LoggerFactory.getLogger(FacadeTranslationProvider.class);

	private final JeeslIoRevisionFacade<?,?,?,?,?,?,?,RE,?,?,?,?> fRevision;
	private final IoRevisionFactoryBuilder<?,?,?,?,?,?,?,RE,?,?,?,?,?,?> fbRevision;

	public FacadeTranslationProvider(IoRevisionFactoryBuilder<?,?,?,?,?,?,?,RE,?,?,?,?,?,?> fbRevision,
								JeeslIoRevisionFacade<?,?,?,?,?,?,?,RE,?,?,?,?> fRevision)
	{
		this.fbRevision=fbRevision;
		this.fRevision=fRevision;
	}

	@Override public <E extends Enum<E>> String xpAttribute(String localeCode, Class<?> c, E code)
	{
		try
		{
			RE entity = fRevision.fByCode(fbRevision.getClassEntity(), c.getName());
			entity = fRevision.load(fbRevision.getClassEntity(), entity);
			logger.info(entity.toString()+" for "+code.toString());
			for(RA ra : entity.getAttributes())
			{
				if(ra.getCode().equals(code.toString()) && ra.getXpath()!=null && ra.getXpath().trim().length()>0)
				{
					return ra.getXpath();
				}
//				logger.info("\t"+ra.toString()+" "+ra.getCode());
			}
		}
		catch (JeeslNotFoundException e) {e.printStackTrace();}
		logger.warn("No XPATH devfined for "+c.getSimpleName()+" and attribute:"+code.toString());
		return "@id";
	}

	@Override public String tlEntity(String localeCode, Class<?> c)
	{
		try
		{
			RE entity = fRevision.fByCode(fbRevision.getClassEntity(), c.getName());
			return entity.getName().get(localeCode).getLang();
		}
		catch (JeeslNotFoundException e) {e.printStackTrace();}
		return c.getSimpleName();
	}

	@Override
	public boolean hasLocale(String localeCode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<String> getLocaleCodes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String tlEntity(String localeCode, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String tlAttribute(String localeCode, String key1, String key2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E extends Enum<E>> String toLabel(String localeCode, Class<?> c, E code) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public <E extends Enum<E>> String toDescription(String localeCode, Class<?> c, E code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toDate(String localeCode, Date record) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toTime(String localeCode, Date record) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toCurrency(String localeCode, Double value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toCurrency(String localeCode, boolean grouping, int decimals, Double value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLanguages(List<LOC> locales) {
		// TODO Auto-generated method stub

	}

	@Override
	public String toDate(String localeCode, LocalDate record) {
		// TODO Auto-generated method stub
		return null;
	}
}