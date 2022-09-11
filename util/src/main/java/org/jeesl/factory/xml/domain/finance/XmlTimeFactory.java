package org.jeesl.factory.xml.domain.finance;

import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.jeesl.exception.processing.UtilsProcessingException;
import org.jeesl.factory.xls.system.io.report.XlsCellFactory;
import org.jeesl.factory.xls.system.io.report.XlsColumnFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		xml.setRecord(DateUtil.toXmlGc(record));
		return xml;
	}
	
	public static Time nr(int nr, Date record)
	{
		Time xml = new Time();
		xml.setNr(nr);
		xml.setRecord(DateUtil.toXmlGc(record));
		return xml;
	}
	
	public static Time create(Sheet sheet, int row, String col, String code, String label) throws UtilsProcessingException
	{
		return create(sheet, row, XlsColumnFactory.code2index(col), code, label);
	}
	public static Time create(Sheet sheet, int row, int col, String code, String label) throws UtilsProcessingException
	{
		Cell cell = sheet.getRow(row).getCell(col);
		if(cell==null)
		{
			throw new UtilsProcessingException("The cell is null. No Date in "+XlsCellFactory.debugPosition(row, col));
		}
		else if(cell.getCellTypeEnum()!=CellType.NUMERIC)
		{
			StringBuffer sb = new StringBuffer();
			sb.append(XmlTimeFactory.class.getSimpleName());
			sb.append(": Cell ");
			if(label!=null){sb.append("(").append(label).append(") ");}
			sb.append(XlsCellFactory.debugPosition(row, col));
			sb.append(" has wrong CellType.");
			sb.append(" Expected: ").append(CellType.NUMERIC);//.append(PoiSsCellType.translate(0));
			sb.append(" Actual:").append(cell.getCellTypeEnum());
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