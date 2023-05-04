package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.Educator;
import com.patikadev.Model.Operator;
import com.patikadev.Model.Student;
import com.patikadev.Model.User;
import javax.swing.*;

public class LoginGUI extends JFrame{
    private JPanel wrapper;
    private JPanel wtop;
    private JPanel wbottom;
    private JTextField fld_user_name;
    private JPasswordField fld_user_pass;
    private JButton btn_login;
    private JButton btn_register;

    public LoginGUI(){
        add(wrapper);
        setSize(400,400);
        setLocation(Helper.screenCenterPoint("x", getSize()), Helper.screenCenterPoint("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);

        btn_login.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_user_name) || Helper.isFieldEmpty(fld_user_pass)) Helper.showMsg("fill");
            else{
                User u = User.getFetch(fld_user_name.getText(), String.valueOf(fld_user_pass.getPassword()));
                if (u==null) Helper.showMsg("Kullanıcı bulunamadı!");
                else{
                    switch (u.getType()){
                        case "operator":
                            OperatorGUI opGUI = new OperatorGUI((Operator) u);
                            break;
                        case "educator":
                            EducatorGUI edGUI = new EducatorGUI( (Educator) u);
                            break;
                        case "student":
                            StudentGUI stGUI = new StudentGUI((Student)u);
                            break;
                        default:
                    }
                    dispose();
                }
            }
        });
        btn_register.addActionListener(e -> {
            RegisterGUI.getInstance();

        });
    }

    public static void main(String[] args) {
        Helper.setLayout();
        LoginGUI login = new LoginGUI();
    }

}