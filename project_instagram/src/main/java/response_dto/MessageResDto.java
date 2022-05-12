package response_dto;

import java.time.LocalDateTime;
import java.util.List;

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
public class MessageResDto {

	// ------------------------------------ message_id
	private int id;
	private int room_id;
	private int user_id;
	private String contents;
	private boolean is_image;
	private int image_id;
	private String file_name;
	private List<Integer> like_users;
	private LocalDateTime create_date;
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof MessageResDto) {
			MessageResDto dto = (MessageResDto) obj;
			if(dto.id == this.id) {
				return true;
			}
		}
		return false;
	}
	
}
