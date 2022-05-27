package config;

public class FileUploadPathConfig {

	private static String file_upload_path;
	private static String log_file_path;
	private static final String PROFILE_IMAGE_PATH = "/user_profile_images/"; 
	private static final String ARTICLE_IMAGE_PATH = "/article_medias/"; 
	private static final String MESSAGE_IMAGE_PATH = "/message_images/"; 

	public static String getFileUploadPath() {
		return file_upload_path;
	}
	
	public static String getLogFilePath() {
		return log_file_path;
	}
	
	public static String getMessageImagePath(String file_name) {
		return MESSAGE_IMAGE_PATH + file_name;
	}

	public static String getArticleImagePath(int article_id, String media_name) {
		return ARTICLE_IMAGE_PATH + article_id + "/" + media_name;
	}
	
	public static String getProfileImagePath(String file_name) {
		return PROFILE_IMAGE_PATH + file_name;
	}
	
	
	public static void setFile_upload_path(String file_upload_path) {
		FileUploadPathConfig.file_upload_path = file_upload_path.replace("\\", "/");
	}
	
	public static void setLog_file_path(String log_file_path) {
		FileUploadPathConfig.log_file_path = log_file_path.replace("\\", "/");
	}
}
