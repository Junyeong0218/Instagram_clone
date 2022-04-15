package entity;

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
	private String username;
	private String feature;
	private String media_type;
	private String description;
	private boolean stored;
	private String media_name;
}
