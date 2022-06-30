package ca.jrvs.apps.grep;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class JavaGrepImp implements JavaGrep {

  final static Logger logger = LoggerFactory.getLogger(JavaGrepImp.class);

  private String regex;
  private String rootPath;
  private String outFile;

  public static void main(String[] args) {
    //Check for valid number of arguments
    if (args.length != 3) {
      throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
    }
    JavaGrepImp javaGrepImp = new JavaGrepImp();

    //setting the regex, rootpath and outFile instance variables
    javaGrepImp.setRegex(args[0]);
    javaGrepImp.setRootPath(args[1]);
    javaGrepImp.setOutFile(args[2]);

    try {
      javaGrepImp.process();
    } catch (Exception ex) {
      logger.error("Error: Unable to process", ex);
    }
  }

  /**
   * Top level search workflow
   * <p>
   * For each file in the rootPath directory, we will get the lines the matches with input regex
   * pattern and output the lines to the outFile.
   *
   * @throws IOException
   */
  @Override
  public void process() throws IOException {
    List<String> matchedLines = new ArrayList<>();

    //Gets the list of files for the rootPath directory
    List<File> files = listFiles(rootPath);
    List<String> file_lines;

    //Gets the lines that matches with the provided regex pattern for each of the file
    //and adds it to the matchedLines list.
    for (File file : files) {
      file_lines = readLines(file);
      for (String file_line : file_lines) {
        if (containsPattern(file_line)) {
          matchedLines.add(file_line);
        }
      }
    }
    writeToFile(matchedLines);
  }

  /**
   * Traverse a given directory and return all files
   *
   * @param rootDir input directory
   * @return files under the rootDir
   */
  @Override
  public List<File> listFiles(String rootDir) throws IOException {
    List<File> file_list = new ArrayList<>();
    File directory = new File(rootDir);
    listFilesRecursively(directory, file_list);
    return file_list;
  }

  //recursively searches for files in sub-folders
  private void listFilesRecursively(File dir, List<File> accumulator) {
    File[] files = dir.listFiles();

    for (File file : files) {
      if (file.isDirectory()) {
        listFilesRecursively(file, accumulator);
      } else {
        accumulator.add(file);
      }
    }
  }

  /**
   * Read a file and return all the lines
   *
   * @param inputFile file to be read
   * @return lines
   * @throws IOException
   * @throws IllegalArgumentException if a given inputFile is not a file
   */
  @Override
  public List<String> readLines(File inputFile) throws IllegalArgumentException, IOException {
    List<String> file_lines = new ArrayList<>();
    String current_line;

    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile));) {
      while ((current_line = bufferedReader.readLine()) != null) {
        file_lines.add(current_line);
      }
    }
    return file_lines;
  }

  /**
   * check if a line contains the regex pattern (passed by user)
   *
   * @param line input string
   * @return true if there is a match
   */
  @Override
  public boolean containsPattern(String line) {
    return line.matches(regex);
  }

  /**
   * Write lines to a file
   *
   * @param lines matched line
   * @throws IOException if write failed
   */
  @Override
  public void writeToFile(List<String> lines) throws IOException {
    try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outFile));) {
      for (String line : lines) {
        bufferedWriter.write(line + System.lineSeparator());
      }
    }
  }

  @Override
  public String getRootPath() {
    return rootPath;
  }

  @Override
  public void setRootPath(String rootPath) {
    this.rootPath = rootPath;
  }

  @Override
  public String getRegex() {
    return regex;
  }

  @Override
  public void setRegex(String regex) {
    this.regex = regex;
  }

  @Override
  public String getOutFile() {
    return outFile;
  }

  @Override
  public void setOutFile(String outFile) {
    this.outFile = outFile;
  }

}
