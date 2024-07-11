package com.fireflink.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class SearchHelper {
    Map<String, List<String>> filter;
    String searchByIdAndSummary;

}

