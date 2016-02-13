/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.rx14.lang;

/**
 *
 * @author CH14565
 */
public class Util {
    static String getToken(String source, int lineNumStart, int lineColStart, int lineNumEnd, int lineColEnd) {
        if (lineNumStart > lineNumEnd) return "";
        if (lineNumStart == lineNumEnd && lineColStart > lineColEnd) return "";

        int diffLine = lineNumEnd - lineNumStart;
        int diffCol = lineColEnd;
        if (diffLine == 0) {
            diffCol = lineColEnd - lineColStart;
        }

        int startIndex = readIndex(source, lineNumStart, lineColStart);
        int endIndex = readIndex(source, startIndex, diffLine + 1, diffCol + 1);
        
        return source.substring(startIndex, endIndex + 1);
    }

    static int readIndex(String source, int line, int col) {
        return readIndex(source, 0, line, col);
    }

    static int readIndex(String source, int startIndex, int line, int col) {
        int curLine = 1;
        int curCol = 1;

        int len = source.length();
        for (int i = startIndex; i < len; i++) {
            if (curLine == line && curCol == col) return i;

            char c = source.charAt(i);

            curCol++;
            if (c == '\n') {
                curCol = 0;
                curLine++;
            }
        }

        // line/col is never reached, return length
        return len;
    }
}
