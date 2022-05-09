package service;

import java.util.List;
import java.util.Map;

import entity.Message;
import entity.RoomInfo;

public interface MessageService {

	public Map<String, List<?>> selectRecentMessages(int user_id);
	
	public boolean insertDirectTextMessage(int user_id, int target_user_id, String contents);
	
	public List<Message> insertNewRoom(int user_id, List<Integer> target_user_ids);
	
	public List<RoomInfo> selectRoomInfoForInit(int user_id);
}
