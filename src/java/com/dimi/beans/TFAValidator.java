/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimi.beans;

import com.dimi.db.DAOUser;
import com.dimi.utils.TimeOut;
import com.dimi.utils.UserInfo;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author MITA
 */
@ManagedBean(name = "tfaValidator")
@ViewScoped
public class TFAValidator {

    private String verificationCode;

    private short loginCnt = 3;
    /**
     * in seconds
     */
    private int timeOutDuration = 10;

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public String checkVerificationCode(String username) {
        String secret = DAOUser.getSecret(username);

        GoogleAuthenticator ga = new GoogleAuthenticator();
        boolean isAuthorized = false;
        FacesMessage fm = new FacesMessage("");

        try {
            isAuthorized = ga.authorize(secret, Integer.parseInt(verificationCode));

            if (isAuthorized) {
                DAOUser.activateTFA(username);
                return Names.page_welcome;
            } else {
                if (--loginCnt == 0) {
                    fm.setSummary("Too many failed login attempts. Please, try again in 1 minute");
                    String ip = UserInfo.getIpAddress();
                    TimeOut.startTimeOut(ip, timeOutDuration);
                    return Names.page_timeout;
                }
                fm.setSummary("Invalid verification code:" + loginCnt + " tries left");
            }

        } catch (Exception e) {
            e.printStackTrace();
            fm.setSummary("Error");
        } finally {
            FacesContext.getCurrentInstance().addMessage("tfaForm", fm);
        }
        return "";
    }

}
