package treading;

import java.util.ArrayList;

import tools.Database;

public class Test {

	public static void main(String[] args) {
		String str = "ÄãºÃ";
		
		ArrayList<ArrayList<String>> a = new ArrayList<ArrayList<String>>();
		ArrayList<String> book = new ArrayList<String>();
		ArrayList<String> url = new ArrayList<String>();
		
		a = Database.selectKeyword(str);
		for(int i = 0; i < a.get(0).size(); i++) {
			System.out.print("book: " + a.get(0).get(i));
			System.out.println("  url: " + a.get(1).get(i));
		}

	}

}
