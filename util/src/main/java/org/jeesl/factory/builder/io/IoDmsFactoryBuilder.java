package org.jeesl.factory.builder.io;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.io.dms.EjbIoDmsDocumentFactory;
import org.jeesl.factory.ejb.io.dms.EjbIoDmsFactory;
import org.jeesl.factory.ejb.io.dms.EjbIoDmsSectionFactory;
import org.jeesl.factory.ejb.io.dms.EjbIoDmsViewFactory;
import org.jeesl.interfaces.model.io.dms.JeeslIoDms;
import org.jeesl.interfaces.model.io.dms.JeeslIoDmsDocument;
import org.jeesl.interfaces.model.io.dms.JeeslIoDmsLayer;
import org.jeesl.interfaces.model.io.dms.JeeslIoDmsSection;
import org.jeesl.interfaces.model.io.dms.JeeslIoDmsView;
import org.jeesl.interfaces.model.io.fr.JeeslFileStorage;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoDmsFactoryBuilder<L extends JeeslLang,D extends JeeslDescription,LOC extends JeeslStatus<L,D,LOC>,
								DMS extends JeeslIoDms<L,D,STORAGE,?,?,SECTION>,
								STORAGE extends JeeslFileStorage<L,D,?,?,?>,
								SECTION extends JeeslIoDmsSection<L,D,SECTION>,
								FILE extends JeeslIoDmsDocument<L,SECTION,?,?>,
								VIEW extends JeeslIoDmsView<L,D,DMS>,
								LAYER extends JeeslIoDmsLayer<VIEW,?>>
				extends AbstractFactoryBuilder<L,D>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(IoDmsFactoryBuilder.class);

	private final Class<LOC> cLoc; public Class<LOC> getClassLocale() {return cLoc;}
	private final Class<DMS> cDms; public Class<DMS> getClassDms() {return cDms;}
	private final Class<STORAGE> cStorage; public Class<STORAGE> getClassStorage() {return cStorage;}
	private final Class<SECTION> cSection; public Class<SECTION> getClassSection() {return cSection;}
	private final Class<FILE> cFile; public Class<FILE> getClassFile() {return cFile;}
	private final Class<VIEW> cView; public Class<VIEW> getClassView() {return cView;}
    
	public IoDmsFactoryBuilder(final Class<L> cL, final Class<D> cD, final Class<LOC> cLoc, final Class<DMS> cDms, final Class<STORAGE> cStorage, final Class<SECTION> cSection, final Class<FILE> cFile, final Class<VIEW> cView)
	{
		super(cL,cD);
		this.cLoc=cLoc;
		this.cDms=cDms;
		this.cStorage=cStorage;

		this.cSection=cSection;
		this.cFile=cFile;
		this.cView=cView;
	}
	
	public EjbIoDmsFactory<DMS> ejbDms()
	{
		return new EjbIoDmsFactory<DMS>(cDms);
	}
	
	public EjbIoDmsSectionFactory<SECTION> ejbSection()
	{
		return new EjbIoDmsSectionFactory<SECTION>(cSection);
	}
	
	public EjbIoDmsDocumentFactory<SECTION,FILE> ejbFile()
	{
		return new EjbIoDmsDocumentFactory<SECTION,FILE>(cFile);
	}
	
	public EjbIoDmsViewFactory<DMS,VIEW> ejbView()
	{
		return new EjbIoDmsViewFactory<DMS,VIEW>(cView);
	}
}