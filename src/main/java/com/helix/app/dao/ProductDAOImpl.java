package com.helix.app.dao;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.helix.app.exception.EntityNotExistsException;
import com.helix.app.model.Event;
import com.helix.app.model.Product;
import com.helix.app.model.ProductPK;

@Component
public class ProductDAOImpl implements ProductDAO {

	private org.slf4j.Logger LOG = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private EntityManagerFactory entityManagerFactory;

	@Autowired
	private HibernateTemplate template;
		
	
	@Transactional
	public void saveEvents(Event event){
		for(Product product : event.getProducts()){
			product.setEvent(event);
			product.setpKey(new ProductPK(event.getId(), product.getId()));
		}
		
		Event evt = template.get(Event.class, event.getId());
		if(evt != null) {
			throw new EntityExistsException("Event Exists With same event Id " +event.getId() + ", Provide a Alternate Event Id");
		}
		LOG.info("Proceeding to Save Entity {} ", event);
		template.save(event);
		LOG.info("Saved Entity {} ", event);
		
	}

	@Override
	public Event getEvent(String eventId) {
		Event event = template.get(Event.class, eventId);
		
		if(event == null) {		
			throw new EntityNotExistsException("Event doesn't Exists.");
		}
		
		return event;
	}

	@Transactional
	@Override
	public void deleteEvent(String eventId) {
		template.delete(getEvent(eventId));		
	}
	
}
