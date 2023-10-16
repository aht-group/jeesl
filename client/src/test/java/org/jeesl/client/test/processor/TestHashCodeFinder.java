package org.jeesl.client.test.processor;

import java.io.File;
import java.util.Map;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.client.maintenance.DuplicateHashCodeFinder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class TestHashCodeFinder
{
	DuplicateHashCodeFinder hcc;

	@BeforeEach
	public void init()
	{
		File f = new File("..\\ejb\\src\\test\\java\\net\\sf\\ahtutils\\test\\model");
		hcc = new DuplicateHashCodeFinder(f);
		hcc.searchForHashCodeBuilder();
	}

	@Test @Disabled
	public void test()
	{
		int expectedSize = 2;
		Map<String, String> testList = hcc.compareCode();
		int actual = testList.size();
		Assertions.assertEquals(expectedSize, actual);
		for(Map.Entry<String, String> entry : testList.entrySet()){System.out.println(entry);}
		System.out.println(actual);
	}

	public int hashCode(){return new HashCodeBuilder(17, 43).toHashCode();}

	public static void main(String[] args)
	{
		TestHashCodeFinder test = new TestHashCodeFinder();
		test.init();test.test();
	}
}
