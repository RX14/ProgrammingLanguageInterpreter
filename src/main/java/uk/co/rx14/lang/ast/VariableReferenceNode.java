package uk.co.rx14.lang.ast;

import uk.co.rx14.lang.ast.type.NumberType;
import uk.co.rx14.lang.ast.type.Type;

public class VariableReferenceNode implements ASTNode {
    public final Context context;
    public final String variableName;

    public VariableReferenceNode(Context context, String variableName) {
        this.context = context;
        this.variableName = variableName;
    }

    @Override
    public Type getType() {
        return NumberType.INSTANCE;
    }

    @Override
    public String toString() {
        return "@" + variableName;
    }
}
