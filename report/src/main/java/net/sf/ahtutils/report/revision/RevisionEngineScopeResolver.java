package net.sf.ahtutils.report.revision;

import org.apache.commons.jxpath.JXPathContext;
import org.jeesl.api.facade.io.JeeslIoRevisionFacade;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionCategory;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionEntityMapping;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionScope;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionScopeType;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionView;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionViewMapping;
import org.jeesl.interfaces.model.io.label.revision.envers.JeeslRevision;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.audit.Scope;

public class RevisionEngineScopeResolver<L extends JeeslLang,D extends JeeslDescription,
							RC extends JeeslRevisionCategory<L,D,RC,?>,
							RV extends JeeslRevisionView<L,D,RVM>,
							RVM extends JeeslRevisionViewMapping<RV,RE,REM>,
							RS extends JeeslRevisionScope<L,D,RC,RA>,
							RST extends JeeslRevisionScopeType<L,D,RST,?>,
							RE extends JeeslRevisionEntity<L,D,RC,REM,RA,?>,
							REM extends JeeslRevisionEntityMapping<RS,RST,RE>,
							RA extends JeeslRevisionAttribute<L,D,RE,RER,RAT>,
							RER extends JeeslStatus<L,D,RER>,
							RAT extends JeeslStatus<L,D,RAT>,
							REV extends JeeslRevision,
							C extends JeeslSecurityCategory<L,D>,
							R extends JeeslSecurityRole<L,D,C,V,U,A>,
							V extends JeeslSecurityView<L,D,C,R,U,A>,
							U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
							A extends JeeslSecurityAction<L,D,R,V,U,AT>,
							AT extends JeeslSecurityTemplate<L,D,C>,
							USER extends JeeslUser<R>>
{
	final static Logger logger = LoggerFactory.getLogger(RevisionEngineScopeResolver.class);

	private JeeslIoRevisionFacade<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,?,?> fRevision;

	private RevisionEngineAttributeResolver<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT,REV,C,R,V,U,A,AT,USER> rear;

	public RevisionEngineScopeResolver(JeeslIoRevisionFacade<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,?,?> fRevision, RevisionEngineAttributeResolver<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT,REV,C,R,V,U,A,AT,USER> rear)
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
		catch (JeeslNotFoundException e) {e.printStackTrace();}
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
	private Scope jpqlTree(String lang, RVM rvm, JXPathContext context, Object oChild) throws ClassNotFoundException, JeeslNotFoundException
	{
		Long id = (Long)getXPathScopeObject(rvm,context,oChild);
		Class<EjbWithId> c = (Class<EjbWithId>)Class.forName(rvm.getEntityMapping().getScope().getCode()).asSubclass(EjbWithId.class);

		EjbWithId oScope = fRevision.jpaTree(c, rvm.getEntityMapping().getJpqlTree(), id);
		return build(lang,oScope,rvm.getEntityMapping().getScope(),JXPathContext.newContext(oScope));
	}
}