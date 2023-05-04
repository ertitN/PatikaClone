package com.patikadev.Model;

import com.patikadev.Helper.DBConnector;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Course {
    private int id;
    private int user_id;
    private int patika_id;
    private String name;
    private String lang;
    private Patika patika;
    private User educator;

    public Course(int id, int user_id, int patika_id, String name, String lang) {
        this.id = id;
        this.user_id = user_id;
        this.patika_id = patika_id;
        this.name = name;
        this.lang = lang;
        this.patika= Patika.getFetch(patika_id);
        this.educator= User.getFetch(user_id);
    }



    public Course(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getpatika_id() {
        return patika_id;
    }

    public void setpatika_id(int patika_id) {
        this.patika_id = patika_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Patika getPatika() {
        return patika;
    }

    public void setPatika(Patika patika) {
        this.patika = patika;
    }

    public User getEducator() {
        return educator;
    }

    public void setEducator(User educator) {
        this.educator = educator;
    }

    public static ArrayList<Course> getList() {
        ArrayList<Course> courseList = new ArrayList<>();
        Course obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM course");
            while(rs.next()){
                int id = rs.getInt("id");
                int user_id = rs.getInt("user_id");
                int patika_id = rs.getInt("patika_id");
                String name = rs.getString("name");
                String lang = rs.getString("lang");
                obj = new Course(id,user_id,patika_id,name,lang);
                courseList.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courseList;
    }

    public static ArrayList<Course> getListByPatikaId(int patikaID){
        ArrayList<Course> courseList = new ArrayList<>();
        String query = "select * from course where patika_id ="+patikaID;

        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()){
           int id = rs.getInt("id");
           int user_id = rs.getInt("user_id");
           int patika_id = rs.getInt("patika_id");
           String courseName = rs.getString("name");
           String lang = rs.getString("lang");

           courseList.add(new Course(id,user_id,patika_id,courseName,lang));


            }


        } catch (SQLException e) {
            e.printStackTrace();
        }


        return courseList;
    }

    public static String getCourseNameById(int course_id){
        String query = "select name from course where id ="+course_id;
        String courseName = null;

        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);

            if (rs.next()){
                courseName = rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return courseName;
    }

    public static boolean add(int user_id, int patika_id, String name, String lang){
        String query = "INSERT INTO course (user_id, patika_id, name, lang) VALUES (?,?,?,?)";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, user_id);
            pr.setInt(2, patika_id);
            pr.setString(3, name);
            pr.setString(4, lang);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Course> getListByUser(int user_id) {
        ArrayList<Course> courseList = new ArrayList<>();
        Course obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM course WHERE user_id = "+user_id);
            while(rs.next()){
                int id = rs.getInt("id");
                int userID = rs.getInt("user_id");
                int patika_id = rs.getInt("patika_id");
                String name = rs.getString("name");
                String lang = rs.getString("lang");
                obj = new Course(id,userID,patika_id,name,lang);
                courseList.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courseList;
    }

    public static ArrayList<Course> getRegisteredCourse(int user_id){
        ArrayList<Course> registeredCourses = new ArrayList<>();
        String query = "SELECT registeredcourses.course_id, registeredcourses.course_name,course.lang from registeredcourses" +
                " INNER JOIN course ON course.name =registeredcourses.course_name where registeredcourses.user_id = "+user_id;

        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                Course course = new Course();
                int id = rs.getInt("course_id");
                String name = rs.getString("course_name");
                String lang = rs.getString("lang");
                course.setId(id);
                course.setName(name);
                course.setLang(lang);
                registeredCourses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return registeredCourses;


    }


    public static boolean delete(int id){
        String query = "DELETE FROM course WHERE id = ?";

        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1,id);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}