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
public class Message {

	private int id;
	private int room_id;
	private int user_id;
	private String contents;
	private LocalDateTime create_date;
	private LocalDateTime update_date;
	private boolean delete_flag;
	private LocalDateTime delete_date;
	
	// ------------------------------------- image_flag_and_image_id
	private boolean is_image;
	private int image_id;
	private String file_name;
	
	private boolean reaction_flag;
	
}
