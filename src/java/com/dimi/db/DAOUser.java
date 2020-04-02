/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimi.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author MITA
 */
@ManagedBean(name = "daoUser")
@RequestScoped
public class DAOUser {

    public static class User {

        String username;
        String password;
        String secret;
        boolean isActivated;

        //--------------------------------------------------------------------------
        public User() {

        }

        public User(String username, String password, String secret, boolean isActivated) {
            this.username = username;
            this.password = password;
            this.secret = secret;
            this.isActivated = isActivated;
        }

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

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public boolean isIsActivated() {
            return isActivated;
        }

        public void setIsActivated(boolean isActivated) {
            this.isActivated = isActivated;
        }

    }

    //--------------------------------------------------------------------------
    //-----------DAOUser--------------------------------------------------------
    final static String luser = "_user";
    final static String lusername = "_username";
    final static String lpassword = "_password";
    final static String lsecret = "_secret";
    final static String lactivated = "_activated";
    
    static Connection con = null;

    //--------------------------------------------------------------------------
    public static User getUser(String username) {

        User user = new User();
        PreparedStatement ps = null;
        String sql = null;
        ResultSet rs = null;

        try {

            con = DB.getInstance().getConnection();

            sql = "select * from _user where _username = ?";

            ps = con.prepareStatement(sql);
            ps.setString(1, username);

            rs = ps.executeQuery();

            rs.next();

            user.setUsername(rs.getString(2));
            user.setPassword(rs.getString(lpassword));
            user.setSecret(rs.getString(lsecret));
            user.setIsActivated(rs.getBoolean(lactivated));

            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    public static String getSecret(String username) {
        PreparedStatement ps = null;
        String sql = null;
        ResultSet rs = null;
        String secret = "";
        try {
            con = DB.getInstance().getConnection();
            sql = String.format("select %s from %s where %s = ?", lsecret, luser, lusername);

            ps = con.prepareStatement(sql);
            ps.setString(1, username);

            rs = ps.executeQuery();
            rs.next();

            secret = rs.getString(1);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DAOUser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DAOUser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return secret;
    }

    public static boolean saveSecret(String username, String secret) {

        PreparedStatement ps = null;
        String sql = null;
        try {
            con = DB.getInstance().getConnection();
            sql = String.format("update %s set %s=? where %s=?", luser, lsecret, lusername);

            ps = con.prepareStatement(sql);
            ps.setString(1, secret);
            ps.setString(2, username);

            int affectedRows = ps.executeUpdate();

            return affectedRows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DAOUser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return false;
    }

    public static boolean activateTFA(String username) {
        PreparedStatement ps = null;
        String sql = null;
        try {
            con = DB.getInstance().getConnection();

            sql = String.format("update %s set %s = ? where %s = ?", luser, lactivated, lusername);

            ps = con.prepareStatement(sql);
            ps.setInt(1, 1);
            ps.setString(2, username);

            int affectedRows = ps.executeUpdate();

            return affectedRows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    }

    public static boolean deactivateTFA(String username) {
        PreparedStatement ps = null;
        String sql = null;
        try {
            con = DB.getInstance().getConnection();

            sql = String.format("update %s set %s = ? where %s = ?", luser, lactivated, lusername);

            ps = con.prepareStatement(sql);
            ps.setInt(1, 0);
            ps.setString(2, username);

            int affectedRows = ps.executeUpdate();

            return affectedRows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    }

    public static boolean isActivated(String username) {

        PreparedStatement ps = null;
        String sql = null;
        ResultSet rs = null;
        try {
            con = DB.getInstance().getConnection();

            sql = String.format("select %s from %s where %s = ?", lactivated, luser, lusername);

            ps = con.prepareStatement(sql);
            ps.setString(1, username);

            rs = ps.executeQuery();

            rs.next();

            boolean isActivated = rs.getBoolean(1);

            return isActivated;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    }

    public static boolean addUser(String username, String password) {

        PreparedStatement ps = null;
        String sql = null;
        try {
            con = DB.getInstance().getConnection();

            sql = String.format("insert into %s(%s,%s) values (?,?)", luser, lusername, lpassword);

            ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            int affectedRows = ps.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static boolean addKey(String username, String secret) {

        PreparedStatement ps = null;
        String sql = null;

        try {

            con = DB.getInstance().getConnection();

            sql = "update _user set _secret = ? where _username = ?";

            ps = con.prepareStatement(sql);
            ps.setString(1, secret);
            ps.setString(2, username);

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                return true;
            }

            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DAOUser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    public static String getKey(String username) {
        PreparedStatement ps = null;
        String sql = null;
        ResultSet rs = null;
        try {
            con = DB.getInstance().getConnection();

            sql = "select _secret from _user where _username = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, username);

            rs = ps.executeQuery();

            String secret = rs.getString(1);

            if (secret != null) {
                return secret;
            }

            return null;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isValidUsername(String username) {

        User u = getUser(username);

        return u.username == null || "".equals(u.username);

    }

    
    //----------------------------------------------------------------------------
    
    
    
    
}
