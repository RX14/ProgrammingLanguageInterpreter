package uk.co.rx14.lang.ast;

import uk.co.rx14.lang.ast.type.NumberType;
import uk.co.rx14.lang.ast.type.Type;

public class FunctionNode implements ASTNode {
    public final String name;
    public final ASTNode body;

    public FunctionNode(String name, ASTNode body) {
        this.name = name;
        this.body = body;
    }

    @Override
    public Type getType() {
        return NumberType.INSTANCE;
    }
}
