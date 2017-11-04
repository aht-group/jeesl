package net.sf.ahtutils.report.revision;

import org.apache.commons.jxpath.JXPathContext;
import org.jeesl.api.facade.io.JeeslIoRevisionFacade;
import org.jeesl.interfaces.model.system.io.revision.JeeslRevision;
import org.jeesl.interfaces.model.system.io.revision.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.system.io.revision.JeeslRevisionEntity;
import org.jeesl.interfaces.model.system.io.revision.JeeslRevisionEntityMapping;
import org.jeesl.interfaces.model.system.io.revision.JeeslRevisionScope;
import org.jeesl.interfaces.model.system.io.revision.JeeslRevisionView;
import org.jeesl.interfaces.model.system.io.revision.JeeslRevisionViewMapping;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.exception.ejb.UtilsNotFoundException;
import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;
import net.sf.ahtutils.model.interfaces.with.EjbWithId;
import net.sf.ahtutils.xml.audit.Scope;

public class RevisionEngineScopeResolver<L extends UtilsLang,D extends UtilsDescription,
							RC extends UtilsStatus<RC,L,D>,
							RV extends JeeslRevisionView<L,D,RVM>,
							RVM extends JeeslRevisionViewMapping<RV,RE,REM>,
							RS extends JeeslRevisionScope<L,D,RC,RA>,
							RST extends UtilsStatus<RST,L,D>,
							RE extends JeeslRevisionEntity<L,D,RC,REM,RA>,
							REM extends JeeslRevisionEntityMapping<RS,RST,RE>,
							RA extends JeeslRevisionAttribute<L,D,RE,RAT>,
							RAT extends UtilsStatus<RAT,L,D>,
							REV extends JeeslRevision,
							C extends JeeslSecurityCategory<L,D>,
							R extends JeeslSecurityRole<L,D,C,R,V,U,A,AT,USER>,
							V extends JeeslSecurityView<L,D,C,R,U,A>,
							U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
							A extends JeeslSecurityAction<L,D,C,R,V,U,A,AT,USER>,
							AT extends JeeslSecurityTemplate<L,D,C,R,V,U,A,AT,USER>,
							USER extends JeeslUser<L,D,C,R,V,U,A,AT,USER>>
{
	final static Logger logger = LoggerFactory.getLogger(RevisionEngineScopeResolver.class);
	
	private JeeslIoRevisionFacade<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RAT> fRevision;
	
	private RevisionEngineAttributeResolver<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RAT,REV,C,R,V,U,A,AT,USER> rear;
	
	public RevisionEngineScopeResolver(JeeslIoRevisionFacade<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RAT> fRevision, RevisionEngineAttributeResolver<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RAT,REV,C,R,V,U,A,AT,USER> rear)
	{
		this.fRevision=fRevision;
		this.rear=rear;
	}
	
	public Scope build(String lang, RVM rvm, JXPathContext context, Object oChild)
	{
		JeeslRevisionEntityMapping.Type type = JeeslRevisionEntityMapping.Type.valueOf(rvm.getEntityMapping().getType().getCode());
		try{
		switch(type)
		{
			case xpath: return xpath(lang,rvm,context,oChild);
			case jpqlTree: return jpqlTree(lang,rvm,context,oChild);
			default: return null;
		}
		}
		catch (ClassNotFoundException e){e.printStackTrace();}
		catch (UtilsNotFoundException e) {e.printStackTrace();}
		return null;
	}
	
	private Scope xpath(String lang, RVM rvm, JXPathContext context, Object oChild)
	{
		Object oScope = getXPathScopeObject(rvm,context,oChild);
		JXPathContext ctx = getXPathContext(rvm,context,oScope); 
		return build(lang,oScope,rvm.getEntityMapping().getScope(),ctx);
	}
	
	private Scope build(String lang, Object oScope, RS scope, JXPathContext ctx)
	{
		Scope xScope = new Scope();
		xScope.setClazz(oScope.getClass().getName());
		xScope.setCategory(scope.getCategory().getName().get(lang).getLang());
		
		if(oScope instanceof EjbWithId){xScope.setId(((EjbWithId)oScope).getId());}
		StringBuffer sb = new StringBuffer();
		for(RA attribute : scope.getAttributes())
		{
			if(attribute.isShowPrint())
			{
				sb.append(rear.build(lang, attribute, ctx));
				sb.append(" ");
			}
		}
		xScope.setEntity(sb.toString().trim());
		return xScope;
	}
	
	private Object getXPathScopeObject(RVM rvm, JXPathContext context, Object oChild)
	{
		if(rvm.getEntityMapping().getXpath().trim().length()==0){return oChild;}
		else{return context.getValue(rvm.getEntityMapping().getXpath());}
	}
	
	private JXPathContext getXPathContext(RVM rvm, JXPathContext context, Object oScope)
	{
		if(rvm.getEntityMapping().getXpath().trim().length()==0) {return context;}
		else {return JXPathContext.newContext(oScope);}
	}
	
	@SuppressWarnings("unchecked")
	private Scope jpqlTree(String lang, RVM rvm, JXPathContext context, Object oChild) throws ClassNotFoundException, UtilsNotFoundException
	{
		Long id = (Long)getXPathScopeObject(rvm,context,oChild);
		Class<EjbWithId> c = (Class<EjbWithId>)Class.forName(rvm.getEntityMapping().getScope().getCode()).asSubclass(EjbWithId.class);
		
		EjbWithId oScope = fRevision.jpaTree(c, rvm.getEntityMapping().getJpqlTree(), id);
		return build(lang,oScope,rvm.getEntityMapping().getScope(),JXPathContext.newContext(oScope));
	}
}