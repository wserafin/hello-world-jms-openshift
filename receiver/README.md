        mvn clean package dependency:copy-dependencies
        java -cp "target/classes:target/dependency/*" net.example.Main

        curl -X POST http://localhost:8081/receive
