package com.fireflink.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class RequestTicketDto {

	@NotNull
	private String summary;
	@NotNull
	private String issueRelated;

	private String description;
	@NotNull
	private String severity;
	@NotNull
	private String priority;
	@NotNull
	private String licence;

	private List<MultipartFile> files;
	
}
