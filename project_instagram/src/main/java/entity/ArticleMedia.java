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
public class ArticleMedia {
	
	private int id;
	private int article_id;
	private String media_type;
	private String media_name;
	private LocalDateTime create_date;
	private LocalDateTime update_date;
	
}
