package entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

	private int id;
	private String username;
	private String password;
	private String name;
	private String email;
	private String phone;
	private String website;
	private String description;
	private int gender;
	private boolean has_profile_image;
	private LocalDateTime last_username_update_date;
	private LocalDateTime create_date;
	private LocalDateTime update_date;
	
	// ---------------------------- profile_file_name
	private String file_name;
}
