package org.jeesl.factory.xml.system.io.report;

import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.jeesl.interfaces.model.io.report.data.JeeslReportTemplate;
import org.jeesl.interfaces.model.io.report.style.JeeslReportStyle;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportCell;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumn;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumnGroup;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportRow;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportSheet;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportWorkbook;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLight;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.report.DataAssociation;
import static org.hibernate.criterion.Projections.property;
import org.jeesl.api.controller.ImportStrategy;
import org.jeesl.api.controller.ValidationStrategy;


public class XmlDataAssociationFactory <L extends JeeslLang,D extends JeeslDescription,
								CATEGORY extends JeeslStatus<L,D,CATEGORY>,
								REPORT extends JeeslIoReport<L,D,CATEGORY,WORKBOOK>,
								IMPLEMENTATION extends JeeslStatus<L,D,IMPLEMENTATION>,
								WORKBOOK extends JeeslReportWorkbook<REPORT,SHEET>,
								SHEET extends JeeslReportSheet<L,D,IMPLEMENTATION,WORKBOOK,GROUP,ROW>,
								GROUP extends JeeslReportColumnGroup<L,D,SHEET,COLUMN,STYLE>,
								COLUMN extends JeeslReportColumn<L,D,GROUP,STYLE,CDT,CW,TLS>,
								ROW extends JeeslReportRow<L,D,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS>,
								TEMPLATE extends JeeslReportTemplate<L,D,CELL>,
								CELL extends JeeslReportCell<L,D,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS>,
								STYLE extends JeeslReportStyle<L,D>,CDT extends JeeslStatus<L,D,CDT>,CW extends JeeslStatus<L,D,CW>,
								RT extends JeeslStatus<L,D,RT>,
								ENTITY extends EjbWithId,
								ATTRIBUTE extends EjbWithId,
								TL extends JeeslTrafficLight<L,D,TLS>,
								TLS extends JeeslStatus<L,D,TLS>,
								FILLING extends JeeslStatus<L,D,FILLING>,
								TRANSFORMATION extends JeeslStatus<L,D,TRANSFORMATION>
								>
{
    final static Logger logger = LoggerFactory.getLogger(XmlDataAssociationFactory.class);

    /**
    * Add a simple data association to import property from column.
    * @param column The column nr in spreadsheet, starting with 0
    * @param property The property of the target class
    * @return Initialized data association
    */
    public static DataAssociation buildSimpleAssociation(int column, String property)
    {
        DataAssociation association = new DataAssociation();
        association.setColumn("" +column);
        association.setProperty(property);
        return association;
    }

    /**
    * Add a data association for import of property in given column that is handled by the given class.
    * @param column The column nr in spreadsheet, starting with 0
    * @param property The property of the target class
    * @param handler The handler class that implements the ImportStrategy interface
    * @return Initialized data association
    */
    public static DataAssociation buildHandledAssociation(int column, String property, Class<? extends ImportStrategy> handler)
    {
        DataAssociation association = new DataAssociation();
        association.setColumn("" +column);
        association.setProperty(property);
        association.setHandledBy(handler.getName());
        return association;
    }
    
    /**
    * Add a validation by the given class.
    * @param handler The handler class that implements the ValidationStrategy interface
    * @return Data association with validation parameter set
    */
    public static DataAssociation addValidation(DataAssociation association, Class<? extends ValidationStrategy> handler)
    {
	association.setValidatedBy(handler.getName());
        return association;
    }
}