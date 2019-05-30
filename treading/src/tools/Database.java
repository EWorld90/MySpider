package tools;

import java.sql.*;
import java.util.ArrayList;

public class Database {
 
    // JDBC �����������ݿ� URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://localhost:3306/crawl?serverTimezone=UTC";
 
    // ���ݿ���û��������룬��Ҫ�����Լ�������
    static final String USER = "root";
    static final String PASS = "hc980729";
 
    static int code = 1;
    
    public static int read() {
        Connection conn = null;
        Statement stmt = null;
        
        try{
            // ע�� JDBC ����
            Class.forName(JDBC_DRIVER);
        
            // ������
            System.out.println("�������ݿ�...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
        
            // ִ�в�ѯ
            System.out.println(" ʵ����Statement����...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT url, bookname FROM url_list";
            ResultSet rs = stmt.executeQuery(sql);
        
            // չ����������ݿ�
            while(rs.next()){
                // ͨ���ֶμ���
                String url = rs.getString("url");
                String bookname = rs.getString("bookname");
    
                // �������
                System.out.print("URL: " + url + "�� ");
                System.out.print("������" + bookname);
                System.out.print("\n");
            }
            System.out.println("������������ݡ�");
            
            // ��ɺ�ر�
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // ���� JDBC ����
            se.printStackTrace();
            System.out.println("�������ʧ�ܡ�");
            code = -1;
        }catch(Exception e){
            // ���� Class.forName ����
            e.printStackTrace();
            System.out.println("�������ʧ�ܡ�");
            code = -2;
        }finally{
            // �ر���Դ
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se){
            	se.printStackTrace();
            	code = -3;
            }
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
                code = -4;
            }
        }
        
        return code;
    }
    
    /** 
     * ����һ��װ��bookList��urlList��ArrayList��0��������ӦbookList��1��������ӦurlList
     * 
     * @param keyword
     * @return list
     */
    public static ArrayList<ArrayList<String>> selectKeyword(String keyword) {
        Connection conn = null;
        Statement stmt = null;
        
        ArrayList<String> urlList = new ArrayList<String>();
        ArrayList<String> bookList = new ArrayList<String>();
        
        try{
            // ע�� JDBC ����
            Class.forName(JDBC_DRIVER);
        
            // ������
            // System.out.println("�������ݿ�...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
        
            // ִ�в�ѯ
            // System.out.println(" ʵ����Statement����...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT url, bookname FROM url_list WHERE bookname LIKE '%" + keyword + "%'";
            ResultSet rs = stmt.executeQuery(sql);
        
            // չ����������ݿ�
            while(rs.next()){
                // ͨ���ֶμ���
                String url = rs.getString("url");
                String bookname = rs.getString("bookname");
    
                urlList.add(url);
                bookList.add(bookname);
                
                // �������
                //System.out.print("URL: " + url + "�� ");
                //System.out.print("������" + bookname);
                //System.out.print("\n");
            }
            System.out.println("�ѷ��ز�ѯ���ݡ�");
            
            // ��ɺ�ر�
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // ���� JDBC ����
            se.printStackTrace();
            System.out.println("�������ʧ�ܡ�");
            code = -1;
        }catch(Exception e){
            // ���� Class.forName ����
            e.printStackTrace();
            System.out.println("�������ʧ�ܡ�");
            code = -2;
        }finally{
            // �ر���Դ
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se){
            	se.printStackTrace();
            	code = -3;
            }
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
                code = -4;
            }
        }
        
        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        list.add(bookList);
        list.add(urlList);
        
        return list;
    }
    
    public static int write(String url, String bookname) {
        Connection conn = null;
        Statement stmt = null;
        try{
            // ע�� JDBC ����
            Class.forName(JDBC_DRIVER);
        
            // ������
            //System.out.println("�������ݿ�...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
        
            // ִ�в���
            //System.out.println(" ʵ����Statement����...");
            stmt = conn.createStatement();
            
            stmt.executeUpdate("insert into url_list values('" + url + "', '" + bookname + "')");
            
            System.out.println("���������ݡ�");
            
            // ��ɺ�ر�
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // ���� JDBC ����
            se.printStackTrace();
            System.out.println("��������ʧ�ܡ�");
            code = -1;
        }catch(Exception e){
            // ���� Class.forName ����
            e.printStackTrace();
            System.out.println("��������ʧ�ܡ�");
            code = -2;
        }finally{
            // �ر���Դ
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se){
            	se.printStackTrace();
            	code = -3;
            }
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
                code = -4;
            }
        }
        
        return code;
    }
    
    public static int writeArrayList(ArrayList<String> urlList, ArrayList<String> bookList) {
        Connection conn = null;
        Statement stmt = null;
        try{
            // ע�� JDBC ����
            Class.forName(JDBC_DRIVER);
        
            // ������
            //System.out.println("�������ݿ�...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
        
            // ִ�в���
            //System.out.println(" ʵ����Statement����...");
            stmt = conn.createStatement();
            
            for(int i = 0; i < urlList.size(); i++) {
            	stmt.executeUpdate("insert into url_list values('" + 
            	                    urlList.get(i) + "', '" + 
            			            bookList.get(i) + "')");
            }
            
            System.out.println("���������ݡ�");
            
            // ��ɺ�ر�
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // ���� JDBC ����
            se.printStackTrace();
            System.out.println("��������ʧ�ܡ�");
            code = -1;
        }catch(Exception e){
            // ���� Class.forName ����
            e.printStackTrace();
            System.out.println("��������ʧ�ܡ�");
            code = -2;
        }finally{
            // �ر���Դ
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se){
            	se.printStackTrace();
            	code = -3;
            }
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
                code = -4;
            }
        }
        
        return code;
    }
}
