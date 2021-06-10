# ActiveMQ Tool UI

A webapp "self-hosted" in a Java web application providing ActiveMQ Convenience Tools.


# TO BUILD

    $ mvn clean install


# TO USE

    $ PROJECT_VERSION="$(mvn -Dexec.executable=echo -Dexec.args='${project.version}' --non-recursive -q org.codehaus.mojo:exec-maven-plugin:1.6.0:exec)"
    $ java -jar "amq-tool-ui-app/target/amq-tool-ui-app-${PROJECT_VERSION}.jar"
    $ open http://localhost:8001/static

