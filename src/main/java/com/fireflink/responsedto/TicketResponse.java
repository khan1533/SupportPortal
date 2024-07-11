package com.fireflink.responsedto;

import lombok.Data;

@Data
public class TicketResponse {

	private String id;
	private String summary;
	private String status;
	private String issueRelated;
	private String createdByName;
	private String createdOn;
	private String assignedTo;
	private String description;
	private String severity;
	private String priority;
	
}
