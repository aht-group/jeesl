package org.jeesl.connectors.weap;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement
public class WeapResultValueRequest implements Serializable {
	
	final static Logger logger = LoggerFactory.getLogger(WeapResultValueRequest.class);
    
	private String sheet;
	private int column;
	private int row;
	
    private String scenario;
    private List<String> branches;
    private String variable;
    private String year;
    private String month;
    private String endYear;
    private String endMonth;
    private String function;
    private String unit;
    private String dimension;
	private String aggregationType;
	private String errorMessage;
    
    private Double result;

    public String getScenario() {return scenario;}
    public void setScenario(String scenario) {this.scenario = scenario;}

    public List<String> getBranches() {return branches;}
    public void setBranches(List<String> branches) {this.branches = branches;}

    public String getVariable() {return variable;}
    public void setVariable(String variable) {this.variable = variable;}

    public String getYear() {return year;}
    public void setYear(String year) {this.year = year;}

    public String getMonth() {return month;}
    public void setMonth(String month) {this.month = month;}

    public String getEndYear() {return endYear;}
    public void setEndYear(String endYear) {this.endYear = endYear;}

    public String getEndMonth() {return endMonth;}

    public void setEndMonth(String endMonth) {this.endMonth = endMonth;}

    public String getFunction() { return function;}
    public void setFunction(String function) {this.function = function;}

    public String getUnit() {return unit;}
    public void setUnit(String unit) {this.unit = unit;}

    public String getDimension() {return dimension;}
    public void setDimension(String dimension) {this.dimension = dimension;}

    public Double getResult() {return result;}
    public void setResult(Double result) {this.result = result;}

	public String getAggregationType() {return aggregationType;}
	public void setAggregationType(String aggregationType) {this.aggregationType = aggregationType;}

	public String getErrorMessage() {return errorMessage;}
	public void setErrorMessage(String errorMessage) {this.errorMessage = errorMessage;}
}
