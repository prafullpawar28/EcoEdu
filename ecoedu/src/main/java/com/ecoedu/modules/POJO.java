package com.ecoedu.modules;

public class POJO {
    public static POJO instance ;
    private String userName;
    private String email;
    private String contactNo;
    private String role;
    public POJO(String userName,String email,String contactNo,String role) {
        this.userName = userName;
        this.email = email;
        this.contactNo = contactNo;
        this.role = role;
    }
    public String getUserName() {
        return userName;
    }
    public String getEmail() {
        return email;
    }
    public String getContactNo() {
        return contactNo;
    }
    public String getRole() {
        return role;
    }

}
