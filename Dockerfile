FROM maven as builder
COPY . /chefsKiss
WORKDIR /chefsKiss
RUN ["mvn","package"]

FROM tomcat
COPY --from=builder /chefsKiss/target/chefsKiss-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
ENV DB_HOSTNAME=chefskiss_database
EXPOSE 8080