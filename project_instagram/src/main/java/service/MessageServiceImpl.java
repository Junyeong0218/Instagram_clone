package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import entity.Message;
import entity.RoomInfo;
import entity.User;
import repository.MessageDao;
import response_dto.RoomSummaryResDto;

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
	
//	@Override
//	public List<Message> insertNewRoom(int user_id, List<Integer> target_user_ids) {
//		List<RoomInfo> roomInfoList = messageDao.selectSpecificRoom(user_id, target_user_ids);
//		List<Room> rooms = new ArrayList<Room>();
//		if(roomInfoList.size() == 0) {
//			// insert
//			int room_id = messageDao.insertNewRoom(user_id, target_user_ids);
//			if(room_id != 0) {
//				// select
//				
//				
//			}
//			return null;
//		} else {
//			// search room and insert if not exist
//			int room_id = 0;
//			for(RoomInfo roomInfo : roomInfoList) {
//				Room roomId = Room.builder().room_id(roomInfo.getRoom_id()).build();
//				if(rooms.contains( roomId )) {
//					List<Integer> entered_user_list = rooms.get(rooms.indexOf( roomId )).getEntered_user_list();
//					entered_user_list.add(roomInfo.getEntered_user_id());
//				} else {
//					Room room = new Room();
//					List<Integer> entered_user_list = new ArrayList<Integer>();
//					entered_user_list.add(roomInfo.getEntered_user_id());
//					room.setRoom_id(roomInfo.getRoom_id());
//					room.setEntered_user_list(entered_user_list);
//					room.setEntered_users_count(roomInfo.getEntered_users_count());
//				}
//			}
//			for(Room room : rooms) {
//				if(room.getEntered_user_list().containsAll(target_user_ids) && 
//					room.getEntered_user_list().contains(user_id) &&
//					room.getEntered_users_count() == target_user_ids.size() + 1) {
//					room_id = room.getRoom_id();
//					break;
//				}
//			}
//			if(room_id != 0) {
//				// select
//				List<Message> messages = 
//				
//				return null;
//			} else {
//				// insert
//				room_id = messageDao.insertNewRoom(user_id, target_user_ids);
//				if(room_id != 0) {
//					// select
//					
//					
//				}
//				return null;
//			}
//		}
//	}
	
	@Override
	public List<Message> insertNewRoom(int user_id, List<Integer> target_user_ids) {
		int room_id = messageDao.selectSpecificRoomId(user_id, target_user_ids);
		
		if(room_id == 0) {
			room_id = messageDao.insertNewRoom(user_id, target_user_ids);	
		}
		return selectMessages(room_id);
	}
	
	@Override
	public List<RoomSummaryResDto> selectRoomInfoForInit(int user_id) {
		List<RoomInfo> rooms = messageDao.selectRoomInfoForInit(user_id);
		List<RoomSummaryResDto> dtos = new ArrayList<RoomSummaryResDto>();
		
		for(RoomInfo room : rooms) {
			RoomSummaryResDto room_id = RoomSummaryResDto.builder().room_id(room.getRoom_id()).build();
			if( dtos.contains( room_id ) ) {
				List<User> users = dtos.get(dtos.indexOf( room_id )).getEntered_users();
				User user = User.builder().id(room.getUser_id())
																 .username(room.getUsername())
																 .name(room.getName())
																 .has_profile_image(room.isHas_profile_image())
																 .file_name(room.getFile_name())
																 .build();
				users.add(user);
			} else {
				RoomSummaryResDto dto = new RoomSummaryResDto();
				dto.setRoom_id(room.getRoom_id());
				List<User> users = new ArrayList<User>();
				User user = User.builder().id(room.getUser_id())
																 .username(room.getUsername())
																 .name(room.getName())
																 .has_profile_image(room.isHas_profile_image())
																 .file_name(room.getFile_name())
																 .build();
				users.add(user);
				dto.setEntered_users(users);
				Message message = Message.builder().id(room.getId())
																						 .contents(room.getContents())
																						 .create_date(room.getCreate_date())
																						 .build();
				dto.setMessage(message);
				dtos.add(dto);
			}
		}
		return dtos;
	}
	
	@Override
	public List<Message> selectMessages(int room_id) {
		List<Message> messages = messageDao.selectMessages(room_id);
		
//		messages = messages.stream().filter(new Predicate<Message>() {
//																		@Override
//																		public boolean test(Message message) {
//																			if(message.getId() == 0) {
//																				return false;
//																			}
//																			return true;
//																		}
//																	})
//																	.collect(Collectors.toList());
		return messages;
	}
}
