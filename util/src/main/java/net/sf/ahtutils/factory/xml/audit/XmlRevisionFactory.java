package net.sf.ahtutils.factory.xml.audit;

import java.util.Date;

import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.xml.audit.Revision;
import net.sf.ahtutils.xml.audit.User;
import net.sf.exlp.util.DateUtil;

public class XmlRevisionFactory <L extends UtilsLang, D extends UtilsDescription,
									C extends JeeslSecurityCategory<L,D>,
									R extends JeeslSecurityRole<L,D,C,V,U,A,USER>,
									V extends JeeslSecurityView<L,D,C,R,U,A>,
									U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
									A extends JeeslSecurityAction<L,D,R,V,U,AT>,
									AT extends JeeslSecurityTemplate<L,D,C>,
									USER extends JeeslUser<R>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlRevisionFactory.class);
	
	private XmlUserFactory<L,D,C,R,V,U,A,AT,USER> fUser;
	
	public XmlRevisionFactory()
	{
		fUser = new XmlUserFactory<L,D,C,R,V,U,A,AT,USER>();
	}
	
	public Revision build(long rev, Date record)
	{
		Revision xml = new Revision();
		xml.setRev(rev);
		xml.setDate(DateUtil.toXmlGc(record));
		return xml;
	}
	
	public Revision build(long rev, Date record, USER user)
	{
		Revision xml = new Revision();
		xml.setRev(rev);
		xml.setDate(DateUtil.toXmlGc(record));
		xml.setUser(fUser.build(user));
		return xml;
	}
	
	public Revision build(long rev, Date record, User user)
	{
		Revision xml = new Revision();
		xml.setRev(rev);
		xml.setDate(DateUtil.toXmlGc(record));
		xml.setUser(user);
		return xml;
	}
}