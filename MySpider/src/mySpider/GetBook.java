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
		// TODO �Զ����ɵĹ��캯�����
	}	
	ArrayList threadList = new ArrayList();	//�½��߳�����
	
	public static void main(String[]args) throws Exception {
		GetBook book = new GetBook();
	    book.go(null);
	}
	
	//���̳߳���
	public void go(String url) throws Exception {
	    // �������߳���
		System.out.println("Spider��Start Searching!");
		for (int i = 0; i < threads; i++) {
	      Thread  t = new Thread(this,"Spide " + (i+1));
	      
	      t.start();
	        threadList.add(t);
	    }
	    while (threadList.size() >0) {
	        Thread child = (Thread)threadList.remove(0);
	        child.join();
	    }
	    System.out.println("Spider��Finish Searching��");
	}
	
	//���̳߳���
	public void run() {
	    String url;
	    try {
	        while ((url = getUrl()) != null) 
	        {	
	        	//�������ݿ�ʱ����ע�����д��룻ɾȥ�����ݿ���Դ���
	        	Database.writeArrayList(getBook(url,0),getBook(url,1));
	        	
	        	/*
	        	//�����ݿ���Դ���
	        	for(int i = 0; i < getBook(url,0).size(); i++) {
	        		System.out.println(Thread.currentThread().getName()+":"+getBook(url,0).get(i)+"\t"+getBook(url,1).get(i));
	            }
	        	*/
	        
	        
	        
	        
	        }
	    }catch(Exception e) {
	    	 e.printStackTrace();
	    }
	}
	
	//�ṩurl�ĺ���
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
