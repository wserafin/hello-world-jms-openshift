package net.example;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("receive")
public class Receiver {
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String receive() {
        return "XXX\n";
    }
}
