package net.example;

import io.netty.channel.Channel;
import java.net.URI;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.glassfish.jersey.netty.httpserver.NettyHttpContainerProvider;
import org.glassfish.jersey.server.ResourceConfig;

public class Application {
    static final ConcurrentLinkedQueue<String> texts = new ConcurrentLinkedQueue<>();
    
    public static void main(String[] args) throws Exception {
        ResourceConfig rc = new ResourceConfig(ReceiverResource.class);
        Channel server = NettyHttpContainerProvider.createHttp2Server
            (URI.create("http://0.0.0.0:8081/"), rc, null);
        ReceiverThread thread = new ReceiverThread();

        try {
            thread.start();
            thread.join();
        } finally {
            server.close();
        }
    }
}
