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
public class LatestSearchRecord {

	private int id;
	private int user_id;
	private int searched_user_id;
	private int hash_tag_id;
	private LocalDateTime create_date;
	private LocalDateTime update_date;
	
}
