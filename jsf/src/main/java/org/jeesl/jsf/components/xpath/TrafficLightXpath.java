package org.jeesl.jsf.components.xpath;

import java.io.IOException;

import javax.el.ValueExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathNotFoundException;
import org.jeesl.factory.css.CssAlignmentFactory;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumn;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.jsf.util.ComponentAttribute;
import org.jeesl.jsf.util.ComponentAttributeProcessor;
import org.jeesl.jsf.util.TrafficLightProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FacesComponent("org.jeesl.jsf.components.xpath.TrafficLightXpath")
@SuppressWarnings("rawtypes")
public class TrafficLightXpath extends AbstractXpath
{	
	final static Logger logger = LoggerFactory.getLogger(TrafficLightXpath.class);
	private static enum Properties {value,xpath,xpathref,column,style,styleClass}
		
	@Override
	public void encodeBegin(FacesContext context) throws IOException
	{
		context.getResponseWriter().startElement("div", this);
	}

	@Override
	public void encodeEnd(FacesContext context) throws IOException
	{
		context.getResponseWriter().endElement("div");
	}
	
	@Override
	public void encodeChildren(FacesContext context) throws IOException
	{		
		String xpath = ComponentAttribute.get(Properties.xpath,"",context,this);
		String xpathref = ComponentAttribute.get(Properties.xpathref,"",context,this);
		
		ValueExpression ve = this.getValueExpression(Properties.value.toString());
		JXPathContext xpathContext = JXPathContext.newContext(ve.getValue(context.getELContext()));
		
		StringBuilder sbStyle = new StringBuilder();
		ComponentAttributeProcessor.defaultStyle(sbStyle, this.getAttributes());
		
		StringBuilder sbValue = new StringBuilder("");

		Double refValue = null;
		if(xpathref!=null && xpathref.length()>0)
		{
			try
			{
				Object o = xpathContext.getValue(xpathref);
				if(o instanceof Double) {refValue = (Double)o;}
				else if(o instanceof Integer) {refValue = ((Integer)o).doubleValue();}
			}
			catch (JXPathNotFoundException ex){}
		}
		
		try
		{
			Object value = xpathContext.getValue(xpath);
//			StringBuilder sbDebug = new StringBuilder();
//			sbDebug.append("Object: Type "+value.getClass().getName());
//			sbDebug.append(" ");
//			if(value!=null) {sbDebug.append(" ").append(value.toString());} else {sbDebug.append("NULL");}
//			logger.warn(sbDebug.toString());
			
			Object oColumn = ComponentAttribute.getObject(Properties.column,null,context,this);
			if(oColumn!=null && JeeslReportColumn.class.isAssignableFrom(oColumn.getClass()))
			{
				JeeslReportColumn c = (JeeslReportColumn)oColumn;
//				logger.info(c.getClass().getName()+" "+c.toString()+" "+c.getCode());
				if(c.getDataType()!=null)
				{
					JeeslStatus dt = c.getDataType();
//					logger.info("   DataType  code:"+dt.getCode()+" style:"+dt.getStyle()+" symbol:"+dt.getSymbol());
					
					if(dt.getCode().startsWith("numberDouble"))
					{
						Double d = (Double)value;
						sbValue.append(super.getDecimalFormat(dt.getSymbol()).format(d));
						TrafficLightProcessor.appendStyle(sbStyle,context,this.getAttributes(),d,refValue);
					}
					else if(dt.getCode().startsWith("numberInteger"))
					{
						Integer i = null;
						if(value instanceof Double){i = ((Double)value).intValue();}
						else if(value instanceof Integer){i = (Integer)value;}
						else if(value instanceof String) {i = Integer.valueOf((String)value);}
						sbValue.append(super.getDecimalFormat(dt.getSymbol()).format(i));
						TrafficLightProcessor.appendStyle(sbStyle,context,this.getAttributes(),i.doubleValue(),refValue);
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
					else if(dt.getCode().startsWith("text")) {sbValue.append(value);}
					else
					{
						logger.warn("NYI Handling of "+dt.getCode());sbValue.append(value);
					}
				}
				else{logger.warn("DataType of "+JeeslReportColumn.class.getSimpleName()+" NULL");}
				
				if(c.getStyleCell()!=null)
				{
					CssAlignmentFactory.appendTextCenter(sbStyle);
				}
				else {CssAlignmentFactory.appendTextCenter(sbStyle);}
			}
			else
			{
				CssAlignmentFactory.appendTextCenter(sbStyle);
				logger.warn("Fallback, column==null or not assignable");
				sbValue.append(value);
			}
		}
		catch (JXPathNotFoundException ex){}
		
		if(sbStyle.length()>0) {context.getResponseWriter().writeAttribute(Properties.style.toString(), sbStyle.toString(), null);}
		context.getResponseWriter().write(sbValue.toString());
		for(UIComponent uic : this.getChildren())
		{
			uic.encodeAll(context);
		}
	}
}