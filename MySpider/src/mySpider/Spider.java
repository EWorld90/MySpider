package mySpider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import databaseTools.Database;
import gui.GUI;

public class Spider implements Runnable {
LinkQueue link = new LinkQueue ();       //新建的对URL队列操作的实例对象
private int threads ;     //初始化线程数   
int count=0;
static String bookName;
static String bookUrl;

static GUI gui = null;
//主函数

public static void main(String[] args) throws Exception
{	
	gui = new GUI();
	/*
	 * 
	//无数据库测试代码
	ArrayList<String> book=new ArrayList<String>();
	ArrayList<String> url=new ArrayList<String>();
	book.add("锦绣清宫：四爷的心尖宠妃");
	url.add("https://www.77xsw.la/book/13093/");
	book.add("百炼成神");
	url.add("https://www.77xsw.la/book/10647/");
	source.add(0, book);source.add(1, url);
	//接入数据库时，解注释数据库代码，删除以上无数据库代码
	 */
}

public void runSpider(String keyword) throws Exception {
	ArrayList<ArrayList<String>> source=new ArrayList<ArrayList<String>>();
	
	gui.clearText();
	gui.addText("程序启动！");
	System.out.println("程序启动！");
	source = Database.selectKeyword(keyword);
	
	gui.addText("为你搜索到共计"+ source.get(0).size() + "条结果：");
	System.out.println("为你搜索到共计"+ source.get(0).size() + "条结果：");
	for(String name : source.get(0)) {
		gui.addText(name);
		System.out.println(name);
	}
	
	while(source.size() != 0)	
	{	bookName=source.get(0).remove(0);
	    bookUrl=source.get(1).remove(0);
		Spider Spider = new Spider(bookUrl);
           Spider.go(null);}
}


ArrayList threadList = new ArrayList();	//新建线程链表

//允许创建一个空的Spider实例
public Spider() {}


//初始化化多线程爬虫
public Spider(String strURL) 
{
    threads = 100;		//定义线程数
    link.unVisitedUrl.add(strURL);		//添加种子节点
   
}

//主线程程序
public void go(String strURL) throws Exception {
    // 创建子线程数
	System.out.println("Spider：StartDownLoad!");
	gui.addText("爬虫开始下载！");
	for (int i = 0; i < threads; i++) {
      Thread  t = new Thread(this, "Spide " + (i+1));
      
      t.start();
        threadList.add(t);
    }
    while (threadList.size() >0) {
        Thread child = (Thread)threadList.remove(0);
        child.join();
    }
    link.visitedUrl.clear();
    
    gui.addText("爬虫下载完毕！");
    System.out.println("Spider：Finish DownLoad！");
}

//子线程程序
public void run() {
    String url;
    try {
        while ((url = dequeueURL()) != null) {
            indexURL(url);
        }
    }catch(Exception e) {
    	 e.printStackTrace();
    }   
    threads--;
}

//带锁的出队函数
public synchronized String dequeueURL() throws Exception {
    while (true) {
        if (link.unVisitedUrl.size() > 0) {
            String newitem = (String)link.unVisitedUrl.remove();
            return newitem;
            
        } 
        else {
               
               if (threads>count) {
                        wait();count++;
               } 
               else   {
                       notifyAll();count=120;
                       return null;
                      }
             }
    }
}

//带锁的入队函数
public synchronized void enqueueURL(String Group) {
   
	  link.addUnvisitedUrl(Group);
        notifyAll();
        count=20;
    
}


//访问url的扩展、下载、入队操作
private void indexURL(String url) throws Exception {
    boolean flag = true ;
    
   
    
    String strBody = "null" ;
    try{
            //下载
    	GetTxt txt = new GetTxt();
		String filePath="//temp";
		try {
			filePath = "temp\\"+bookName+"\\"+txt.getTxtTitle(url)+".txt";
			String body=txt.getTxtBody(url);
			File file = new File(filePath);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
				}
				file.createNewFile();
				if(body != null && !"".equals(body)){
				FileWriter fw = new FileWriter(file, true);
				       fw.write(body);//写入本地文件中
				       fw.flush();
				       fw.close();
				       gui.addText(txt.getTxtTitle(url)+"下载完毕！");
				       System.out.println(txt.getTxtTitle(url)+"下载完毕！");
			
		} }catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
            System.out.println(Thread.currentThread().getName()+" Visiting "+url);
            link.addVisitedUrl(url);
            
       }catch(Exception e){
            return ;
          }
    
    
    if (strBody != null) {
    	//过滤操作
    	LinkFilter filter = new LinkFilter(){
			public boolean accept(String url) {
				      if(url.startsWith(bookUrl))
					      return true;
			          else
					      return false;
			          }
		};
     Set<String>urlGroups =HtmlParserTool.extracLinks(url, filter) ;
   
        //入队
      for (String Group:urlGroups) {
          enqueueURL(Group);
            }
  }
}

}