package ca.jrvs.apps.grep;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface JavaGrep {

  /**
   * Top level search workflow
   *
   * @throws IOException
   */
  void process() throws IOException;

  /**
   * Traverse a given directory and return all files
   *
   * @param rootDir input directory
   * @return files under the rootDir
   */
  List<File> listFiles(String rootDir) throws IOException;

  /**
   * Read a file and return all the lines
   * <p>
   * FileReader -> Reads the data(in characters) from the files. FileReader class extends the
   * InputStreamReader class. BufferedReader -> Read characters from any character input stream such
   * as Files, String etc. and buffers the characters for efficient reading of text data. Both
   * FileReader and BufferedReader classes are from java.io package. Character Encoding -> Process
   * of assigning numbers to the human-readable characters, so that it can be stored, transmitted,
   * and transformed using digital computers.
   *
   * @param inputFile file to be read
   * @return lines
   * @throws IOException
   * @throws IllegalArgumentException if a given inputFile is not a file
   */
  List<String> readLines(File inputFile) throws IOException;

  /**
   * check if a line contains the regex pattern (passed by user)
   *
   * @param line input string
   * @return true if there is a match
   */
  boolean containsPattern(String line);

  /**
   * Write lines to a file
   * <p>
   * FileOutputStream-> Write streams of raw bytes(image data) to a file. OutputStreamWriter->
   * Convert character stream to byte stream, the characters are encoded into byte using a specified
   * charset. BufferedWriter-> Buffer the characters to character output stream. So, once the buffer
   * is full, it will write the data to the underlying output stream.
   *
   * @param lines matched line
   * @throws IOException if write failed
   */
  void writeToFile(List<String> lines) throws IOException;

  String getRootPath();

  void setRootPath(String rootPath);

  String getRegex();

  void setRegex(String regex);

  String getOutFile();

  void setOutFile(String outFile);
}
