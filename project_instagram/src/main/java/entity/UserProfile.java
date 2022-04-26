package entity;

import java.time.LocalDateTime;
import java.util.List;

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

	private int user_id;
	private String username;
	private String name;
	private boolean has_profile_image;
	private String file_name;
	private int article_id;
	private String media_type;
	private String media_name;
	private boolean is_stored;
	private LocalDateTime create_date;
	private boolean follow_flag;
	private int following;
	private int follower;
}
