package response_dto;

import java.util.List;

import entity.Message;
import entity.User;
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
public class RoomSummaryResDto {

	private int room_id;
	private List<User> entered_users;
	private Message message;
	private int all_message_count;
	private int read_message_count;
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof RoomSummaryResDto) {
			RoomSummaryResDto dto = (RoomSummaryResDto) obj;
			if(dto.room_id == this.room_id) {
				return true;
			}
		}
		return false;
	}
	
}
