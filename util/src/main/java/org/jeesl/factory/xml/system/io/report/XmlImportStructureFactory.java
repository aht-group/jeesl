package org.jeesl.factory.xml.system.io.report;

import java.util.List;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.jeesl.interfaces.model.io.report.JeeslIoReportCategory;
import org.jeesl.interfaces.model.io.report.col.JeeslReportCellType;
import org.jeesl.interfaces.model.io.report.row.JeeslReportRow;
import org.jeesl.interfaces.model.io.report.row.JeeslReportRowType;
import org.jeesl.interfaces.model.io.report.row.JeeslReportTemplate;
import org.jeesl.interfaces.model.io.report.style.JeeslReportStyle;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportCell;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumn;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumnGroup;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportSheet;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportWorkbook;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLight;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLightScope;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.xml.io.report.DataAssociation;
import org.jeesl.model.xml.io.report.DataAssociations;
import org.jeesl.model.xml.io.report.ImportStructure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlImportStructureFactory <L extends JeeslLang,D extends JeeslDescription,
								CATEGORY extends JeeslIoReportCategory<L,D,CATEGORY,?>,
								REPORT extends JeeslIoReport<L,D,CATEGORY,WORKBOOK>,
								IMPLEMENTATION extends JeeslStatus<L,D,IMPLEMENTATION>,
								WORKBOOK extends JeeslReportWorkbook<REPORT,SHEET>,
								SHEET extends JeeslReportSheet<L,D,IMPLEMENTATION,WORKBOOK,GROUP,ROW>,
								GROUP extends JeeslReportColumnGroup<L,D,SHEET,COLUMN,STYLE>,
								COLUMN extends JeeslReportColumn<L,D,GROUP,STYLE,CDT,CW,TLS>,
								ROW extends JeeslReportRow<L,D,SHEET,TEMPLATE,CDT,RT>,
								TEMPLATE extends JeeslReportTemplate<L,D,CELL>,
								CELL extends JeeslReportCell<L,D,TEMPLATE>,
								STYLE extends JeeslReportStyle<L,D,?>,
								CDT extends JeeslReportCellType<L,D,CDT,?>,
								CW extends JeeslStatus<L,D,CW>,
								RT extends JeeslReportRowType<L,D,RT,?>,
								ENTITY extends EjbWithId,
								ATTRIBUTE extends EjbWithId,
								TL extends JeeslTrafficLight<L,D,TLS>,
								TLS extends JeeslTrafficLightScope<L,D,TLS,?>,
								FILLING extends JeeslStatus<L,D,FILLING>,
								TRANSFORMATION extends JeeslStatus<L,D,TRANSFORMATION>
								>
{
    final static Logger logger = LoggerFactory.getLogger(XmlImportStructureFactory.class);

    /*
    * Createsan import structure for the given target class and the data associations.
    * @param targetClass The class to hold the properties to be imported from spreadsheet
    * @param associations Definitions of what information will be imported to which property using which strategy and validation method
    * @return Initialized import structure
    */
    public static ImportStructure build(Class targetClass, List<DataAssociation> associations) throws InstantiationException, IllegalAccessException
    {
        ImportStructure structure = new ImportStructure();
        DataAssociations associationsContainer = XmlDataAssociationsFactory.build();

        // Validate existence of given property in targetclass
        PropertyUtilsBean propertyUtil = new PropertyUtilsBean();
        Object test = targetClass.newInstance();
        for (DataAssociation association : associations)
        {
            associationsContainer.getDataAssociation().add(association);
            if (propertyUtil.isWriteable(test, association.getProperty())) 
            {
                if (logger.isTraceEnabled()){logger.trace("Added " +association.getProperty() + " to be imported from column " +association.getColumn());}
            }
            else
            {
                if (logger.isTraceEnabled()){logger.warn("The property " +association.getProperty() +" is not a writable field of " +targetClass.getName() +". Please fix your configuration!");}
            }
        }

        structure.setTargetClass(targetClass.getName());
        structure.setDataAssociations(associationsContainer);
        return structure;
    }
}