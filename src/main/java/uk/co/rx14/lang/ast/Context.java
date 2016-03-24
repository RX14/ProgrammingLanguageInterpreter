package uk.co.rx14.lang.ast;

import java.util.Map;
import java.util.Optional;

public interface Context {
    Map<String, ASTNode> getContext();
    Optional<Context> getParentContext();
}
