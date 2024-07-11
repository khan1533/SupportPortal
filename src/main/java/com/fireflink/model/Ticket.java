package com.fireflink.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import lombok.Data;
import org.springframework.data.mongodb.core.index.TextIndexed;

import java.util.List;

@Data
public class Ticket  extends BaseEntity{

	@Id
	private String id;
	private String summary;
	private String status;
	private String issueRelated;
	private String assignedTo;
	private String assignedToEmail;
	private String assignedToPrivilege;
	private String description;
	private String severity;
	private String priority;
	private List<String> supportingDocumentIds;
	
	
	@Transient
	public static final String SEQUENCE_NAME="ticket_sequence";

}
