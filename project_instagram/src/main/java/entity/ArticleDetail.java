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
public class ArticleDetail {

	private int article_id;
	private int article_user_id;
	private String article_username;
	private boolean article_user_has_profile_image;
	private String article_user_file_name;
	private String feature;
	private String media_type;
	private String contents;
	private boolean is_stored;
	private LocalDateTime create_date;
	private String media_name; // many
	private int like_user_count;
	private boolean like_flag;
	
	private int total_commented_user_count;
	
	private int comment_id; // article_comment
	private int commented_user_id; // article_comment
	private String commented_username; // article_comment
	
	private boolean commented_user_has_profile_image;
	private String commented_user_file_name;
	
	private String comment_contents;  // article_comment
	private LocalDateTime comment_create_date;  // article_comment
	
	private boolean comment_like_flag;
	
	private int related_comment_count; // article_comment
	private int comment_like_user_count; // article_comment
	
}
