package service;

import java.util.ArrayList;
import java.util.List;

import config.FileUploadPathConfig;
import entity.Activity;
import entity.ArticleMedia;
import entity.NonReadActivities;
import entity.User;
import entity.UserProfile;
import repository.FollowDao;
import repository.NewActivityDao;
import response_dto.ArticleResDto;
import response_dto.UserProfileResDto;
import response_dto.UserRecommendResDto;

public class FollowServiceImpl implements FollowService {
	
	private FollowDao followDao;
	private NewActivityDao newActivityDao;
	
	public FollowServiceImpl(FollowDao followDao) {
		this.followDao = followDao;
	}
	
	public FollowServiceImpl(FollowDao followDao, NewActivityDao newActivityDao) {
		this.followDao = followDao;
		this.newActivityDao = newActivityDao;
	}

	@Override
	public List<UserRecommendResDto> selectRecommendUsers(int user_id) {
		List<UserRecommendResDto> recommendUsers =  followDao.selectRecommendUsers(user_id);
		for(UserRecommendResDto dto : recommendUsers) {
			if(dto.getFile_name() != null) {
				dto.setFile_name(FileUploadPathConfig.getProfileImagePath(dto.getFile_name()));
			}
		}
		return recommendUsers;
	}
	
	@Override
	public int insertFollowUser(int partner_user_id, int user_id) {
		int result = followDao.insertFollowUser(partner_user_id, user_id);
		if(result == 1) {
			List<Activity> followActivity = newActivityDao.selectFollowActivity(partner_user_id, user_id, Activity.FOLLOW);
			if(followActivity.size() > 0) {
				NonReadActivities.addNonReadActivities(followActivity);
			}
		}
		return result;
	}
	
	@Override
	public int deleteFollowUser(int partner_user_id, int user_id) {
		return followDao.deleteFollowUser(partner_user_id, user_id);
	}
	
	@Override
	public List<Activity> selectActivities(int user_id) {
		if(NonReadActivities.getNonReadActivityCount(user_id) > 0) {
			int result = newActivityDao.updateActivityReadFlag(user_id);
			System.out.println("update read flag");
			if(result > 0) {
				System.out.println("complete update");
				NonReadActivities.readAllActivities(user_id);
			}
		}
		List<Activity> activities = followDao.selectActivities(user_id);
		for(Activity activity : activities) {
			if(activity.getFile_name() != null) {
				activity.setFile_name(FileUploadPathConfig.getProfileImagePath(activity.getFile_name()));
			}
			if(activity.getRelated_user_file_name() != null) {
				activity.setRelated_user_file_name(FileUploadPathConfig.getProfileImagePath(activity.getRelated_user_file_name()));
			}
			if(activity.getArticleDetail().getMedia_name() != null) {
				activity.getArticleDetail().setMedia_name(FileUploadPathConfig.getArticleImagePath(activity.getArticleDetail().getArticle_id(), activity.getArticleDetail().getMedia_name()));
			}
		}
		return activities;
	}
	
	@Override
	public UserProfileResDto selectUserProfileInfo(String username, int session_user_id) {
		List<UserProfile> userProfile =  followDao.selectUserProfileInfo(username, session_user_id);
		UserProfileResDto dto = new UserProfileResDto();
		
		dto.setUser_id(userProfile.get(0).getUser_id());
		dto.setUsername(userProfile.get(0).getUsername());
		dto.setName(userProfile.get(0).getName());
		dto.setHas_profile_image(userProfile.get(0).isHas_profile_image());
		if(userProfile.get(0).getFile_name() == null) {
			dto.setFile_name(null);
		} else {
			dto.setFile_name(FileUploadPathConfig.getProfileImagePath(userProfile.get(0).getFile_name()));
		}
		dto.setFollow_flag(userProfile.get(0).isFollow_flag());
		dto.setFollower(userProfile.get(0).getFollower());
		dto.setFollowing(userProfile.get(0).getFollowing());
		dto.setArticle_list(new ArrayList<ArticleResDto>());
		
		for(int i = 0 ; i < userProfile.size(); i++) {
			if(userProfile.get(i).is_stored() == true) continue;
			
			UserProfile profile = userProfile.get(i);
			List<ArticleResDto> articleList = dto.getArticle_list();
			
			ArticleResDto article = new ArticleResDto();
			article.setId(profile.getArticle_id());
			article.setMedia_list(new ArrayList<ArticleMedia>());
			
			ArticleMedia media = new ArticleMedia();
			media.setMedia_type(profile.getMedia_type());
			if(profile.getMedia_name() == null) {
				media.setMedia_name(null);
			} else {
				media.setMedia_name(FileUploadPathConfig.getArticleImagePath(profile.getArticle_id(), profile.getMedia_name()));
			}
			article.getMedia_list().add(media);
			article.setArticle_create_date(profile.getCreate_date());
			articleList.add(article);
			
			if(i == userProfile.size() - 1) {
				if(dto.getArticle_list().size() == 1 && dto.getArticle_list().get(0).getId() == 0) {
					dto.setArticle_count(0);
				} else {
					dto.setArticle_count(dto.getArticle_list().size());
				}
			}
		}
		
		return dto;
	}
	
	@Override
	public List<User> selectFollowingUsers(int user_id, int count_indicator) {
		return followDao.selectFollowingUsers(user_id, count_indicator);
	}
	
	@Override
	public List<User> selectFollowers(int user_id, int count_indicator) {
		return followDao.selectFollwers(user_id, count_indicator);
	}
}
