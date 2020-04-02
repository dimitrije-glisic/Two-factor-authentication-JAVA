/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimi.beans;

import com.dimi.db.DAOTimeOut;
import com.dimi.utils.TimeOut;
import com.dimi.utils.UserInfo;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author MITA
 */
@ManagedBean
@ViewScoped
public class TimeOutBean implements Serializable {

    private long timeOutInSeconds;

    public TimeOutBean() {
        String ip = UserInfo.getIpAddress();
        this.timeOutInSeconds = TimeOut.timeOutEndsIn(ip);

    }

    //--------------------------------------------------------------------------
    public void init() {
        /*
        new Thread(new Runnable(){
            public void run(){
                while(timeOutInSeconds > 0){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(TimeOutBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    timeOutInSeconds--;
                }
            }
        }).start();
        
         */

    }

    public void checkIfTimeOutFinished() {

        if (timeOutInSeconds <= 0) {

            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance()
                    .getExternalContext().getRequest();

            //this will end timeout and delete from from _timeout table
            TimeOut.isTimeOuted(UserInfo.getIpAddress());
            String loginURL = request.getContextPath() + "/user/index.xhtml";

            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(loginURL);
            } catch (IOException ex) {
                Logger.getLogger(TimeOutBean.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    
    public void decrement(){
        timeOutInSeconds--;
        if(timeOutInSeconds == 1)
            timeOutInSeconds--;
    }
    
    public int getTimeOutInSeconds() {
        return (int) timeOutInSeconds;
    }

    public void setTimeOutInSeconds(int timeOutInSeconds) {
        this.timeOutInSeconds = timeOutInSeconds;
    }

}
