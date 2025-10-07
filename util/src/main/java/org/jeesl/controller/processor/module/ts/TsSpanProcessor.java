package org.jeesl.controller.processor.module.ts;

import java.time.LocalDateTime;
import java.util.Objects;

import org.jeesl.interfaces.model.module.ts.stat.JeeslTsSpan;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TsSpanProcessor
{
	final static Logger logger = LoggerFactory.getLogger(TsSpanProcessor.class);
	
	public static <L extends JeeslLang, D extends JeeslDescription, S extends JeeslStatus<L,D,S>, G extends JeeslGraphic<?,?,?>>
			LocalDateTime backwards(LocalDateTime ldt, S span)
	{
		if(Objects.nonNull(span))
		{
			switch(JeeslTsSpan.Code.valueOf(span.getCode()))
			{
				case h24: return ldt.minusHours(24);
				case w4: return ldt.minusWeeks(4);
				case d7: return ldt.minusDays(7);
			}
		}
		return ldt;
	}
}
