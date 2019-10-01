import javax.jms.*;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

public class Main {
    public static void main(String[] args) throws Exception {

        JmsReadRoute routeBuilder = new JmsReadRoute();
        CamelContext ctx = new DefaultCamelContext();
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);

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
}
