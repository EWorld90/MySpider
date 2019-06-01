package mySpider;

import java.io.IOException;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class GetTxt {
	
	public GetTxt() {
		// TODO �Զ����ɵĹ��캯�����
	}
	
	//ȡ��ǧǧС˵��title��ǩ�µ��ı����ݣ�������
	public String getTxtTitle(String url) throws IOException {
		Document doc = Jsoup.connect(url).get();
		String title =  doc.title();
	    return doBody(title);}
	
	
	//ȡ��ǧǧС˵��div class=panel-body���ı����ݣ����д�������
	public String getTxtBody(String url) throws IOException {
		Document doc = Jsoup.connect(url).get();
		Element temp = doc.select("div.panel-body").first();
		String body =  temp.text();
	    return doBody(body);}
	
	//���ı����ݵĻ��д���
	public String doBody(String string) {
		try {
			string = new String(string.getBytes(),"GBK").replace('?', ' ').replaceAll("    ","\r\n");
	} catch (Exception e){
	        e.printStackTrace();
	}
        return string;
		}
	
}

