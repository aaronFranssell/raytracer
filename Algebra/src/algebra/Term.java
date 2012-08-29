package algebra;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class represents a term in an algebraic expression, that is a combination of variables.
 * This means that all variables in this term will be multiplied by one another.
 * The constant will be placed in the first variable of the term.
 * @author afranssell
 */
public class Term
{
	private ArrayList<Variable> variables;
	
	public Term()
	{
		variables = new ArrayList<Variable>();
	}
	
	public Term(ArrayList<Variable> incomingVariables)
	{
		variables = incomingVariables;
	}
	
	public void addVariable(Variable incomingVar)
	{
		variables.add(incomingVar);
	}
	
	public Term multiply(Term other)
	{
		Iterator<Variable> it;
		it = other.getVariables().iterator();
		Variable var;
		Term newTerm = this.copy();
		while(it.hasNext())
		{
			var = (Variable) it.next();
			var = var.copy();
			newTerm.addVariable(var);
		}//while
		newTerm.simplify();
		return newTerm;
	}

	public ArrayList<Variable> getVariables() {
		return variables;
	}

	public void setVariables(ArrayList<Variable> variables) {
		this.variables = variables;
	}
	
	//the whole point of this class
	public void simplify()
	{
		if(variables.size() < 2)
		{
			return;//already simplified (hopefully)
		}//if
		
		Iterator<Variable> it = variables.iterator();
		double constants = 1;
		Variable var;
		while(it.hasNext())//first iterate through and multiply all constants by eachother
		{
			var = (Variable) it.next();
			constants *= var.getConstant();
			var.setConstant(1.0);
		}//while
		variables.get(0).setConstant(constants);
		
		it = variables.iterator();
		Variable var2;
		for(int b = 0; b < variables.size(); b++)
			//iterate through and find all variables equal to eachother to get the exponent
		{
			var = variables.get(b);
			it = variables.iterator();
			while(it.hasNext())
			{
				var2 = (Variable) it.next();
				if(var != var2 && //don't want to be adding exponents of the same var...
				   var.getVariableName().equals(var2.getVariableName()))
				{
					var.setExponent(var.getExponent() + var2.getExponent());
					it.remove();
				}//if
			}//while
		}//while
		for(int i = 0; i < variables.size(); i++)
		{
			var = (Variable) variables.get(i);
			if(var.getExponent() == 0.0 && variables.size() != 1)//don't want to remove the only var in the
																//term
			{
				if(i == 0)//the first var holds the constant of the term.
				{
					variables.get(1).setConstant(variables.get(0).getConstant());
				}//if
				variables.remove(i);
			}//if
		}//while
	}
	
	public String toString()
	{
		String retVal = "";
		Iterator<Variable> it = variables.iterator();
		Variable var;
		while(it.hasNext())
		{
			var = (Variable) it.next();
			if(Math.abs(var.getConstant()) != 1 || var.getExponent() == 0.0)
			{
				if(var.getConstant() < 0)
				{
					retVal += Math.abs(var.getConstant());
				}//if
				else
				{
					retVal += var.getConstant();
				}//else
			}
			if(var.getExponent() != 0.0)
			{
				retVal += var.getVariableName();
				if(var.getExponent() != 1)
				{
					retVal += "^" + var.getExponent();
				}
			}//if
		}//while
		return retVal;
	}//toString()
	
	//returns true if you can add two terms together
	public boolean canAdd(Term other)
	{
		this.simplify();
		other.simplify();
		Iterator<Variable> it = other.getVariables().iterator();
		Variable var;
		Variable var2;
		boolean varEquality = false;
		while(it.hasNext())
		{
			var = (Variable) it.next();
			varEquality = false;
			Iterator<Variable> it2 = variables.iterator();
			while (it2.hasNext())
			{
				var2 = (Variable) it2.next();
				if(var.getVariableName().equals(var2.getVariableName()) 
				   && var.getExponent() == var2.getExponent())
				{
					varEquality = true;
				}//if
			}//while
			if(!varEquality)
			{
				return false;
			}//if
		}//while
		varEquality = false;
		it = variables.iterator();
		while(it.hasNext())//the intersection of variable names must be equal to the union to be able to add
						   //to algebraic expressions.
		{
			var = (Variable) it.next();
			varEquality = false;
			Iterator<Variable> it2 = other.getVariables().iterator();
			while (it2.hasNext())
			{
				var2 = (Variable) it2.next();
				if(var.getVariableName().equals(var2.getVariableName())
				   && var.getExponent() == var2.getExponent())
				{
					varEquality = true;
				}//if
			}//while
			if(!varEquality)
			{
				return false;
			}//if
		}//while
		return true;
	}//canAdd
	
	public Term add(Term other)
	{
		Term copiedTerm;
		if(!canAdd(other))
		{
			try {
				throw new Exception("Trying to add two terms that can't be added!");
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.exit(0);
		}//if
		this.simplify();
		other.simplify();
		double constantSum = other.getVariables().get(0).getConstant() + variables.get(0).getConstant();
		copiedTerm = copy();
		copiedTerm.getVariables().get(0).setConstant(constantSum);
		return copiedTerm;
	}
	
	//makes a deep copy of this
	public Term copy()
	{
		Term newTerm = new Term();
		Iterator<Variable> it = variables.iterator();
		while(it.hasNext())
		{
			newTerm.addVariable(((Variable) it.next()).copy());
		}
		return newTerm;
	}
	
	public boolean containsVariableAndExponent(String varName,int exponent)
	//returns true if this term contains varName
	{
		Iterator<Variable> it = variables.iterator();
		Variable tempVar;
		while(it.hasNext())
		{
			tempVar = (Variable) it.next();
			if(tempVar.getVariableName().equals(varName) && tempVar.getExponent() == exponent)
			{
				return true;
			}//if
		}//while
		return false;
	}
	
	public boolean containsVariable(String varName)
	//returns true if this term contains varName
	{
		Iterator<Variable> it = variables.iterator();
		Variable tempVar;
		while(it.hasNext())
		{
			tempVar = (Variable) it.next();
			if(tempVar.getVariableName().equals(varName))
			{
				return true;
			}//if
		}//while
		return false;
	}
	
	public Variable getVariable(String varName)
	{
		Iterator<Variable> it = variables.iterator();
		Variable tempVar;
		while(it.hasNext())
		{
			tempVar = (Variable) it.next();
			if(tempVar.getVariableName().equals(varName))
			{
				return tempVar;
			}//if
		}//while
		return null;
	}
}
