package ece351.vhdl;

import org.parboiled.Rule;

import ece351.common.ast.AndExpr;
import ece351.common.ast.AssignmentStatement;
import ece351.common.ast.ConstantExpr;
import ece351.common.ast.EqualExpr;
import ece351.common.ast.Expr;
import ece351.common.ast.NAndExpr;
import ece351.common.ast.NOrExpr;
import ece351.common.ast.NotExpr;
import ece351.common.ast.OrExpr;
import ece351.common.ast.VarExpr;
import ece351.common.ast.XNOrExpr;
import ece351.common.ast.XOrExpr;
import ece351.util.CommandLine;
import ece351.vhdl.ast.Architecture;
import ece351.vhdl.ast.Component;
import ece351.vhdl.ast.DesignUnit;
import ece351.vhdl.ast.Entity;
import ece351.vhdl.ast.IfElseStatement;
import ece351.vhdl.ast.Process;
import ece351.vhdl.ast.VProgram;

//Parboiled requires that this class not be final
public/* final */class VParser extends VBase {

	public static void main(final String arg) {
		main(new String[] { arg });
	}

	public static void main(final String[] args) {
		final CommandLine c = new CommandLine(args);
		VProgram vprog = parse(c.readInputSpec());
		System.out.println(vprog);
	}
	 public static VProgram parse(final String[] args) {
	    	final CommandLine c = new CommandLine(args);
	    	return parse(c.readInputSpec());
	    }
	
	public static VProgram parse(final String arg) {
		return (VProgram) process(VParser.class, arg).resultValue;
	}

	public Rule Program() {
		// TODO: Write a VHDL parser that pushes an instance of VProgram to the
		// top of the stack when it is done parsing
		// For the grammar production Id, ensure that the Id does not match any
		// of the keywords specified
		// in the rule, 'Keyword'
// TODO: 248 lines snipped
throw new ece351.util.Todo351Exception();
	}

}