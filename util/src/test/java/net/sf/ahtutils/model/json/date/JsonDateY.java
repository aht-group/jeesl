package net.sf.ahtutils.model.json.date;

import org.jeesl.interfaces.model.with.date.primitive.EjbWithYear;

public class JsonDateY implements EjbWithYear
{
	private int year;
	@Override public int getYear() {return year;}
	@Override public void setYear(int year) {this.year = year;}
}