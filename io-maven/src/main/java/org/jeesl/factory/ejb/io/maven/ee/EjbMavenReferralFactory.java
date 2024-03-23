package org.jeesl.factory.ejb.io.maven.ee;

import java.util.List;
import java.util.Objects;

import org.jeesl.factory.ejb.util.EjbPositionFactory;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.model.ejb.io.maven.ee.IoMavenEeEdition;
import org.jeesl.model.ejb.io.maven.ee.IoMavenEeReferral;
import org.jeesl.model.ejb.io.maven.ee.IoMavenEeStandard;

public class EjbMavenReferralFactory
{
	public static IoMavenEeReferral build(IoMavenEeEdition edition, IoMavenEeStandard standard, List<IoMavenEeReferral> list)
	{
		IoMavenEeReferral ejb = new IoMavenEeReferral();
		ejb.setEdition(edition);
		ejb.setStandard(standard);
		EjbPositionFactory.next(ejb,list);
		return ejb;
	}
	
	public static void converter(JeeslFacade facade, IoMavenEeReferral ejb)
	{
		EjbMavenReferralFactory.recommendation(ejb);
		if(Objects.nonNull(ejb.getEdition())) {ejb.setEdition(facade.find(IoMavenEeEdition.class,ejb.getEdition()));}
	}
	
	public static void recommendation(IoMavenEeReferral ejb)
	{
		if(ejb.getPosition()==1) {ejb.setRecommendation(true);}
		else {ejb.setRecommendation(null);}
	}
}