package algebra;

import java.util.ArrayList;
import java.util.Iterator;

public class Equation
{
	private ArrayList<ExpressionOp> expressions;
	
	public Equation (ArrayList<ExpressionOp> incomingExpressions)
	{
		expressions = incomingExpressions;
	}
	
	public void addExpressionOp(ExpressionOp incoming)
	{
		expressions.add(incoming);
	}
	
	public Equation()
	{
		expressions = new ArrayList<ExpressionOp>();
	}

	public ArrayList<ExpressionOp> getExpressions()
	{
		return expressions;
	}

	public void setExpressions(ArrayList<ExpressionOp> expressions)
	{
		this.expressions = expressions;
	}
	
	public void simplify()
	{
		ExpressionOp ex1;
		ExpressionOp ex2;
		for(int i = 0; i < expressions.size() - 1; i++)//multiply first
		{
			ex1 = expressions.get(i);
			ex2 = expressions.get(i + 1);
			//System.out.println("ex1: " + ex1.getExpression().toString());
			//System.out.println("ex2: " + ex2.getExpression().toString());
			if(ex2.getOp() != '*' && ex2.getOp() != '+')
			{
				try
				{
					throw new Exception("Unrecognized operation: " + ex2.getOp());
				}
				catch (Exception e)
				{
					e.printStackTrace();
					System.exit(0);
				}
			}
			if(ex2.getOp() == '*')
			{
				ExpressionOp newExOp = new ExpressionOp(ex1.getExpression().multiply(ex2.getExpression()),ex1.getOp());
				expressions.set(i, newExOp);
				expressions.remove(i + 1);
				i = -1;//must be -1 because for loop increments
			}//if
		}//for
		for(int i = 0; i < expressions.size() - 1; i++)//add now
		{
			ex1 = expressions.get(i);
			ex2 = expressions.get(i + 1);
			if(ex2.getOp() == '+')
			{
				ExpressionOp newExOp = new ExpressionOp(ex1.getExpression().add(ex2.getExpression()),ex1.getOp());
				expressions.set(i, newExOp);
				expressions.remove(i + 1);
				i = -1;
			}//if
		}//for
	}//simplify
	
	public String toString()
	{
		boolean first = true;
		String retString = "";
		ExpressionOp temp;
		Iterator<ExpressionOp> it = expressions.iterator();
		while(it.hasNext())
		{
			temp = it.next();
			if(first)
			{
				first = false;
				retString = "(" + temp.getExpression().toString() + ")";
			}//if
			else
			{
				if(temp.getOp() == '+')
				{
					retString += " + (" + temp.getExpression().toString() + ")";
				}//if
				else if(temp.getOp() == '*')
				{
					retString += "(" + temp.getExpression().toString() + ")";
				}//else
			}//else
		}//while
		return retString;
	}
	
	public Equation copy()
	{
		Iterator<ExpressionOp> it = expressions.iterator();
		Equation returnEquation = new Equation();
		while(it.hasNext())
		{
			returnEquation.getExpressions().add(it.next().copy());
		}//while
		return returnEquation;
	}
}
