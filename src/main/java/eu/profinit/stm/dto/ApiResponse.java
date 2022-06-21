package eu.profinit.stm.dto;

import lombok.Value;

@Value
public class ApiResponse {
	Boolean success;
	String message;
}