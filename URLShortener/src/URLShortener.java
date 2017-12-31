import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

/*
 * URL Shortener by Schahin Rajab
 * 
 * Shortens a long URL in the format ti.ny/xxxxxx where the last six characters
 * are random characters of A-Z, a-z, and 0-9. The last six characters, the slug,
 * can be changed to any other length by changing the value stored in
 * the int variable slugLength.   
 * 
 * If a shortened URL is given it will output the original URL.
 * 
 * The program also deals with various urls that direccts to the same website.
 * However, the program is case sensitive (e.g. Http != http)
 * 
 * The entered URLs are stored as long as the code is running and the number
 * of possible combinations of slugs is 62^slugLength. With slugLength = 6
 * there exist 62^6 number of combinations.
 *
 *The program does not:
 * - Check if the entered URL is valid
 * - Allow the user to choose their own slug
 * - Try to shorten URLs that are already shorter than six characters in total
 * 
 */
public class URLShortener {
	// storage for generated slugs
	private HashMap<String, String> slugMap; // slug-url map
	private HashMap<String, String> urlMap;// url-slug map to check whether
											// a url has been previously stored
	private String domain; // Use this attribute to generate urls for a custom
							// domain name
	private char myChars[]; // This array is used for character to number
							// mapping
	private Random myRand; // Random object used to generate random integers
	private int slugLength; // Length of slug

	public static void main(String args[]) {
		URLShortener u = new URLShortener();
		Scanner sc = new Scanner(System.in);
				
		while(true) {
		System.out.println("Enter URL: ");
		String url = sc.next();
		
		if(url.substring(0,6).equals("ti.ny/")){
		url = url.substring(6);
		System.out.println("The expanded URL is: " + 
		u.slugMap.get(url));
		}
		
		else
		System.out.println("The shortened URL is: " +
		u.shortenURL(url));
		}
	}	
	
	//Constructor
	URLShortener() {
		slugMap = new HashMap<String, String>();
		urlMap = new HashMap<String, String>();
		myRand = new Random();
		slugLength = 6;
		domain = "ti.ny";
		myChars = new char[62];
		for (int i = 0; i < 62; i++) {
			int j = 0;
			if (i < 10) 
				j = i + 48;
			 else if (i > 9 && i <= 35) 
				j = i + 55;
			 else 
				j = i + 61;
			
			myChars[i] = (char) j;
		}
	}

	// This method is called when shortening a URL
	public String shortenURL(String longURL) {
		String shortURL = "";
			longURL = sanitizeURL(longURL);
			if (urlMap.containsKey(longURL))
				shortURL = domain + "/" + urlMap.get(longURL);
			 else 
				shortURL = domain + "/" + getSlug(longURL);
			
		
		return shortURL;
	}

	// sanitizeURL takes care of various urls that directs to same site.
	// e.g. www.google.com, www.google.com/, https://www.google.com,
	// http://www.google.com/, google.com/ so they don't get assigned
	//unique shortened urls
	String sanitizeURL(String url) {
		
		if (url.substring(0, 4).equals("www."))
			url = url.substring(4);

		if (url.substring(0, 7).equals("http://") &&
				url.substring(7, 11).equals("www."))
			url = url.substring(11);
		
		if (url.substring(0, 8).equals("https://") &&
				url.substring(8, 12).equals("www."))
			url = url.substring(12);
					
		if (url.charAt(url.length() - 1) == '/')
			url = url.substring(0, url.length() - 1);
		
		if (url.substring(0, 5).equals("https") && 
				url.charAt(url.length() - 1) == '/')
			url = url.substring(12, url.length() - 1);
		
		if (url.substring(0, 4).equals("http") && 
				url.charAt(url.length() - 1) == '/')
			url = url.substring(11, url.length() - 1);
		
		if (url.substring(0, 4).equals("www.") && 
				url.charAt(url.length() - 1) == '/')
			url = url.substring(4, url.length() - 1);
		
		if (url.substring(0, 7).equals("http://") &&
				!url.substring(7, 11).equals("www."))
			url = url.substring(7);
		
		if (url.substring(0, 8).equals("https://") &&
				!url.substring(8, 12).equals("www."))
			url = url.substring(8);
			
				return url;
	}

	
	//Return a generated slug
	private String getSlug(String longURL) {
		String slug;
		slug = generateSlug();
		slugMap.put(slug, longURL);
		urlMap.put(longURL, slug);
		return slug;
	}

	//Generates a slug
	private String generateSlug() {
		String slug = "";
		boolean flag = true;
		while (flag) {
			for (int i = 0; i < slugLength; i++) {
				slug += myChars[myRand.nextInt(62)];
			}
				flag = false;
		}
		return slug;
	}
}