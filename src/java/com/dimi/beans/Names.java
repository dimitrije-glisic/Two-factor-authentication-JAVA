/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimi.beans;

import com.dimi.utils.*;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author MITA
 */
@ManagedBean
@ApplicationScoped
public class Names {

    final static String page_index = "/user/index";
    final static String page_secret = "/user/secretPage";
    final static String page_signUp = "/user/sign_up";
    final static String page_successActivation = "/user/succesful_activation";
    final static String page_tfaWizard = "/user/tfa_wizard";
    final static String page_welcome = "/user/welcome";

    final static String page_timeout = "/user/timeout";

    public String getPage_index() {
        return page_index;
    }

    public String getPage_secret() {
        return page_secret;
    }

    public String getPage_signUp() {
        return page_signUp;
    }

    public String getPage_successActivation() {
        return page_successActivation;
    }

    public String getPage_tfaWizard() {
        return page_tfaWizard;
    }

    public String getPage_welcome() {
        return page_welcome;
    }

    public String getPage_timeout() {
        return page_timeout;
    }

}
