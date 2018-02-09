package jms;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class AgentMQ implements AutoCloseable {
    private ConnectionFactory factory = null;
    private Connection connection = null;
    private Session session = null;
    private Destination destinationProduction = null;
    private Destination destinationConsumer = null;
    private MessageProducer producer = null;
    private MessageConsumer consumer = null;

    public AgentMQ(String clientId, String queueProductionName, String queueConsumerName) {
        factory = new ActiveMQConnectionFactory(
                ActiveMQConnectionFactory.DEFAULT_BROKER_URL);
        try {
            connection = factory.createConnection();
            connection.setClientID(clientId);
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            destinationProduction = session.createQueue(queueProductionName);
            destinationConsumer = session.createQueue(queueConsumerName);
            producer = session.createProducer(destinationProduction);
            consumer = session.createConsumer(destinationConsumer);
            connection.start();


        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws JMSException {
        connection.close();
    }

    public void sendMessage(String message) throws JMSException {
        TextMessage textMessage = session.createTextMessage();
        textMessage.setText(message);
        producer.send(textMessage);
    }

    public String receiveMessage() throws JMSException {
        TextMessage message = (TextMessage) consumer.receive();
        return message.getText();
    }
}