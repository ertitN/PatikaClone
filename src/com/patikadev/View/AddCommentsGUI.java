package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.Student;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddCommentsGUI extends JFrame {
    private JPanel wrapper;
    private JTextArea comment_area;
    private JPanel header;
    private JSpinner rate_spinner;
    private JLabel lbl_comment;
    private JLabel lbl_rate;
    private JButton btn_submit;
    private Student student;

    public AddCommentsGUI(int content_id,Student student) {
        this.student = student;
        add(wrapper);
        SpinnerModel value = new SpinnerNumberModel(1,1,5,1);
        rate_spinner.setModel(value);
        rate_spinner.setEditor(new JSpinner.DefaultEditor((rate_spinner)));
        setSize(300,400);
        setLocation(Helper.screenCenterPoint("x",getSize()), Helper.screenCenterPoint("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);


        btn_submit.addActionListener(e -> {
            if (Helper.isFieldEmpty(comment_area)){
                Helper.showMsg("fill");
            }
            else {
               String comment = comment_area.getText();
               int rate = (int) rate_spinner.getValue();
               int user_id = student.getId();

               if (Student.addComment(comment,rate,content_id,user_id)){
                   Helper.showMsg("done");
               }
               else {
                   Helper.showMsg("error");
               }
            }
        });
    }
}
