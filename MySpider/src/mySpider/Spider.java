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
LinkQueue link = new LinkQueue ();       //�½��Ķ�URL���в�����ʵ������
private int threads ;     //��ʼ���߳���   
int count=0;
static String bookName;
static String bookUrl;

static GUI gui = null;
//������

public static void main(String[] args) throws Exception
{	
	gui = new GUI();
	/*
	 * 
	//�����ݿ���Դ���
	ArrayList<String> book=new ArrayList<String>();
	ArrayList<String> url=new ArrayList<String>();
	book.add("�����幬����ү���ļ����");
	url.add("https://www.77xsw.la/book/13093/");
	book.add("��������");
	url.add("https://www.77xsw.la/book/10647/");
	source.add(0, book);source.add(1, url);
	//�������ݿ�ʱ����ע�����ݿ���룬ɾ�����������ݿ����
	 */
}

public void runSpider(String keyword) throws Exception {
	ArrayList<ArrayList<String>> source=new ArrayList<ArrayList<String>>();
	
	gui.clearText();
	gui.addText("����������");
	System.out.println("����������");
	source = Database.selectKeyword(keyword);
	
	gui.addText("Ϊ������������"+ source.get(0).size() + "�������");
	System.out.println("Ϊ������������"+ source.get(0).size() + "�������");
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


ArrayList threadList = new ArrayList();	//�½��߳�����

//������һ���յ�Spiderʵ��
public Spider() {}


//��ʼ�������߳�����
public Spider(String strURL) 
{
    threads = 100;		//�����߳���
    link.unVisitedUrl.add(strURL);		//������ӽڵ�
   
}

//���̳߳���
public void go(String strURL) throws Exception {
    // �������߳���
	System.out.println("Spider��StartDownLoad!");
	gui.addText("���濪ʼ���أ�");
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
    
    gui.addText("����������ϣ�");
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
                       notifyAll();count=120;
                       return null;
                      }
             }
    }
}

//��������Ӻ���
public synchronized void enqueueURL(String Group) {
   
	  link.addUnvisitedUrl(Group);
        notifyAll();
        count=20;
    
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
			filePath = "temp\\"+bookName+"\\"+txt.getTxtTitle(url)+".txt";
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
				       gui.addText(txt.getTxtTitle(url)+"������ϣ�");
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
				      if(url.startsWith(bookUrl))
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