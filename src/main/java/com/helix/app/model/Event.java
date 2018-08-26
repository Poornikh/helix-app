package com.helix.app.model;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.helix.app.custom.CustomDateDeserializer;
import com.helix.app.custom.CustomeDateSerializer;

@Entity
@Table(name ="EVENT")
public class Event implements Serializable {
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return "Event [id=" + id + ", timestamp=" + timestamp + ", products="
				+ products + "]";
	}
	
	@NotBlank(message="Id cannnot be empty")
	private String id;
	
	
	@NotNull(message="Event Timestamp property Cannot be empty")
	@JsonDeserialize(using=CustomDateDeserializer.class)
	@JsonSerialize(using=CustomeDateSerializer.class)
	private ZonedDateTime timestamp;
	@Valid
	@NotEmpty(message="Requires Atleast One Product Data")
	private Collection<Product> products= new ArrayList<Product>();
	
	@Id
	@Column(nullable=false, name="event_id", unique = true)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(nullable=false, name="event_timestamp")
	public ZonedDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(ZonedDateTime timestamp) {
		this.timestamp = timestamp;
	}
	
	@OneToMany(mappedBy="event",cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	public Collection<Product> getProducts() {
		return products;
	}
	public void setProducts(Collection<Product> products) {
		this.products = products;
	}

}
  