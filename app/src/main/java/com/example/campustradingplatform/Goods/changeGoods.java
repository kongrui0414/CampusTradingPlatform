package com.example.campustradingplatform.Goods;

import android.util.Log;
import android.widget.Toast;

import com.example.campustradingplatform.Login.User;
import com.example.campustradingplatform.MyCenter_mySellGoods;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class changeGoods {
    //连接的数据库
    String sqlUrl = "jdbc:mysql://121.37.212.124:3306/ctp";
    //连接数据库的用户名
    String sqlUserName = "root";
    //连接数据库的密码
    String sqlPsw = "ABC123!!";
    Connection connection = null;
    private Goods goods;
    private List<Goods> goodsList = new ArrayList<>();
    private User user;

    public changeGoods(Goods goods){
        this.goods = goods;
    }

    public changeGoods(){
        this.goods = new Goods();
    }

    public void UpdateGoods(Goods goods){
        this.goods = goods;
        new Thread(updateGoods).start();
    }
    Runnable updateGoods = new Runnable() {
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
                ps = connection.prepareStatement("update goods set goodsName = ?, description = ?, originalPrice = ?, presentPrice = ?, oldorNew = ?  where goodsId = ?");
                ps.setString(1,goods.getGoodsName());
                ps.setString(2,goods.getDescription());
                ps.setString(3,Float.toString(goods.getOriginalPrice()));
                ps.setString(4,Float.toString(goods.getPresentPrice()));
                ps.setString(5,goods.getOldorNew());
                ps.setString(6,Integer.toString(goods.getGoodsId()));
                int rs = ps.executeUpdate();
                connection.close();
            }catch (Exception e){
                Log.d("zjj","连接数据库失败");
                e.printStackTrace();
            }
        }
    };


    public List<Goods> getAllGoods(User user){
        this.user = user;
        goodsList.clear();
        Thread thread = new Thread(getGoods);
        thread.start();
        try{
            thread.join();          //让查询数据库的线程完成后再执行函数里的后续操作
        }catch (Exception e){
            e.printStackTrace();
        }
        return goodsList;
    }
    Runnable getGoods = new Runnable() {
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
                ps = connection.prepareStatement("select * from goods where sellerId = ?");
                ps.setString(1,Integer.toString(user.getId()));
                ResultSet rs = ps.executeQuery();
                int i = 0;
                while (rs.next()){
                    Goods goods = new Goods();
                    goods.setSellerId(rs.getInt("sellerId"));
                    goods.setGoodsId(rs.getInt("goodsId"));
                    goods.setGoodsName(rs.getString("goodsName"));
                    goods.setDescription(rs.getString("description"));
                    goods.setOriginalPrice(rs.getFloat("originalPrice"));
                    goods.setPresentPrice(rs.getFloat("presentPrice"));
                    goods.setOldorNew(rs.getString("oldorNew"));
                    goods.setLaunchTime(rs.getTimestamp("launchTime"));
                    goodsList.add(goods);
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
}
