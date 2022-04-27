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
public class Follow {

	private int id;
	private int user_id;
	private int partner_user_id;
	private String follower_group;
	private LocalDateTime follow_date;
	private LocalDateTime update_date;
	
}
