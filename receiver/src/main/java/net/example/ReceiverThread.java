package net.example;

public class ReceiverThread extends Thread {
    public void run() {
        while (true) {
            try {
                receiveMessages();
            } catch (Exception e) {
            }
        }
    }

    private void receiveMessages() {
    }
}
