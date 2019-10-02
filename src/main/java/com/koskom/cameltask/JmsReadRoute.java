package com.koskom.cameltask;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

public class JmsReadRoute extends RouteBuilder {
    public void configure() {
        from("activemq:queue:testQueue")
                .choice()
                    .when(body().isNull())
                        .log(LoggingLevel.INFO,"Is NULL Message")
                    .when(body().in(""))
                        .log(LoggingLevel.INFO,"Is Empty Message")
                    .otherwise()
                        .unmarshal().base64()
                        .to("activemq:queue:testQueue2");
    }
}