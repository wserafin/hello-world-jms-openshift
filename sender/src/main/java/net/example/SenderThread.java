package net.example;

public class SenderThread extends Thread {
    public void run() {
        while (true) {
            try {
                sendMessages();
            } catch (Exception e) {
            }
        }
    }

    private void sendMessages() {
    }
}
