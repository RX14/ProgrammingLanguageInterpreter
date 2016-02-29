package uk.co.rx14.lang.interpreter;

import uk.co.rx14.lang.ast.ASTNode;
import uk.co.rx14.lang.ast.ConstNumNode;
import uk.co.rx14.lang.ast.ExpressionNode;

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
        } else {
            throw new IllegalArgumentException("Not numeric.");
        }

        return val;
    }

    private BigDecimal runExpressionNode(ASTNode node) {
        assertType(node, ExpressionNode.class);

        ExpressionNode expr = (ExpressionNode) node;
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

    private void assertType(ASTNode node, Class clazz) {
        if (node.getClass() != clazz) {
            throw new AssertionError("Node is not " + clazz.getName());
        }
    }
}
