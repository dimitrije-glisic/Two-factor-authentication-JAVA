/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimi.beans;

import com.dimi.db.DAOUser;
import com.dimi.db.DAOUser.User;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author MITA
 */
@ManagedBean
@ViewScoped
public class SignUpController {

    
    @ManagedProperty("#{loginController}")
    private LoginController loginController;
    
    public void setLoginController(LoginController loginController){
        this.loginController = loginController;
    }
    
    
    private String username;
    private String password;
    private String repPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepPassword() {
        return repPassword;
    }

    public void setRepPassword(String repPassword) {
        this.repPassword = repPassword;
    }

    public String signUp() {

        FacesMessage fm = new FacesMessage();

        boolean isValidUsername = DAOUser.isValidUsername(username);
        
        if(!isValidUsername){
            fm.setSummary("Pick another username");
            FacesContext.getCurrentInstance().addMessage("signUpForm", fm);
            return "";
        }
        
        
        if(!password.equals(repPassword)){
            fm.setSummary("Passwords must match");
            FacesContext.getCurrentInstance().addMessage("signUpForm", fm);
            return "";
        }
        
        
        boolean isSuccess = DAOUser.addUser(username, password);
        
        if(!isSuccess){
            fm.setSummary("Database error: Can't register user");
            FacesContext.getCurrentInstance().addMessage("signUpForm", fm);
            return "";
        }
        
        
        loginController.setUsername(username);
        
        return Names.page_welcome;
    }

}
