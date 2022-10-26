# duplicate-file-check
A simple console application, written in Java, to check for duplicate files in a given folder. The application would scan through the files in a given folder path, and identifies the potential duplicate candidates either by comparing w.r.t size, name or both size and name. Comparison mode is an optional argument provided by the user. Once the potential candidates are identified, we compare files using the MD5 hash and identifies the duplicate files.

## Assumptions
2 files are considered to be identical by name, if the initial 5 characters match.

## Preconditions
If you have JDK 17 installed on your machine, please checkout the code and run the following commands:
 - Goto the project root folder and run the following command
 - `java -jar out/artifacts/duplicate_check_program_jar/duplicate-check-program.jar ${folderPath} ${compareMode}`
 - Please provide folderPath as a relative or absolute path. The compare mode could be 0, 1 or 2.

If you do not have JDK 17 installed, you may run the service within the docker container. You need to have docker runtime installed in your machine.
 - Kindly copy the required test files into the testFiles folder in the project. This is an extremely important step.
 - Goto the project root folder and run the following command
 - `docker build . -t duplicate-service` - this creates a docker image. You may verify image is created successfully by running `docker images`.
 - Once the image is successfully created, run the following command
 - `docker run duplicate-service {compareMode}` - this runs the docker container based on the image created before. Compare mode can be passed as a runtime argument into the container
 - Again, compareMode is optional. You could give 0, 1 or 2. By default, it takes 2

## Expected Results
Duplicate files are logged in the console itself, along with the Compare mode. Please find an example below.

`Compare mode: NAME
Duplicate files: [/testFiles/firstFile_copy2.txt, /testFiles/firstFile_copy.txt]
Duplicate files: [/testFiles/firstFile copy.txt, /testFiles/firstFile.txt]`

## Cleanup
You can stop the docker containers by running the following commands:
 - `docker stop $(docker ps -a -q)`
 - `docker rm $(docker ps -a -q)  `
 - `docker rmi duplicate-service`
