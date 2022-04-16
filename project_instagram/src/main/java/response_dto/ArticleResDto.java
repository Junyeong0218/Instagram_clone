package response_dto;

import java.time.LocalDateTime;
import java.util.List;

import entity.ArticleComment;
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
public class ArticleResDto {

	private int id;
	private int user_id;
	private String username;
	private boolean has_profile_image;
	private String file_name;
	private String feature;
	private String media_type;
	private String contents;
	private boolean stored;
	private boolean like_flag;
	private LocalDateTime article_create_date;
	
	private List<String> media_name_list;
	private int total_like_count;
	private int total_commented_user_count;
	
	private List<ArticleComment> article_comment_list;
	
}
