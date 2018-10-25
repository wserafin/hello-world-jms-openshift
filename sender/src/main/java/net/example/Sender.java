package net.example;

import io.netty.channel.Channel;
import java.net.URI;
import java.util.concurrent.LinkedBlockingQueue;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.netty.httpserver.NettyHttpContainerProvider;
import org.glassfish.jersey.server.ResourceConfig;

@Path("/api")
public class Sender {
    static final LinkedBlockingQueue<String> strings = new LinkedBlockingQueue<>();

    public static void main(String[] args) throws Exception {
        String host = System.getenv("HTTP_HOST");
        String port = System.getenv("HTTP_PORT");

        if (host == null) host = "0.0.0.0";
        if (port == null) port = "8080";

        URI uri = URI.create(String.format("http://%s:%s/", host, port));
        ResourceConfig rc = new ResourceConfig(Sender.class);
        Channel server = NettyHttpContainerProvider.createHttp2Server(uri, rc, null);
        SenderThread thread = new SenderThread();

        try {
            thread.start();
            thread.join();
        } finally {
            server.close();
        }
    }

    @POST
    @Path("/send")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String send(@FormParam("string") String string) {
        strings.add(string);
        return "OK\n";
    }

    @GET
    @Path("/health")
    @Produces(MediaType.TEXT_PLAIN)
    public String health() {
        return "OK\n";
    }
}
