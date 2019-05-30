package treading;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import tools.Database;

public class Spider<DownLoadFile> implements Runnable {
LinkQueue link = new LinkQueue ();       //新建的对URL队列操作的实例对象
private int threads ;     //初始化线程数   
int count=0;
//主函数

public static void main(String argv[]) throws Exception
{	
	GetBook getBook = new GetBook();
	
	for(int i = 0; i < 1779; i++) {
		Database.writeArrayList(getBook.getBook("https://www.77xsw.la/topallvisit/" + i + ".html", 1), 
				                getBook.getBook("https://www.77xsw.la/topallvisit/" + i + ".html", 0));
	}
       // 输入小说的起始url
		//Spider Spider = new Spider("https://www.77xsw.la/book/53588");
           //Spider.go(null);
}


ArrayList threadList = new ArrayList();	//新建线程链表

//初始化化多线程爬虫
public Spider(String strURL) 
{
    threads = 10;		//定义线程数
    link.unVisitedUrl.add(strURL);		//添加种子节点
   
}

//主线程程序
public void go(String strURL) throws Exception {
    // 创建子线程数
	System.out.println("Spider：StartDownLoad!");
	for (int i = 0; i < threads; i++) {
      Thread  t = new Thread(this, "Spide " + (i+1));
      
      t.start();
        threadList.add(t);
    }
    while (threadList.size() >0) {
        Thread child = (Thread)threadList.remove(0);
        child.join();
    }
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
                       notifyAll();count=20;
                       return null;
                      }
             }
    }
}

//带锁的入队函数
public synchronized void enqueueURL(String Group) {
   
	  link.addUnvisitedUrl(Group);
        notifyAll();
        count=1;
    
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
			filePath = "temp\\"+"测试下载的第一本小说\\"+txt.getTxtTitle(url)+".txt";
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
				      if(url.startsWith("https://www.77xsw.la/book/53588"))
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