package request_dto;

import java.util.ArrayList;
import java.util.List;

import entity.Article;
import entity.ArticleMedia;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class InsertArticleReqDto {
	
	private int user_id;
	private int article_id;
	private String feature;
	private String contents;
	private List<String> media_type_list;
	private List<String> media_name_list;
	
	public Article toArticleEntity() {
		return Article.builder()
								   .user_id(user_id)
								   .feature(feature)
								   .contents(contents)
								   .media_type(media_type_list.get(0))
								   .build();
	}
	
	public List<ArticleMedia> toArticleMediaList() {
		List<ArticleMedia> media_list = new ArrayList<ArticleMedia>();
		for(int i = 0; i < media_type_list.size(); i++) {
			media_list.add(ArticleMedia.builder()
																  .article_id(article_id)
																  .media_type(media_type_list.get(i))
																  .media_name(media_name_list.get(i))
																  .build());
			
		}
		return media_list;
	}
	
}
