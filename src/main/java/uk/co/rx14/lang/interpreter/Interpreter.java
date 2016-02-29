package uk.co.rx14.lang.interpreter;

import uk.co.rx14.lang.ast.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Interpreter {
    private final Map<String, BigDecimal> ctx = new HashMap<>();

    public void run(ASTNode node) {
        String val;

        try {
            val = runNumeric(node).toString();
        } catch (IllegalArgumentException ignored) {
            throw new IllegalArgumentException("wat");
        }

        System.out.println("VAL: " + val);
    }

    private BigDecimal runNumeric(ASTNode node) {
        BigDecimal val;
        Class c = node.getClass();

        if (c == ConstNumNode.class) {
            val = ((ConstNumNode) node).value;
        } else if (c == ExpressionNode.class) {
            val = runExpressionNode(node);
        } else if (c == AssignmentNode.class) {
            val = runAssignmentNode(node);
        } else if (c == VariableReferenceNode.class) {
            val = runVariableReferenceNode(node);
        } else {
            throw new IllegalArgumentException("Not numeric.");
        }

        return val;
    }

    private BigDecimal runExpressionNode(ASTNode node) {
        ExpressionNode expr = assertType(node, ExpressionNode.class);

        BigDecimal lhs = runNumeric(expr.lhs);
        BigDecimal rhs = runNumeric(expr.rhs);

        switch (expr.operator) {
            case ADD:
                return lhs.add(rhs);
            case DIVIDE:
                return lhs.divide(rhs, BigDecimal.ROUND_HALF_UP);
            case EXPONENT:
                return lhs.pow(rhs.intValue());
            case MULTIPLY:
                return lhs.multiply(rhs);
            case SUBTRACT:
                return lhs.subtract(rhs);
            default:
                throw new AssertionError("Impossible.");
        }
    }

    private BigDecimal runAssignmentNode(ASTNode node) {
        AssignmentNode ass = assertType(node, AssignmentNode.class);
        BigDecimal val = runNumeric(ass.rhs);

        ctx.put(ass.variableName, val);
        return val;
    }

    private BigDecimal runVariableReferenceNode(ASTNode node) {
        VariableReferenceNode var = assertType(node, VariableReferenceNode.class);
        BigDecimal val = ctx.get(var.variableName);

        if (val == null) {
            throw new RuntimeException("Variable " + var.variableName + " is undefined");
        }

        return val;
    }

    private <T extends ASTNode> T assertType(ASTNode node, Class<T> clazz) {
        if (clazz.isAssignableFrom(node.getClass())) {
            return (T) node;
        } else {
            throw new AssertionError("Node is not " + clazz.getName());
        }
    }
}
