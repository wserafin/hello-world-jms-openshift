        mvn clean package dependency:copy-dependencies
        java -cp "target/classes:target/dependency/*" net.example.Main

        curl -X POST -d "text=Hello World!" http://localhost:8080/send
