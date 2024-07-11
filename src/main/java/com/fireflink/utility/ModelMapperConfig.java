package com.fireflink.utility;

import com.fireflink.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

		@Bean
	    public ModelMapper modelMapper() {
	        return new ModelMapper();
	    }
		
		@Bean
		public User user() {
			return new User();
		}
}
