package org.davisr.spring.camel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component("transformer")
public class Transformer {
	public static Thing mapThing (Map<String, Object> map) {
		return Thing.builder()
						.id((Integer)map.get("id"))
						.name((String)map.get("name"))
						.owner((String)map.get("owner"))
						.build();
	}
	
	public static ThingSearchResults mapThingSearchResults (List<Map<String, Object>> body){
		return ThingSearchResults.builder()
									.size(body.size())
									.things(mapThings(body))
									.build();
	}
	
	private static List<Thing> mapThings (List<Map<String, Object>> body){
		List<Thing> things = new ArrayList<Thing>();
		body.forEach(map -> things.add(Transformer.mapThing(map)));
		return things;
	}
	
	public static String constructQuery(Map<String, Object> h) {
		String query = "select * from THING ";
		String wheres = h.entrySet()
							.stream()
							.filter(e -> e.getKey().equals("owner") || e.getKey().equals("name"))
							.map(e -> e.getKey() + " = '" + e.getValue().toString() + "'")
								.collect(Collectors.joining(" and "));
		if (wheres == null || wheres.length() == 0)
			return query;
		System.out.println(wheres);
		return query + "where " + wheres;
	}
}
