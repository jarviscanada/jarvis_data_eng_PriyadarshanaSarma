# Introduction

The Java Grep application is an implementation of the Linux Grep command. Similar to the grep
command,
the application recursively searches each file in the specified root directory for lines that match
the user-specified regular expression pattern. The matching lines are printed to another specified
file.
The application was developed using Apache Maven, core java features and libraries like regex,
lambda, Stream API in the IntelliJ IDE.
It was deployed using docker.

# Quick Start

The following steps are to be followed since the application is distributed using docker:
- `docker pull priyasarma14/grep`
- `docker run --rm -v `pwd`/data:/data -v `
pwd`/log:/log priyasarma14/grep [regex_pattern] [src_dir] [outfile]`

# Implemenation

## Pseudocode

    Pseudocode for process method:
        matchedLines = []
        for file in listFilesRecursively(rootDir)
            for line in readLines(file)
                if containsPattern(line)
                    matchedLines.add(line)
        writeToFile(matchedLines)

## Performance Issue

The memory issue arises when the data to be read is larger than the heap memory's capacity, and it
will result in
OutOfMemoryError. It can be fixed by updating the grep app implementation by replacing list of lines
with BufferedReader or Stream APIs.

# Test

The application has been tested with different input combinations like giving valid input, invalid
number of arguments,
non-matching regex pattern, large input file with by setting the minimum and maximum heap size.

# Deployment

The application was deployed using Docker. A dockerfile was created which contains the fat jar file.
A docker image was created using `docker build` command and pushed it to DockerHub using the
`docker push` command.

# Improvement

- The application can only be run through CLI.
- Files can be scanned in parallel for faster processing.
- Output containing the file name in which the line was found.