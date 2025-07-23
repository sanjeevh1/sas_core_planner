FROM openjdk:17
ADD target/sas_core_planner-1.0-SNAPSHOT.jar sas_core_planner.jar
ENTRYPOINT ["java", "-jar", "sas_core_planner.jar"]
CMD ["src/main/resources/AHo.csv", "src/main/resources/AHp.csv", "src/main/resources/AHq.csv", "src/main/resources/AHr.csv", "src/main/resources/CCD.csv", "src/main/resources/CCO.csv", "src/main/resources/HST.csv", "src/main/resources/NS.csv", "src/main/resources/QQ.csv", "src/main/resources/QR.csv", "src/main/resources/SCL.csv", "src/main/resources/WC.csv", "src/main/resources/WCd.csv", "src/main/resources/WCr.csv"]