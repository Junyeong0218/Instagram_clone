package repository;

import java.util.List;

import entity.Activity;
import entity.Message;
import entity.NonRead;

public interface NewActivityDao {

	public List<NonRead> selectNonReadActivities(int user_id);
	
	public int updateActivityReadFlag(int user_id);
	
	public List<Integer> insertMessageReadFlag(int user_id, int room_id);
	
	public List<Activity> selectRelatedUserIdsAboutArticle(int article_id, String activity_flag);

	public List<Activity> selectRelatedUserIdsAboutComment(int comment_id, String activity_flag);
	
	public List<Message> selectNewMessage(int user_id, int room_id);
	
	public List<Activity> selectFollowActivity(int partner_user_id, int user_id, String activity_flag);
}
