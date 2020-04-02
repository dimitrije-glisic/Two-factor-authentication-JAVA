/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimi.beans;

import com.dimi.db.DAOUser;
import com.dimi.db.DAOUser.User;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;



/**
 *
 * @author MITA
 */
@ManagedBean
@SessionScoped
public class LoginController implements Serializable {

    private String username;
    private String password;

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

    public String login() {

        User user = DAOUser.getUser(this.username);

        FacesMessage message = new FacesMessage();

        if (user.getUsername() == null || !(user.getUsername().equals(this.username))) {
            message.setSummary("Wrong username");
            FacesContext.getCurrentInstance().addMessage("loginForm", message);
            return "";
        }

        if (!(user.getPassword().equals(this.password))) {
            message.setSummary("Wrong password");
            FacesContext.getCurrentInstance().addMessage("loginForm", message);
            return "";
        }

        Map<String, Object> requestMap = FacesContext
                .getCurrentInstance()
                .getExternalContext()
                .getSessionMap();

        requestMap.put("username", this.username);
        
        
        return DAOUser.isActivated(username) ? Names.page_tfaWizard : Names.page_welcome;

    }

    public String logout() {

        ((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false)).invalidate();

        return Names.page_index;
    }

}
