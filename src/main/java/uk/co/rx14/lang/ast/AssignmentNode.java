package uk.co.rx14.lang.ast;

public class AssignmentNode implements ASTNode {
    public final String variableName;
    public final ASTNode rhs;

    public AssignmentNode(String variableName, ASTNode rhs) {
        this.variableName = variableName;
        this.rhs = rhs;
    }

    @Override
    public String toString() {
        return "<" + variableName + " = " + rhs.toString() + ">";
    }
}
