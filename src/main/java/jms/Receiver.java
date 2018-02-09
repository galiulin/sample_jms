package jms;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Receiver implements AutoCloseable {
    private ConnectionFactory factory = null;
    private Connection connection = null;
    private Session session = null;
    private Destination destination = null;
    private MessageConsumer consumer = null;

    public Receiver(String clientId, String queueName){
        factory = new ActiveMQConnectionFactory(
                ActiveMQConnectionFactory.DEFAULT_BROKER_URL);
        try {
            connection = factory.createConnection();
            connection.setClientID(clientId);
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            destination = session.createQueue(queueName);
            consumer = session.createConsumer(destination);
            connection.start();

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws JMSException {
        connection.close();
    }

    public String receiveMessage() throws JMSException {
        TextMessage message = (TextMessage) consumer.receive();
        return message.getText();
    }

}
