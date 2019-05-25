package main;

import java.util.Set;

public class MyCrawler {
	/**
	 * ʹ�����ӳ�ʼ�� URL ����
	 * @return
	 * @param seeds ����URL
	 */ 
	private void initCrawlerWithSeeds(String[] seeds)
	{
		for(int i=0;i<seeds.length;i++)
			LinkQueue.addUnvisitedUrl(seeds[i]);
	}	
	/**
	 * ץȡ����
	 * @return
	 * @param seeds
	 */
	public void crawling(String[] seeds)
	{   //���������
		LinkFilter filter = new LinkFilter(){
			public boolean accept(String url) {
				int flag = 1;
				/*if(url.indexOf(".ppt") != -1 || url.indexOf(".doc") != -1 || url.indexOf(".zip") != -1 || url.indexOf(".jpg") != -1)
					flag = 0;*/
				
				if(url.startsWith("https://www.77xsw.la/book/53588") && flag == 1)
					return true;
				else
					return false;
			}
		};
		//��ʼ�� URL ����
		initCrawlerWithSeeds(seeds);
		//ѭ����������ץȡ�����Ӳ�����ץȡ����ҳ������1000
		while(!LinkQueue.unVisitedUrlsEmpty()&&LinkQueue.getVisitedUrlNum()<=1000)
		{
			//��ͷURL������
			String visitUrl=(String)LinkQueue.unVisitedUrlDeQueue();
			if(visitUrl==null)
				continue;
			
			DownLoadFile downLoader=new DownLoadFile();
			//������ҳ
			downLoader.downloadFile(visitUrl);
			
			//�� url ���뵽�ѷ��ʵ� URL ��
			LinkQueue.addVisitedUrl(visitUrl);
			
			//��ȡ��������ҳ�е� URL
			Set<String> links=HtmlParserTool.extracLinks(visitUrl,filter);
			//�µ�δ���ʵ� URL ���
			for(String link:links)
			{
					LinkQueue.addUnvisitedUrl(link);
			}
		}
	}
	//main �������
	public static void main(String[]args)
	{
		MyCrawler crawler = new MyCrawler();
		crawler.crawling(new String[]{"https://www.77xsw.la/book/53588"});
	}

}
