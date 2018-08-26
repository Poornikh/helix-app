package com.helix.app.dao;

import com.helix.app.model.Event;


public interface ProductDAO {
	
	void saveEvents(Event event);
	
	Event getEvent(String eventId);

	
	void deleteEvent(String eventId);
}
