package tools;

import java.sql.*;

public class Database {
 
    // JDBC �����������ݿ� URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://localhost:3306/crawl?serverTimezone=UTC";
 
    // ���ݿ���û��������룬��Ҫ�����Լ�������
    static final String USER = "root";
    static final String PASS = "hc980729";
 
    public static int read() {
        Connection conn = null;
        Statement stmt = null;
        
        int code = 1;
        
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
            sql = "SELECT url, filename FROM url_list";
            ResultSet rs = stmt.executeQuery(sql);
        
            // չ����������ݿ�
            while(rs.next()){
                // ͨ���ֶμ���
                String url = rs.getString("url");
                String filename = rs.getString("filename");
    
                // �������
                System.out.print("URL: " + url + "�� ");
                System.out.print("�ļ�����" + filename);
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
            }
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        
        return code;
    }
    
    public static void write(String url, String filename) {
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
            
            stmt.executeUpdate("insert into url_list values('" + url + "', '" + filename + "')");
            
            System.out.println("���������ݡ�");
            
            // ��ɺ�ر�
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // ���� JDBC ����
            se.printStackTrace();
            System.out.println("��������ʧ�ܡ�");
        }catch(Exception e){
            // ���� Class.forName ����
            e.printStackTrace();
            System.out.println("��������ʧ�ܡ�");
        }finally{
            // �ر���Դ
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se){
            	se.printStackTrace();
            }
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
    }
}
