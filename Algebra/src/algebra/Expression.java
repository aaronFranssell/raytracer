package algebra;

import java.util.ArrayList;
import java.util.Iterator;

public class Expression
{	
	private ArrayList<Term> terms;

	public Expression()
	{
		terms = new ArrayList<Term>();
	}
	
	public Expression(ArrayList<Term> incomingList)
	{
		terms = incomingList;
	}
	
	/*
	 * This function assumes other expression is all multiplied out and whatever
	 */
	public Expression add(Expression other)
	{
		Term t;
		Term t2;
		Expression copiedOther = other.copy();
		Expression copiedThis = this.copy();
		for(int i = 0; i < copiedThis.getTerms().size(); i++)
		{
			t = (Term) copiedThis.getTerms().get(i);
			for(int u = 0; u < copiedOther.getTerms().size(); u++)
			{
				t2 = (Term) copiedOther.getTerms().get(u);
				/*System.out.println("copiedOther.getTerms().get(u): " + copiedOther.getTerms().get(u));
				System.out.println("t2: " + t2.toString());
				System.out.println("copiedThis: " + copiedThis.toString());
				System.out.println("copiedOther: " + copiedOther.toString());
				System.out.println("above, t: " + t.toString() + " t2: " + t2.toString());*/
				if(t.canAdd(t2))
				{
					/*System.out.println("\nthis: " + this.toString());
					System.out.println("other: " + other.toString());
					System.out.println("can add, t: " + t.toString() + " t2: " + t2.toString() + "\n");*/
					copiedThis.getTerms().set(i,t.add(t2));
					t = copiedThis.getTerms().get(i);
					copiedOther.getTerms().remove(t2);
				}//if
			}//for
		}//for
		for(int u = 0; u < copiedOther.getTerms().size(); u++)//all terms that aren't removed can't be added
		{
			copiedThis.getTerms().add((Term) copiedOther.getTerms().get(u));
		}//for
		return copiedThis;
	}
	
	public Expression multiply(Expression other)
	{
		Expression copiedOther = other.copy();
		Expression copiedThis = this.copy();
		Expression answer = new Expression();
		//System.out.println("copiedThis: " + copiedThis.toString());
		//System.out.println("copiedOther: " + copiedOther.toString());
		Term newTerm;
		for(int i = 0; i < copiedThis.getTerms().size(); i++)
		{
			Term t1 = copiedThis.getTerms().get(i);
			for(int u = 0; u < copiedOther.getTerms().size(); u++)
			{
				Term t2 = copiedOther.getTerms().get(u);
				newTerm = t1.multiply(t2);
				//System.out.println("newTerm: " + newTerm.toString());
				answer.addTerm(newTerm);
			}//for
		}//for
		//System.out.println("answer.size: " + answer.getTerms().size());
		//System.out.println("answer: " + answer.toString());
		answer.simplify();
		return answer;
	}
	
	public void simplify()
	{
		Term t1;
		Term t2;
		boolean removed = false;
		for(int i = 0; i < terms.size(); i++)
		{
			t1 = terms.get(i);
			for(int u = 0; u < terms.size(); u++)
			{
				t2 = terms.get(u);
				if(t1 != t2 && (t1.canAdd(t2)))//don't want to add this to itself
				{
					//System.out.println("t1: " + t1.toString());
					//System.out.println("t2: " + t2.toString());
					terms.set(i,t1.add(t2));
					t1 = terms.get(i);
					terms.remove(t2);
					removed = true;
					u = 0;
				}//if
			}//for
			if(removed)
			{
				removed = false;
				i = 0;
			}
		}//for
		
		Iterator<Term> it = terms.iterator();
		Term t;
		while(it.hasNext())
		{
			t = (Term) it.next();
			if(t.getVariables().get(0).getConstant() == 0.0)
			{
				it.remove();
			}//if
		}//while
	}//simplify

	public ArrayList<Term> getTerms() {
		return terms;
	}

	public void setTerms(ArrayList<Term> terms) {
		this.terms = terms;
	}
	
	public void addTerm(Term t)
	{
		terms.add(t);
	}
	
	public String toString()
	{
		String retVal = "";
		Iterator<Term> it = terms.iterator();
		boolean first = true;
		Term t;
		Variable var;
		while(it.hasNext())
		{
			t = (Term) it.next();
			var = t.getVariables().get(0);
			if(first && var.getConstant() > 0)
			{
				first = false;
				retVal += t.toString();
			}//if
			else if(var.getConstant() > 0)
			{
				retVal += " + ";
				retVal += t.toString();
			}//else
			else
			{
				retVal += " - ";
				retVal += t.toString();
			}
		}//while
		return retVal;
	}//toString()
	
	//makes a deep copy of this
	public Expression copy()
	{
		ArrayList<Term> newTerms = new ArrayList<Term>();
		Iterator<Term> it = terms.iterator();
		while(it.hasNext())
		{
			newTerms.add((Term) it.next().copy());
		}//while
		Expression newExpression = new Expression(newTerms);
		return newExpression;
	}//copy
	
	public Expression pow(int power)
	{
		Expression copy1 = this.copy();
		Expression copy2 = this.copy();
		Expression answer = new Expression();
		Iterator<Term> it = copy1.getTerms().iterator();
		for(int i = 0; i < power; i++)
		{
			while(it.hasNext())
			{
				Term temp1 = it.next();
				Iterator<Term> it2 = copy2.getTerms().iterator();
				while(it2.hasNext())
				{
					Term temp2 = it2.next();
					answer.addTerm(temp1.multiply(temp2));
				}//while
			}//while
		}//for
		//System.out.println("\nanswer:\n" + answer.toString()+ "\n");
		answer.simplify();
		return answer;
	}//pow
	
	public void addExpression(Expression other)
	{
		Iterator<Term> it = other.getTerms().iterator();
		while(it.hasNext())
		{
			this.addTerm(it.next());
		}//while
	}
}
