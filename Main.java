package org.example;

import java.io.IOException;
public class Main
{
    public static void main( String[] propertiesPath ) throws IOException {

        SuffixEditor suffixEditor = new SuffixEditor();
        suffixEditor.addSuffix(propertiesPath[0]);
    }
}
