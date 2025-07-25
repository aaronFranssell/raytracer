package primitives.cylinder;

import java.util.ArrayList;

import algebra.*;

public class BasicCylinder
{
	public static void main(String[] args)
	{
		Variable t = new Variable("t", 1,1.0);	
		Variable Ex = new Variable("Ex",1,1.0);
		Variable Ez = new Variable("Ez",1,1.0);
		Variable Dx = new Variable("Dx",1,1.0);
		Variable Dz = new Variable("Dz",1,1.0);
		Variable rSquared = new Variable("r",2,-1.0);
		
//((Ex + tPx)^2 + (Ez + tPz)^2) - r^2 = 0
//		 tPx
		Term XTDx = new Term();
		XTDx.addVariable(Dx.copy());
		XTDx.addVariable(t.copy());
		
//((Ex + tPx)^2 + (Ez + tPz)^2) - r^2 = 0
//  Ex
		Term XEx = new Term();
		XEx.addVariable(Ex.copy());
		
//((Ex + tPx)^2 + (Ez + tPz)^2) - r^2 = 0
// (Ex + tPx)^2
		Expression X = new Expression();
		X.addTerm(XEx.copy());
		X.addTerm(XTDx.copy());
		X = X.pow(2);
		System.out.println("X: " + X.toString());

//((Ex + tPx)^2 + (Ez + tPz)^2) - r^2 = 0
//				  		tPz
		Term ZTDz = new Term();
		ZTDz.addVariable(Dz.copy());
		ZTDz.addVariable(t.copy());
		
//((Ex + tPx)^2 + (Ez + tPz)^2) - r^2 = 0
//				   Ez
		Term ZEz = new Term();
		ZEz.addVariable(Ez.copy());

//((Ex + tPx)^2 + (Ez + tPz)^2) - r^2 = 0
//				  (Ez + tPz)^2
		Expression Z = new Expression();
		Z.addTerm(ZEz.copy());
		Z.addTerm(ZTDz.copy());
		Z = Z.pow(2);
		System.out.println("Z: " + Z.toString());
		
//((Ex + tPx)^2 + (Ez + tPz)^2) - r^2 = 0
//								- r^2
		Term TRSquared = new Term();
		TRSquared.addVariable(rSquared.copy());
		Expression ERSquared = new Expression();
		ERSquared.addTerm(TRSquared.copy());
		System.out.println("r squared: " + ERSquared.toString());
		
//((Ex + tPx)^2 + (Ez + tPz)^2) - r^2 = 0		
		Expression total = X.copy().add(Z.copy()).add(ERSquared.copy());
		ExpressionOp EO = new ExpressionOp(total,'+');
		ArrayList<ExpressionOp> list = new ArrayList<ExpressionOp>();
		list.add(EO);
		Equation e = new Equation(list);
		GenerateCode code = new GenerateCode(e);
		System.out.println(code.generateCode());
	}

}
