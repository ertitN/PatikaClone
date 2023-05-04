package com.patikadev.Model;

import com.patikadev.Helper.DBConnector;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;

public class Content {
    int id;
    String title;
    String desc;
    String link;
    String question;
    int course_id;
    String courseName;

    Course course;

    public Content() {

    }
    public Content(int id,String title, String desc, String link,Course course) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.link = link;
        this.course = course;

    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public static boolean add(String title, String desc, String link, int course_id){
        String query = "insert into content (title,description,link,course_id) values (?,?,?,?)";

        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1,title);
            pr.setString(2,desc);
            pr.setString(3,link);
            pr.setInt(4,course_id);
            int result = pr.executeUpdate();
            return (result==1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("fail");
        return false;
    }

    public static ArrayList<Content> getFetch(){
        ArrayList<Content> contentList = new ArrayList<>();
        Content content = null;
        String query = "select title,description,link,course_id,content_id from content";

        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);

            while(rs.next()){
                content = new Content();
                content.setTitle(rs.getString("title"));
                content.setDesc(rs.getString("description"));
                content.setLink(rs.getString("link"));
                content.setCourse_id(rs.getInt("course_id"));
                content.setId(rs.getInt("content_id"));
                contentList.add(content);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contentList;

    }

    public static ArrayList<Content> getMergedContentandCourse(){
        ArrayList<Content> contentList = new ArrayList<>();
        Content content = null;
        String query = "select content.content_id,content.title,content.description,course.name,content.link from content " +
                "INNER JOIN course on course.id = content.course_id;";

        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);

            while(rs.next()){
                content = new Content();
                content.setId(rs.getInt("content.content_id"));
                content.setTitle(rs.getString("content.title"));
                content.setDesc(rs.getString("content.description"));
                content.setLink(rs.getString("content.link"));
                content.setCourse(new Course());
                content.getCourse().setName(rs.getString("course.name"));
                contentList.add(content);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contentList;

    }

    public static ArrayList<Content> getFetchByCourseId(int course_id){
        ArrayList<Content> contentList = new ArrayList<>();
        Content content = null;
        String query = "select content_id,title,description,link from content where course_id = "+course_id;

        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);



            while(rs.next()){
                content = new Content();
                content.setId(rs.getInt("content_id"));
                content.setTitle(rs.getString("title"));
                content.setDesc(rs.getString("description"));
                content.setLink(rs.getString("link"));
                contentList.add(content);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contentList;

    }

    public static ArrayList<Content> getFetchByUserID(int user_id){

     String query = "select content.content_id ,content.title,content.description,content.link,course.name from content\n" +
             "INNER JOIN course on content.course_id = course.id WHERE course.user_id = "+user_id;

            ArrayList<Content> contentList = new ArrayList<>();
            Content content = null;
            Course course = null;

        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()){
                course = new Course();
                int id = rs.getInt("content.content_id");
                String title = rs.getString("content.title");
                String desc = rs.getString("content.description");
                String link = rs.getString("content.link");
                String courseName = rs.getString("course.name");
                course.setName(courseName);
                content = new Content(id,title,desc,link,course);
                contentList.add(content);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return contentList;
    }


    public static boolean delete(int content_id){
            String query = "delete from content where content_id = ?";
        try {
            PreparedStatement prst = DBConnector.getInstance().prepareStatement(query);
            prst.setInt(1,content_id);
            int result = prst.executeUpdate();
            return result == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ArrayList<Content> filterByContentName(String contentName,String courseName,int user_id){
        String query = "select course.name, content.title,content.description,content.link,content.content_id from content "+
                "INNER JOIN course ON content.course_id = course.id INNER JOIN user ON course.user_id = user.id WHERE"+
                " content.title LIKE '%"+contentName+"%' AND user_id= "+user_id;

        if (!courseName.isEmpty()){
            query +=" AND course.name = '"+courseName+"'";

        }

        ArrayList<Content> filteredList = new ArrayList<>();
        Content content;


        try {
           Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()){
                content = new Content();
                content.setCourse(new Course());
                content.getCourse().setName(rs.getString("course.name"));
                content.setTitle(rs.getString("title"));
                content.setDesc(rs.getString("description"));
                content.setLink(rs.getString("link"));
                content.setId(rs.getInt("content_id"));

                filteredList.add(content);


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return filteredList;

    }
    public static ArrayList<Content> filterByContentName(String contentName,String courseName){
        String query = "select course.name, content.title,content.description,content.link,content.content_id from content "+
                "INNER JOIN course ON content.course_id = course.id INNER JOIN user ON course.user_id = user.id WHERE"+
                " content.title LIKE '%"+contentName+"%'";

        if (!courseName.isEmpty()){
            query +=" AND course.name = '"+courseName+"'";

        }

        ArrayList<Content> filteredList = new ArrayList<>();
        Content content;


        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()){
                content = new Content();
                content.setCourseName(rs.getString("name"));
                content.setTitle(rs.getString("title"));
                content.setDesc(rs.getString("description"));
                content.setLink(rs.getString("link"));
                content.setId(rs.getInt("content_id"));

                filteredList.add(content);


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return filteredList;

    }




    public static boolean update(String title,String description,String link,int content_id){
        String query = "update content set title = ?,description = ?, link = ? WHERE content_id = ?";

        try {
            PreparedStatement prst = DBConnector.getInstance().prepareStatement(query);
            prst.setString(1,title);
            prst.setString(2,description);
            prst.setString(3,link);
            prst.setInt(4,content_id);
            int result = prst.executeUpdate();

            return (result ==1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean update(String title,String description,int content_id){
        String query = "update content set title = ?,description = ? WHERE content_id = ?";

        try {
            PreparedStatement prst = DBConnector.getInstance().prepareStatement(query);
            prst.setString(1,title);
            prst.setString(2,description);
            prst.setInt(3,content_id);
            int result = prst.executeUpdate();

            return (result ==1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateQuizQuestion(String question,int content_id){
        String query = "update questions set question = ? WHERE content_id = ?";

        try {
            PreparedStatement prst = DBConnector.getInstance().prepareStatement(query);
            prst.setString(1,question);
            prst.setInt(2,content_id);
            int result = prst.executeUpdate();
            return result == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean addQuizQuestion(String question,int content_id){
        String query = "insert into questions (questions.question,questions.content_id) VALUES (?,?)";

        try {
            PreparedStatement prst = DBConnector.getInstance().prepareStatement(query);
            prst.setString(1,question);
            prst.setInt(2,content_id);
            int result = prst.executeUpdate();
            return result == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void getContentLink(int content_id){
        String query = "select link from content where content_id ="+content_id;
            Content content;
        Statement st = null;
        try {
            st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            content  = new Content();
        } catch (SQLException e) {

        }

    }

}
