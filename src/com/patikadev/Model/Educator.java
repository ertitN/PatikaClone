package com.patikadev.Model;

import com.patikadev.Helper.DBConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Educator extends User{
    public Educator() {
    }

    public Educator(int id, String name, String uname, String pass, String type) {
        super(id, name, uname, pass, type);
    }


    public static boolean addQuizQuestion(String question,String right_answer,int contentID){
        String query = "insert into questions (content_id,question,right_answer) VALUES (?,?,?)";

        Question quest;

        try {
            PreparedStatement prst = DBConnector.getInstance().prepareStatement(query);
            prst.setInt(1,contentID);
            prst.setString(2,question);
            prst.setString(3,right_answer);

            int result = prst.executeUpdate();

            return result == 1;



        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;


    }

    public static boolean addOtherOptions(String rightAnswer,String opt1, String opt2, String opt3){
            int question_id = Question.getLastRecordID();
        String query = "insert into answers (question_id,answer) VALUES (?,?),(?,?),(?,?),(?,?)";

        try {
            PreparedStatement prst = DBConnector.getInstance().prepareStatement(query);
            prst.setInt(1,question_id);
            prst.setString(2,rightAnswer);
            prst.setInt(3,question_id);
            prst.setString(4,opt1);
            prst.setInt(5,question_id);
            prst.setString(6,opt2);
            prst.setInt(7,question_id);
            prst.setString(8,opt3);

            int result = prst.executeUpdate();
            return result==1;
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return false;

    }




}
