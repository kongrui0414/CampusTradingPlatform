package com.example.campustradingplatform.Chat.dao;

import com.example.campustradingplatform.Login.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author: 竹林
 * @date: 2021/4/5
 */
public class UserDao {
    public static User getUserByUID(int userID, Connection conn) {

        try {
            String sql = "SELECT * from user where id="+userID;
            ResultSet rs = null;
            rs = BaseDao.select(sql,conn);
            if(rs.next()){

                String phoneNum = rs.getString("phoneNum");
                String psw = rs.getString("psw");
                String userName = rs.getString("userName");
                String name = rs.getString("name");
                String sex = rs.getString("sex");
                String school = rs.getString("school");
                String identity = rs.getString("identity");
                String idNum = rs.getString("idNum");

                return new User(userID,phoneNum,psw,userName,name, sex,school,identity, idNum );
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }
}
