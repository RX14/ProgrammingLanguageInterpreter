/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.rx14.lang;

import uk.co.rx14.lang.lexer.Lexer;
import uk.co.rx14.lang.parser.Parser;

import java.nio.file.Paths;
import java.util.Scanner;

/**
 * @author CH14565
 */
public class REPL {

    static Scanner in = new Scanner(System.in);

    static String prompt() {
        System.out.print("> ");
        return in.nextLine();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        while (true) {
            run(prompt());
        }
    }

    static void run(String line) {
        line = Util.normaliseSource(line);
        try {
            Lexer l = new Lexer(line, Paths.get("repl"));
            new Parser(l).parse();
        } catch (SyntaxError s) {
            s.printStackTrace(System.out);
        }
    }
}
