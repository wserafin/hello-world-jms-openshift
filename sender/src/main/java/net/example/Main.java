package net.example;

import io.netty.channel.Channel;
import java.net.URI;
import org.glassfish.jersey.netty.httpserver.NettyHttpContainerProvider;
import org.glassfish.jersey.server.ResourceConfig;

public class Main {
    public static void main(String[] args) throws Exception {
        ResourceConfig rc = new ResourceConfig(Sender.class);
        Channel server = NettyHttpContainerProvider.createHttp2Server
            (URI.create("http://0.0.0.0:8080/"), rc, null);
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> { server.close(); }));
        
        Thread.currentThread().join();
    }
}
