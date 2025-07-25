package primitives.torus;

import java.util.ArrayList;

import algebra.Equation;
import algebra.Expression;
import algebra.ExpressionOp;
import algebra.GenerateCode;
import algebra.Term;
import algebra.Variable;

public class GenCodeArbitraryTorus
{
	public static void main(String args[])
	{
		Variable minusSmallRSquared = new Variable("smallR",2,-1.0);
		Variable minusLargeRSquared = new Variable("largeR",2,-1.0);
		Variable fourLargeRSquared = new Variable("largeR",2,4.0);
		Variable t = new Variable("t",1,1.0);
		
		Variable Px = new Variable("Px",1,1.0);
		Variable Dx = new Variable("Dx",1,1.0);
		
		Variable Py = new Variable("Py",1,1.0);
		Variable Dy = new Variable("Dy",1,1.0);
		
		Variable Pz = new Variable("Pz",1,1.0);
		Variable Dz = new Variable("Dz",1,1.0);
		
		Variable Cx = new Variable("Cx",1,-1.0);
		Variable Cy = new Variable("Cy",1,-1.0);
		Variable Cz = new Variable("Cz",1,-1.0);
		
		//((px + tdx - Cx)2 + (py + tdy - Cy)2 + (pz + tdz - Cz)2 - r2 - R2)2 + 4R2((pz + tdz - Cz)2 - r2)
		//       tdx
		Term termTXLeft = new Term();
		termTXLeft.addVariable(t.copy());
		termTXLeft.addVariable(Dx.copy());
		
//		((px + tdx - Cx)2 + (py + tdy - Cy)2 + (pz + tdz - Cz)2 - r2 - R2)2 + 4R2((pz + tdz - Cz)2 - r2)
//  	  px
		Term termPXLeft = new Term();
		termPXLeft.addVariable(Px.copy());
		
//		((px + tdx - Cx)2 + (py + tdy - Cy)2 + (pz + tdz - Cz)2 - r2 - R2)2 + 4R2((pz + tdz - Cz)2 - r2)
//		           - Cx
		Term termCXLeft = new Term();
		termCXLeft.addVariable(Cx);
		
//		((px + tdx - Cx)2 + (py + tdy - Cy)2 + (pz + tdz - Cz)2 - r2 - R2)2 + 4R2((pz + tdz - Cz)2 - r2)
//		((px + tdx - Cx)2
		Expression left = new Expression();
		left.addTerm(termPXLeft.copy());
		left.addTerm(termTXLeft.copy());
		left.addTerm(termCXLeft.copy());
		left = left.pow(2);
		left.simplify();
		System.out.println("left: " + left.toString());
		
//		((px + tdx - Cx)2 + (py + tdy - Cy)2 + (pz + tdz - Cz)2 - r2 - R2)2 + 4R2((pz + tdz - Cz)2 - r2)
//					              tdy
		Term termTYMiddle = new Term();
		termTYMiddle.addVariable(t.copy());
		termTYMiddle.addVariable(Dy.copy());
		
//		((px + tdx - Cx)2 + (py + tdy - Cy)2 + (pz + tdz - Cz)2 - r2 - R2)2 + 4R2((pz + tdz - Cz)2 - r2)
//				  			 py
		Term termPyMiddle = new Term();
		termPyMiddle.addVariable(Py.copy());
		
//		((px + tdx - Cx)2 + (py + tdy - Cy)2 + (pz + tdz - Cz)2 - r2 - R2)2 + 4R2((pz + tdz - Cz)2 - r2)
//									  - Cy
		Term termCyMiddle = new Term();
		termCyMiddle.addVariable(Cy);
		
//		((px + tdx - Cx)2 + (py + tdy - Cy)2 + (pz + tdz - Cz)2 - r2 - R2)2 + 4R2((pz + tdz - Cz)2 - r2)
//				 			(py + tdy - Cy)2
		Expression middle = new Expression();
		middle.addTerm(termPyMiddle.copy());
		middle.addTerm(termTYMiddle.copy());
		middle.addTerm(termCyMiddle.copy());
		middle = middle.pow(2);
		middle.simplify();
		System.out.println("middle: " + middle.toString());
		
//		((px + tdx - Cx)2 + (py + tdy - Cy)2 + (pz + tdz - Cz)2 - r2 - R2)2 + 4R2((pz + tdz - Cz)2 - r2)
//			    									 tdz
		Term termTZRight = new Term();
		termTZRight.addVariable(t.copy());
		termTZRight.addVariable(Dz.copy());

//		((px + tdx - Cx)2 + (py + tdy - Cy)2 + (pz + tdz - Cz)2 - r2 - R2)2 + 4R2((pz + tdz - Cz)2 - r2)
//												pz
		Term termPzRight = new Term();
		termPzRight.addVariable(Pz.copy());
		
//		((px + tdx - Cx)2 + (py + tdy - Cy)2 + (pz + tdz - Cz)2 - r2 - R2)2 + 4R2((pz + tdz - Cz)2 - r2)
//														 - Cz
		Term termCZRight = new Term();
		termCZRight.addVariable(Cz.copy());
		
//		((px + tdx - Cx)2 + (py + tdy - Cy)2 + (pz + tdz - Cz)2 - r2 - R2)2 + 4R2((pz + tdz - Cz)2 - r2)
//							   				   (pz + tdz - Cz)2
		Expression right = new Expression();
		right.addTerm(termPzRight.copy());
		right.addTerm(termTZRight.copy());
		right.addTerm(termCZRight.copy());
		right = right.pow(2);
		right.simplify();
		System.out.println("right: " + right.toString());
		
//		((px + tdx - Cx)^2 + (py + tdy - Cy)^2 + (pz + tdz - Cz)^2 - r^2 - R^2)^2 + 4R^2((pz + tdz - Cz)^2 - r^2)
//				    											   - r^2
		Term tLittleRSquared = new Term();
		tLittleRSquared.addVariable(minusSmallRSquared.copy());
		
//		((px + tdx - Cx)^2 + (py + tdy - Cy)^2 + (pz + tdz - Cz)^2 - r^2 - R^2)^2 + 4R^2((pz + tdz - Cz)^2 - r^2)
//																	 	 - R^2
		Term tLargeRSquared = new Term();
		tLargeRSquared.addVariable(minusLargeRSquared.copy());
		
//		((px + tdx - Cx)^2 + (py + tdy - Cy)^2 + (pz + tdz - Cz)^2 - r^2 - R^2)^2 + 4R^2((pz + tdz - Cz)^2 - r^2)
//			  	  											       - r^2 - R^2
		Expression constants = new Expression();
		constants.addTerm(tLittleRSquared.copy());
		constants.addTerm(tLargeRSquared.copy());
		System.out.println("constants: " + constants.toString());
		
//		((px + tdx - Cx)^2 + (py + tdy - Cy)^2 + (pz + tdz - Cz)^2 - r^2 - R^2)^2 + 4R^2((pz + tdz - Cz)^2 - r^2)
//		((px + tdx - Cx)^2 + (py + tdy - Cy)^2 + (pz + tdz - Cz)^2 - r^2 - R^2)^2
		Expression leftSide = new Expression();
		leftSide.addExpression(left.copy());
		leftSide.addExpression(middle.copy());
		leftSide.addExpression(right.copy());
		leftSide.addExpression(constants.copy());
		leftSide.simplify();
		leftSide = leftSide.pow(2);
		leftSide.simplify();
		System.out.println("leftSide: " + leftSide.toString());
		
//		((px + tdx - Cx)^2 + (py + tdy - Cy)^2 + (pz + tdz - Cz)^2 - r^2 - R^2)^2 + 4R^2((pz + tdz - Cz)^2 - r^2)
//															   						4R^2
		Term tFourLargeRSquared = new Term();
		tFourLargeRSquared.addVariable(fourLargeRSquared.copy());
		Expression eFourLargeRSquared = new Expression();
		eFourLargeRSquared.addTerm(tFourLargeRSquared.copy());
		
		//right is: (pz + tdz)^2
//		((px + tdx - Cx)^2 + (py + tdy - Cy)^2 + (pz + tdz - Cz)^2 - r^2 - R^2)^2 + 4R^2((pz + tdz - Cz)^2 - r^2)
//															   						4R^2((pz + tdz - Cz)^2 - r^2)

		Expression rightSide = right.copy();
		rightSide.addTerm(tLittleRSquared.copy());
		rightSide = rightSide.multiply(eFourLargeRSquared.copy());
		System.out.println("rightSide: " + rightSide.toString());
		
//		((px + tdx - Cx)^2 + (py + tdy - Cy)^2 + (pz + tdz - Cz)^2 - r^2 - R^2)^2 + 4R^2((pz + tdz - Cz)^2 - r^2)
		Expression answer = new Expression();
		answer.addExpression(leftSide);
		answer.addExpression(rightSide);
		answer.simplify();
		
		ExpressionOp EO = new ExpressionOp(answer,'+');
		ArrayList<ExpressionOp> list = new ArrayList<ExpressionOp>();
		list.add(EO);
		Equation e = new Equation(list);
		GenerateCode code = new GenerateCode(e);
		System.out.println(code.generateCode());
	}
}
