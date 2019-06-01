package mySpider;

import java.io.IOException;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class GetTxt {
	
	public GetTxt() {
		// TODO 自动生成的构造函数存根
	}
	
	//取出千千小说网title标签下的文本内容，并返回
	public String getTxtTitle(String url) throws IOException {
		Document doc = Jsoup.connect(url).get();
		String title =  doc.title();
	    return doBody(title);}
	
	
	//取出千千小说网div class=panel-body的文本内容，换行处理并返回
	public String getTxtBody(String url) throws IOException {
		Document doc = Jsoup.connect(url).get();
		Element temp = doc.select("div.panel-body").first();
		String body =  temp.text();
	    return doBody(body);}
	
	//对文本内容的换行处理
	public String doBody(String string) {
		try {
			string = new String(string.getBytes(),"GBK").replace('?', ' ').replaceAll("    ","\r\n");
	} catch (Exception e){
	        e.printStackTrace();
	}
        return string;
		}
	
}

