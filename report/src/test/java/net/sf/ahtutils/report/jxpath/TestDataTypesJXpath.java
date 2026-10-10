/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.ahtutils.report.jxpath;

import java.util.Iterator;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.junit.jupiter.api.Test;

/**
 *
 * @author helgehemmer
 */
public class TestDataTypesJXpath {
	
	public class TestDataStructure {
		private Integer integerValue = 1;
		private Long longValue   = new Long(2);

		public int getIntegerValue() {return integerValue;}
		public void setIntegerValue(int integerValue) {this.integerValue = integerValue;}

		public Long getLongValue() {return longValue;}
		public void setLongValue(Long longValue) {this.longValue = longValue;}
	}
	
	public TestDataTypesJXpath() {
	}
	
	@Test
	public void test()
	{
		TestDataStructure structure = new TestDataStructure();
		//structure.setIntegerValue(1);
		//structure.setLongValue(2);
		
		JXPathContext	context  = JXPathContext.newContext(structure);
		String queryInteger      = "@integerValue";
		String queryLong         = "@longValue";
		
		Iterator iteratorLong = context.iteratePointers(queryLong);
		while (iteratorLong.hasNext())
        {
            Pointer pointerToItem = (Pointer)iteratorLong.next();
			System.out.println("Got pointer: " +pointerToItem.getValue().getClass() +" with value = " +pointerToItem.getValue().toString());
		}
		
		Iterator iteratorInteger = context.iteratePointers(queryInteger);
		while (iteratorInteger.hasNext())
        {
            Pointer pointerToItem = (Pointer)iteratorInteger.next();
			System.out.println("Got pointer: " +pointerToItem.getValue().getClass() +" with value = " +pointerToItem.getValue().toString());
		}
		
		
	}
	
}
