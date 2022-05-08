package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import entity.Room;
import entity.RoomInfo;
import repository.MessageDao;

public class MessageServiceImpl implements MessageService {
	
	private MessageDao messageDao;
	
	public MessageServiceImpl(MessageDao messageDao) {
		this.messageDao = messageDao;
	}

	@Override
	public Map<String, List<?>> selectRecentMessages(int user_id) {
		Map<String, List<?>> messageInfo = messageDao.selectRecentMessages(user_id);
		return null;
	}
	
	@Override
	public boolean insertDirectTextMessage(int user_id, int target_user_id, String contents) {
		return messageDao.insertDirectTextMessage(user_id, target_user_id, contents) > 0 ? true : false;
	}
	
	@Override
	public int insertNewRoom(int user_id, List<Integer> target_user_ids) {
		List<RoomInfo> roomInfoList = messageDao.selectSpecificRoom(user_id, target_user_ids);
		List<Room> rooms = new ArrayList<Room>();
		if(roomInfoList.size() == 0) {
			int result = 0;
			
			// insert
			return result;
		} else {
			// search room and insert if not exist
			boolean isExist = false;
			for(RoomInfo roomInfo : roomInfoList) {
				Room roomId = Room.builder().room_id(roomInfo.getRoom_id()).build();
				if(rooms.contains( roomId )) {
					List<Integer> entered_user_list = rooms.get(rooms.indexOf( roomId )).getEntered_user_list();
					entered_user_list.add(roomInfo.getEntered_user_id());
				} else {
					Room room = new Room();
					List<Integer> entered_user_list = new ArrayList<Integer>();
					entered_user_list.add(roomInfo.getEntered_user_id());
					room.setRoom_id(roomInfo.getRoom_id());
					room.setEntered_user_list(entered_user_list);
					room.setEntered_users_count(roomInfo.getEntered_users_count());
				}
			}
			for(Room room : rooms) {
				if(room.getEntered_user_list().containsAll(target_user_ids) && 
					room.getEntered_user_list().contains(user_id) &&
					room.getEntered_users_count() == target_user_ids.size() + 1) {
					isExist = true;
					break;
				}
			}
			if(isExist) {
				return 1;
			} else {
				// insert
			}
		}
		
		return 0;
	}
}
