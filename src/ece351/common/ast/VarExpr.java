package ece351.common.ast;

import ece351.common.visitor.ExprVisitor;
import ece351.util.Examinable;

public final class VarExpr extends Expr {
	
	public String identifier;
	
	public VarExpr(final String name){
		this.identifier = name;
	}
	
	public VarExpr(final Object name) {
		this((String)name);
	}

	@Override
	public final boolean repOk() {
		assert identifier != null : "identifier should not be null";
		assert identifier.length() > 0 : "identifier should be a non-empty string";
		return true;
	}


    public String toString() {
    	return this.identifier;
    }
    
    public Expr accept(final ExprVisitor v){
    	return v.visitVar(this);
    }

    @Override
    public int hashCode() {
    	return identifier.hashCode();
    }

	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof Examinable)) return false;
		return isomorphic((Examinable)obj);
	}

	@Override
	public boolean isomorphic(final Examinable obj) {
		// basics
		if (obj == null) return false;
		if (!getClass().equals(obj.getClass())) return false;
		final VarExpr that = (VarExpr) obj;
		// compare field values
// TODO: 1 lines snipped
		return (that.identifier.equals(this.identifier) 
				&& this.identifier.equals(that.identifier));
//throw new ece351.util.Todo351Exception();
	}

	public String operator() {
		return "var";
	}
}
