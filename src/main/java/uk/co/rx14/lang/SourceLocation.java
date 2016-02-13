/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.rx14.lang;

import java.nio.file.Path;
import java.util.Objects;

/**
 *
 * @author CH14565
 */
public final class SourceLocation {
    public final Path fileLocation;
    public final String fileSource;
    public final int lineNumber;
    public final int columnNumber;

    public SourceLocation(Path fileLocation, String fileSource, int lineNumber, int columnNumber) {
        this.fileLocation = fileLocation;
        this.fileSource = fileSource;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    @Override
    public String toString() {
        return fileLocation.toString() + ":" + lineNumber + ":" + columnNumber;
    }

    public String getUntil(SourceLocation end) {
        if (!fileLocation.equals(end.fileLocation)) throw new IllegalArgumentException("fileLocation is in another file");
        return Util.getToken(fileSource, lineNumber, columnNumber, end.lineNumber, end.columnNumber);
    }

    public static class Builder {
        public Path fileLocation;
        public String fileSource;
        public int lineNumber = 1;
        public int columnNumber = 1;

        public SourceLocation build() {
            Objects.requireNonNull(fileLocation);
            Objects.requireNonNull(fileSource);

            return new SourceLocation(fileLocation, fileSource, lineNumber, columnNumber);
        }

        @Override
        public String toString() {
            return fileLocation.toString() + ":" + lineNumber + ":" + columnNumber;
        }
    }
}
