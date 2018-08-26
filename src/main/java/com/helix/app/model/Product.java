package com.helix.app.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;


@Entity
@Table(name ="PRODUCT")
public class Product implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonIgnore
	private ProductPK pKey;
	
	@NotBlank(message="Product Name cannot be Blank")
	@JsonProperty(access = Access.WRITE_ONLY)
    private String name;
	@NotNull(message="Product Quantity cannot be Blank")
	@JsonProperty(access = Access.WRITE_ONLY)
    private Integer quantity;
	
	@NotNull(message="Product Sale Amount cannot be Blank")
    @JsonProperty(value="sale_amount",access = Access.WRITE_ONLY)
    private Double saleAmount;   
    @JsonIgnore
    private Event event;
    
    @NotNull(message="Id cannot be Blank")
    private Long id;
    
    @Id
    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "productId", column = @Column(name = "product_id", nullable = false)),
            @AttributeOverride(name = "eventId", column = @Column(name = "event_id", nullable = false)) })
    public ProductPK getpKey() {
		return pKey;
	}
	public void setpKey(ProductPK pKey) {
		this.pKey = pKey;
		this.id = pKey.getProductId();
	}
	
	@Transient
	public Long getId() {
		return id;
	}
	
	
	public void setId(Long id) {
		this.id=id;
	}
	
	@Column(name="Name", nullable=false)	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="Quantity", nullable=false)
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	@Column(name="sale_amount", nullable=false)
	public Double getSaleAmount() {
		return saleAmount;
	}
	public void setSaleAmount(Double saleAmount) {
		this.saleAmount = saleAmount;
	}
	
	@ManyToOne
    @JoinColumn(name="event_id", nullable = false, insertable = false, updatable = false)
	public Event getEvent() {
		return event;
	}
	
	public void setEvent(Event stock) {
		this.event = stock;
	}
	
}
