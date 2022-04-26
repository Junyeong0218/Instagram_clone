package response_dto;

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
public class UserProfileResDto {

	private int user_id;
	private String username;
	private String name;
	private boolean has_profile_image;
	private String file_name;
	private List<ArticleResDto> article_list;
	private int article_count;
	private boolean follow_flag;
	private int following;
	private int follower;
}
