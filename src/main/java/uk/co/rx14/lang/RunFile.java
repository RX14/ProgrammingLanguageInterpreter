package uk.co.rx14.lang;

import org.pmw.tinylog.Logger;
import uk.co.rx14.lang.ast.ASTNode;
import uk.co.rx14.lang.interpreter.Interpreter;
import uk.co.rx14.lang.lexer.Lexer;
import uk.co.rx14.lang.parser.Parser;

import java.nio.file.Path;
import java.nio.file.Paths;

public class RunFile {
    public static void main(String[] args) {
        Path sourceFile = Paths.get(args[0]);

        Logger.info("Running file {}", sourceFile);

        Lexer lexer = new Lexer(sourceFile);
        ASTNode rootNode = new Parser(lexer).parse();
        new Interpreter().run(rootNode);
    }
}
