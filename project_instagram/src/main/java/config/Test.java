package config;

public class Test {

	public static void main(String[] args) {
		String uri = "/article";
		uri = uri.replace("/article", "");
		System.out.println("uri : " + uri);
		
		String[] words = uri.split("/");
		System.out.println(words.length);
		
		for(String s : words) {
			System.out.println(s);
			System.out.println(s.equals(""));
			
		}
	}
}
