package ca.jrvs.apps.grep;

public class JavaGrepLamdaImp extends JavaGrepImp{

  public static void main(String[] args) {
    //Check for valid number of arguments
    if (args.length != 3) {
      throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
    }
    JavaGrepLamdaImp javaGrepLamdaImp = new JavaGrepLamdaImp();

    //setting the regex, rootpath and outFile instance variables
    javaGrepLamdaImp.setRegex(args[0]);
    javaGrepLamdaImp.setRootPath(args[1]);
    javaGrepLamdaImp.setOutFile(args[2]);

    try {
      javaGrepLamdaImp.process();
    } catch (Exception ex) {
      logger.error("Error: Unable to process", ex);
    }
  }
}
