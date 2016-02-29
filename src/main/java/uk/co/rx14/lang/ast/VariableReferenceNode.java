package uk.co.rx14.lang.ast;

public class VariableReferenceNode implements ASTNode {
    public final String variableName;

    public VariableReferenceNode(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public String toString() {
        return "@" + variableName;
    }
}
