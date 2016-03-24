package uk.co.rx14.lang.ast;

import uk.co.rx14.lang.ast.type.Type;

public class FunctionCallNode  implements ASTNode {
    public final Context context;
    public final String functionName;

    public FunctionCallNode(Context context, String functionName) {
        this.context = context;
        this.functionName = functionName;
    }

    @Override
    public Type getType() {
        return null;
    }
}
