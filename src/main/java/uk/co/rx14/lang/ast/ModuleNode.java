package uk.co.rx14.lang.ast;

import uk.co.rx14.lang.ast.type.Type;
import uk.co.rx14.lang.ast.type.VoidType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ModuleNode implements ASTNode, Context {
    public final Optional<ModuleNode> parentModule;
    public final Map<String, ASTNode> context = new HashMap<>();

    public ModuleNode(ModuleNode parentModule) {
        this.parentModule = Optional.ofNullable(parentModule);
    }

    @Override
    public Map<String, ASTNode> getContext() {
        return context;
    }

    @Override
    public Optional<Context> getParentContext() {
        return parentModule.map(mod -> (Context)mod);
    }

    @Override
    public Type getType() {
        return VoidType.INSTANCE;
    }
}
