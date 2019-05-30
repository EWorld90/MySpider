package treading;

import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class GetBook {
	
	public GetBook() {
		// TODO 自动生成的构造函数存根
	}	
	
	/*
	 * url待爬取的排行榜；
	 * order=0，返回书名列表；
	 * order=1 返回相应url列表；
	 */
	public  ArrayList<String> getBook(String url,int order){
		ArrayList<String> urlList=new ArrayList<String>();
		ArrayList<String> bookList=new ArrayList<String>();
		 try { 
			Document doc = Jsoup.connect(url).get(); 
			Elements trs = doc.select("table").select("tr");
	        int i;
	        for ( i=0; i<trs.size(); i++)
	        {
	            Elements tds = trs.get(i).select("td");
	            for (int j=0; j<tds.size(); j++)
	            {
	            	//只处理每个tr标签下第一个td标签
	                if(j==0)
	                {	
	                	String book = tds.first().text();
	                	String src = tds.first().select("a").attr("href");
	                	urlList.add(src);
	                	bookList.add(book);
	                }
	            }
	         
	        }
		 	 
		}catch (IOException e){ 
		// TODO自动生成的catch程序块
		e.printStackTrace(); 
		} 
		
		if(order==0)return bookList;
		else return urlList;
		
	}
	
}
