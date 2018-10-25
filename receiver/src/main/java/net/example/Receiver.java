package net.example;

import io.netty.channel.Channel;
import java.net.URI;
import java.util.concurrent.LinkedBlockingQueue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.netty.httpserver.NettyHttpContainerProvider;
import org.glassfish.jersey.server.ResourceConfig;

@Path("/api")
public class Receiver {
    static final LinkedBlockingQueue<String> strings = new LinkedBlockingQueue<>();

    public static void main(String[] args) throws Exception {
        String host = System.getenv("HTTP_HOST");
        String port = System.getenv("HTTP_PORT");

        if (host == null) host = "0.0.0.0";
        if (port == null) port = "8080";

        URI uri = URI.create(String.format("http://%s:%s/", host, port));
        ResourceConfig rc = new ResourceConfig(Receiver.class);
        Channel server = NettyHttpContainerProvider.createHttp2Server(uri, rc, null);
        ReceiverMessagingThread thread = new ReceiverMessagingThread();

        try {
            thread.start();
            thread.join();
        } finally {
            server.close();
        }
    }

    @POST
    @Path("/receive")
    @Produces(MediaType.TEXT_PLAIN)
    public String receive() {
        return String.format("%s\n", strings.poll());
    }

    @GET
    @Path("/health")
    @Produces(MediaType.TEXT_PLAIN)
    public String health() {
        return "OK\n";
    }
}
