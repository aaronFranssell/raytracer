package primitives.cone;

import algebra.Equation;
import algebra.Expression;
import algebra.ExpressionOp;
import algebra.GenerateCode;
import algebra.Term;
import algebra.Variable;

public class GenerateConeCode
{
	public static void main(String args[])
	{
//cos(alpha)^2*dot(P-M,P-M)=dot(P-M,N)^2
//cos(alpha)^2*((Px-Mx)^2+(Py-My)^2+(Pz-Mz)^2)-((PxNx-MxNx)+(PyNy-MyNy)+(PzNz-MzNz))^2=0
//cos(alpha)^2*((Px-Mx)^2+(Py-My)^2+(Pz-Mz)^2)-((PxNx-MxNx)+(PyNy-MyNy)+(PzNz-MzNz))^2
//cos(alpha)^2*((Ex+TDx-Mx)^2+(Ey+TDy-My)^2+(Ez+TDz-Mz)^2)-(((Ex+TDx)Nx-MxNx)+((Ey+TDy)Ny-MyNy)+((Ez+TDz)Nz-MzNz))^2
//-cos(alpha)^2*((Ex+TDx-Mx)^2+(Ey+TDy-My)^2+(Ez+TDz-Mz)^2)+((ExNx+TDxNx-MxNx)+(EyNy+TDyNy-MyNy)+(EzNz+TDzNz-MzNz))^2
		
		//M is the vertex of the cone
		//alpha is the angle between the cone direction and its edge
		//N is the unit direction vector of the cone
		//P is the hit point
		
		//cosAlphaSquared is just a constant
		Variable cosAlphaSquared = new Variable("cosAlphaSquared", 1, -1.0);
		
		Variable t = new Variable("t", 1, 1.0);
		
		Variable Ex = new Variable("Ex", 1, 1.0);
		Variable Dx = new Variable("Dx", 1, 1.0);
		Variable Mx = new Variable("Mx", 1, -1.0);
		
		Variable Ey = new Variable("Ey", 1, 1.0);
		Variable Dy = new Variable("Dy", 1, 1.0);
		Variable My = new Variable("My", 1, -1.0);
		
		Variable Ez = new Variable("Ez", 1, 1.0);
		Variable Dz = new Variable("Dz", 1, 1.0);
		Variable Mz = new Variable("Mz", 1, -1.0);
		
		Variable Nx = new Variable("Nx", 1, 1.0);
		Variable Ny = new Variable("Ny", 1, 1.0);
		Variable Nz = new Variable("Nz", 1, 1.0);

//-cos(alpha)^2*((Ex+TDx-Mx)^2+(Ey+TDy-My)^2+(Ez+TDz-Mz)^2)+((ExNx+TDxNx-MxNx)+(EyNy+TDyNy-MyNy)+(EzNz+TDzNz-MzNz))^2
//-cos(alpha)^2
		Term tCosAlphaSquared = new Term();
		tCosAlphaSquared.addVariable(cosAlphaSquared.copy());
		Expression eCosAlphaSquared = new Expression();
		eCosAlphaSquared.addTerm(tCosAlphaSquared);
		System.out.println("eCosAlphaSquared: " + eCosAlphaSquared.toString());

//-cos(alpha)^2*((Ex+TDx-Mx)^2+(Ey+TDy-My)^2+(Ez+TDz-Mz)^2)+((ExNx+TDxNx-MxNx)+(EyNy+TDyNy-MyNy)+(EzNz+TDzNz-MzNz))^2
//              ((Ex+TDX-Mx)^2+(Ey+TDy-My)^2+(Ez+TDz-Mz)^2)
//			     (Ex+TDx-Mx)^2
		Term tEx = new Term();
		tEx.addVariable(Ex.copy());
		Term tTDx = new Term();
		tTDx.addVariable(t.copy());
		tTDx.addVariable(Dx.copy());
		Term tMx = new Term();
		tMx.addVariable(Mx.copy());
		Expression leftLeftDot = new Expression();
		leftLeftDot.addTerm(tEx.copy());
		leftLeftDot.addTerm(tTDx.copy());
		leftLeftDot.addTerm(tMx.copy());
		leftLeftDot = leftLeftDot.pow(2);
		System.out.println("leftLeftDot: " + leftLeftDot.toString());
		
//-cos(alpha)^2*((Ex+TDx-Mx)^2+(Ey+TDy-My)^2+(Ez+TDz-Mz)^2)+((ExNx+TDxNx-MxNx)+(EyNy+TDyNy-MyNy)+(EzNz+TDzNz-MzNz))^2
//              ((Ex+TDX-Mx)^2+(Ey+TDy-My)^2+(Ez+TDz-Mz)^2)
//                             (Ey+tDy-My)^2
		Term tEy = new Term();
		tEy.addVariable(Ey.copy());
		Term tTDy = new Term();
		tTDy.addVariable(t.copy());
		tTDy.addVariable(Dy.copy());
		Term tMy = new Term();
		tMy.addVariable(My.copy());
		Expression leftMiddleDot = new Expression();
		leftMiddleDot.addTerm(tEy.copy());
		leftMiddleDot.addTerm(tTDy.copy());
		leftMiddleDot.addTerm(tMy.copy());
		leftMiddleDot = leftMiddleDot.pow(2);
		System.out.println("leftMiddleDot: " + leftMiddleDot.toString());
		
//-cos(alpha)^2*((Ex+TDx-Mx)^2+(Ey+TDy-My)^2+(Ez+TDz-Mz)^2)+((ExNx+TDxNx-MxNx)+(EyNy+TDyNy-MyNy)+(EzNz+TDzNz-MzNz))^2
//              ((Ex+TDX-Mx)^2+(Ey+TDy-My)^2+(Ez+TDz-Mz)^2)
//										     (Ez+tDz-Mz)^2
		Term tEz = new Term();
		tEz.addVariable(Ez.copy());
		Term tTDz = new Term();
		tTDz.addVariable(t.copy());
		tTDz.addVariable(Dz.copy());
		Term tMz = new Term();
		tMz.addVariable(Mz.copy());
		Expression leftRightDot = new Expression();
		leftRightDot.addTerm(tEz.copy());
		leftRightDot.addTerm(tTDz.copy());
		leftRightDot.addTerm(tMz.copy());
		leftRightDot = leftRightDot.pow(2);
		System.out.println("leftRightDot: " + leftRightDot.toString());

//-cos(alpha)^2*((Ex+TDx-Mx)^2+(Ey+TDy-My)^2+(Ez+TDz-Mz)^2)+((ExNx+TDxNx-MxNx)+(EyNy+TDyNy-MyNy)+(EzNz+TDzNz-MzNz))^2
//              ((Ex+TDX-Mx)^2+(Ey+TDy-My)^2+(Ez+TDz-Mz)^2)		
		Expression leftDot = leftLeftDot.add(leftMiddleDot).add(leftRightDot);
		
//-cos(alpha)^2*((Ex+TDx-Mx)^2+(Ey+TDy-My)^2+(Ez+TDz-Mz)^2)+((ExNx+TDxNx-MxNx)+(EyNy+TDyNy-MyNy)+(EzNz+TDzNz-MzNz))^2
// cos(alpha)^2*((Ex+TDX-Mx)^2+(Ey+TDy-My)^2+(Ez+TDz-Mz)^2)
		Expression left = eCosAlphaSquared.multiply(leftDot);
		left.simplify();
		System.out.println("left: " + left.toString());

//-cos(alpha)^2*((Ex+TDx-Mx)^2+(Ey+TDy-My)^2+(Ez+TDz-Mz)^2)+((ExNx+TDxNx-MxNx)+(EyNy+TDyNy-MyNy)+(EzNz+TDzNz-MzNz))^2
//														   +((ExNx+TDxNx-MxNx)+(EyNy+TDyNy-MyNy)+(EzNz+TDzNz-MzNz))^2
//														     (ExNx+TDxNx-MxNx)
		Term tExNx = new Term();
		tExNx.addVariable(Ex.copy());
		tExNx.addVariable(Nx.copy());
		Term tTDxNx = new Term();
		tTDxNx.addVariable(Dx.copy());
		tTDxNx.addVariable(Nx.copy());
		tTDxNx.addVariable(t.copy());
		Term tMxNx = new Term();
		tMxNx.addVariable(Mx.copy());
		tMxNx.addVariable(Nx.copy());
		Expression rightLeftDot = new Expression();
		rightLeftDot.addTerm(tExNx.copy());
		rightLeftDot.addTerm(tTDxNx.copy());
		rightLeftDot.addTerm(tMxNx.copy());
		System.out.println("rightLeftDot: " + rightLeftDot.toString());
		
//-cos(alpha)^2*((Ex+TDx-Mx)^2+(Ey+TDy-My)^2+(Ez+TDz-Mz)^2)+((ExNx+TDxNx-MxNx)+(EyNy+TDyNy-MyNy)+(EzNz+TDzNz-MzNz))^2
//		  												   +((ExNx+TDxNx-MxNx)+(EyNy+TDyNy-MyNy)+(EzNz+TDzNz-MzNz))^2
//																			   (EyNy+TDyNy-MyNy)
		Term tEyNy = new Term();
		tEyNy.addVariable(Ey.copy());
		tEyNy.addVariable(Ny.copy());
		Term tTDyNy = new Term();
		tTDyNy.addVariable(Dy.copy());
		tTDyNy.addVariable(Ny.copy());
		tTDyNy.addVariable(t.copy());
		Term tMyNy = new Term();
		tMyNy.addVariable(My.copy());
		tMyNy.addVariable(Ny.copy());
		Expression rightMiddleDot = new Expression();
		rightMiddleDot.addTerm(tEyNy.copy());
		rightMiddleDot.addTerm(tTDyNy.copy());
		rightMiddleDot.addTerm(tMyNy.copy());
		System.out.println("rightMiddleDot: " + rightMiddleDot.toString());

//-cos(alpha)^2*((Ex+TDx-Mx)^2+(Ey+TDy-My)^2+(Ez+TDz-Mz)^2)+((ExNx+TDxNx-MxNx)+(EyNy+TDyNy-MyNy)+(EzNz+TDzNz-MzNz))^2
//														   +((ExNx+TDxNx-MxNx)+(EyNy+TDyNy-MyNy)+(EzNz+TDzNz-MzNz))^2
//																								 (EzNz+tDzNz-MzNz)
		Term tEzNz = new Term();
		tEzNz.addVariable(Ez.copy());
		tEzNz.addVariable(Nz.copy());
		Term tTDzNz = new Term();
		tTDzNz.addVariable(t.copy());
		tTDzNz.addVariable(Dz.copy());
		tTDzNz.addVariable(Nz.copy());
		Term tMzNz = new Term();
		tMzNz.addVariable(Mz.copy());
		tMzNz.addVariable(Nz.copy());
		Expression rightRightDot = new Expression();
		rightRightDot.addTerm(tEzNz.copy());
		rightRightDot.addTerm(tTDzNz.copy());
		rightRightDot.addTerm(tMzNz.copy());
		System.out.println("rightRightDot: " + rightRightDot.toString());

//-cos(alpha)^2*((Ex+TDx-Mx)^2+(Ey+TDy-My)^2+(Ez+TDz-Mz)^2)+((ExNx+TDxNx-MxNx)+(EyNy+TDyNy-MyNy)+(EzNz+TDzNz-MzNz))^2
//		  												   +((ExNx+TDxNx-MxNx)+(EyNy+TDyNy-MyNy)+(EzNz+TDzNz-MzNz))^2
//														    ((ExNx+TDxNx-MxNx)+(EyNy+TDyNy-MyNy)+(EzNz+TDzNz-MzNz))^2
		Expression right = rightLeftDot.add(rightMiddleDot.add(rightRightDot));
		right = right.pow(2);
		System.out.println("right: " + right.toString());
		
//-cos(alpha)^2*((Ex+TDx-Mx)^2+(Ey+TDy-My)^2+(Ez+TDz-Mz)^2)+((ExNx+TDxNx-MxNx)+(EyNy+TDyNy-MyNy)+(EzNz+TDzNz-MzNz))^2
		Expression answer = new Expression();
		answer = left.add(right);
		
		Equation equ = new Equation();
		equ.addExpressionOp(new ExpressionOp(answer,'+'));
		GenerateCode gen = new GenerateCode(equ);
		System.out.println("code: " + gen.generateCode());
	}
}
