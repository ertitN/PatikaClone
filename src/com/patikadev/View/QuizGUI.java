package com.patikadev.View;

import com.patikadev.Helper.Helper;
import com.patikadev.Model.Operator;
import com.patikadev.Model.Question;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Enumeration;

public class QuizGUI extends JFrame{


    private JPanel wrapper;
    private JLabel lbl_quest;
    private JPanel question_header;
    private JPanel question_section;
    private JRadioButton aRadioButton;
    private JRadioButton bRadioButton;
    private JRadioButton cRadioButton;
    private JRadioButton dRadioButton;
    private JPanel answer_section;
    private JLabel lbl_question_fld;
    private JButton btn_submit;
    private WindowAdapter listener;
    private ButtonGroup bg;
    private static int i;

    public QuizGUI(int contentID) {

        add(wrapper);
        setSize(400, 400);
        setLocation(Helper.screenCenterPoint("x", getSize()), Helper.screenCenterPoint("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Quiz Bölümü");
        setResizable(false);
        setVisible(true);

        bg = new ButtonGroup();
        bg.add(aRadioButton);
        bg.add(bRadioButton);
        bg.add(cRadioButton);
        bg.add(dRadioButton);




        ArrayList<String> answers;
        ArrayList<Question> question = Question.getQuestionList(contentID);

        if (question.size() == i){
            dispose();
            i = 0;

        }


        try {
            answers = new ArrayList<>(Question.getAnswer(question.get(i).getQuestion_id()));
            lbl_question_fld.setText(question.get(i).getQuestion());
            aRadioButton.setText(answers.get(0));
            bRadioButton.setText(answers.get(1));
            cRadioButton.setText(answers.get(2));
            dRadioButton.setText(answers.get(3));
        } catch (IndexOutOfBoundsException e) {

        }

        aRadioButton.setActionCommand(aRadioButton.getText());
        bRadioButton.setActionCommand(bRadioButton.getText());
        cRadioButton.setActionCommand(cRadioButton.getText());
        dRadioButton.setActionCommand(dRadioButton.getText());



        listener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                i = 0;
            }
        };
        addWindowListener(listener);


        btn_submit.addActionListener(e -> {
            try {
                String answer =  bg.getSelection().getActionCommand();
                if (Question.isOptionTrue(contentID,answer)){
                    Helper.showMsg("Doğru cevap");
                }
                else {
                    Helper.showMsg("Yanlış cevap");
                }

                i++;
                dispose();
                new QuizGUI(contentID);
            }
            catch (NullPointerException exception){
                Helper.showMsg("Bir şık seçiniz.");

            }


        });
    }

}

