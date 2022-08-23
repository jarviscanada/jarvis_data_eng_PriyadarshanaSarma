package ca.jrvs.apps.grep;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaGrepLamdaImp extends JavaGrepImp {

  final static Logger logger = LoggerFactory.getLogger(JavaGrepLamdaImp.class);

  public static void main(String[] args) {
    JavaGrepLamdaImp javaGrepLamdaImp = new JavaGrepLamdaImp();

    if (args.length != 3) {
      throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
    }
    javaGrepLamdaImp.setRegex(args[0]);
    javaGrepLamdaImp.setRootPath(args[1]);
    javaGrepLamdaImp.setOutFile(args[2]);

    try {
      javaGrepLamdaImp.process();
    } catch (Exception ex) {
      logger.error("Error: Unable to process", ex);
    }
  }

  @Override
  public void process() throws IOException {
    Pattern pattern = Pattern.compile(getRegex());
    try (BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(getOutFile()))) {

      //returns a stream of file paths
      Files.walk(Paths.get(getRootPath()))

          //check if the file is a normal file
          .filter(Files::isRegularFile)
          .flatMap(p -> {
            try {
              return Files.lines(p);
            } catch (IOException e) {
              logger.error(e.getMessage());
            }
            return Stream.empty();
          }).filter(line -> pattern.matcher(line).matches())
          .forEach(line -> {
            try {
              bufferedWriter.write(line);
              bufferedWriter.newLine();
            } catch (IOException e) {
              logger.error(e.getMessage());
            }
          });
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
