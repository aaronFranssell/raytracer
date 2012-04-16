package algebra;

/**
 * This class represents an algebraic variable, complete with exponent and coefficient.
 * @author afranssell
 */
public class Variable
{
	private String variableName;
	private int exponent;
	private double constant;
	
	public Variable(String incomingVariableName, int incomingExponent, double incomingConstant)
	{
		variableName = incomingVariableName;
		exponent = incomingExponent;
		constant = incomingConstant;
	}//Variable

	public double getConstant() {
		return constant;
	}

	public void setConstant(double constant) {
		this.constant = constant;
	}

	public int getExponent() {
		return exponent;
	}

	public void setExponent(int exponent) {
		this.exponent = exponent;
	}

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}
	
	//creates a deep copy of this
	public Variable copy()
	{
		return new Variable(variableName,exponent,constant);
	}
	
	public String toString()
	{
		return constant + variableName + "^" + exponent;
	}
}//Variable
