package com.example.campustradingplatform.Chat.dao;

import com.mysql.jdbc.Statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author: 竹林
 * @date: 2021/3/30
 * 描述： 给其他表dao 调用，每个表dao都有一个baseDao
 *        让service控制事务边界
 */
public class BaseDao {

     

    //增
    public static int insert(String insertSql, Connection conn){
//        String sql2 = "insert into student value ('"
//                +sn+"',' "+ss+"',' "+sa+"',` "+so+"` ); ";
        try {
            PreparedStatement stmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()){
                return rs.getInt(1);
            }
        }catch (SQLException e){
            e.printStackTrace();
            return -1;
        }
        return -1;
    }

    //删
    public static void delet(String delSql,Connection conn){
//        String sql1 = "delete from teststudent "+" where sn= '"+sn+"'; ";
        try {
            PreparedStatement stmt = conn.prepareStatement(delSql);
            stmt.executeUpdate();

        }catch (Exception e){
            e.printStackTrace();
        }
    }



    //查
    public static ResultSet select(String selectSql,Connection conn) throws SQLException {
//        String sql = "SELECT * from main_chat_tb";

        PreparedStatement stmt = conn.prepareStatement(selectSql);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }

    //改
    public static void update(String updateSql,Connection conn){
//        String sql = "update sturesult set ss ="+ss
//                +" where sn ='"+sn+"' ";
        try {
            PreparedStatement stmt = conn.prepareStatement(updateSql);
            stmt .executeUpdate(updateSql);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void alert(String sql,Connection conn){
//        try {
//            PreparedStatement stmt = conn.prepareStatement(sql);
//            stmt .executeUpdate(sql);
//        }catch (SQLException e){
//            e.printStackTrace();
//        }
    }

}