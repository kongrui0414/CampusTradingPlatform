package com.example.campustradingplatform.Chat.dao;

/**
 * @author: 竹林
 * @date: 2021/3/30
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class DBConnection {
    private static final String DBDRIVER = "com.mysql.jdbc.Driver";

    private static final String DBURL = "jdbc:mysql://121.37.212.124:3306/ctp?useSSL=false&amp;serverTimezone=UTC";
    private static final String DBUSER = "root";
    private static final String DBPASSWORD = "ABC123!!";


    /**
     * methodName(方法名）
     * description:用于获取sql连接 connection，成功就返回conn，不成功返回null
     *             短连接，因为聊天不需要长时间的修改数据，提高资源利用率
     * @param
     * @param
     */
    public static Connection getDBconnection() {

        Connection conn=null;
        PreparedStatement stmt=null;
        try {
//            Class.forName(DBDRIVER).newInstance();
//            Log.d("TAG", "驱动加载成功");
        }
        catch (Exception e){
//            Log.d("TAG", "驱动加载失败");
            e.printStackTrace();
            return null;
        }

        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DBURL,DBUSER,DBPASSWORD);
//            Log.d("TAG", "数据库连接成功");
            return conn;

        } catch (Exception e) {
//            Log.d("TAG", "数据库连接失败！");
            e.printStackTrace();
            return null;
        }
        //关闭conn连接
//        finally {
//            if(conn!=null){
//                try {
////                    conn.close();
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//
//        }

    }
}
