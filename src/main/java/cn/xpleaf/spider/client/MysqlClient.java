package cn.xpleaf.spider.client;

import cn.xpleaf.spider.core.pojo.Page;

import java.sql.PreparedStatement;
//import java.sql.*;
//import java.util.Map;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MysqlClient {
    // 定义了 MySQL 数据库的连接 URL、用户名和密码
    private static String URL = "jdbc:mysql://localhost:3306/ispider?useSSL=false&serverTimezone=Hongkong&characterEncoding=utf-8&autoReconnect=true";
    private static String NAME = "root";
    private static String PASS = "zzxx1215";
    private static PreparedStatement ps;
//    static {
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
////            Connection conn = DriverManager.getConnection(URL, NAME, PASS);
//            ////stmt = conn.createStatement();
//            //ps = conn.prepareStatement();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
    public static Connection getConnection(){
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, NAME, PASS);
            ////stmt = conn.createStatement();
            //ps = conn.prepareStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 向 image 表中插入图片相关信息
     * @param page 包含图片信息的 Page 对象
//     * @param imageData 图片的二进制数据
//     * @param imgUrl 图片的 URL
//     * @param image_size 图片的分类
//     * @param image_memory 图片的分类
//     * @param tags 图片的标签
     * @return 插入操作影响的行数
     */

    public static int insert(Page page) {
        int row = 0;
        String sql = "INSERT INTO image (id, img_url, image_data, image_size, image_memory, tags) VALUES (?, ?, ?, ?, ?, ?)";
        Connection connection = getConnection();
        try {
            connection.setAutoCommit(false);

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, page.getId());
            ps.setString(2, page.getImgUrl());
            ps.setBytes(3, page.getImageData());
            ps.setString(4, page.getImageSize());
            ps.setString(5, page.getImageMemory());

            String tagsString = String.join(",", page.getTags());
            ps.setString(6, tagsString);

            row = ps.executeUpdate();

            System.out.println("Inserted rows: " + row); // 添加日志

            connection.commit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return row;
    }
