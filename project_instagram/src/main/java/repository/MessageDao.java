package repository;

import java.util.List;
import java.util.Map;

import entity.Message;
import entity.RoomInfo;

public interface MessageDao {

	public Map<String, List<?>> selectRecentMessages(int user_id);
	
	public int insertDirectTextMessage(int user_id, int target_user_id, String contents);
	
	public int selectSpecificRoomId(int user_id, List<Integer> target_user_ids);
	
	public int insertNewRoom(int user_id, List<Integer> target_user_ids);
	
	public List<Message> selectMessages(int room_id);
	
	public List<RoomInfo> selectRoomInfoForInit(int user_id);
}
