package service;

import java.util.ArrayList;
import java.util.List;

import entity.Activity;
import entity.ArticleMedia;
import entity.User;
import entity.UserProfile;
import repository.FollowDao;
import response_dto.ArticleResDto;
import response_dto.UserProfileResDto;
import response_dto.UserRecommendResDto;

public class FollowServiceImpl implements FollowService {
	
	private FollowDao followDao;
	
	public FollowServiceImpl(FollowDao followDao) {
		this.followDao = followDao;
	}

	@Override
	public List<UserRecommendResDto> selectRecommendUsers(int user_id) {
		return followDao.selectRecommendUsers(user_id);
	}
	
	@Override
	public int insertFollowUser(int partner_user_id, int user_id) {
		return followDao.insertFollowUser(partner_user_id, user_id);
	}
	
	@Override
	public int deleteFollowUser(int partner_user_id, int user_id) {
		return followDao.deleteFollowUser(partner_user_id, user_id);
	}
	
	@Override
	public List<Activity> selectActivities(int user_id) {
		return followDao.selectActivities(user_id);
	}
	
	@Override
	public UserProfileResDto selectUserProfileInfo(String username, int session_user_id) {
		List<UserProfile> userProfile =  followDao.selectUserProfileInfo(username, session_user_id);
		UserProfileResDto dto = new UserProfileResDto();
		
		dto.setUser_id(userProfile.get(0).getUser_id());
		dto.setUsername(userProfile.get(0).getUsername());
		dto.setName(userProfile.get(0).getName());
		dto.setHas_profile_image(userProfile.get(0).isHas_profile_image());
		dto.setFile_name(userProfile.get(0).getFile_name());
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
			media.setMedia_name(profile.getMedia_name());
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
