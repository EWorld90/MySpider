package tools;

import java.sql.*;
import java.util.ArrayList;

public class Database {
 
    // JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://localhost:3306/crawl?serverTimezone=UTC";
 
    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "root";
    static final String PASS = "hc980729";
 
    static int code = 1;
    
    public static int read() {
        Connection conn = null;
        Statement stmt = null;
        
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);
        
            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
        
            // 执行查询
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT url, bookname FROM url_list";
            ResultSet rs = stmt.executeQuery(sql);
        
            // 展开结果集数据库
            while(rs.next()){
                // 通过字段检索
                String url = rs.getString("url");
                String bookname = rs.getString("bookname");
    
                // 输出数据
                System.out.print("URL: " + url + "， ");
                System.out.print("书名：" + bookname);
                System.out.print("\n");
            }
            System.out.println("已输出所有数据。");
            
            // 完成后关闭
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
            System.out.println("输出数据失败。");
            code = -1;
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
            System.out.println("输出数据失败。");
            code = -2;
        }finally{
            // 关闭资源
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
     * 返回一个装有bookList与urlList的ArrayList，0号索引对应bookList，1号索引对应urlList
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
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);
        
            // 打开链接
            // System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
        
            // 执行查询
            // System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT url, bookname FROM url_list WHERE bookname LIKE '%" + keyword + "%'";
            ResultSet rs = stmt.executeQuery(sql);
        
            // 展开结果集数据库
            while(rs.next()){
                // 通过字段检索
                String url = rs.getString("url");
                String bookname = rs.getString("bookname");
    
                urlList.add(url);
                bookList.add(bookname);
                
                // 输出数据
                //System.out.print("URL: " + url + "， ");
                //System.out.print("书名：" + bookname);
                //System.out.print("\n");
            }
            System.out.println("已返回查询数据。");
            
            // 完成后关闭
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
            System.out.println("输出数据失败。");
            code = -1;
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
            System.out.println("输出数据失败。");
            code = -2;
        }finally{
            // 关闭资源
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
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);
        
            // 打开链接
            //System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
        
            // 执行插入
            //System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            
            stmt.executeUpdate("insert into url_list values('" + url + "', '" + bookname + "')");
            
            System.out.println("已输入数据。");
            
            // 完成后关闭
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
            System.out.println("输入数据失败。");
            code = -1;
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
            System.out.println("输入数据失败。");
            code = -2;
        }finally{
            // 关闭资源
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
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);
        
            // 打开链接
            //System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
        
            // 执行插入
            //System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            
            for(int i = 0; i < urlList.size(); i++) {
            	stmt.executeUpdate("insert into url_list values('" + 
            	                    urlList.get(i) + "', '" + 
            			            bookList.get(i) + "')");
            }
            
            System.out.println("已输入数据。");
            
            // 完成后关闭
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
            System.out.println("输入数据失败。");
            code = -1;
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
            System.out.println("输入数据失败。");
            code = -2;
        }finally{
            // 关闭资源
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
