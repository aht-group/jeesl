package org.jeesl.interfaces.model.io.report.style;

public interface JeeslReportLayout
{
	public static enum Code{columnWidth,alignment}
	

	public static enum Style{header,cell,footer}
	public static enum Color{background}
	
	
	public static enum Data{string,dble,lng,intgr,dte,bool}
}