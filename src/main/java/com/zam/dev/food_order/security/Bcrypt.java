package com.zam.dev.food_order.security;


import org.springframework.stereotype.Component;

@Component
public class Bcrypt {


    public String hashPw(String pw){
        return BCrypt.hashpw(pw, BCrypt.gensalt());
    }

    public  boolean checkPw(String plaintPw , String hasPw){
        return BCrypt.checkpw(plaintPw , hasPw);
    }



}
