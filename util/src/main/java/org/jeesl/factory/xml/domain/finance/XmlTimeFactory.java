package org.jeesl.factory.xml.domain.finance;

import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.controller.util.poi.PoiRowColNumerator;
import net.sf.ahtutils.controller.util.poi.PoiSsCellType;
import net.sf.ahtutils.exception.processing.UtilsProcessingException;
import net.sf.ahtutils.xml.finance.Figures;
import net.sf.ahtutils.xml.finance.Time;
import net.sf.exlp.util.DateUtil;

public class XmlTimeFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlTimeFactory.class);
	
	public static <E extends Enum<E>> void add(Figures figures, E code, Date value)
	{
		if(value!=null){figures.getTime().add(XmlTimeFactory.build(code, value));}
	}
	
	public static <E extends Enum<E>> Time build(E code, Date record){return create(code.toString(),record);}
	public static Time create(String code, Date record)
	{
		Time xml = new Time();
		xml.setCode(code);
		xml.setRecord(DateUtil.getXmlGc4D(record));
		return xml;
	}
	
	public static Time nr(int nr, Date record)
	{
		Time xml = new Time();
		xml.setNr(nr);
		xml.setRecord(DateUtil.getXmlGc4D(record));
		return xml;
	}
	
	public static Time create(Sheet sheet, int row, String col, String code, String label) throws UtilsProcessingException
	{
		return create(sheet, row, PoiRowColNumerator.translateNameToIndex(col), code, label);
	}
	public static Time create(Sheet sheet, int row, int col, String code, String label) throws UtilsProcessingException
	{
		Cell cell = sheet.getRow(row).getCell(col);
		if(cell==null)
		{
			throw new UtilsProcessingException("The cell is null. No Date in "+PoiRowColNumerator.create(row, col));
		}
		else if(cell.getCellType()!=Cell.CELL_TYPE_NUMERIC)
		{
			StringBuffer sb = new StringBuffer();
			sb.append(XmlTimeFactory.class.getSimpleName());
			sb.append(": Cell ");
			if(label!=null){sb.append("(").append(label).append(") ");}
			sb.append(PoiRowColNumerator.create(row, col));
			sb.append(" has wrong CellType.");
			sb.append(" Expected: ").append(PoiSsCellType.translate(0));
			sb.append(" Actual:").append(PoiSsCellType.translate(cell.getCellType()));
			throw new UtilsProcessingException(sb.toString());
		}
		else
		{
			Time t = XmlTimeFactory.create(code, cell.getDateCellValue());
			t.setLabel(label);
			return t;
		}
	}
}