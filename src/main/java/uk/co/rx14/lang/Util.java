/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.rx14.lang;

/**
 * @author CH14565
 */
public class Util {
    public static String getToken(String source, int lineNumStart, int lineColStart, int lineNumEnd, int lineColEnd) {
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

    public static int readIndex(String source, int line, int col) {
        return readIndex(source, 0, line, col);
    }

    public static int readIndex(String source, int startIndex, int line, int col) {
        int curLine = 1;
        int curCol = 1;

        int len = source.length();
        for (int i = startIndex; i < len; i++) {
            if (curLine == line && curCol == col) return i;

            char c = source.charAt(i);

            if (c == '\n') {
                curCol = 0;
                curLine++;
            }
            curCol++;
        }

        // line/col is never reached, return index of last char
        return len - 1;
    }

    public static String getLines(SourceLocation start, SourceLocation end) {
        if (!start.fileSource.equals(end.fileSource)) {
            throw new UnsupportedOperationException("start and end must be in the same file");
        }

        String line = getToken(start.fileSource, start.lineNumber, 1, end.lineNumber + 1, 1);
        if (line.charAt(line.length() - 1) != '\n') {
            // Last char is first letter of line after, remove it and newline preceding
            line = line.substring(0, line.length() - 2);
        } else {
            // Last char is newline at end of file, remove newline
            line = line.substring(0, line.length() - 1);
        }

        return line;
    }

    public static String getLine(SourceLocation sourceLocation) {
        return getLines(sourceLocation, sourceLocation);
    }

    public static String repeat(String s, int n) {
        return new String(new char[n]).replace("\0", s);
    }

    public static String normaliseSource(String source) {
        // Replace tabs with 4 spaces
        source = source.replace("\t", "    ");

        // Normalise line endings
        source = source.replace("\r", "\n").replace("\r\n", "\n");

        // Ensure newline at end of file
        if (source.charAt(source.length() - 1) != '\n') {
            source = source + "\n";
        }

        return source;
    }
}
