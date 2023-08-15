package helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;

import util.Constants;
import util.UtilImpl;

public class TestsHelper
{
	public static void arrayListSubsets(ArrayList<?> expected, ArrayList<?> actual)
	{
		assert expected.size() == actual.size();
		
		for(int i = 0; i < expected.size(); i++)
		{
			boolean found = false;
			for(int m = 0; m < actual.size(); m++)
			{
				if(expected.get(i).equals(actual.get(m)))
				{
					found = true;
					break;
				}
			}
			if(!found)
			{
				fail("Did not find: " + expected.get(i) + " in actual list.");
			}
		}
		
		for(int i = 0; i < actual.size(); i++)
		{
			boolean found = false;
			for(int m = 0; m < expected.size(); m++)
			{
				if(expected.get(m).equals(actual.get(i)))
				{
					found = true;
					break;
				}
			}
			if(!found)
			{
				fail("Did not find: " + actual.get(i) + " in expected list.");
			}
		}
	}
	
	public static void doubleArraysSubsets(double[] expected, double[] actual)
	{
		assertEquals(expected.length, actual.length);
		
		for(int i = 0; i < expected.length; i++)
		{
			boolean found = false;
			for(int m = 0; m < actual.length; m++)
			{
				if(UtilImpl.doubleEqual(expected[i], actual[m], Constants.POSITIVE_ZERO) || areNans(expected[i], actual[m]))
				{
					found = true;
					break;
				}
			}
			if(!found)
			{
				fail("Did not find: " + expected[i] + " in actual list.");
			}
		}
		
		for(int i = 0; i < actual.length; i++)
		{
			boolean found = false;
			for(int m = 0; m < expected.length; m++)
			{
				if(UtilImpl.doubleEqual(expected[m], actual[i], Constants.POSITIVE_ZERO) || areNans(expected[i], actual[m]))
				{
					found = true;
					break;
				}
			}
			if(!found)
			{
				fail("Did not find: " + actual[i] + " in expected list.");
			}
		}
	}
	
	private static boolean areNans(double d1, double d2)
	{
		return Double.isNaN(d1) && Double.isNaN(d2);
	}
	
}
