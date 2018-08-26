package com.helix.app.advice;

import java.time.format.DateTimeParseException;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.helix.app.controller.EventController;
import com.helix.app.exception.EntityExistsException;
import com.helix.app.exception.EntityNotExistsException;
import com.helix.app.exception.InvalidDateFormatException;

@EnableWebMvc
@ControllerAdvice(basePackageClasses=EventController.class)
public class BadRequestAdvice extends ResponseEntityExceptionHandler  {
	
	
	private org.slf4j.Logger LOG = LoggerFactory.getLogger(this.getClass());
	@Override
	@ResponseBody
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
	    HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		String errorDesc = "";
		int index =0;
		for(FieldError error : ex.getBindingResult().getFieldErrors()) {
			errorDesc += index != 0 ? ",  " : "";
			index++;
			errorDesc += index + ")." + "Field Name : " + error.getField() + " - " + error.getDefaultMessage();
		}
		com.helix.app.model.Error errorDetails = new com.helix.app.model.Error("Validation Failed", errorDesc);
		LOG.info("Binding Error", ex.getBindingResult());
	    return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
	  } 
	
	  @ResponseBody
	  @ExceptionHandler({JsonMappingException.class, JsonParseException.class, DateTimeParseException.class})	  
	  public final ResponseEntity<Object> handleParseExceptions(Exception ex, WebRequest request) {
		  com.helix.app.model.Error errorDetails = new com.helix.app.model.Error(ex.getCause().getMessage(), request.getDescription(false));
		  LOG.error("Exception Occured", ex);
	    return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
	  }
	  
	  @Override
	  protected ResponseEntity<Object> handleHttpMessageNotReadable(
				HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		  Throwable thr = ex.getRootCause();
		  com.helix.app.model.Error errorDetails = new com.helix.app.model.Error("", request.getDescription(false));
		  if(thr instanceof InvalidDateFormatException) {
			 errorDetails = new com.helix.app.model.Error(ex.getRootCause().getMessage(), request.getDescription(false));
		  } else{
			  errorDetails = new com.helix.app.model.Error("Invalid Json Format", request.getDescription(false));
		  }		  
		  LOG.error("Message Not Readable", ex);
	    return new ResponseEntity(errorDetails, status);
	  }
	  
	 
	
	  @ResponseBody
	  @ExceptionHandler({Exception.class, EntityExistsException.class, EntityNotExistsException.class})
	  public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
		  com.helix.app.model.Error errorDetails = new com.helix.app.model.Error(ex.getMessage(), request.getDescription(false));
		  LOG.error("Exception Occured", ex);
	    return new ResponseEntity(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	  }

}
