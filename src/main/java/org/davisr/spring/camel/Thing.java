package org.davisr.spring.camel;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Entity(name="THING")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class Thing {
	@Id @GeneratedValue @Column(name="ID")
	private Integer id;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="OWNER")
	private String owner;
}

@Data
@Builder
class ThingSearchResults {
	Integer size;
	@Singular("thing")
	List<Thing> things;
	
}
