/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimi.db;

import static com.dimi.db.DAOUser.con;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author MITA
 */
public class DAOTimeOut {

    final static String ltimeOut = "_timeout";
    final static String lip = "ip";
    final static String lstartTime = "startTime";
    final static String lduration = "duration";

    //--------------------------------------------------------------------------
    public static void startTimeOut(String ip, int time) {
        PreparedStatement ps = null;
        String sql = null;
        try {
            con = DB.getInstance().getConnection();

            sql = String.format("delete from %s where %s = ?",
                    ltimeOut, lip);
            ps = con.prepareStatement(sql);
            ps.setString(1, ip);
            ps.executeUpdate();

            sql = String.format("insert into %s (%s,%s,%s) values (?,?,?)",
                    ltimeOut, lip, lstartTime, lduration);
            ps = con.prepareStatement(sql);

            ps.setString(1, ip);
            ps.setLong(2, System.currentTimeMillis());
            ps.setInt(3, time);

            ps.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static boolean isTimeOuted(String ip) {

        PreparedStatement ps = null;
        String sql = null;
        ResultSet rs = null;
        try {
            con = DB.getInstance().getConnection();
            sql = String.format("select %s, %s from %s where %s = ?",
                    lstartTime, lduration, ltimeOut, lip);
            ps = con.prepareStatement(sql);
            ps.setString(1, ip);

            rs = ps.executeQuery();

            if (!rs.next()) {
                return false;
            }

            long startTime = rs.getLong(1);
            int timeoutTime = rs.getInt(2); //in seconds;

            long currTime = System.currentTimeMillis();

            long timePassed = (currTime - startTime) / 1000; //in seconds

            boolean isTimeOut = (timeoutTime - timePassed) > 0;

            if (isTimeOut == false) {
                sql = String.format("delete from %s where %s = ?",
                        ltimeOut, lip);
                ps = con.prepareStatement(sql);
                ps.setString(1, ip);
                ps.executeUpdate();

            }

            return isTimeOut;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static long timeOutEndsInSeconds(String ip) {

        PreparedStatement ps = null;
        String sql = null;
        ResultSet rs = null;
        try {
            con = DB.getInstance().getConnection();

            sql = String.format("select %s, %s from %s where %s = ?",
                    lstartTime, lduration, ltimeOut, lip);
            ps = con.prepareStatement(sql);
            ps.setString(1, ip);

            rs = ps.executeQuery();

            rs.next();

            long startTime = rs.getLong(1);
            int timeoutTime = rs.getInt(2); //in seconds;

            long currTime = System.currentTimeMillis();

            long timePassed = currTime - startTime;
            timePassed /= 1000;

            return timeoutTime - timePassed;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return Integer.MAX_VALUE;
    }

    public void updateTimeOut(String ip) {

    }

}
