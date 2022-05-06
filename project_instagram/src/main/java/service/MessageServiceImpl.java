package service;

import java.util.List;
import java.util.Map;

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
}
