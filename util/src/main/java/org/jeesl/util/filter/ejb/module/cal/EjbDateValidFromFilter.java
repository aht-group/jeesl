package org.jeesl.util.filter.ejb.module.cal;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.jeesl.interfaces.model.with.date.jt.JeeslWithDateValidFrom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbDateValidFromFilter 
{
	final static Logger logger = LoggerFactory.getLogger(EjbDateValidFromFilter.class);
	
	public static <T extends JeeslWithDateValidFrom> Optional<T> toCurrent(LocalDate reference, List<T> list)
	{
		return list.stream()
	            .filter(obj -> obj.getValidFrom().isBefore(reference) || obj.getValidFrom().isEqual(reference))
	            .max((o1, o2) -> o1.getValidFrom().compareTo(o2.getValidFrom()));
	}
}