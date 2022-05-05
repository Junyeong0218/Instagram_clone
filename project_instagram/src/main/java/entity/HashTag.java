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
public class HashTag {

	private int id;
	private String tag_name;
	private LocalDateTime create_date;
	private LocalDateTime update_date;
	
	// ------------------------------- is_session_user_follow_this_hash_tag?
	private boolean hash_tag_follow_flag;
}
