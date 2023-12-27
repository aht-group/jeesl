package org.jeesl.web.rest.module;

import org.jeesl.api.facade.module.JeeslTrainingFacade;
import org.jeesl.api.rest.rs.module.training.JeeslTrainingRestExport;
import org.jeesl.api.rest.rs.module.training.JeeslTrainingRestImport;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.xsd.Container;
import org.jeesl.web.rest.AbstractJeeslRestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jeesl.model.xml.io.ssi.sync.DataUpdate;

public class TrainingRestService <L extends JeeslLang,D extends JeeslDescription,
									TYPE extends JeeslStatus<L,D,TYPE>>
					extends AbstractJeeslRestHandler<L,D>
					implements JeeslTrainingRestExport,JeeslTrainingRestImport
{
	final static Logger logger = LoggerFactory.getLogger(TrainingRestService.class);
	
	private JeeslTrainingFacade<L,D,TYPE> fTraining;
	
	private final Class<TYPE> cType;
	
	private TrainingRestService(JeeslTrainingFacade<L,D,TYPE> fTraining,final Class<L> cL, final Class<D> cD, final Class<TYPE> cType)
	{
		super(fTraining,cL,cD);
		this.fTraining=fTraining;
		
		this.cType=cType;
	}
	
	public static <L extends JeeslLang,D extends JeeslDescription,
							TYPE extends JeeslStatus<L,D,TYPE>>
			TrainingRestService<L,D,TYPE>
			factory(JeeslTrainingFacade<L,D,TYPE> fTraining,final Class<L> cL, final Class<D> cD, final Class<TYPE> cType)
	{
		return new TrainingRestService<L,D,TYPE>(fTraining,cL,cD,cType);
	}
	
	@Override public Container exportSystemTrainingSlotType() {return xfContainer.build(fTraining.allOrderedPosition(cType));}

	@Override public DataUpdate importSystemTrainingSlotType(Container container){return importStatus(cType,container,null);}
}