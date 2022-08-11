package org.jeesl.factory.ejb.io.label;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.ejb.system.status.EjbDescriptionFactory;
import org.jeesl.factory.ejb.system.status.EjbLangFactory;
import org.jeesl.factory.ejb.util.EjbPositionFactory;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionCategory;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.label.er.JeeslRevisionDiagram;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionEntityMapping;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionView;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionViewMapping;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.system.revision.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.io.StringUtil;

public class EjbLabelEntityFactory<L extends JeeslLang,D extends JeeslDescription,
									RC extends JeeslRevisionCategory<L,D,RC,?>,
									RV extends JeeslRevisionView<L,D,RVM>,
									RVM extends JeeslRevisionViewMapping<RV,RE,REM>,
									
									RE extends JeeslRevisionEntity<L,D,RC,REM,RA,ERD>,
									REM extends JeeslRevisionEntityMapping<?,?,RE>,
									RA extends JeeslRevisionAttribute<L,D,RE,RER,RAT>, RER extends JeeslStatus<L,D,RER>,
									RAT extends JeeslStatus<L,D,RAT>,
									ERD extends JeeslRevisionDiagram<L,D,RC>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbLabelEntityFactory.class);
	
	final Class<RE> cEntity;
	
	private EjbLangFactory<L> efLang;
	private EjbDescriptionFactory<D> efDescription;
    
	public EjbLabelEntityFactory(final Class<L> cL,final Class<D> cD,final Class<RE> cEntity)
	{       
        this.cEntity = cEntity;
		efLang = EjbLangFactory.instance(cL);
		efDescription = EjbDescriptionFactory.factory(cD);
	}
	
	public RE build(RC category, Entity xml)
	{
		RE ejb = build(category,new ArrayList<RE>());
		ejb.setCode(xml.getCode());
		ejb.setPosition(xml.getPosition());
		try
		{
			ejb.setName(efLang.getLangMap(xml.getLangs()));
			ejb.setDescription(efDescription.create(xml.getDescriptions()));
		}
		catch (JeeslConstraintViolationException e) {e.printStackTrace();}
		return ejb;
	}
    
	public RE build(RC category, List<RE> list)
	{
		RE ejb = null;
		try
		{
			ejb = cEntity.newInstance();
			ejb.setCategory(category);
			ejb.setVisible(true);
			ejb.setTimeseries(false);
			ejb.setDocumentation(false);
			EjbPositionFactory.next(ejb, list);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public void applyValues(RE ejb, Entity xml)
	{
		if(xml.isSetRemark()){ejb.setDeveloperInfo(xml.getRemark().getValue());}
		else{ejb.setDeveloperInfo(null);}
		
		ejb.setPosition(xml.getPosition());
	}
	
	public List<ERD> toDiagrams(List<RE> entities)
	{
		Set<ERD> sDiagrams = new HashSet<>();
		for(RE re : entities) {if(re.getDiagram()!=null && !sDiagrams.contains(re.getDiagram())) {sDiagrams.add(re.getDiagram());}}
		return new ArrayList<ERD>(sDiagrams);
	}
	
	
	public void applyJscn(JeeslFacade facade, List<RE> list)
	{
		List<RE> jscn = new ArrayList<>();
		for(RE re : list)
		{
			if(re.getJscn()==null) {jscn.add(re);}
		}
		logger.info("applyJscn re:"+list.size()+" jscn:"+jscn.size());
		
		if(!jscn.isEmpty())
		{
			logger.info("Applying JSDN for "+jscn.size());
			for(RE re : list)
			{
				try
				{
					Class<?> c = Class.forName(re.getCode());
					re.setJscn(c.getSimpleName());
					facade.save(re);
				}
				catch (ClassNotFoundException e) {logger.error(e.getMessage());}
				catch (JeeslConstraintViolationException e) {logger.error(e.getMessage());}
				catch (JeeslLockingException e) {logger.error(e.getMessage());}
			}
			
		}
	}
	
	public boolean checkMisconfiguration(RE ejb)
	{
		boolean isClassNotFound = false;
		boolean isDiagramMissing = false;
		boolean isCategoryMissing = false;
		boolean isCategoryMismatch = false;
		boolean isJscnMismatch = false;
		
		try
		{
			Class<?> c = Class.forName(ejb.getCode());
			isJscnMismatch = !c.getSimpleName().equals(ejb.getJscn());
		}
		catch (ClassNotFoundException e) {isClassNotFound = true;}
		
		if(ejb.getCategory()==null) {isCategoryMissing = true;}
		{
			if(ejb.getDiagram()==null){isDiagramMissing = true;}
			else
			{
				isCategoryMismatch = (ejb.getCategory()!=ejb.getDiagram().getCategory());
			}
		}
		
		boolean isMisconfigured = (isClassNotFound || isDiagramMissing || isCategoryMissing || isCategoryMismatch || isJscnMismatch);
		
		if(isMisconfigured)
		{
			logger.info(StringUtil.stars());
			logger.info(ejb.getCode());
			logger.info("isClassNotFound: "+isClassNotFound);
			logger.info("isDiagramMissing: "+isDiagramMissing);
			logger.info("isCategoryMissing: "+isCategoryMissing);
			logger.info("isCategoryMismatch: "+isCategoryMismatch);
			logger.info("isJscnMismatch: "+isJscnMismatch);
		}
		
		return isMisconfigured;
	}
}