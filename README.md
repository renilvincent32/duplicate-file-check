# duplicate-file-check
A simple console application, written in Java, to look for duplicate files in a given folder. The application would walk through the files in a given folder path, and identifies the potential duplicate candidates either by comparing w.r.t size, name or both size and name. Comparison mode is an optional argument provided by the user. Once the potential candidates are identified, we compare files using the MD5 hash and identifies the duplicate files.

## Assumptions
2 files are considered to be identical by name, if the initial 5 characters match.

## Preconditions
If you have JDK 17 installed on your machine, please checkout the code and run the following commands:
 - Goto the project root folder and run the following command
 - `java -jar out/artifacts/duplicate_check_program_jar/duplicate-check-program.jar ${folderPath} ${compareMode}`
 - Please provide folderPath as a relative or absolute path. The compare mode could be 0, 1 or 2.

If you do not have JDK 17 installed, you may run the service within the docker container. You need to have docker runtime installed in your machine.
 - Kindly copy the required test files into the testFiles folder in the project
 - Goto the project root folder and run the following command
 - `docker build . -t duplicate-service` - this creates a docker image
 - Once the image is successfully created, run the following command
 - `docker run duplicate-service {compareMode}`
 - Again, compareMode is optional. You could give 0, 1 or 2. By default, it takes 2
