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
public class ArticleReaction {

	private int id;
	private int article_id;
	private int like_user_id;
	private LocalDateTime create_date;
	private LocalDateTime update_date;
	
}
