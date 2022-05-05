package repository;

import java.util.List;

import entity.ArticleDetail;
import entity.LatestSearchDetail;
import entity.SearchKeyword;

public interface SearchDao {

	public List<LatestSearchDetail> selectLatestSearches(int user_id);
	
	public List<SearchKeyword> selectHashTags(String keyword, int user_id);
	
	public List<SearchKeyword> selectKeyword(String keyword, int user_id);
	
	public List<ArticleDetail> selectArticlesAboutHashTag(String tag_name);
}
