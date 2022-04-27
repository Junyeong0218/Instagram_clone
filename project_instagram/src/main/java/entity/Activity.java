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
public class Activity {

	private int id;
	//--------------------------- acted_user 
	private int user_id;
	private String username;
	private boolean has_profile_image;
	private String file_name;
	//--------------------------- related_user
	private int related_user_id;
	private String related_username;
	private boolean related_user_has_profile_image;
	private String related_user_file_name;
	//--------------------------- activity_info
	private String activity_flag;
	private String activity_message;
	//--------------------------- related_article_info
	private ArticleDetail articleDetail;
	private ArticleComment articleComment;
	private Follow follow;
	//--------------------------- date
	private LocalDateTime create_date;
	private LocalDateTime update_date;
	
}
