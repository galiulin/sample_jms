package jms;

import javax.jms.JMSException;
import java.util.Scanner;

public class ClentSecond {
    public static void main(String[] args) {

        try (AgentMQ agentMQ = new AgentMQ("second_client", "second_client", "first_client")) {
            Thread threadListener = new Thread(() -> {
                while (true) {
                    try {
                        System.out.println(agentMQ.receiveMessage());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });
            threadListener.start();

            Scanner scanner = new Scanner(System.in);
            while (true) {
                String message = scanner.nextLine();
                agentMQ.sendMessage(message);
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
