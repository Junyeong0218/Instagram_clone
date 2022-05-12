package repository;

import java.util.List;

import entity.NonRead;

public interface NewActivityDao {

	public List<NonRead> selectNonReadActivities(int user_id);
	
	public int updateActivityReadFlag(int user_id);
	
	public List<Integer> insertMessageReadFlag(int user_id, int room_id);
}
