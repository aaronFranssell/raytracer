package algebra;

import java.util.Hashtable;
import java.util.Iterator;

public class GenerateCode
{
	private Equation equation;
	private Hashtable<String,String> variables = new Hashtable<String,String>();
	
	private char candidate;
	
	
	public static final String UNKOWN = "t";
	public static final String CONSTANT = "Constant";
	
	public GenerateCode(Equation equ)
	{
		equation = equ.copy();
		candidate = 'A';
	}
	
	public String generateCode()
	{
		String returnString;
		returnString = declareVariables();
		returnString += "\n";
		returnString += "//constants: these are the equivalent of A, B, and C in Ax^2.0 + Bx + C\n";
		returnString += declareConstants();
		//showVariables();
		return returnString;
	}
	
	private String declareVariables()
	{
		String returnString = "";
		Expression e = equation.getExpressions().get(0).getExpression();
		
		Iterator<Term> it = e.getTerms().iterator();
		while(it.hasNext())
		{
			Term t = it.next();
			Iterator<Variable> varIt = t.getVariables().iterator();
			while(varIt.hasNext())
			{
				Variable v = varIt.next();
				if(!v.getVariableName().equals(UNKOWN))
				{
					variables.put(v.getVariableName(), v.getVariableName());
				}//if
			}//while
		}//while
		Iterator<String> variablesIt = variables.values().iterator();
		while(variablesIt.hasNext())
		{
			returnString += "double " + variablesIt.next() + ";\n";
		}//while
		return returnString;
	}
	
	private String declareConstants()
	{
		String returnString = "";
		int highestPower = getHighestPower();
		
		for(int i = highestPower; i >= 0; i--)
		{
			returnString += getConstantValue(i) + ";\n";
		}//for
		
		return returnString;
	}//declareConstants
	
	private int getHighestPower()
	{
		Expression e = equation.getExpressions().get(0).getExpression();
		int highestPower = 0;
		Iterator<Term> it = e.getTerms().iterator();
		while(it.hasNext())
		{
			Term t = it.next();
			Iterator<Variable> varIt = t.getVariables().iterator();
			while(varIt.hasNext())
			{
				Variable v = varIt.next();
				if(v.getVariableName().equals(UNKOWN))
				{
					if(highestPower < v.getExponent())
					{
						highestPower = v.getExponent();
					}//if
				}//if
			}//while
		}//while
		return highestPower;
	}//getHighestPower
	
	private String getConstantValue(int currentExponent)
	{
		String returnString = "";
		String value = "";
		boolean firstVariable = true;
		Expression e = equation.getExpressions().get(0).getExpression();
		
		Iterator<Term> it = e.getTerms().iterator();
		while(it.hasNext())
		{
			Term t = it.next();
			if(currentExponent > 0 && t.containsVariableAndExponent(UNKOWN,currentExponent))
			{
				boolean first = true;
				Iterator<Variable> varIt = t.getVariables().iterator();
				while(varIt.hasNext())
				{
					Variable v = varIt.next();
					if(first)
					{
						first = false;
						if(firstVariable)
						{
							firstVariable = false;
							value += v.getConstant();
						}//if
						else
						{
							if(v.getConstant() > 0.0)
							{
								value += " + " + v.getConstant();
							}//if
							else
							{
								value += v.getConstant();
							}//if
						}//else
						if(v.getExponent() == 1)
						{
							if(!v.getVariableName().equals(CONSTANT) && !v.getVariableName().equals(UNKOWN))
							{
								value += "*" + v.getVariableName();
							}//if
						}//if
						else
						{
							if(!v.getVariableName().equals(CONSTANT) && !v.getVariableName().equals(UNKOWN))
							{
								value += "*Math.pow("+v.getVariableName()+","+v.getExponent()+")";
							}//if
						}//else
					}//if
					else
					{
						if(v.getExponent() == 1)
						{
							if(!v.getVariableName().equals(CONSTANT) && !v.getVariableName().equals(UNKOWN))
							{
								value += "*" + v.getVariableName();
							}//if
						}//if
						else
						{
							if(!v.getVariableName().equals(CONSTANT) && !v.getVariableName().equals(UNKOWN))
							{
								value += "*Math.pow("+v.getVariableName()+","+v.getExponent()+")";
							}//if
						}//else
					}//else
				}//while
			}//if
			else if(currentExponent == 0 && !t.containsVariable(UNKOWN))
			{
				boolean first = true;
				Iterator<Variable> varIt = t.getVariables().iterator();
				while(varIt.hasNext())
				{
					Variable v = varIt.next();
					if(first)
					{
						first = false;
						if(firstVariable)
						{
							firstVariable = false;
							value += v.getConstant();
						}//if
						else
						{
							if(v.getConstant() > 0.0)
							{
								value += " + " + v.getConstant();
							}//if
							else
							{
								value += v.getConstant();
							}//else
						}//else
						if(v.getExponent() == 1)
						{
							if(!v.getVariableName().equals(CONSTANT) && !v.getVariableName().equals(UNKOWN))
							{
								value += "*" + v.getVariableName();
							}//if
						}//if
						else
						{
							if(!v.getVariableName().equals(CONSTANT) && !v.getVariableName().equals(UNKOWN))
							{
								value += "*Math.pow("+v.getVariableName()+","+v.getExponent()+")";
							}//if
						}//else
					}//if
					else
					{
						if(v.getExponent() == 1)
						{
							if(!v.getVariableName().equals(CONSTANT) && !v.getVariableName().equals(UNKOWN))
							{
								value += "*" + v.getVariableName();
							}//if
						}//if
						else
						{
							if(!v.getVariableName().equals(CONSTANT) && !v.getVariableName().equals(UNKOWN))
							{
								value += "*Math.pow("+v.getVariableName()+","+v.getExponent()+")";
							}//if
						}//else
					}//else
				}//while
			}//if
		}//while
		if(value.equals(""))
		{
			returnString += "double " + getNextConstant() + " = 0";
			return returnString;
		}//if
		returnString += "double ";
		returnString += getNextConstant() + " = " + value;
		return returnString;
	}
	
	private String getNextConstant()
	{
		for(;;)
		{
			if(isLegal(candidate))
			{
				String aString = "" + candidate;
				variables.put(aString, aString);
				break;
			}///if
			else
			{
				candidate++;
			}//else
		}//for
		
		return "" + candidate;
	}
	
	private boolean isLegal(char candidate)
	{
		if(!(candidate >= 'A' && candidate <= 'Z') || !(candidate >= 'a' || candidate <= 'z'))
		{
			return false;
		}//if
		String aString = "";
		aString += candidate;
		if(variables.get(aString) != null)
		{
			return false;
		}
		return true;
	}//isLegal
}