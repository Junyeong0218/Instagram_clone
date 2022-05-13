package entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NonReadActivities {
	
	public static final String ACTIVITY = "activity";
	public static final String MESSAGE = "message";

	private static NonReadActivities instance;
	private static Map<Integer, Map<String, List<Integer>>> nonReadActivities;
	private static Map<Integer, Map<String, Boolean>> changeFlags;
	
	private NonReadActivities() {
		nonReadActivities = new ConcurrentHashMap<Integer, Map<String, List<Integer>>>();
		changeFlags = new ConcurrentHashMap<Integer, Map<String, Boolean>>();
	}
	
	public static void createInstance() {
		if(instance == null) {
			instance = new NonReadActivities();
			System.out.println("새로운 객체 생성");
		}
	}
	
	public static void addNonReadActivities(List<Activity> activities) {
		for(Activity activity : activities) {
			if( isLoginedUser(activity.getRelated_user_id()) ) {
				List<Integer> activityList = nonReadActivities.get(activity.getRelated_user_id()).get(ACTIVITY);
				if(! activityList.contains(activity.getId())) {
					activityList.add(activity.getId());
					changeFlags.get(activity.getRelated_user_id()).put(ACTIVITY, true);
				}
			}
		}
	}
	
	public static void addNonReadMessages(List<Message> messages) {
		for(Message message : messages) {
			if( isLoginedUser(message.getUser_id()) ) {
				List<Integer> messageList = nonReadActivities.get(message.getUser_id()).get(MESSAGE);
				if(! messageList.contains(message.getId())) {
					messageList.add(message.getId());
					changeFlags.get(message.getUser_id()).put(MESSAGE, true);
				}
			}
		}
	}
	
	public static int getNonReadActivityCount(int user_id) {
		return nonReadActivities.get(user_id).get(ACTIVITY).size();
	}
	
	public static int getNonReadMessageCount(int user_id) {
		return nonReadActivities.get(user_id).get(MESSAGE).size();
	}
	
	public static String getNonReadMessage(int user_id) {
		StringBuilder sb = new StringBuilder();
		Map<String, List<Integer>> userReadInfo = getUserReadInfo(user_id);
		List<Integer> activityList = userReadInfo.get(ACTIVITY);
		List<Integer> messageList = userReadInfo.get(MESSAGE);
		sb.append(" { \"activity_list\": [ ");
		for(int activity_id : activityList) {
			sb.append(activity_id + ", ");
		}
		if(activityList.size() > 0) sb.replace(sb.lastIndexOf(","), sb.length(), "");
		sb.append(" ], \"message_list\": [ ");
		for(int message_id : messageList) {
			sb.append(message_id + ", ");
		}
		if(messageList.size() > 0) sb.replace(sb.lastIndexOf(","), sb.length(), "");
		sb.append(" ] }");
		
		return sb.toString();
	}

	public static void readAllActivities(int user_id) {
		nonReadActivities.get(user_id).get(ACTIVITY).clear();
		changeFlags.get(user_id).put(ACTIVITY, false);
	}
	
	public static void readMessage(int user_id, List<Integer> message_ids) {
		nonReadActivities.get(user_id).get(MESSAGE).removeAll(message_ids);
		if(nonReadActivities.get(user_id).get(MESSAGE).size() == 0) {
			changeFlags.get(user_id).put(MESSAGE, false);
		}
	}
	
	public static String isChanged(int user_id) {
		System.out.println(user_id + " : " + getNonReadMessage(user_id));
		Map<String, Boolean> userChangeFlags = changeFlags.get(user_id);
		if(userChangeFlags.get(ACTIVITY) && userChangeFlags.get(MESSAGE)) {
			return allChangedMessage();
		} else if(userChangeFlags.get(ACTIVITY)) {
			return ACTIVITY;
		} else if(userChangeFlags.get(MESSAGE)) {
			return MESSAGE;
		} else {
			return null;
		}
	}
	
	public static Map<String, List<Integer>> getUserReadInfo(int user_id) {
		return nonReadActivities.get(user_id);
	}
	
	public static boolean setUser(int user_id, Map<String, List<Integer>> nonReadInfo) {
		boolean a = nonReadActivities.put(user_id, nonReadInfo) != null;
		boolean b = changeFlags.put(user_id, newFlagMap(nonReadInfo)) != null;
		return ! (a && b);
	}
	
	public static boolean eraseUser(int user_id) {
		return nonReadActivities.remove(user_id) != null && changeFlags.remove(user_id) != null;
	}
	
	private static Map<String, Boolean> newFlagMap(Map<String, List<Integer>> nonReadInfo) {
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		map.put(ACTIVITY, nonReadInfo.get(ACTIVITY).size() > 0);
		map.put(MESSAGE, nonReadInfo.get(MESSAGE).size() > 0);
		return map;
	}
	
	public static boolean isLoginedUser(int user_id) {
		return nonReadActivities.keySet().contains(user_id);
	}
	
	private static String allChangedMessage() {
		return ACTIVITY + ", " + MESSAGE;
	}

}
