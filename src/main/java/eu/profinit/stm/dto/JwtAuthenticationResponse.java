package eu.profinit.stm.dto;

import eu.profinit.stm.dto.user.UserInfo;
import lombok.Value;

@Value
public class JwtAuthenticationResponse {
	String accessToken;
	UserInfo user;
}