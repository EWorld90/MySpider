package treading;

import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class GetBook {
	
	public GetBook() {
		// TODO �Զ����ɵĹ��캯�����
	}	
	
	/*
	 * url����ȡ�����а�
	 * order=0�����������б�
	 * order=1 ������Ӧurl�б�
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
	            	//ֻ����ÿ��tr��ǩ�µ�һ��td��ǩ
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
		// TODO�Զ����ɵ�catch�����
		e.printStackTrace(); 
		} 
		
		if(order==0)return bookList;
		else return urlList;
		
	}
	
}
