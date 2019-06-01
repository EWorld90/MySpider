package mySpider;

import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import databaseTools.Database;

public class GetBook implements Runnable {
static int urlcount=1;
static int threads=10;

	public GetBook() {
		// TODO 自动生成的构造函数存根
	}	
	ArrayList threadList = new ArrayList();	//新建线程链表
	
	public static void main(String[]args) throws Exception {
		GetBook book = new GetBook();
	    book.go(null);
	}
	
	//主线程程序
	public void go(String url) throws Exception {
	    // 创建子线程数
		System.out.println("Spider：Start Searching!");
		for (int i = 0; i < threads; i++) {
	      Thread  t = new Thread(this,"Spide " + (i+1));
	      
	      t.start();
	        threadList.add(t);
	    }
	    while (threadList.size() >0) {
	        Thread child = (Thread)threadList.remove(0);
	        child.join();
	    }
	    System.out.println("Spider：Finish Searching！");
	}
	
	//子线程程序
	public void run() {
	    String url;
	    try {
	        while ((url = getUrl()) != null) 
	        {	
	        	//接入数据库时，解注释下行代码；删去无数据库测试代码
	        	Database.writeArrayList(getBook(url,0),getBook(url,1));
	        	
	        	/*
	        	//无数据库测试代码
	        	for(int i = 0; i < getBook(url,0).size(); i++) {
	        		System.out.println(Thread.currentThread().getName()+":"+getBook(url,0).get(i)+"\t"+getBook(url,1).get(i));
	            }
	        	*/
	        
	        
	        
	        
	        }
	    }catch(Exception e) {
	    	 e.printStackTrace();
	    }
	}
	
	//提供url的函数
	public synchronized String getUrl() throws Exception {
	    while (true) {
	        if (urlcount<1780) {
	            String newitem = "https://www.77xsw.la/topallvisit/"+urlcount+".html";
	            urlcount++;
	            return newitem;
	            
	        } 
	        else {
	               return null;
	             }
	    }
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
