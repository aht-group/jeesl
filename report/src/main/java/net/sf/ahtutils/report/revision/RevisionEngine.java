package net.sf.ahtutils.report.revision;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jxpath.JXPathContext;
import org.jeesl.api.facade.io.JeeslIoRevisionFacade;
import org.jeesl.interfaces.model.system.io.revision.JeeslRevision;
import org.jeesl.interfaces.model.system.io.revision.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.system.io.revision.JeeslRevisionContainer;
import org.jeesl.interfaces.model.system.io.revision.JeeslRevisionEntity;
import org.jeesl.interfaces.model.system.io.revision.JeeslRevisionEntityMapping;
import org.jeesl.interfaces.model.system.io.revision.JeeslRevisionScope;
import org.jeesl.interfaces.model.system.io.revision.JeeslRevisionView;
import org.jeesl.interfaces.model.system.io.revision.JeeslRevisionViewMapping;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.user.UtilsUser;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;
import net.sf.ahtutils.xml.audit.Change;

public class RevisionEngine<L extends UtilsLang,D extends UtilsDescription,
							RC extends UtilsStatus<RC,L,D>,
							RV extends JeeslRevisionView<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RAT>,
							RVM extends JeeslRevisionViewMapping<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RAT>,
							RS extends JeeslRevisionScope<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RAT>,
							RST extends UtilsStatus<RST,L,D>,
							RE extends JeeslRevisionEntity<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RAT>,
							REM extends JeeslRevisionEntityMapping<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RAT>,
							RA extends JeeslRevisionAttribute<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RAT>,
							RAT extends UtilsStatus<RAT,L,D>,
							REV extends JeeslRevision,
							C extends JeeslSecurityCategory<L,D,C,R,V,U,A,AT,USER>,
							R extends JeeslSecurityRole<L,D,C,R,V,U,A,AT,USER>,
							V extends JeeslSecurityView<L,D,C,R,V,U,A,AT,USER>,
							U extends JeeslSecurityUsecase<L,D,C,R,V,U,A,AT,USER>,
							A extends JeeslSecurityAction<L,D,C,R,V,U,A,AT,USER>,
							AT extends JeeslSecurityTemplate<L,D,C,R,V,U,A,AT,USER>,
							USER extends UtilsUser<L,D,C,R,V,U,A,AT,USER>>
{
	final static Logger logger = LoggerFactory.getLogger(RevisionEngine.class);
	
	private JeeslIoRevisionFacade<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RAT> fRevision;
	
	private RevisionEngineScopeResolver<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RAT,REV,C,R,V,U,A,AT,USER> resr;
	private RevisionEngineAttributeResolver<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RAT,REV,C,R,V,U,A,AT,USER> rear;
	
	private final Class<RV> cView;
	private final Class<RS> cScope;
	private final Class<RE> cEntity;
	
	private String lang;
	private Map<String,RVM> map;
	
	private Map<RAT,DecimalFormat> mapDecimalFormatter;
	private Map<RAT,SimpleDateFormat> mapDateFormatter;
	
	public RevisionEngine(JeeslIoRevisionFacade<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RAT> fRevision, final Class<RV> cView, final Class<RS> cScope, final Class<RE> cEntity, final Class<RAT> cRat)
	{
		this.fRevision=fRevision;
		this.cView=cView;
		this.cScope=cScope;
		this.cEntity=cEntity;
		
		map = new HashMap<String,RVM>();
		mapDecimalFormatter = new HashMap<RAT,DecimalFormat>();
		mapDateFormatter = new HashMap<RAT,SimpleDateFormat>();
		
		buildTypes(cRat);
		
		rear = RevisionEngineFactory.attribute(mapDecimalFormatter,mapDateFormatter);
		resr = RevisionEngineFactory.scope(fRevision,rear);
	}
	
	private void buildTypes(Class<RAT> cRat)
	{
		for(RAT rat : fRevision.all(cRat))
		{
			if(rat.getCode().startsWith(JeeslRevisionAttribute.Type.number.toString())){mapDecimalFormatter.put(rat, new DecimalFormat(rat.getSymbol()));}
			else if(rat.getCode().startsWith(JeeslRevisionAttribute.Type.date.toString())){mapDateFormatter.put(rat, new SimpleDateFormat(rat.getSymbol()));}
		}
	}
	
	public void init(String lang, RV view)
	{
		this.lang=lang;
		view = fRevision.load(cView, view);
		map.clear();
		for(RVM m : view.getMaps())
		{
			m.setEntity(fRevision.load(cEntity, m.getEntity()));
			m.getEntityMapping().setScope(fRevision.load(cScope, m.getEntityMapping().getScope()));
			map.put(m.getEntity().getCode(),m);
		}
		logger.info(this.getClass().getSimpleName()+" initialized with "+map.size()+" entities");
	}
	
	public Change build(JeeslRevisionContainer<REV,?,L,D,C,R,V,U,A,AT,USER> revision)
	{
		Object o = revision.getEntity();
		String key = o.getClass().getName();
		Change xml;
		boolean entityIsAvailable = map.containsKey(key);
		
		if(entityIsAvailable){xml = build(map.get(key),o);}
		else{return null;}
		xml.setAid(revision.getType().ordinal());
		return xml;
	}
	
	public Change build(RVM rvm, Object o)
	{
		logger.info(o.getClass().getSimpleName());
		JXPathContext context = JXPathContext.newContext(o);
		
		Change change = new Change();
		change.setType(rvm.getEntity().getName().get(lang).getLang());
		
		StringBuffer sb = new StringBuffer();
		for(RA attribute : rvm.getEntity().getAttributes())
		{
			if(attribute.isShowPrint())
			{
				sb.append(rear.build(lang,attribute,context));
				sb.append(" ");
			}
		}
		change.setText(sb.toString().trim());
		change.setScope(resr.build(lang,rvm,context,o));
		
		return change;
	}
}