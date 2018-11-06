package org.davisr.spring.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class RestApi extends RouteBuilder {

    @Value("${server.port}")
    String serverPort;
    
	@Override
	public void configure() throws Exception {
		CamelContext context = new DefaultCamelContext();
		restConfiguration()
			.port(serverPort)
			.enableCORS(true)
			.apiContextPath("/api-doc")
			.apiProperty("api.title", "Test REST API")
			.apiProperty("api.version", "v1")
			.apiContextRouteId("doc-api")
			.component("servlet")
			.bindingMode(RestBindingMode.json)
			.dataFormatProperty("prettyPrint", "true");
			
		rest("/api/")
			.id("api-route")
			.consumes(MediaType.APPLICATION_JSON_VALUE)
			.produces(MediaType.APPLICATION_JSON_VALUE)
			.post("/bean")
			.bindingMode(RestBindingMode.auto)
			.type(MyBean.class)
			.to("direct:remoteService");
		
		from("direct:remoteService")
			.routeId("direct-route")
			.tracing()
			.log(">>> ${body.id}")
			.log(">>> ${body.name}")
			.process(new Processor() {
				@Override
				public void process (Exchange exchange) throws Exception {
					MyBean bodyIn = (MyBean) exchange.getIn().getBody();
					ExampleServices.example(bodyIn);
					exchange.getIn().setBody(bodyIn);
				}
			})
			.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(201));
	}

}
