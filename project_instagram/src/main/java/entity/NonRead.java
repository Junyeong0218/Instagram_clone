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
public class NonRead {

	private int log_id;
	private boolean related_user_read_flag;
	
	private int message_id;
	private boolean message_read_flag;
	
}
