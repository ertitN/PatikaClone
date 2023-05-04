package com.patikadev.Model;

import com.patikadev.Helper.DBConnector;

import javax.swing.plaf.nimbus.State;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

public class Question {
    private int question_id;
    private String question;
    private int content_id;
    private String right_answer;

    public Question() {

    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getContent_id() {
        return content_id;
    }

    public void setContent_id(int content_id) {
        this.content_id = content_id;
    }

    public String getRight_answer() {
        return right_answer;
    }

    public void setRight_answer(String right_answer) {
        this.right_answer = right_answer;
    }

    public static int getLastRecordID(){

        String query = "select id from questions ORDER BY id DESC LIMIT 1";

        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);

            if (rs.next()){
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;



    }

    public static ArrayList<Question> getQuestionList(int content_id){
        ArrayList<Question> questionList = new ArrayList<>();
        Question question;
        String query = "select id,question from questions where content_id = "+content_id;

        Statement st = null;
        try {
            st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                question = new Question();
                question.setQuestion_id(rs.getInt("id"));
                question.setQuestion(rs.getString("question"));
                questionList.add(question);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return questionList;


    }

    public static ArrayList<String> getAnswer(int question_id){

        ArrayList<String> answers = new ArrayList<>();
        String query = "select answer from answers where question_id = "+question_id;

        Statement st = null;
        try {
            st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()){
                String answer = rs.getString("answer");
                answers.add(answer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Collections.shuffle(answers);


        return answers;


    }

    public static boolean isOptionTrue(int content_id,String answer){
        answer = "'"+answer+"'";
        String query = "select * from questions where content_id = "+content_id+" and right_answer= "+answer;

        Statement st = null;
        try {
            st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next()){
                return true;
            }

        } catch (SQLException e) {

        }


        return false;
    }

    public static boolean isEmpty(int content_id){
        String query = "select * from questions where content_id ="+content_id;

        Statement st = null;
        try {
            st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next()){
                return false;
            }

        } catch (SQLException e) {

        }

        return true;

    }




}
