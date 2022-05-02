package entity;

import java.time.LocalDateTime;

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
public class UserProfile {

	// ------------------------------ user_info
	private int user_id;
	private String username;
	private String name;
	private boolean has_profile_image;
	private String file_name;
	
	// ------------------------------ article_info
	private int article_id;
	private String media_type;
	private String media_name;
	private boolean is_stored;
	private LocalDateTime create_date;
	
	// ------------------------------ is_session_user_following_this_user?
	private boolean follow_flag;
	
	// ------------------------------ how_many_people_follow_this_user?
	private int following;
	
	// ------------------------------ how_many_people_followed_this_user?
	private int follower;
}
