package request_dto;

import entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CheckInputReqDto {

	private String email;
	private String phone;
	private String name;
	private String username;
	
	public User toEntity() {
		return User.builder()
							   .email(email)
							   .phone(phone)
							   .name(name)
							   .username(username)
							   .build();
	}
	
	public static CheckInputReqDto of(String paramName, String value) {
		if(paramName.equals("email")) return new CheckInputReqDto(value, null, null, null);
		else if(paramName.equals("phone")) return new CheckInputReqDto(null, value, null, null);
		else if(paramName.equals("username")) return new CheckInputReqDto(null, null, value, null);
		else return null;
	}
}
