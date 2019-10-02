package com.koskom.cameltask;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.spi.ShutdownStrategy;

public class MessageDecoder {
    private String url;

    MessageDecoder(String url){
        this.url = url;
    }
    public void run() throws Exception {
        JmsReadRoute routeBuilder = new JmsReadRoute();
        CamelContext ctx = new DefaultCamelContext();
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);

        ctx.addComponent("activemq", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

        try {
            ctx.addRoutes(routeBuilder);
            ctx.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        MessageDecoder messageDecoder = new MessageDecoder (ActiveMQConnection.DEFAULT_BROKER_URL);
        messageDecoder.run ();
    }
}
