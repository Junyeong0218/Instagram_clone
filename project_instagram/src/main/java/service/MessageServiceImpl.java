package service;

import java.util.ArrayList;
import java.util.List;

import config.FileUploadPathConfig;
import entity.Message;
import entity.NonReadActivities;
import entity.RoomInfo;
import entity.User;
import repository.MessageDao;
import repository.NewActivityDao;
import response_dto.MessageResDto;
import response_dto.RoomSummaryResDto;

public class MessageServiceImpl implements MessageService {
	
	private MessageDao messageDao;
	private NewActivityDao newActivityDao;
	
	public MessageServiceImpl(MessageDao messageDao) {
		this.messageDao = messageDao;
	}
	
	public MessageServiceImpl(MessageDao messageDao, NewActivityDao newActivityDao) {
		this.messageDao = messageDao;
		this.newActivityDao = newActivityDao;
	}
	
	@Override
	public boolean insertDirectTextMessage(int user_id, int room_id, String contents) {
		int result = messageDao.insertDirectTextMessage(user_id, room_id, contents);
		if(result == 1) {
			List<Message> messages = newActivityDao.selectNewMessage(user_id, room_id);
			if(messages.size() > 0) {
				NonReadActivities.addNonReadMessages(messages);
			}
		}
		return result > 0 ? true : false;
	}
	
	@Override
	public boolean insertDirectImageMessage(int user_id, int room_id, String file_name) {
		int result = messageDao.insertDirectImageMessage(user_id, room_id, file_name);
		if(result > 1) {
			List<Message> messages = newActivityDao.selectNewMessage(user_id, room_id);
			if(messages.size() > 0) {
				NonReadActivities.addNonReadMessages(messages);
			}
		}
		return result > 1 ? true : false;
	}
	
	@Override
	public List<MessageResDto> insertNewRoom(int user_id, List<Integer> target_user_ids) {
		int room_id = messageDao.selectSpecificRoomId(user_id, target_user_ids);
		
		if(room_id == 0) {
			room_id = messageDao.insertNewRoom(user_id, target_user_ids);	
		}
		return selectMessages(user_id, room_id);
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
				dto.setAll_message_count(room.getAll_message_count());
				dto.setRead_message_count(room.getRead_message_count());
				
				dtos.add(dto);
			}
		}
		return dtos;
	}
	
	@Override
	public List<MessageResDto> selectMessages(int user_id, int room_id) {
		List<Integer> message_ids = newActivityDao.insertMessageReadFlag(user_id, room_id);
		if(message_ids.size() > 0) {
			NonReadActivities.readMessage(user_id, message_ids);
		}
		
		List<Message> messages = messageDao.selectMessages(room_id);
		List<MessageResDto> dtos = new ArrayList<MessageResDto>();
		
		for(Message message : messages) {
			MessageResDto message_id = MessageResDto.builder().id(message.getId()).build();
			if(dtos.contains( message_id )) {
				List<Integer> like_users = dtos.get(dtos.indexOf(message_id)).getLike_users();
				int like_user_id = message.getLike_user_id();
				if(like_user_id != 0) {
					like_users.add(message.getLike_user_id());
				}
			} else {
				MessageResDto dto = new MessageResDto();
				dto.setId(message.getId());
				dto.setRoom_id(message.getRoom_id());
				dto.setUser_id(message.getUser_id());
				dto.setContents(message.getContents());
				dto.set_image(message.is_image());
				dto.setImage_id(message.getImage_id());
				dto.setFile_name(FileUploadPathConfig.getMessageImagePath(message.getFile_name()));
				dto.setCreate_date(message.getCreate_date());
				List<Integer> like_users = new ArrayList<Integer>();
				int like_user_id = message.getLike_user_id();
				if(like_user_id != 0) {
					like_users.add(message.getLike_user_id());
				}
				dto.setLike_users(like_users);
				
				dtos.add(dto);
			}
		}
		return dtos;
	}
	
	@Override
	public List<Integer> toggleMessageReaction(int user_id, int message_id) {
		return messageDao.toggleMessageReaction(user_id, message_id);
	}
	
	@Override
	public List<Boolean> checkRoomIdByMessageId(int room_id, List<Integer> message_ids) {
		List<Integer> room_ids = messageDao.selectRoomIdByMessageId(message_ids);
		List<Boolean> flags = new ArrayList<Boolean>();
		
		for(int each_room_id : room_ids) {
			if(each_room_id == room_id) flags.add(true);
			else												  flags.add(false);
		}
		
		return flags;
	}
}
