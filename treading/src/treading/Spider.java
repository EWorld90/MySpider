package treading;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Spider implements Runnable {
	private static final String SPIDER_URL = "https://www.77xsw.la/book/53588" ;
	
	int count = 0;
	
	LinkQueue link = new LinkQueue ();       //新建的对URL队列操作的实例对象
	private int threads ;     //初始化线程数   
	DownLoadFile download =new DownLoadFile(); 		//HTML下载的实例对象


	//主函数
	public static void main(String argv[]) throws Exception {
           Spider Spider = new Spider(SPIDER_URL);
           Spider.go(null);
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
		for (int i = 0; i < threads; i++) {
			Thread t = new Thread(this, "Spide " + (i+1));
			t.start();
			threadList.add(t);
		}
		while (threadList.size() >0) {
			Thread child = (Thread)threadList.remove(0);
			child.join();
		}
		
		System.out.println("Thread over");
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
	
	// 带锁的出队函数
	public synchronized String dequeueURL() throws Exception {
		while (true) {
			if (link.unVisitedUrl.size() > 0) {
				String newitem = (String)link.unVisitedUrl.remove();
				return newitem;
				
			} 
			else {
				threads--;
				if (threads > count) {
					wait();
					count++;//停下一个子线程
				} 
				else   {
					notifyAll();
					count = 20;
					return null;
				}
			}
		}
	}
	
	//带锁的入队函数
	public synchronized void enqueueURL(String Group) {
		
		link.addUnvisitedUrl(Group);
		notifyAll();
		
	}
	
	
//	访问url的扩展、下载、入队操作
	private void indexURL(String url) throws Exception {
		boolean flag = true ;
		
		
		
		String strBody = null ;
		try{
				//下载
            	strBody = download.downloadFile(url);
            	link.addVisitedUrl(url);
            
			} catch(Exception e){
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