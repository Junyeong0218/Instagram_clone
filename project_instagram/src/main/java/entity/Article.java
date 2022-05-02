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
public class Article {

	private int id;
	private int user_id;
	private String feature;
	private String contents;
	private boolean is_stored;
	private LocalDateTime create_date;
	private LocalDateTime update_date;
	
}
