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
public class ArticleComment {
	
	// ---------------------- comment_id
	private int id;
	
	// ---------------------- related_article_id
	private int article_id;
	private int user_id;
	private String username;
	private boolean has_profile_image;
	private String file_name;
	
	// ---------------------- comment_info
	private String contents;
	private LocalDateTime create_date;
	private LocalDateTime update_date;
	private boolean deleted_flag;
	private LocalDateTime deleted_date;
	private boolean related_flag;
	private int related_comment_id;
	
	// ---------------------- is_session_user_like_this_comment?
	private boolean like_flag;
	
	// ---------------------- reply_count
	private int related_comment_count;
	
	// ---------------------- how_many_people_like_this_comment
	private int comment_like_user_count;
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ArticleComment) {
			ArticleComment temp = (ArticleComment) obj;
			if(temp.getId() == this.id) {
				return true;
			}
		} else if(obj instanceof Integer) {
			int temp = (int) obj;
			if(temp == this.id) {
				return true;
			}
		}
		return false;
	}
	
}
