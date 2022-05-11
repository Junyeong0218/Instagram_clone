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
	public boolean insertDirectTextMessage(int user_id, int room_id, String contents) {
		return messageDao.insertDirectTextMessage(user_id, room_id, contents) > 0 ? true : false;
	}
	
	@Override
	public boolean insertDirectImageMessage(int user_id, int room_id, String file_name) {
		return messageDao.insertDirectImageMessage(user_id, room_id, file_name) > 1 ? true : false;
	}
	
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
		return messageDao.selectMessages(room_id);
	}
	
	@Override
	public List<Integer> toggleMessageReaction(int user_id, int message_id) {
		return messageDao.toggleMessageReaction(user_id, message_id);
	}
}
