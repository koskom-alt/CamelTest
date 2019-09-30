import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.DataFormatDefinition;

import static org.apache.activemq.camel.component.ActiveMQComponent.activeMQComponent;
public class JmsReadRoute extends RouteBuilder {
    public void configure() throws Exception {
        from("activemq:queue:testQueue")
                .to("log:?level=INFO&showBody=true")
                .unmarshal().base64()
                .to("activemq:queue:readQueue")
                .to("log:?level=INFO&showBody=true");
    }
}