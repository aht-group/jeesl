package net.sf.ahtutils.controller.audit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jeesl.JeeslUtilTestBootstrap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.factory.xml.audit.XmlChangeFactory;
import net.sf.ahtutils.factory.xml.audit.XmlRevisionFactory;
import net.sf.ahtutils.factory.xml.audit.XmlRevisionsFactory;
import net.sf.ahtutils.factory.xml.audit.XmlScopeFactory;
import net.sf.ahtutils.model.ejb.status.Description;
import net.sf.ahtutils.model.ejb.status.Lang;
import net.sf.ahtutils.model.ejb.system.security.SecurityAction;
import net.sf.ahtutils.model.ejb.system.security.SecurityActionTemplate;
import net.sf.ahtutils.model.ejb.system.security.SecurityCategory;
import net.sf.ahtutils.model.ejb.system.security.SecurityRole;
import net.sf.ahtutils.model.ejb.system.security.SecurityUsecase;
import net.sf.ahtutils.model.ejb.system.security.SecurityView;
import net.sf.ahtutils.model.ejb.user.AhtUtilsUser;
import net.sf.ahtutils.test.AbstractJeeslTest;
import net.sf.ahtutils.xml.audit.Change;
import net.sf.ahtutils.xml.audit.Revision;
import net.sf.ahtutils.xml.audit.Revisions;
import net.sf.ahtutils.xml.audit.Scope;
import net.sf.exlp.util.xml.JaxbUtil;

public class TestAuditScopeProcessor extends AbstractJeeslTest
{
	final static Logger logger = LoggerFactory.getLogger(TestAuditScopeProcessor.class);
	
	private List<Change> list;
	private Revisions revisions;
	
	private AuditScopeProcessor asp;
	private XmlRevisionFactory<Lang,Description,SecurityCategory,SecurityRole,SecurityView,SecurityUsecase,SecurityAction,SecurityActionTemplate,AhtUtilsUser> xfRevision;
	
	@Before
    public void init()
    {
		asp = new AuditScopeProcessor();
	    xfRevision = new XmlRevisionFactory<Lang,Description,SecurityCategory,SecurityRole,SecurityView,SecurityUsecase,SecurityAction,SecurityActionTemplate,AhtUtilsUser>();
		initScopeList();
		initRevisions();
    }
	
	private void initScopeList()
	{
		Scope a = XmlScopeFactory.build(1, "a");
        Scope b = XmlScopeFactory.build(1, "b");
        Scope c = XmlScopeFactory.build(2, "c");c.setCategory("catC");
        Scope d = XmlScopeFactory.build(2, "c");d.setCategory("catC");

        list = new ArrayList<Change>();
        list.add(XmlChangeFactory.build(1, a));
        list.add(XmlChangeFactory.build(2, a));
        list.add(XmlChangeFactory.build(3, b));
        list.add(XmlChangeFactory.build(4, c));
        list.add(XmlChangeFactory.build(5, d));
	}
	
	private void initRevisions()
	{
		revisions = XmlRevisionsFactory.build();
		
		Revision r1 = xfRevision.build(1, new Date());
		Scope a = XmlScopeFactory.build(1, "a");
		a.getChange().add(XmlChangeFactory.build(1, "a1"));
		a.getChange().add(XmlChangeFactory.build(2, "a2"));
		r1.getScope().add(a);
		revisions.getRevision().add(r1);
       
		Revision r2 = xfRevision.build(2, new Date());
		Scope b = XmlScopeFactory.build(2, "b");
		Scope c = XmlScopeFactory.build(3, "c");
		b.getChange().add(XmlChangeFactory.build(1, "b1"));
		b.getChange().add(XmlChangeFactory.build(2, "b2"));
		c.getChange().add(XmlChangeFactory.build(3, "c3"));
		r2.getScope().add(b);
		r2.getScope().add(c);
		revisions.getRevision().add(r2);
		
		Revision r3 = xfRevision.build(3, new Date());
		Scope s4 = XmlScopeFactory.build(1, "a");
		s4.getChange().add(XmlChangeFactory.build(1, "a1"));
		s4.getChange().add(XmlChangeFactory.build(2, "a2"));
		r3.getScope().add(s4);
		revisions.getRevision().add(r3);
	}

    @Test
    public void nrOfScopes()
    {
        List<Scope> actual = asp.group(list);
        Assert.assertEquals(3, actual.size());
    }

    @Test
    public void childs()
    {
        List<Scope> actual = asp.group(list);
        for(Scope s : actual)
        {
            Assert.assertTrue(s.isSetChange());
        }
        Revision r = new Revision();
        r.getScope().addAll(actual);
        JaxbUtil.info(r);
        Assert.assertEquals(3, actual.size());
    }
    
    @Test @Ignore
    public void ordering()
    {
    	int i=0;
    	List<Scope> actual = asp.group(list);
    	for(Scope s : actual)
    	{
    		for(Change c : s.getChange())
    		{
    			Assert.assertEquals("Element i="+i,list.get(i), c);
    			i++;
    		}
    	}
    }
    
    @Test public void flatSize()
    {
    	Scope scope = XmlScopeFactory.build(asp.flat(revisions.getRevision()));
    	Assert.assertEquals(7, scope.getChange().size());
    }
    
    public void flat()
    {
    	JaxbUtil.info(revisions);
    	Scope scope = XmlScopeFactory.build(asp.flat(revisions.getRevision()));
    	JaxbUtil.info(scope);
    }

    public static void main (String[] args) throws Exception
	{
		JeeslUtilTestBootstrap.init();
		TestAuditScopeProcessor test = new TestAuditScopeProcessor();
		
//		test.init();test.nrOfScopes();
//		test.init();test.childs();
		test.init();test.ordering();
//		test.init();test.flat();
	} 
}