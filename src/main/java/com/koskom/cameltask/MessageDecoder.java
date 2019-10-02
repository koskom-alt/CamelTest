package com.koskom.cameltask;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

public class MessageDecoder {
    private String brokerUrl;

    MessageDecoder(String brokerUrl){
        this.brokerUrl = brokerUrl;
    }

    public void run() throws Exception {
        JmsReadRoute routeBuilder = new JmsReadRoute();
        CamelContext ctx = new DefaultCamelContext();
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);

        ctx.addComponent("activemq", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

        try {
            ctx.addRoutes(routeBuilder);
            ctx.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            ctx.stop();
        }
    }

    public static void main(String[] args) {

        MessageDecoder messageDecoder = new MessageDecoder (ActiveMQConnection.DEFAULT_BROKER_URL);
        try {
            messageDecoder.run ();
        } catch (Exception e) {
            e.printStackTrace ( );
        }

    }
}
