package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.Content;
import com.patikadev.Model.Educator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddQuestionPopUpGUI extends JFrame {
    private JPanel wrapper;
    private JTextArea question_fld;
    private JPanel question_pnl;
    private JPanel answers_pnl;
    private JTextField txt_right_answer;
    private JTextField txt_opt_1;
    private JTextField txt_opt_2;
    private JTextField txt_opt_3;
    private JButton btn_addQuestion;

    public AddQuestionPopUpGUI(Content content){
        add(wrapper);
        setSize(300,550);
        setLocation(Helper.screenCenterPoint("x",getSize()), Helper.screenCenterPoint("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);


        btn_addQuestion.addActionListener(e -> {
           if(Helper.isFieldEmpty(question_fld) || Helper.isFieldEmpty(txt_right_answer) ||
                    Helper.isFieldEmpty(txt_opt_1)
           || Helper.isFieldEmpty(txt_opt_2) || Helper.isFieldEmpty(txt_opt_3)){

               Helper.showMsg("fill");

           }
           else {
               String question = question_fld.getText();
               String rightAnswer = txt_right_answer.getText();
               if (Educator.addQuizQuestion(question,rightAnswer,content.getId())){
                   if (Educator.addOtherOptions(rightAnswer,txt_opt_1.getText(),txt_opt_2.getText(),txt_opt_3.getText())){
                       Helper.showMsg("done");
                   }


               }
           }
        });
    }
}
