package service;

import java.util.List;
import java.util.Map;

public interface NewActivityService {

	public Map<String, List<Integer>> selectNonReadActivities(int user_id);
	
}
