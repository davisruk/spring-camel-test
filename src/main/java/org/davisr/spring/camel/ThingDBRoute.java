package org.davisr.spring.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ThingDBRoute extends RouteBuilder {

    @Value("${server.port}")
    String port;

    @Value("${server.address}")
    String host;

    @Override
	public void configure() throws Exception {
    	restConfiguration()
    		.host(host)
    		.port(port)
    		.bindingMode(RestBindingMode.auto);
    	
    	rest("/things")
    		.post()
    			.type(Thing.class)
    			.to("direct:createThing")
    		.get()
    			.outType(ThingSearchResults.class)
    			.to("direct:getThings")
    		.get("/{id}")
    			.outType(Thing.class)
    			.to("direct:getThing")
    		.delete("/{id}")
    			.outType(Thing.class)
    			.to("direct:removeThing");
    	
    	from("direct:createThing")
    		.to("jpa:org.davisruk.spring.camel.Thing");
    	
    	from("direct:getThing")
    		.to("sql:select * from THING where id = :#${header.id}?dataSource=dataSource&outputType=SelectOne")
    		.bean("transformer", "mapThing");
    	
    	from("direct:getThings")
    		.setProperty("query")
    		.method("transformer", "constructQuery(${headers})")
    		.toD("sql:${property.query}?dataSource=dataSource")
    		//.transform().body(ThingSearchResults.class, Transformer::mapThingSearchResults);
    		.bean("transformer", "mapThingSearchResults");
    	
    	from("direct:removeThing")
    		.to("direct:getThing")
    		.setProperty("thing", body())
    		.to("sql:delete from THING where id = :#${body.id}?dataSource=dataSource")
    		.setBody(exchangeProperty("thing"));
	}

}
