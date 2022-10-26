FROM openjdk:17-jdk-slim
MAINTAINER renilvincent
ADD testFiles /testFiles
COPY out/artifacts/duplicate_check_program_jar/duplicate-check-program.jar application.jar
ENTRYPOINT ["java", "-jar", "application.jar", "/testFiles"]
CMD ["2"]