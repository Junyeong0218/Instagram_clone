package service;

import java.util.ArrayList;
import java.util.List;

import config.FileUploadPathConfig;
import entity.ArticleDetail;
import entity.HashTag;
import entity.LatestSearchDetail;
import entity.LatestSearchRecord;
import entity.SearchKeyword;
import entity.User;
import repository.SearchDao;
import response_dto.LatestSearchResDto;
import response_dto.SearchResultResDto;

public class SearchServiceImpl implements SearchService {

	private SearchDao searchDao;
	
	public SearchServiceImpl(SearchDao searchDao) {
		this.searchDao = searchDao;
	}
	
	@Override
	public LatestSearchResDto selectLatestSearches(int user_id) {
		List<LatestSearchDetail> latestSearches = searchDao.selectLatestSearches(user_id);
		LatestSearchResDto dto = new LatestSearchResDto();
		
		List<LatestSearchRecord> latestSearchList = new ArrayList<LatestSearchRecord>();
		List<User> userInfos = new ArrayList<User>();
		List<HashTag> hashTags = new ArrayList<HashTag>();
		
		for(LatestSearchDetail detail : latestSearches) {
			LatestSearchRecord search = new LatestSearchRecord();
			User user = new User();
			HashTag hashTag = new HashTag();
			
			search.setId(detail.getId());
			search.setUser_id(detail.getUser_id());
			search.setCreate_date(detail.getCreate_date());
			search.setUpdate_date(detail.getUpdate_date());
			
			user.setId(detail.getSearched_user_id());
			user.setUsername(detail.getUsername());
			user.setName(detail.getName());
			user.setHas_profile_image(detail.isHas_profile_image());
			if(detail.getFile_name() == null) {
				user.setFile_name(null);
			} else {
				user.setFile_name(FileUploadPathConfig.getProfileImagePath(detail.getFile_name()));
			}
			user.setUser_follow_flag(detail.isUser_follow_flag());
			
			hashTag.setId(detail.getHash_tag_id());
			hashTag.setTag_name(detail.getTag_name());
			hashTag.setHash_tag_follow_flag(detail.isHash_tag_follow_flag());
			
			latestSearchList.add(search);
			userInfos.add(user);
			hashTags.add(hashTag);
		}
		
		dto.setLatestSearchList(latestSearchList);
		dto.setUserInfos(userInfos);
		dto.setHashTags(hashTags);
		
		return dto;
	}
	
	@Override
	public List<SearchKeyword> selectKeyword(String keyword, int user_id) {
		keyword = keyword.trim().strip();
		boolean isHash = keyword.charAt(0) == '#';
		if(isHash) {
			return searchDao.selectHashTags(keyword, user_id);
		} else {
			List<SearchKeyword> resultList = searchDao.selectKeyword(keyword, user_id);
			boolean dupUserFlag = false;
			boolean dupHashFlag = false;
			for(int i=0; i < resultList.size(); i++) {
				SearchKeyword result = resultList.get(i);
				if(i == 0) {
					if(result.getSearched_user_id() != 0 && result.getHash_tag_id() != 0) {
						SearchKeyword newLine = new SearchKeyword();
						newLine.setHash_tag_id(result.getHash_tag_id());
						newLine.setTag_name(result.getTag_name());
						newLine.setHash_tag_follow_flag(result.isHash_tag_follow_flag());
						
						result.setHash_tag_id(0);
						result.setTag_name(null);
						result.setHash_tag_follow_flag(false);
						resultList.add(1, newLine);
					}
				} else {
					for(int j=0; j < i; j++) {
						SearchKeyword before = resultList.get(j);
						if(before.getSearched_user_id() == result.getSearched_user_id()) dupUserFlag = true;
						if(before.getHash_tag_id() == result.getHash_tag_id()) dupHashFlag = true;
					}
				}
				if(dupUserFlag && dupHashFlag) {
					resultList.remove(i);
					i--;
					dupUserFlag = false;
					dupHashFlag = false;
				} else if(dupUserFlag) {
					result.setSearched_user_id(0);
					result.setUsername(null);
					result.setName(null);
					result.setHas_profile_image(false);
					result.setFile_name(null);
					dupUserFlag = false;
				} else if(dupHashFlag) {
					result.setHash_tag_id(0);
					result.setTag_name(null);
					result.setHash_tag_follow_flag(false);
				}
			}
			return resultList;
		}
	}
	
	@Override
	public SearchResultResDto selectAboutHashTag(String tag_name) {
		List<ArticleDetail> articleList = searchDao.selectArticlesAboutHashTag(tag_name);
		SearchResultResDto dto = new SearchResultResDto();
		dto.setArticle_list(articleList);
		if(articleList.size() > 0) {
			dto.setRelated_article_count(articleList.get(0).getRelated_article_count());
			for(ArticleDetail article : articleList) {
				article.setMedia_name(FileUploadPathConfig.getArticleImagePath(article.getArticle_id(), article.getMedia_name()));
			}
		} else {
			dto.setRelated_article_count(0);
		}
		
		return dto;
	}
	
	@Override
	public boolean insertLatestSearch(boolean isUser, int id, int user_id) {
		int result = searchDao.insertLatestSearch(isUser, id, user_id);
		return result > 0 ? true : false;
	}
	
	@Override
	public List<User> selectUsers(String keyword, int user_id) {
		List<User> users = searchDao.selectUsers(keyword, user_id);
		for(User user : users) {
			if(user.getFile_name() != null) {
				user.setFile_name(FileUploadPathConfig.getProfileImagePath(user.getFile_name()));
			}
		}
		return users;
	}
}
