package response_dto;

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
public class RecentStoryResDto {
	
	private int id;
	private String username;
	private String name;
	private boolean has_profile_image;
	private String file_name;
	private int story_id;
	
}
