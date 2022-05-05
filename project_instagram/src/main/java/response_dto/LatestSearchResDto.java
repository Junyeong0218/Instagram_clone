package response_dto;

import java.util.List;

import entity.HashTag;
import entity.LatestSearchRecord;
import entity.User;
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
public class LatestSearchResDto {

	private List<LatestSearchRecord> latestSearchList;
	private List<User> userInfos;
	private List<HashTag> hashTags;
}
