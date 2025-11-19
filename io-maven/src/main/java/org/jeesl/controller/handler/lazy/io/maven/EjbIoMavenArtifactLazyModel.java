package org.jeesl.controller.handler.lazy.io.maven;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.api.facade.io.JeeslIoMavenFacade;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenArtifact;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenGroup;
import org.jeesl.jsf.util.JeeslLazyListHandler;
import org.jeesl.jsf.util.PrimefacesPredicateBuilder;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenArtifact;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenDependency;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenGroup;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenMaintainer;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenOutdate;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenScope;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenVersion;
import org.jeesl.model.ejb.io.maven.ee.IoMavenEeReferral;
import org.jeesl.model.ejb.io.maven.module.IoMavenModule;
import org.jeesl.model.ejb.io.maven.module.IoMavenStructure;
import org.jeesl.model.ejb.io.maven.module.IoMavenType;
import org.jeesl.model.ejb.io.maven.usage.IoMavenUsage;
import org.jeesl.util.query.cq.CqOrdering;
import org.jeesl.util.query.ejb.io.maven.EjbIoMavenQuery;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIoMavenArtifactLazyModel extends LazyDataModel<IoMavenArtifact>
{
	final static Logger logger = LoggerFactory.getLogger(EjbIoMavenArtifactLazyModel.class);
	private static final long serialVersionUID = 1L;

	private final JeeslIoMavenFacade<IoMavenGroup,IoMavenArtifact,IoMavenVersion,IoMavenDependency,IoMavenScope,IoMavenOutdate,IoMavenMaintainer,IoMavenModule,IoMavenStructure,IoMavenType,IoMavenUsage,IoMavenEeReferral> fMaven;

	private final JeeslLazyListHandler<IoMavenArtifact> llh;
	
	public EjbIoMavenArtifactLazyModel(JeeslIoMavenFacade<IoMavenGroup,IoMavenArtifact,IoMavenVersion,IoMavenDependency,IoMavenScope,IoMavenOutdate,IoMavenMaintainer,IoMavenModule,IoMavenStructure,IoMavenType,IoMavenUsage,IoMavenEeReferral> fMaven)
	{
//		super(new IoMavenVersionConverter());
		this.fMaven=fMaven;
		
		llh = new JeeslLazyListHandler<>();
	}
	
	@Override public IoMavenArtifact getRowData(String rowKey) {return llh.getRowData(rowKey);}
    @Override public Object getRowKey(IoMavenArtifact item) {return llh.getRowKey(item);}

	private EjbIoMavenQuery query(Map<String,FilterMeta> filterBy)
	{
		EjbIoMavenQuery query = EjbIoMavenQuery.instance();
		PrimefacesPredicateBuilder.apply(filterBy,query);		
		return query;
	}
	
//	@Override
	public int count(Map<String,FilterMeta> filterBy) {return fMaven.cIoMavenVersions(query(filterBy)).intValue();}
	@Override public List<IoMavenArtifact> load(int first, int pageSize, Map<String,SortMeta> sortBy, Map<String,FilterMeta> filterBy)
	{
		EjbIoMavenQuery q = query(filterBy);
		q.setFirstResult(first);
		q.maxResults(pageSize);
		
		if(ObjectUtils.isEmpty(sortBy))
		{
			q.orderBy(CqOrdering.ascending(JeeslIoMavenArtifact.Attributes.group,JeeslIoMavenGroup.Attributes.code));
			q.orderBy(CqOrdering.ascending(JeeslIoMavenArtifact.Attributes.code));
		}
		
		super.setRowCount(this.count(filterBy));
		
		List<IoMavenArtifact> list = fMaven.fIoMavenArtifacts(q);
		llh.getTmp().clear();
		llh.getTmp().addAll(list);
		
		return list;
	}
}