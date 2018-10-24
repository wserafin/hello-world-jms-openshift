        mvn clean package dependency:copy-dependencies
        java -cp "target/classes:target/dependency/*" net.example.Application

        curl -X POST http://localhost:8081/receive
