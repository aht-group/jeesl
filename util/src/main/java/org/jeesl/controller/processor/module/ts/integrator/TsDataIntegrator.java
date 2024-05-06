package org.jeesl.controller.processor.module.ts.integrator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.TrapezoidIntegrator;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;
import org.apache.commons.math3.util.Precision;
import org.apache.poi.ss.usermodel.DateUtil;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TsDataIntegrator <DATA extends JeeslTsData<?,?,?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(TsDataIntegrator.class);
	
	private final List<Long> time;
    private final List<Double> values;
    
    private final UnivariateIntegrator integrator;
    private final UnivariateFunction function;
	
	public static <DATA extends JeeslTsData<?,?,?,?,?>> TsDataIntegrator<DATA> instance() {return new TsDataIntegrator<>();}
	public TsDataIntegrator()
	{
		integrator = new TrapezoidIntegrator();
		function = this.toUnivariateFunction();
		
		 time = new ArrayList<>();
	     values = new ArrayList<>();
	}
		
	public void feed(List<DATA> list)
	{
		time.clear();
		values.clear();
		
		for(DATA d : list)
		{
//			long epochSeconds = t.toEpochSecond(ZoneOffset.UTC);
			long second = DateUtil.toLocalDateTime(d.getRecord()).toEpochSecond(ZoneOffset.UTC);
	        time.add(second);
	        values.add(d.getValue());
		}
	}
	
	public double integrate (LocalDateTime ldtFrom, LocalDateTime ldtTo)
	{
		logger.info(ldtFrom +" -> " +ldtTo);
        // Konvertiere LocalDateTime in epoch seconds für Integration
        long lowerBound = ldtFrom.toEpochSecond(ZoneOffset.UTC);
        long upperBound = ldtTo.toEpochSecond(ZoneOffset.UTC);

        // Berechne das Integral
        double result = integrator.integrate(10000000, function, lowerBound, upperBound);

        long diff = ChronoUnit.SECONDS.between(ldtFrom, ldtTo);
        
        
        
        return Precision.round(result/diff, 1, BigDecimal.ROUND_FLOOR);
	}
	
	public UnivariateFunction toUnivariateFunction()
	{
        return x ->
        {
            // Suche das nächstgelegene Zeitreihen-Punkt
            for (int i=0; i<time.size()-1; i++)
            {
                if (x >= time.get(i) && x < time.get(i + 1)) {
                    // Lineare Interpolation
                    double slope = (values.get(i + 1) - values.get(i)) / (time.get(i + 1) - time.get(i));
                    return values.get(i) + slope * (x - time.get(i));
                }
            }
            // Falls x außerhalb der Zeitreihe liegt, Rückgabe des letzten Wertes
            return values.get(values.size() - 1);
        };
    }
}