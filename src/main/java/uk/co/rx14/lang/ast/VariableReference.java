package uk.co.rx14.lang.ast;

public class VariableReference implements ASTNode {
    public final String variableName;

    public VariableReference(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public String toString() {
        return "@" + variableName;
    }
}
