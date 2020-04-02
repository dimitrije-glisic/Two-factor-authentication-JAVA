/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimi.utils;

import com.dimi.db.DAOTimeOut;
import com.dimi.db.DAOUser;

/**
 *
 * @author MITA
 */
public class TimeOut {
    
    public static boolean isTimeOuted(String ip){
        
        return DAOTimeOut.isTimeOuted(ip);
        
    }
    
    
    public static long timeOutEndsIn(String ip){
        return DAOTimeOut.timeOutEndsInSeconds(ip);
    }
    
    
    public static void startTimeOut(String ip, int time){
        DAOTimeOut.startTimeOut(ip, time);
    }
    
    
}
