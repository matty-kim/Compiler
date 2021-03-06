package ece351.vhdl;

import ece351.common.ast.AndExpr;
import ece351.common.ast.AssignmentStatement;
import ece351.common.ast.ConstantExpr;
import ece351.common.ast.EqualExpr;
import ece351.common.ast.Expr;
import ece351.common.ast.NAndExpr;
import ece351.common.ast.NOrExpr;
import ece351.common.ast.NaryAndExpr;
import ece351.common.ast.NaryOrExpr;
import ece351.common.ast.NotExpr;
import ece351.common.ast.OrExpr;
import ece351.common.ast.VarExpr;
import ece351.common.ast.XNOrExpr;
import ece351.common.ast.XOrExpr;
import ece351.common.visitor.PostOrderExprVisitor;
import ece351.f.ast.FProgram;
import ece351.util.CommandLine;
import ece351.vhdl.ast.DesignUnit;
import ece351.vhdl.ast.IfElseStatement;
import ece351.vhdl.ast.Process;
import ece351.vhdl.ast.Statement;
import ece351.vhdl.ast.VProgram;

/**
 * Translates VHDL to F.
 */
public final class Synthesizer extends PostOrderExprVisitor {
	
	private String varPrefix;
	private int condCount;
	private static String conditionPrefix = "condition";
	
	public static void main(String[] args) { 
		System.out.println(synthesize(args));
	}
	
	public static FProgram synthesize(final String[] args) {
		return synthesize(new CommandLine(args));
	}
	
	public static FProgram synthesize(final CommandLine c) {
        final VProgram program = DeSugarer.desugar(c);
        return synthesize(program);
	}
	
	public static FProgram synthesize(final VProgram program) {
		VProgram p = Splitter.split(program);
		final Synthesizer synth = new Synthesizer();
		return synth.synthesizeit(p);
	}
	
	public Synthesizer(){
		condCount = 0;
	}
		
	private FProgram synthesizeit(final VProgram root) {	
		FProgram result = new FProgram();
			// set varPrefix for this design unit
// TODO: 22 lines snipped

		for(DesignUnit designunit: root.designUnits)
		{
			this.varPrefix = designunit.arch.entityName;
			for(Statement statement: designunit.arch.statements)
			{
				if(statement instanceof Process)
				{
					for(Statement seqstatement: ((Process)statement).sequentialStatements)
					{
						if(seqstatement instanceof IfElseStatement)
						{
							for(AssignmentStatement child_stmt: implication((IfElseStatement)seqstatement).formulas)
								result = result.append(child_stmt);
						}
						else if(seqstatement instanceof AssignmentStatement)
						{
								result = result.append(new AssignmentStatement((VarExpr)traverseExpr(((AssignmentStatement)seqstatement).outputVar), 
										traverseExpr(((AssignmentStatement)seqstatement).expr)));
						}
					}
				}
				else if(statement instanceof IfElseStatement)
				{
					for(AssignmentStatement child_stmt: implication((IfElseStatement)statement).formulas)
						result = result.append(child_stmt);
				}
				else if(statement instanceof AssignmentStatement)
				{
						result = result.append(new AssignmentStatement((VarExpr)traverseExpr(((AssignmentStatement)statement).outputVar), 
								traverseExpr(((AssignmentStatement)statement).expr)));
				}
			}
		}
		return result;
//throw new ece351.util.Todo351Exception();
	}
	
	private FProgram implication(final IfElseStatement statement) {
		// error checking
		if( statement.ifBody.size() != 1) {
			throw new IllegalArgumentException("if/else statement: " + statement + "\n can only have one assignment statement in the if-body and else-body where the output variable is the same!");
		}
		if (statement.elseBody.size() != 1) {
			throw new IllegalArgumentException("if/else statement: " + statement + "\n can only have one assignment statement in the if-body and else-body where the output variable is the same!");
		}
		final AssignmentStatement ifb = statement.ifBody.get(0);
		final AssignmentStatement elb = statement.elseBody.get(0);
		if (!ifb.outputVar.equals(elb.outputVar)) {
			throw new IllegalArgumentException("if/else statement: " + statement + "\n can only have one assignment statement in the if-body and else-body where the output variable is the same!");
		}

		// build result
// TODO: 10 lines snipped
		FProgram result = new FProgram();
		condCount++;
		VarExpr condition = new VarExpr(Synthesizer.conditionPrefix + condCount);
		result = result.append(new AssignmentStatement(condition, traverseExpr(statement.condition)));
		NotExpr not_condition = new NotExpr(condition);
		VarExpr outputvar = ifb.outputVar;
		AndExpr inner_And = new AndExpr(not_condition,traverseExpr(elb.expr));
		AndExpr outer_And = new AndExpr(traverseExpr(ifb.expr),condition);	
		OrExpr Or = new OrExpr(inner_And,outer_And);
		result = result.append(new AssignmentStatement((VarExpr)traverseExpr(outputvar),(Expr)Or));
		return result;
//throw new ece351.util.Todo351Exception();
	}

	/** Rewrite var names with prefix to mitigate name collision. */
	@Override
	public Expr visitVar(final VarExpr e) {
		return new VarExpr(varPrefix + e.identifier);
	}
	
	@Override public Expr visitConstant(ConstantExpr e) { return e; }
	@Override public Expr visitNot(NotExpr e) { return e; }
	@Override public Expr visitAnd(AndExpr e) { return e; }
	@Override public Expr visitOr(OrExpr e) { return e; }
	@Override public Expr visitNaryAnd(NaryAndExpr e) { return e; }
	@Override public Expr visitNaryOr(NaryOrExpr e) { return e; }
	
	// We shouldn't see these in the AST, since F doesn't support them
	// They should have been desugared away previously
	@Override public Expr visitXOr(XOrExpr e) { throw new IllegalStateException("xor not desugared"); } 
	@Override public Expr visitEqual(EqualExpr e) { throw new IllegalStateException("EqualExpr not desugared"); }
	@Override public Expr visitNAnd(NAndExpr e) { throw new IllegalStateException("nand not desugared"); }
	@Override public Expr visitNOr(NOrExpr e) { throw new IllegalStateException("nor not desugared"); }
	@Override public Expr visitXNOr(XNOrExpr e) { throw new IllegalStateException("xnor not desugared"); }
	
}



