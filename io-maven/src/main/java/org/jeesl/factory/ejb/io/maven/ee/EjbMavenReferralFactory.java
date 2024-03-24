package org.jeesl.factory.ejb.io.maven.ee;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.jeesl.factory.ejb.util.EjbPositionFactory;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenVersion;
import org.jeesl.model.ejb.io.maven.ee.IoMavenEeEdition;
import org.jeesl.model.ejb.io.maven.ee.IoMavenEeReferral;
import org.jeesl.model.ejb.io.maven.ee.IoMavenEeStandard;
import org.jeesl.model.pojo.map.generic.Nested2Map;

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
	
	public static Nested2Map<IoMavenEeStandard,IoMavenEeEdition,IoMavenEeReferral> toN2mStanardEdition(List<IoMavenEeReferral> list)
	{
		Nested2Map<IoMavenEeStandard,IoMavenEeEdition,IoMavenEeReferral> n2m = new Nested2Map<>();
		for(IoMavenEeReferral ejb : list) {n2m.put(ejb.getStandard(),ejb.getEdition(),ejb);}
		return n2m;
	}
	
	public static Nested2Map<IoMavenVersion,IoMavenEeStandard,IoMavenEeReferral> toN2mVersionStanard(List<IoMavenEeReferral> list)
	{
		Nested2Map<IoMavenVersion,IoMavenEeStandard,IoMavenEeReferral> n2m = new Nested2Map<>();
		for(IoMavenEeReferral ejb : list) {n2m.put(ejb.getArtifact(),ejb.getStandard(),ejb);}
		return n2m;
	}
	
	public static List<IoMavenVersion> toVersions(List<IoMavenEeReferral> list)
	{
		Set<IoMavenVersion> set = new HashSet<>();
		for(IoMavenEeReferral ejb : list) {set.add(ejb.getArtifact());}
		return new ArrayList<>(set);
	}
	
	public static List<IoMavenEeEdition> toEeEditions(List<IoMavenEeReferral> list)
	{
		Set<IoMavenEeEdition> set = new HashSet<>();
		for(IoMavenEeReferral ejb : list) {set.add(ejb.getEdition());}
		return new ArrayList<>(set);
	}
	
	public static List<IoMavenEeStandard> toEeStandards(List<IoMavenEeReferral> list)
	{
		Set<IoMavenEeStandard> set = new HashSet<>();
		for(IoMavenEeReferral ejb : list) {set.add(ejb.getStandard());}
		return new ArrayList<>(set);
	}
}