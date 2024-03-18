package org.jeesl.factory.ejb.io.maven.ee;

import java.util.Objects;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.model.ejb.io.maven.ee.IoMavenEeEdition;
import org.jeesl.model.ejb.io.maven.ee.IoMavenEeReferral;

public class EjbMavenReferralFactory
{
	public static IoMavenEeReferral build()
	{
		IoMavenEeReferral ejb = new IoMavenEeReferral();
		
		return ejb;
	}
	
	public static void converter(JeeslFacade facade, IoMavenEeReferral ejb)
	{
		if(Objects.nonNull(ejb.getEdition())) {ejb.setEdition(facade.find(IoMavenEeEdition.class,ejb.getEdition()));}
	}
}