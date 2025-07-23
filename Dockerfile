FROM openjdk:17
ADD target/sas_core_planner-1.0-SNAPSHOT.jar sas_core_planner.jar
ENTRYPOINT ["java", "-jar", "sas_core_planner.jar"]