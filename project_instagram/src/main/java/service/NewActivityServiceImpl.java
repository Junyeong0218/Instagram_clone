package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.NonRead;
import entity.NonReadActivities;
import repository.NewActivityDao;

public class NewActivityServiceImpl implements NewActivityService {

	private NewActivityDao newActivityDao;
	
	public NewActivityServiceImpl(NewActivityDao newActivityDao) {
		this.newActivityDao = newActivityDao;
	}
	
	@Override
	public Map<String, List<Integer>> selectNonReadActivities(int user_id) {
		List<NonRead> nonReads = newActivityDao.selectNonReadActivities(user_id);
		System.out.println(nonReads);
		Map<String, List<Integer>> nonReadActivities = new HashMap<String, List<Integer>>();
		nonReadActivities.put(NonReadActivities.ACTIVITY, new ArrayList<Integer>());
		nonReadActivities.put(NonReadActivities.MESSAGE, new ArrayList<Integer>());
		
		for(NonRead nonRead : nonReads) {
			if(nonRead.getLog_id() != 0) {
				// nonReadLogs
				List<Integer> log_ids = nonReadActivities.get(NonReadActivities.ACTIVITY);
				log_ids.add(nonRead.getLog_id());
				
			} else if(nonRead.getMessage_id() != 0) {
				// nonReadMessages
				List<Integer> message_ids = nonReadActivities.get(NonReadActivities.MESSAGE);
				if(! nonRead.isMessage_read_flag()) {
					message_ids.add(nonRead.getMessage_id());
				}
			}
		}
		
		return nonReadActivities;
	}
	
}
