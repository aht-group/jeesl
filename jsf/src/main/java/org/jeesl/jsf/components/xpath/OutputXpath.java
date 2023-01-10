package org.jeesl.jsf.components.xpath;

import java.io.IOException;

import javax.el.ValueExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathNotFoundException;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumn;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.jsf.util.ComponentAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FacesComponent("org.jeesl.jsf.components.xpath.OutputXpath")
@SuppressWarnings("rawtypes")
public class OutputXpath extends AbstractXpath
{	
	final static Logger logger = LoggerFactory.getLogger(OutputXpath.class);
	private enum Properties {value,xpath,column,styleClass,localeCode}
	
	private static boolean debugOnInfo = false;
	private static String token = "$localeCode$";
	
	@Override
	public void encodeBegin(FacesContext context) throws IOException
	{
		context.getResponseWriter().startElement("span", this);
		context.getResponseWriter().writeAttribute("class",ComponentAttribute.get(Properties.styleClass, "", context, this),null);
	}

	@Override
	public void encodeEnd(FacesContext context) throws IOException
	{
		context.getResponseWriter().endElement("span");
	}
	
	@Override
	public void encodeChildren(FacesContext context) throws IOException
	{	
		String xpath = ComponentAttribute.get(Properties.xpath,"",context,this);
		String localeCode = ComponentAttribute.get(Properties.localeCode, null, context, this);
		if(localeCode!=null && xpath.contains(token))
		{
			xpath=xpath.replace(token, localeCode);
		}
		if(debugOnInfo) {logger.info("localeCode: "+(localeCode!=null)+" "+xpath);}
				
		ValueExpression ve = this.getValueExpression(Properties.value.toString());
		JXPathContext ctx = JXPathContext.newContext(ve.getValue(context.getELContext()));
		
		StringBuilder sbValue = new StringBuilder();

		try
		{
			addStringValue(context,ctx,xpath,sbValue);
		}
		catch (JXPathNotFoundException ex){}
		context.getResponseWriter().write(sbValue.toString());
		
		for(UIComponent uic : this.getChildren())
		{
			uic.encodeAll(context);
		}
	}
	
	private void addStringValue(FacesContext context, JXPathContext ctx, String xpath, StringBuilder sbValue)
	{
		Object value = ctx.getValue(xpath);
//		logger.info("Object: Type "+value.getClass().getName());
		
		Object oColumn = ComponentAttribute.getObject(Properties.column,null,context,this);
		if(oColumn!=null && JeeslReportColumn.class.isAssignableFrom(oColumn.getClass()))
		{
			JeeslReportColumn c = (JeeslReportColumn)oColumn;
//			logger.info(c.getClass().getName()+" "+c.toString()+" "+c.getCode());
			if(c.getDataType()!=null)
			{
				JeeslStatus dt = c.getDataType();
//				logger.info("   DataType  code:"+dt.getCode()+" style:"+dt.getStyle()+" symbol:"+dt.getSymbol());
				
				if(dt.getCode().startsWith("numberDouble"))
				{
					sbValue.append(super.getDecimalFormat(dt.getSymbol()).format(value));
				}
				else if(dt.getCode().equals("date"))
				{						
					if(value instanceof XMLGregorianCalendar)
					{
						XMLGregorianCalendar xmlGc = (XMLGregorianCalendar)value;
						sbValue.append(super.getSimpleDateFormat(c,dt).format(xmlGc.toGregorianCalendar().getTime()));
					}
					else {logger.warn("Fallback, XMLGC");sbValue.append(value);}
				}
				else
				{
					logger.warn("NYI Handling of "+dt.getCode());sbValue.append(value);
				}
			}
			else{logger.warn("DataType of "+JeeslReportColumn.class.getSimpleName()+" NULL");}
		}
		else
		{
			if(debugOnInfo) {logger.warn("Fallback, column==null or not assignable");}
			sbValue.append(value);
		}
	}
}