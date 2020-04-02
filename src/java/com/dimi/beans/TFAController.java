/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimi.beans;

import com.dimi.utils.QRGenerator;
import com.dimi.db.DAOUser;
import com.google.zxing.WriterException;
import com.warrenstrange.googleauth.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.net.ssl.HttpsURLConnection;

/**
 *
 * @author MITA
 */
@ManagedBean(name = "tfaController")
@ViewScoped
public class TFAController {

    static final int QR_CODE_HEIGHT = 250;
    static final int QR_CODE_WIDTH = 250;

    static final String tfaType = "totp";
    static final String ISSUER = "diplomski";

    private String verificationCode;

    
    private boolean isQRImageEnabled = false;
    
    //---------------------------------------------------
    
    
    
    public boolean showQR(){
        return isQRImageEnabled;
    }
    
    public String enableQRImage(){
        isQRImageEnabled = true;
        return "";
    }
    
    
    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    private boolean isValidVerificationCode = false;

    public boolean isValidVerifCode() {
        return isValidVerificationCode;
    }

    
    /*
    public boolean getIsTimeOut() {
        return isTimeOut;
    }

    
    
    
    public int getLoginCnt() {
        return loginCnt;
    }

    public void setLoginCnt(int loginCnt) {
        this.loginCnt = loginCnt;
    }

    
    
    private void startTimeOut() {

        isTimeOut = true;

        new Thread(new Runnable() {
            public void run() {
                int cnt = 60 * 1000;
                while (cnt > 0) {
                    cnt--;
                }
                isTimeOut = false;
            }
        }).start();

    }
    
    
    public boolean waitThenRender(){
        try {
            Thread.sleep(2500);
        } catch (InterruptedException ex) {
            Logger.getLogger(TFAController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }


*/
    public String checkVerificationCodeInit(String username) {

        String secret = getSecret(username);

        GoogleAuthenticator ga = new GoogleAuthenticator();
        boolean isAuthorized = false;
        FacesMessage fm = new FacesMessage("");

        try {
            isAuthorized = ga.authorize(secret, Integer.parseInt(verificationCode));

            if (isAuthorized) {                
                DAOUser.activateTFA(username);
                return Names.page_successActivation;
            } else {
                fm.setSummary("Invalid verification code");
                return "";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            FacesContext.getCurrentInstance().addMessage("secretForm", fm);
        }
    }

    public String getSecret(String username) {
        return DAOUser.getSecret(username);
    }

    static String generateURI(String secret, String user, String issuer) {
        String uri = "";

        try {

            uri = String.format("otpauth://%s/%s?secret=%s&issuer=%s",
                    tfaType, URLEncoder.encode(user, "UTF-8"), secret, URLEncoder.encode(issuer, "UTF-8"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return uri;
    }

    public String deactivate(String username) {

        boolean success = DAOUser.deactivateTFA(username);

        if (!success) {
            FacesMessage fm = new FacesMessage("Try again");
            FacesContext.getCurrentInstance().addMessage("welcomeForm", fm);
        }

        return "";
    }

    public String activate(String username) {

        if (DAOUser.isActivated(username)) {
            return "";
        }

        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        final GoogleAuthenticatorKey key = gAuth.createCredentials();
        String secret = key.getKey();

        String uri = generateURI(secret, username, ISSUER);

        try {
            QRGenerator.generateQRCodeImage(uri, QR_CODE_HEIGHT, QR_CODE_WIDTH, QRGenerator.QR_CODE_IMAGE_PATH);
        } catch (WriterException ex) {
            Logger.getLogger(TFAController.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        } catch (IOException ex) {
            Logger.getLogger(TFAController.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }

        boolean status = DAOUser.saveSecret(username, secret);

        String errMsg = "";
        if (status == false) {
            FacesMessage fm = new FacesMessage();
            fm.setSummary("Error saving secret:\n");
            FacesContext.getCurrentInstance().addMessage("activateForm", fm);
            return "";
        }

        return Names.page_secret;

    }

}
