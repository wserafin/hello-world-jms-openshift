        mvn clean package dependency:copy-dependencies
        java -cp "target/classes:target/dependency/*" net.example.Application

        curl -X POST --data "text=Hello World!" http://localhost:8080/send
