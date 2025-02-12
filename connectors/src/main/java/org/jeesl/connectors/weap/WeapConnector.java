package org.jeesl.connectors.weap;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.util.scripting.Interpreter;
import org.jeesl.util.scripting.ScriptExecutor;
import org.jeesl.util.scripting.Scripts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeapConnector {
    
    final static Logger logger = LoggerFactory.getLogger(WeapConnector.class);
    
    public static Double getResultValue(WeapResultValueRequest request) throws IOException
    {
        if (request.getDimension() == null) {request.setDimension("NOTSET");}
        BigDecimal resultValue = new BigDecimal(0.0);
        try {
		for (String branch : request.getBranches())
		{
                    ScriptExecutor executor = new ScriptExecutor(Interpreter.scripts.VBSCRIPT);
                    List<String> cmdLineParameters = new ArrayList<String>();
                    cmdLineParameters.add(branch);
                    cmdLineParameters.add(request.getVariable());
                    cmdLineParameters.add(request.getUnit());
                    cmdLineParameters.add(request.getDimension());
                    cmdLineParameters.add(request.getYear());
                    cmdLineParameters.add(request.getMonth());
                    cmdLineParameters.add(request.getScenario());
                    cmdLineParameters.add(request.getEndYear());
                    cmdLineParameters.add(request.getEndMonth());

                    String result = executor.execute(Scripts.weapResultValue, cmdLineParameters);

                    logger.debug("Adding value " +result +" from " +branch);
                    resultValue = resultValue.add(new BigDecimal(result));
                    logger.debug("Current Sum " +resultValue);
		}
                
        }
        catch (Exception e)
        {
            logger.error("Could not retrieve value from WEAP.");
            logger.error("For Period: " +request.getYear() +"-" +request.getEndYear());
            logger.error("Of Scenario: " +request.getScenario());
            logger.error("Variable: " +request.getVariable());
            
        } 
        if (request.getAggregationType().equals("Annual Average"))
        {
            int years = (Integer.parseInt(request.getEndYear()) - Integer.parseInt(request.getYear()) +1);
            logger.info("Applying average calculation, dividing by " +years +" years. (" +request.getYear() +" to " +request.getEndYear() +")");
            resultValue = resultValue.divide(new BigDecimal(years), RoundingMode.HALF_UP);
        }
        resultValue = resultValue.setScale(2, RoundingMode.HALF_UP);
        logger.debug("Final Result " +resultValue);
        return resultValue.doubleValue();    
    }
    
    public static double round(double value, int places) {
    if (places < 0) throw new IllegalArgumentException();

    BigDecimal bd = new BigDecimal(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
}
    
    public static String getWeapVersion() throws IOException{
        ScriptExecutor executor = new ScriptExecutor(Interpreter.scripts.VBSCRIPT);
        String result = executor.execute(Scripts.weapSoftwareVersion, new ArrayList<String>());
        if (result == null || result.equals(""))
        {
            triggerWarning("WEAP Version could not be requested!");
        }
        return result;
    }
    
    public static void triggerCalculation() throws IOException{
        ScriptExecutor executor = new ScriptExecutor(Interpreter.scripts.VBSCRIPT);
        String result = executor.execute(Scripts.weapCalculate, new ArrayList<String>());
        logger.info("Trigger Calculate Result: " +result);
    }
    
    public static Integer getBaseYear() throws IOException{
        ScriptExecutor executor = new ScriptExecutor(Interpreter.scripts.VBSCRIPT);
        String result = executor.execute(Scripts.weapBaseYear, new ArrayList<String>());
        if (result == null || result.equals(""))
        {
            triggerWarning("Base Year could not be requested!");
        }
        Integer i = 0;
        try {
            i = new Integer(result);
        }
        catch (Exception e)
        {
            logger.error("Base Year could not be requested!");
        }
        return i;
    }
    
    public static Integer getEndYear() throws IOException{
        ScriptExecutor executor = new ScriptExecutor(Interpreter.scripts.VBSCRIPT);
        String result = executor.execute(Scripts.weapEndYear, new ArrayList<String>());
        if (result == null || result.equals(""))
        {
            triggerWarning("End Year could not be requested!");
        }
        Integer i = 0;
        try {
            i = new Integer(result);
        }
        catch (Exception e)
        {
            logger.error("End Year could not be requested!");
        }
        return i;
    }
    
    public static List<String> getAreas() throws IOException{
        ScriptExecutor executor = new ScriptExecutor(Interpreter.scripts.VBSCRIPT);
        List<String> result = executor.getList(Scripts.weapAreas, new ArrayList<String>());
        if (result == null || result.isEmpty())
        {
            triggerWarning("Areas could not be requested!");
        }
        return result;
    }
    
    public static List<String> getScenarios() throws IOException{
        ScriptExecutor executor = new ScriptExecutor(Interpreter.scripts.VBSCRIPT);
        List<String> result = executor.getList(Scripts.weapScenarios, new ArrayList<String>());
        if (result == null || result.isEmpty())
        {
            triggerWarning("Scenarios could not be requested!");
        }
        return result;
    }
    
    public static String setArea(String areaName) throws IOException
    {
        ScriptExecutor executor = new ScriptExecutor(Interpreter.scripts.VBSCRIPT);
        List<String> parameters = new ArrayList<String>();
        parameters.add(areaName);
        String activeArea = executor.execute(Scripts.weapSetArea, parameters);
        if (activeArea == null || activeArea.equals(""))
        {
            triggerWarning("Area could not be set to " +areaName +"!");
        }
        return activeArea;
    }
    
    public static void setCalculate(String lastYear) throws IOException
    {
        ScriptExecutor executor = new ScriptExecutor(Interpreter.scripts.VBSCRIPT);
        List<String> parameters = new ArrayList<String>();
        parameters.add(lastYear);
        String calculated = executor.execute(Scripts.weapSetCalculate, parameters);
    }
    
    public static String setScenario(String scenarioName) throws IOException
    {
        ScriptExecutor executor = new ScriptExecutor(Interpreter.scripts.VBSCRIPT);
        List<String> parameters = new ArrayList<String>();
        parameters.add(scenarioName);
        String activeScenario = executor.execute(Scripts.weapSetScenario, parameters);
        if (activeScenario == null || activeScenario.equals(""))
        {
            triggerWarning("Area could not be set to " +scenarioName +"!");
        }
        return activeScenario;
    }
    
    public static void triggerWarning(String header)
    {
        logger.warn(header);
    	logger.warn("FX Code deactivated by Thorsten");
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("WEAP Connection Error");
//        alert.setHeaderText(header);
//        alert.setContentText("It was not possible to connect to WEAP using its API. Is a full version of WEAP installed and registered as accessible service for other programs? If not please try to launch this command in the directory WEAP is installed to: ' WEAP /regserver '");
    }
    
}
