package net.sf.ahtutils.report.revision;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.jxpath.JXPathContext;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RevisionEngineAttributeResolver<L extends JeeslLang,D extends JeeslDescription,
							RC extends JeeslRevisionCategory<L,D,RC,?>,
							RV extends JeeslRevisionView<L,D,RVM>,
							RVM extends JeeslRevisionViewMapping<RV,RE,REM>,
							RS extends JeeslRevisionScope<L,D,RC,RA>,
							RST extends JeeslRevisionScopeType<L,D,RST,?>,
							RE extends JeeslRevisionEntity<L,D,RC,REM,RA,?>,
							REM extends JeeslRevisionEntityMapping<RS,RST,RE>,
							RA extends JeeslRevisionAttribute<L,D,RE,RER,RAT>, RER extends JeeslStatus<L,D,RER>,
							RAT extends JeeslStatus<L,D,RAT>,
							REV extends JeeslRevision,
							C extends JeeslSecurityCategory<L,D>,
							R extends JeeslSecurityRole<L,D,C,V,U,A,USER>,
							V extends JeeslSecurityView<L,D,C,R,U,A>,
							U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
							A extends JeeslSecurityAction<L,D,R,V,U,AT>,
							AT extends JeeslSecurityTemplate<L,D,C>,
							USER extends JeeslUser<R>>
{
	final static Logger logger = LoggerFactory.getLogger(RevisionEngineAttributeResolver.class);

	private Map<RAT,DecimalFormat> mapDecimalFormatter;
	private Map<RAT,SimpleDateFormat> mapDateFormatter;
	
	public RevisionEngineAttributeResolver(Map<RAT,DecimalFormat> mapDecimalFormatter, Map<RAT,SimpleDateFormat> mapDateFormatter)
	{
		this.mapDecimalFormatter=mapDecimalFormatter;
		this.mapDateFormatter=mapDateFormatter;
	}
		
	public String build(String lang, RA attribute, JXPathContext ctx)
	{
		StringBuffer sb = new StringBuffer();
		if(attribute.isShowEnclosure()){sb.append("{");}
		if(attribute.isShowName()){sb.append(attribute.getName().get(lang).getLang()).append(": ");}
		sb.append(buildString(attribute,ctx));
		if(attribute.isShowEnclosure()){sb.append("}");}
		return sb.toString();
	}
	
	private String buildString(RA attribute, JXPathContext ctx)
	{
		String result = null;
		if(attribute.getType().getCode().startsWith(JeeslRevisionAttribute.Type.text.toString()))
		{
			result = (String)ctx.getValue(attribute.getXpath());
		}
		else if(attribute.getType().getCode().startsWith(JeeslRevisionAttribute.Type.number.toString()))
		{
			Object o = ctx.getValue(attribute.getXpath());
			if(o!=null){result = mapDecimalFormatter.get(attribute.getType()).format((Number)ctx.getValue(attribute.getXpath()));}
			else {result="null";}
		}
		else if(attribute.getType().getCode().startsWith(JeeslRevisionAttribute.Type.date.toString()))
		{
			Object o = ctx.getValue(attribute.getXpath());
			if(o!=null){result = mapDateFormatter.get(attribute.getType()).format((Date)o);}
			else {result="null";}
		}
		else if(attribute.getType().getCode().startsWith(JeeslRevisionAttribute.Type.bool.toString()))
		{
			Boolean b = (Boolean)ctx.getValue(attribute.getXpath());
			result = b.toString();
		}
		else
		{
			logger.warn("Unsupported Type: "+attribute.getType().getCode());
			result = ""+ctx.getValue(attribute.getXpath());
		}
		
		return result;
	}
}