package com.ineyee;

import com.ineyee.beforeioc.servlet.UserServlet;

public class App {
    public static void main(String[] args) {
        UserServlet userServlet = new UserServlet();
        userServlet.remove(11);
    }
}
