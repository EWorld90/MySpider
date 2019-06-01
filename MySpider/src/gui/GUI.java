package gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JTextField;

import mySpider.Spider;

import javax.swing.JPanel;

import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;

public class GUI extends JFrame
{
	private static final long serialVersionUID = 1L;
	
    public JTextArea info_text=new JTextArea(20, 50);    //构造一个文本域
	
	public GUI()
    {
    	super("图形");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(600, 850);        
        Container c = getContentPane();
        c.setLayout(null);
        
        int i;
        
        
        
        //background面板负责放置背景图片
        JPanel background = new JPanel();
        JLabel background_ImgLable = new JLabel();
        ImageIcon background_Img = new ImageIcon("background.png");
        
        background_ImgLable.setIcon(background_Img);
        background.add(background_ImgLable);
        
        background.setBounds(0, 0, 600, 850);
        this.getLayeredPane().add(background, 0);
        
        
        //主面板p放置5个放置功能组件的面板
        JPanel [] p = {new JPanel(), new JPanel(), new JPanel(), new JPanel(),new JPanel()};
        p[0].setBounds(0, 0, 600, 100);
        p[1].setBounds(5, 130, 580, 40);
        p[2].setBounds(5, 250, 580, 50);
        p[3].setBounds(5, 350, 580, 450);
        p[4].setBounds(100, 760, 400, 30);
        for(i=0; i<p.length; i++) {
        	p[i].setOpaque(false);
        	this.getLayeredPane().add(p[i], i);
        }
        p[4].setOpaque(true);
        
        
        /*
         * 面板1放置程序标题信息
         */
        p[0].setLayout(new BorderLayout());
        
        Font title_Font = new Font("楷体", Font.BOLD, 60);
        
        JLabel title_Text = new JLabel("搜你想搜 看你想看", JLabel.CENTER);
        title_Text.setFont(title_Font);
        title_Text.setVerticalAlignment(JLabel.CENTER);
        
        p[0].add(title_Text, BorderLayout.CENTER);
        
        
        /*
         * 面板2放置输入关键词的提示信息和文本框
         */
        p[1].setLayout(new BorderLayout());
        
        Font hint_Font = new Font("宋体", Font.BOLD, 24);
        Font keyword_Font = new Font("宋体", Font.PLAIN, 16);
        
        JLabel hint_Text = new JLabel("请输入关键词： ");
        hint_Text.setFont(hint_Font);
        
        final JTextField keyword = new JTextField(20);    //final修饰符用于注册事件监听器
        keyword.setFont(keyword_Font);
        
        p[1].add(hint_Text, BorderLayout.WEST);
        p[1].add(keyword, BorderLayout.CENTER);
        
        
        /*
         * 面板3放置Go按钮
         */
        p[2].setLayout(new FlowLayout());
        
        JButton button_Go = new JButton("GO");
        button_Go.setPreferredSize(new Dimension(100,40));
        
        p[2].add(button_Go);
        
        
        /*
         * 面板4放置显示运行信息的文本框
         */
        p[3].setLayout(new FlowLayout());
        
        info_text.setLineWrap(true);    //如果内容过长，自动换行，在文本域加上滚动条，水平和垂直滚动条始终出现。
        info_text.setEditable(false);
        info_text.setText("等待程序运行……\n");
        
		JScrollPane info_pane=new JScrollPane(info_text, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		p[3].add(info_pane);
        
		
        /*
         * 面板5为程序制作信息
         */
        p[4].setLayout(new FlowLayout());

        Font copyright_Font = new Font("宋体", Font.BOLD, 16);
        
        JLabel copyright_Text = new JLabel("资源来自千千小说网    程序由No.22小组制作");
        copyright_Text.setFont(copyright_Font);

        p[4].add(copyright_Text);
        
        
        /*
         * 为Go按钮注册按钮事件监听器
         */
        button_Go.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					ThreadSpider ts = new ThreadSpider();
					try {
						ts.go(keyword.getText());
					} catch (Exception e1) {
						// TODO 自动生成的 catch 块
						e1.printStackTrace();
					}
			}
        	
        });
        
        
        this.setVisible(true);
    }
	
	public void addText(String text) {
		info_text.append(text + "\n");
		info_text.repaint(); 
	}
	
	public void clearText() {
		info_text.setText("");
		info_text.repaint();
	}
}

class ThreadSpider implements Runnable{	
	Spider spider = new Spider();
	
	String keyword = null;
	
	public void go(String str) throws Exception {
	  keyword = str;
		
      // 创建子线程
      Thread  t = new Thread(this,"Spide");
      t.start();
    }

	public void run(){	
		try {
			spider.runSpider(keyword);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}	
	}
}

