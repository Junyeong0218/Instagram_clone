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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LatestSearchDetail {
	
	private int id;
	private int user_id;
	
	private int searched_user_id;
	private String username;
	private String name;
	private boolean has_profile_image;
	private String file_name;
	
	private int hash_tag_id;
	private String tag_name;
	
	private LocalDateTime create_date;
	private LocalDateTime update_date;
	
	private boolean user_follow_flag;
	private boolean hash_tag_follow_flag;
	
}
