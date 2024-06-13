FROM tomcat
COPY /home/runner/work/chefsKiss/chefsKiss/target/chefsKiss-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
ENV DB_HOSTNAME=chefskiss_database
EXPOSE 8080
