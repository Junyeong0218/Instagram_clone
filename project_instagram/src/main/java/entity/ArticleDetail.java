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

	// -------------------------------- article_info
	private int article_id;
	private int article_user_id;
	private String article_username;
	private boolean article_user_has_profile_image;
	private String article_user_file_name;
	private String feature;
	private String media_type;
	//--------------------------------- media_name_to_List<String>
	private String media_name;
	private String contents;
	private boolean is_stored;
	private LocalDateTime create_date;
	
	// --------------------------------- how_many_people_like_this_article
	private int like_user_count;
	
	// --------------------------------- is_session_user_like_this_article?
	private boolean like_flag;
	
	// --------------------------------- how_many_people_commented_in_this_article?
	private int total_commented_user_count;
	
	// --------------------------------- comment_info_for_article_comment
	private int comment_id;
	private int commented_user_id;
	private String commented_username;
	private boolean commented_user_has_profile_image;
	private String commented_user_file_name;
	private String comment_contents;
	private LocalDateTime comment_create_date;
	
	// ---------------------------------- is_session_user_like_this_comment?
	private boolean comment_like_flag;
	
	// ---------------------------------- how_many_repies_about_this_comment
	private int related_comment_count;
	
	// ---------------------------------- how_many_people_like_this_comment 
	private int comment_like_user_count;
	
	// ---------------------------------- how_many_articles_about_this_has_tag
	private int related_article_count;
	
}
