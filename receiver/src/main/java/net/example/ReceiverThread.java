package net.example;

import java.util.Hashtable;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

public class ReceiverThread extends Thread {
    public void run() {
        while (true) {
            try {
                connect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void connect() throws Exception {
        String host = System.getenv("MESSAGING_SERVICE_HOST");
        String port = System.getenv("MESSAGING_SERVICE_PORT");
        String user = System.getenv("MESSAGING_SERVICE_USER");
        String password = System.getenv("MESSAGING_SERVICE_PASSWORD");

        if (host == null) host = "localhost";
        if (port == null) port = "5672";
        if (user == null) user = "example";
        if (password == null) password = "example";

        String url = String.format("failover:(amqp://%s:%s)", host, port);
        String address = "example";

        Hashtable<Object, Object> env = new Hashtable<Object, Object>();
        env.put("connectionfactory.factory1", url);

        InitialContext context = new InitialContext(env);
        ConnectionFactory factory = (ConnectionFactory) context.lookup("factory1");
        Connection conn = factory.createConnection();

        System.out.println(String.format("RECEIVER: Connecting to '%s'", url));

        conn.start();

        try {
            Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue(address);
            MessageConsumer consumer = session.createConsumer(queue);

            receive(consumer);
        } finally {
            conn.close();
        }
    }

    private void receive(MessageConsumer consumer) {
        while (true) {
            try {
                TextMessage message = (TextMessage) consumer.receive();
                String string = message.getText();

                Receiver.strings.add(string);

                System.out.println(String.format("RECEIVER: Received message '%s'", string));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
