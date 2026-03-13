package com.alaasmagi.restaurant_booking_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"spring.datasource.url=jdbc:sqlite:file:context-test?mode=memory&cache=shared",
		"spring.jpa.hibernate.ddl-auto=create-drop",
		"spring.jpa.show-sql=false"
})
class RestaurantBookingApiApplicationTests {

	@Test
	void contextLoads() {
	}

}
