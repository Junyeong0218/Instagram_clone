package entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchKeyword {

	private int searched_user_id;
	private String username;
	private String name;
	private boolean has_profile_image;
	private String file_name;
	private boolean user_follow_flag;
	
	private int hash_tag_id;
	private String tag_name;
	private boolean hash_tag_follow_flag;
	
}
