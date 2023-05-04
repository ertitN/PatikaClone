package com.patikadev.Model;

import com.patikadev.Helper.DBConnector;

import java.sql.*;
import java.util.ArrayList;

public class Student extends User{
    public Student() {
    }

    public Student(int id, String name, String uname, String pass, String type) {
        super(id, name, uname, pass, type);
    }

    public static boolean registerCourse(int user_id,int course_id){
        String subquery = "(SELECT course.name from course where course.id = "+course_id+")";

        String query = "INSERT INTO `registeredcourses`(`user_id`, `course_id`, `course_name`) " +
                "VALUES (?,?,"+subquery+")";

        try {

            if (isAvailable(user_id,course_id)){
                PreparedStatement prst = DBConnector.getInstance().prepareStatement(query);
                prst.setInt(1,user_id);
                prst.setInt(2,course_id);
                int result = prst.executeUpdate();
                return result == 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;


    }

    public static boolean isAvailable(int user_id,int course_id){

        String query = "select * from registeredcourses where user_id = "+user_id+" and course_id = "+course_id;

        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);


            if (rs.next()){
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;

    }

    public static boolean deleteRegisteredCourse(int user_id,int course_id){
        String query = "delete from registeredcourses where user_id = ? and course_id = ?";

        try {
            PreparedStatement prst = DBConnector.getInstance().prepareStatement(query);
            prst.setInt(1,user_id);
            prst.setInt(2,course_id);

            int result = prst.executeUpdate();

            return result==1;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;

    }

    public static boolean addComment(String comment,int rate,int content_id,int user_id){
        String query = "insert into comment (comment,rate,content_id,user_id) VALUES (?,?,?,?)";

        try {
            PreparedStatement prst = DBConnector.getInstance().prepareStatement(query);
            prst.setString(1,comment);
            prst.setInt(2,rate);
            prst.setInt(3,content_id);
            prst.setInt(4,user_id);

            int result = prst.executeUpdate();

            return (result == 1);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;


    }


}
