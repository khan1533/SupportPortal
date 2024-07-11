package com.fireflink.utility;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class SimpleResponse {

    private int status;
    private String message;

}
