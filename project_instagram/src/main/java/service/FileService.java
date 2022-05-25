package service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.Part;

public class FileService {

	private static InputStream fis;
	private static Path path;
	private static File file;
	
	public static boolean hasFile(Collection<Part> collection) {
		Iterator<Part> iterator = collection.iterator();
		boolean isExist = false;
		while(iterator.hasNext()) {
			Part part = iterator.next();
			if(part.getName().equals("file")) {
				isExist = true;
				break;
			}
		}
		return isExist;
	}
	
	public static String uploadProfileImage(Collection<Part> collection, String dir) throws IOException {
		file = new File(dir);
		String fileName = null;
		
		Iterator<Part> iterator = collection.iterator();
		while(iterator.hasNext()) {
			Part part = iterator.next();
			if(part.getName().equals("file")) {
				fis = part.getInputStream();
				fileName = part.getSubmittedFileName();
				Path filePath = Paths.get(dir + "/" + fileName);
				
				try {
					Files.write(filePath, fis.readAllBytes());
				} catch (Exception e) {
					System.out.println("file upload failed!");
					return null;
				}
			}
		}
		fis = null;
		path = null;
		file = null;
		return fileName;
	}
	
	public static String uploadImageMessage(Collection<Part> collection, String dir) throws IOException {
		file = new File(dir);
		if(!file.exists()) file.mkdirs();
		String fileName = null;
		
		Iterator<Part> iterator = collection.iterator();
		while(iterator.hasNext()) {
			Part part = iterator.next();
			if(part.getName().equals("file")) {
				fis = part.getInputStream();
				fileName = UUID.randomUUID().toString().replace("-", "") + "-" + part.getSubmittedFileName();
				Path filePath = Paths.get(dir + fileName);
				
				try {
					Files.write(filePath, fis.readAllBytes());
				} catch (Exception e) {
					System.out.println("file upload failed!");
					return null;
				}
			}
		}
		fis = null;
		path = null;
		file = null;
		return fileName;
	}
	
	public static List<String> uploadArticleMedias(Collection<Part> collection, String dir, String username) throws IOException {
		List<String> fileNameList = new ArrayList<String>();
		file = new File(dir + username);
		if(!file.exists()) file.mkdirs();
		
		Iterator<Part> iterator = collection.iterator();
		while(iterator.hasNext()) {
			Part part = iterator.next();
			if(part.getName().equals("file")) {
				String fileName = part.getSubmittedFileName();
				fileNameList.add(fileName);
				
				path = Paths.get(dir + username + File.separator + fileName);
				fis = part.getInputStream();
				Files.write(path, fis.readAllBytes());
			}
		}
		fis = null;
		path = null;
		return fileNameList;
	}
	
	public static void moveFileToNewFolder(List<String> fileNames, String dir, String username, int article_id) throws IOException {
		File new_folder = new File(dir + article_id);
		new_folder.mkdirs();
		for(int i = 0; i < fileNames.size(); i++) {
			Path before = Paths.get(dir + username + File.separator + fileNames.get(i));
			Path after = Paths.get(dir + article_id + File.separator + fileNames.get(i));
			System.out.println(before.toString());
			System.out.println(after.toString());
			Files.move(before, after, StandardCopyOption.REPLACE_EXISTING);
		}
		file.delete();
		file = null;
	}
}
