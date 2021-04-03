package com.example.campustradingplatform.Order;

import android.util.Log;

import com.example.campustradingplatform.Login.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class changeOrder {
    //连接的数据库
    String sqlUrl = "jdbc:mysql://121.37.212.124:3306/ctp";
    //连接数据库的用户名
    String sqlUserName = "root";
    //连接数据库的密码
    String sqlPsw = "ABC123!!";
    Connection connection = null;
    private Order order;
    private User Order_user;
    private List<Order> orderList = new ArrayList<>();

    public changeOrder(Order order){
        this.order = order;
    }

    public changeOrder(){
        this.order = new Order();
    }

    public List<Order> getAllOrders(User user){
        Order_user = user;
        orderList.clear();
        Thread thread = new Thread(getorders);
        thread.start();
        try{
            thread.join();                     //让查询数据库的线程完成后再执行函数里的后续操作
        }catch (Exception e){
            e.printStackTrace();
        }
        return orderList;
    }

    Runnable getorders = new Runnable() {
        @Override
        public void run() {
            try {
                //加载mysql驱动
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                Log.d("zjj","加载数据库驱动成功");
            } catch (Exception e) {
                Log.d("zjj","加载数据库驱动失败！");
                e.printStackTrace();
            }
            try {
                //连接数据库
                connection = DriverManager.getConnection(sqlUrl,sqlUserName,sqlPsw);
                Log.d("zjj","数据库连接成功");
                //数据库操作
                PreparedStatement ps;
                ps = connection.prepareStatement("select * from orders where buyerId = ?");
                ps.setString(1,Integer.toString(Order_user.getId()));
                ResultSet rs = ps.executeQuery();
                int i = 0;
                while (rs.next()){
                    Order order = new Order();
                    order.setOrderId(rs.getInt("orderId"));
                    order.setBuyerId(rs.getInt("buyerId"));
                    order.setBuyerName(rs.getString("buyerName"));
                    order.setSellerId(rs.getInt("sellerId"));
                    order.setSellerName(rs.getString("sellerName"));
                    order.setGoodsId(rs.getInt("goodsId"));
                    order.setGoodsName(rs.getString("goodsName"));
                    order.setPrice(rs.getFloat("price"));
                    order.setAddress(rs.getString("address"));
                    order.setState(rs.getInt("state"));
                    order.setOrderTime(rs.getTimestamp("ordertime"));
                    orderList.add(order);
                    i++;
                }
                connection.close();
                Log.d("zjj1","线程运行结束");
            }catch (Exception e){
                Log.d("zjj","连接数据库失败");
                e.printStackTrace();
            }
        }
    };


    Runnable changState = new Runnable() {
        @Override
        public void run() {
            try {
                //加载mysql驱动
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                Log.d("zjj","order加载数据库驱动成功");
            } catch (Exception e) {
                Log.d("zjj","order加载数据库驱动失败！");
                e.printStackTrace();
            }
            try {
                //连接数据库
                connection = DriverManager.getConnection(sqlUrl,sqlUserName,sqlPsw);
                Log.d("zjj","order数据库连接成功");
                //数据库操作
                PreparedStatement ps;
                ps = connection.prepareStatement("update orders set state = '1' where orderId = ?");
                ps.setString(1,Integer.toString(order.getOrderId()));
                int rs = ps.executeUpdate();
                connection.close();
            }catch (Exception e){
                Log.d("zjj","order连接数据库失败");
                e.printStackTrace();
            }
        }
    };

    public void changeState(){
        new Thread(changState).start();
    }

}
