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
	
    public JTextArea info_text=new JTextArea(20, 50);    //����һ���ı���
	
	public GUI()
    {
    	super("ͼ��");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(600, 850);        
        Container c = getContentPane();
        c.setLayout(null);
        
        int i;
        
        
        
        //background��帺����ñ���ͼƬ
        JPanel background = new JPanel();
        JLabel background_ImgLable = new JLabel();
        ImageIcon background_Img = new ImageIcon("background.png");
        
        background_ImgLable.setIcon(background_Img);
        background.add(background_ImgLable);
        
        background.setBounds(0, 0, 600, 850);
        this.getLayeredPane().add(background, 0);
        
        
        //�����p����5�����ù�����������
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
         * ���1���ó��������Ϣ
         */
        p[0].setLayout(new BorderLayout());
        
        Font title_Font = new Font("����", Font.BOLD, 60);
        
        JLabel title_Text = new JLabel("�������� �����뿴", JLabel.CENTER);
        title_Text.setFont(title_Font);
        title_Text.setVerticalAlignment(JLabel.CENTER);
        
        p[0].add(title_Text, BorderLayout.CENTER);
        
        
        /*
         * ���2��������ؼ��ʵ���ʾ��Ϣ���ı���
         */
        p[1].setLayout(new BorderLayout());
        
        Font hint_Font = new Font("����", Font.BOLD, 24);
        Font keyword_Font = new Font("����", Font.PLAIN, 16);
        
        JLabel hint_Text = new JLabel("������ؼ��ʣ� ");
        hint_Text.setFont(hint_Font);
        
        final JTextField keyword = new JTextField(20);    //final���η�����ע���¼�������
        keyword.setFont(keyword_Font);
        
        p[1].add(hint_Text, BorderLayout.WEST);
        p[1].add(keyword, BorderLayout.CENTER);
        
        
        /*
         * ���3����Go��ť
         */
        p[2].setLayout(new FlowLayout());
        
        JButton button_Go = new JButton("GO");
        button_Go.setPreferredSize(new Dimension(100,40));
        
        p[2].add(button_Go);
        
        
        /*
         * ���4������ʾ������Ϣ���ı���
         */
        p[3].setLayout(new FlowLayout());
        
        info_text.setLineWrap(true);    //������ݹ������Զ����У����ı�����Ϲ�������ˮƽ�ʹ�ֱ������ʼ�ճ��֡�
        info_text.setEditable(false);
        info_text.setText("�ȴ��������С���\n");
        
		JScrollPane info_pane=new JScrollPane(info_text, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		p[3].add(info_pane);
        
		
        /*
         * ���5Ϊ����������Ϣ
         */
        p[4].setLayout(new FlowLayout());

        Font copyright_Font = new Font("����", Font.BOLD, 16);
        
        JLabel copyright_Text = new JLabel("��Դ����ǧǧС˵��    ������No.22С������");
        copyright_Text.setFont(copyright_Font);

        p[4].add(copyright_Text);
        
        
        /*
         * ΪGo��ťע�ᰴť�¼�������
         */
        button_Go.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					ThreadSpider ts = new ThreadSpider();
					try {
						ts.go(keyword.getText());
					} catch (Exception e1) {
						// TODO �Զ����ɵ� catch ��
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
		
      // �������߳�
      Thread  t = new Thread(this,"Spide");
      t.start();
    }

	public void run(){	
		try {
			spider.runSpider(keyword);
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}	
	}
}

