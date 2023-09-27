package org.jeesl.factory.builder.module;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.module.lf.EjbLfConfigurationFactory;
import org.jeesl.factory.ejb.module.lf.EjbLfIndicatorFactory;
import org.jeesl.factory.ejb.module.lf.EjbLfIndicatorValueFactory;
import org.jeesl.factory.ejb.module.lf.EjbLfLogFrameFactory;
import org.jeesl.factory.ejb.module.lf.EjbLfTimeElementFactory;
import org.jeesl.factory.ejb.module.lf.EjbLfTimeGroupFactory;
import org.jeesl.interfaces.model.module.lf.JeeslLfConfiguration;
import org.jeesl.interfaces.model.module.lf.JeeslLfLogframe;
import org.jeesl.interfaces.model.module.lf.indicator.JeeslLfIndicator;
import org.jeesl.interfaces.model.module.lf.indicator.JeeslLfIndicatorLevel;
import org.jeesl.interfaces.model.module.lf.indicator.JeeslLfIndicatorType;
import org.jeesl.interfaces.model.module.lf.indicator.JeeslLfUnit;
import org.jeesl.interfaces.model.module.lf.indicator.JeeslLfVerificationSource;
import org.jeesl.interfaces.model.module.lf.time.JeeslLfTimeElement;
import org.jeesl.interfaces.model.module.lf.time.JeeslLfTimeGroup;
import org.jeesl.interfaces.model.module.lf.time.JeeslLfTimeInterval;
import org.jeesl.interfaces.model.module.lf.value.JeeslLfValue;
import org.jeesl.interfaces.model.module.lf.value.JeeslLfValueType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LfFactoryBuilder<L extends JeeslLang,D extends JeeslDescription,R extends JeeslTenantRealm<L,D,R,?>,
							LF extends JeeslLfLogframe<L,D,R,I,IL,IT>,
							I extends JeeslLfIndicator<LF,IL,IT,IU,IV,TG,LFM>,
							IL extends JeeslLfIndicatorLevel<L, D,R, IL, ?>,
							IT extends JeeslLfIndicatorType<L, D,R, IT, ?>,
							IU extends JeeslLfUnit<L,D,R,IU,?>,
							IV extends JeeslLfVerificationSource<L,D,R,IV,?>,
							TG extends JeeslLfTimeGroup<L,?>,
							TI extends JeeslLfTimeInterval<L,D,TI,?>,
							TE extends JeeslLfTimeElement<L,TG>,
							LFM extends JeeslLfValue<I,VT,TG,TE>,
							VT extends JeeslLfValueType<L,D,VT,?>,
							LFC extends JeeslLfConfiguration<LF,?>>
extends AbstractFactoryBuilder<L,D>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(LfFactoryBuilder.class);

	private final Class<R> cRealm; public Class<R> getClassRealm() {return cRealm;}
	private final Class<TG> cTG; public Class<TG> getClassTG() {return cTG;}
	private final Class<TI> cTI; public Class<TI> getClassTI() {return cTI;}
	private final Class<TE> cTE; public Class<TE> getClassTE() {return cTE;}
	private final Class<LF> cLF; public Class<LF>  getClassLF() {return cLF;}
	private final Class<I> cI; public Class<I>  getClassLFI() {return cI;}
	private final Class<LFM> cLFM; public Class<LFM>  getClassLFM() {return cLFM;}
	private final Class<LFC> cLFC; public Class<LFC>  getClassLFC() {return cLFC;}
	private final Class<IL> cIL; public Class<IL>  getClassIL() {return cIL;}
	private final Class<IT> cIT; public Class<IT>  getClassIT() {return cIT;}
	private final Class<IU> cIU; public Class<IU>  getClassIU() {return cIU;}
	private final Class<VT> cVT; public Class<VT>  getClassVT() {return cVT;}

	public LfFactoryBuilder(final Class<L> cL,
							final Class<D> cD,
							final Class<R> cRealm,
							final Class<LF> cLf,
							final Class<I> cI,
							final Class<IL> cIL,
							final Class<IT> cIT,
							final Class<IU> cIU,
							final Class<TG> cTG,
							final Class<TI> cTI,
							final Class<TE> cTE,
							final Class<LFM> cLfM,
							final Class<VT> cVT,
							final Class<LFC> cLFC)
	{
		super(cL,cD);
		this.cRealm = cRealm;
		this.cLF = cLf;
		this.cI = cI;
		this.cIL =cIL;
		this.cIT =cIT;
		this.cLFM = cLfM;
		this.cTG = cTG;
		this.cTI = cTI;
		this.cTE = cTE;
		this.cLFC = cLFC;
		this.cIU = cIU;
		this.cVT = cVT;
	}

	public EjbLfTimeGroupFactory<TG> ejbTimeGroup() {return new EjbLfTimeGroupFactory<>(cTG);}

	public EjbLfTimeElementFactory<TG,TE> ejbTimeElement(){return new EjbLfTimeElementFactory<>(cTE);}

	public EjbLfLogFrameFactory<LF> ejbLfLogFrame() {return new EjbLfLogFrameFactory<>(cLF);}
	public EjbLfIndicatorFactory<I,IT,IU,IV> ejbLfIndicator() {return new EjbLfIndicatorFactory<>(cI);}
	public EjbLfIndicatorValueFactory<LFM> ejbLfIndicatorValue() {return new EjbLfIndicatorValueFactory<>(cLFM);}
	public EjbLfConfigurationFactory<LFC> ejbLfConfiguration() {return new EjbLfConfigurationFactory<>(cLFC);}



}