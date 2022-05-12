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
public class RoomInfo {

	private int id;
	private int room_id;
	
	// ----------------------------------- entered_user_info
	private int user_id;
	private String username;
	private String name;
	private boolean has_profile_image;
	private String file_name;
	
	// ----------------------------------- last_message_info
	private int message_id;
	private String contents;
	private LocalDateTime create_date;
	
	// ----------------------------------- non_read_message_info
	private int all_message_count;
	private int read_message_count;
	
}
