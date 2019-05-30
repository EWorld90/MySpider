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
LinkQueue link = new LinkQueue ();       //�½��Ķ�URL���в�����ʵ������
private int threads ;     //��ʼ���߳���   
int count=0;
//������

public static void main(String argv[]) throws Exception
{	
	GetBook getBook = new GetBook();
	
	for(int i = 0; i < 1779; i++) {
		Database.writeArrayList(getBook.getBook("https://www.77xsw.la/topallvisit/" + i + ".html", 1), 
				                getBook.getBook("https://www.77xsw.la/topallvisit/" + i + ".html", 0));
	}
       // ����С˵����ʼurl
		//Spider Spider = new Spider("https://www.77xsw.la/book/53588");
           //Spider.go(null);
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
	System.out.println("Spider��StartDownLoad!");
	for (int i = 0; i < threads; i++) {
      Thread  t = new Thread(this, "Spide " + (i+1));
      
      t.start();
        threadList.add(t);
    }
    while (threadList.size() >0) {
        Thread child = (Thread)threadList.remove(0);
        child.join();
    }
    System.out.println("Spider��Finish DownLoad��");
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

//�����ĳ��Ӻ���
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

//��������Ӻ���
public synchronized void enqueueURL(String Group) {
   
	  link.addUnvisitedUrl(Group);
        notifyAll();
        count=1;
    
}


//����url����չ�����ء���Ӳ���
private void indexURL(String url) throws Exception {
    boolean flag = true ;
    
   
    
    String strBody = "null" ;
    try{
            //����
    	GetTxt txt = new GetTxt();
		String filePath="//temp";
		try {
			filePath = "temp\\"+"�������صĵ�һ��С˵\\"+txt.getTxtTitle(url)+".txt";
			String body=txt.getTxtBody(url);
			File file = new File(filePath);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
				}
				file.createNewFile();
				if(body != null && !"".equals(body)){
				FileWriter fw = new FileWriter(file, true);
				       fw.write(body);//д�뱾���ļ���
				       fw.flush();
				       fw.close();
				       System.out.println(txt.getTxtTitle(url)+"������ϣ�");
			
		} }catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
            System.out.println(Thread.currentThread().getName()+" Visiting "+url);
            link.addVisitedUrl(url);
            
       }catch(Exception e){
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