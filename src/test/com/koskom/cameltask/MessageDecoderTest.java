package com.koskom.cameltask;



import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import javax.jms.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class MessageDecoderTest {

    public String url;
    public String inQueue;
    public String outQueue;
    public TextMessage mess;
    public MessageProducer messageProducer;
    public MessageDecoder messageDecoder;
    public Session session;

    @BeforeEach
    public void prepareTestBroker() throws Exception {

        url = "vm://localhost?broker.persistent=false";
        //url = ActiveMQConnection.DEFAULT_BROKER_URL;
        inQueue = "testQueue";
        outQueue = "testQueue2";

        String example = "Message example";

        byte[] helloBytes = example.getBytes(StandardCharsets.UTF_8);
        String encodedString = Base64.getEncoder().encodeToString(helloBytes);

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        final javax.jms.Connection connection = connectionFactory.createConnection ( );
        session = connection.createSession (false, Session.AUTO_ACKNOWLEDGE);

        connection.start();

        Queue destQueue = new ActiveMQQueue(inQueue);
        messageProducer = session.createProducer(destQueue);
        mess = session.createTextMessage();

        mess.setText(encodedString);

        messageProducer.send(mess);
        System.out.println("mess" + mess.getText());
        messageDecoder = new MessageDecoder(url);
        messageDecoder.run();

    }

    @Test
    void checkMessagesBodyInOutgoingQueues() throws JMSException {

        Destination destination = session.createQueue (outQueue);
        MessageConsumer consumer = session.createConsumer(destination);
        String body = "";
        Message message = consumer.receive();
        if (message instanceof TextMessage) {
            TextMessage textmessage = (TextMessage) message;
            body = textmessage.getText();
            System.out.println("isTextMess");
        }else if (message instanceof BytesMessage){
            System.out.println("isByteMess");
            BytesMessage byteMessage = (BytesMessage) message;
            byte[] byteData = null;
            byteData = new byte[(int) byteMessage.getBodyLength()];
            byteMessage.readBytes(byteData);
            byteMessage.reset();
            body =  new String(byteData);
        }

        Assertions.assertNotNull(message);

        Assertions.assertEquals("Message example", body);

    }

    @AfterEach
    public void stopDuplicator(){
        //System.exit(1);
    }
}
