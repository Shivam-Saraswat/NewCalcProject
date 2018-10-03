package com.cg.maven.test;

import junit.framework.Assert;

import org.junit.Test;

import com.cg.maven.demo.CalculatorApp.Calculator;

@SuppressWarnings("deprecation")
public class TestCalculator {

	@Test
	public void testAdd() {
		Calculator c1 = new Calculator();
		int res = c1.add(3, 6);
		Assert.assertEquals(9, res);
	}
}
