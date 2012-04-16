package algebra;

public class ExpressionOp
{
	private Expression expression;
	private char op;
	
	public ExpressionOp(Expression incomingExpression, char incomingOp)
	{
		expression = incomingExpression;
		op = incomingOp;
	}

	public Expression getExpression() {
		return expression;
	}

	public void setExpression(Expression expression) {
		this.expression = expression;
	}

	public char getOp() {
		return op;
	}

	public void setOp(char op) {
		this.op = op;
	}
	
	public ExpressionOp copy()
	{
		return new ExpressionOp(expression.copy(),op);
	}
}
