package treading;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Spider implements Runnable {
	private static final String SPIDER_URL = "https://www.77xsw.la/book/53588" ;
	
	int count = 0;
	
	LinkQueue link = new LinkQueue ();       //�½��Ķ�URL���в�����ʵ������
	private int threads ;     //��ʼ���߳���   
	DownLoadFile download =new DownLoadFile(); 		//HTML���ص�ʵ������


	//������
	public static void main(String argv[]) throws Exception {
           Spider Spider = new Spider(SPIDER_URL);
           Spider.go(null);
	}


	ArrayList threadList = new ArrayList();	//�½��߳�����

	//��ʼ�������߳�����
	public Spider(String strURL) 
	{
		threads = 10;		//�����߳���
		link.unVisitedUrl.add(strURL);		//������ӽڵ�
   
	}

	//���̳߳���
	public void go(String strURL) throws Exception {
		// �������߳���
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

	//���̳߳���
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
	
	// �����ĳ��Ӻ���
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
					count++;//ͣ��һ�����߳�
				} 
				else   {
					notifyAll();
					count = 20;
					return null;
				}
			}
		}
	}
	
	//��������Ӻ���
	public synchronized void enqueueURL(String Group) {
		
		link.addUnvisitedUrl(Group);
		notifyAll();
		
	}
	
	
//	����url����չ�����ء���Ӳ���
	private void indexURL(String url) throws Exception {
		boolean flag = true ;
		
		
		
		String strBody = null ;
		try{
				//����
            	strBody = download.downloadFile(url);
            	link.addVisitedUrl(url);
            
			} catch(Exception e){
			    return ;
			}
    
    
		if (strBody != null) {
			//���˲���
			LinkFilter filter = new LinkFilter(){
				public boolean accept(String url) {
					if(url.startsWith("https://www.77xsw.la/book/53588"))
					     return true;
			        else
					     return false;
			        }
		};
        Set<String>urlGroups =HtmlParserTool.extracLinks(url, filter) ;
   
        //���
        for (String Group:urlGroups) {
            enqueueURL(Group);
            }
        
		}
	}

}