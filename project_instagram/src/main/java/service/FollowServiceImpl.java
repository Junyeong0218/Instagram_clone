package service;

import java.util.ArrayList;
import java.util.List;

import entity.Activity;
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
	
	@SuppressWarnings("unlikely-arg-type")
	@Override
	public UserProfileResDto selectUserProfileInfo(String username, int session_user_id) {
		List<UserProfile> userProfile =  followDao.selectUserProfileInfo(username, session_user_id);
		UserProfileResDto dto = new UserProfileResDto();
		
		System.out.println(userProfile);
		
		for(int i = 0 ; i < userProfile.size(); i++) {
			if(userProfile.get(i).is_stored() == true) continue;
			if(dto.getUser_id() == 0) {
				dto.setUser_id(userProfile.get(i).getUser_id());
				dto.setUsername(userProfile.get(i).getUsername());
				dto.setName(userProfile.get(i).getName());
				dto.setHas_profile_image(userProfile.get(i).isHas_profile_image());
				dto.setFile_name(userProfile.get(i).getFile_name());
				dto.setFollow_flag(userProfile.get(i).isFollow_flag());
				dto.setFollower(userProfile.get(i).getFollower());
				dto.setFollowing(userProfile.get(i).getFollowing());
				dto.setArticle_list(new ArrayList<ArticleResDto>());
			}
			List<ArticleResDto> articleList = dto.getArticle_list();
			if(articleList.contains(userProfile.get(i).getArticle_id())) {
				articleList.get(articleList.indexOf(userProfile.get(i).getArticle_id())).getMedia_name_list().add(userProfile.get(i).getMedia_name());
			} else {
				ArticleResDto article = new ArticleResDto();
				article.setId(userProfile.get(i).getArticle_id());
				article.setMedia_type(userProfile.get(i).getMedia_type());
				article.setMedia_name_list(new ArrayList<String>());
				article.getMedia_name_list().add(userProfile.get(i).getMedia_name());
				article.setArticle_create_date(userProfile.get(i).getCreate_date());
				articleList.add(article);
			}
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
