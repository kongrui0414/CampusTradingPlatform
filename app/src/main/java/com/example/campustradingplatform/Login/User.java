package com.example.campustradingplatform.Login;

import java.io.Serializable;

public class User implements Serializable {
    private String phoneNum;        //手机号
    private String psw;             //密码
    private String userName;        //用户名
    private String name;            //真实姓名
    private String sex;            //true为男，false为女
    private String school;          //学校
    private String identity;       //true为学生，false为教职工
    private String idNum;           //学号或者是工号

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public User() {
    }

    public User(String phoneNum, String psw, String userName, String name, String sex, String school, String identity, String idNum) {
        this.phoneNum = phoneNum;
        this.psw = psw;
        this.userName = userName;
        this.name = name;
        this.sex = sex;
        this.school = school;
        this.identity = identity;
        this.idNum = idNum;
    }

}
