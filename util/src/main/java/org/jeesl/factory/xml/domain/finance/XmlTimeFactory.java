package org.jeesl.factory.xml.domain.finance;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.exlp.util.system.DateUtil;
import org.jeesl.exception.processing.UtilsProcessingException;
import org.jeesl.factory.xlsx.io.report.XlsCellFactory;
import org.jeesl.factory.xlsx.io.report.XlsColumnFactory;
import org.jeesl.model.xml.module.finance.Figures;
import org.jeesl.model.xml.module.finance.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlTimeFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlTimeFactory.class);
	
	public static <E extends Enum<E>> void add(Figures figures, E code, Date value)
	{
		if(Objects.nonNull(value)){figures.getTime().add(XmlTimeFactory.build(code, value));}
	}
	public static <E extends Enum<E>> void add(Figures figures, E code, LocalDate value)
	{
		if(Objects.nonNull(value)){figures.getTime().add(XmlTimeFactory.build(code, value));}
	}
	
	public static <E extends Enum<E>> Time build(E code, Date record) {return create(code.toString(),record);}
	public static Time create(String code, Date record)
	{
		Time xml = new Time();
		xml.setCode(code);
		xml.setRecord(DateUtil.toXmlGc(record));
		return xml;
	}
	
	public static <E extends Enum<E>> Time build(E code, LocalDateTime record)
	{
		Time xml = new Time();
		xml.setCode(code.toString());
		xml.setRecord(DateUtil.toXmlGc(record));
		return xml;
	}
	public static <E extends Enum<E>> Time build(E code, LocalDate record)
	{
		Time xml = new Time();
		xml.setCode(code.toString());
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
		else if(cell.getCellType()!=CellType.NUMERIC)
		{
			StringBuffer sb = new StringBuffer();
			sb.append(XmlTimeFactory.class.getSimpleName());
			sb.append(": Cell ");
			if(label!=null){sb.append("(").append(label).append(") ");}
			sb.append(XlsCellFactory.debugPosition(row, col));
			sb.append(" has wrong CellType.");
			sb.append(" Expected: ").append(CellType.NUMERIC);//.append(PoiSsCellType.translate(0));
			sb.append(" Actual:").append(cell.getCellType());
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