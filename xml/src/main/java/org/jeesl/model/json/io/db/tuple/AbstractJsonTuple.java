package org.jeesl.model.json.io.db.tuple;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import jakarta.json.bind.annotation.JsonbDateFormat;

public abstract class AbstractJsonTuple implements Serializable
{
	public static final long serialVersionUID=1;
	
	@JsonProperty("record")
	@JsonbDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss.SSS")
	@JsonDeserialize(using=LocalDateTimeDeserializer.class)
	@JsonSerialize(using=LocalDateTimeSerializer.class)
	private LocalDateTime record;
	public LocalDateTime getRecord() {return record;}
	public void setRecord(LocalDateTime record) {this.record = record;}

	@Deprecated //Use sum1
	@JsonProperty("sum")
	private Double sum;
	public Double getSum() {return sum;}
	public void setSum(Double sum) {this.sum = sum;}
	
	@JsonProperty("sum1")
	private Double sum1;
	public Double getSum1() {return sum1;}
	public void setSum1(Double sum1) {this.sum1 = sum1;}
	
	@JsonProperty("sum2")
	private Double sum2;
	public Double getSum2() {return sum2;}
	public void setSum2(Double sum2) {this.sum2 = sum2;}
	
	@JsonProperty("sum3")
	private Double sum3;
	public Double getSum3() {return sum3;}
	public void setSum3(Double sum3) {this.sum3 = sum3;}
	
	@JsonProperty("sum4")
	private Double sum4;
	public Double getSum4() {return sum4;}
	public void setSum4(Double sum4) {this.sum4 = sum4;}
	
	@JsonProperty("sum5")
	private Double sum5;
	public Double getSum5() {return sum5;}
	public void setSum5(Double sum5) {this.sum5 = sum5;}
	
	@Deprecated //Use count1
	@JsonProperty("count")
	private Long count;
	public Long getCount() {return count;}
	public void setCount(Long count) {this.count = count;}
	
	@JsonProperty("count1")
	private Long count1;
	public Long getCount1() {return count1;}
	public void setCount1(Long count1) {this.count1 = count1;}
	
	@JsonProperty("count2")
	private Long count2;
	public Long getCount2() {return count2;}
	public void setCount2(Long count2) {this.count2 = count2;}
	
	@JsonProperty("count3")
	private Long count3;
	public Long getCount3() {return count3;}
	public void setCount3(Long count3) {this.count3 = count3;}
	
	
	@JsonProperty("v1")
	private Double v1;
	public Double getV1() {return v1;}
	public void setV1(Double v1) {this.v1 = v1;}
	
	@JsonProperty("v2")
	private Double v2;
	public Double getV2() {return v2;}
	public void setV2(Double v2) {this.v2 = v2;}
	
	@JsonProperty("v3")
	private Double v3;
	public Double getV3() {return v3;}
	public void setV3(Double v3) {this.v3 = v3;}
}