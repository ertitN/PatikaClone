package com.patikadev.View;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterGUI extends JFrame {
    private JPanel wrapper;
    private JLabel lbl_name;
    private JTextField fld_name;
    private JTextField fld_uname;
    private JPasswordField fld_pass;
    private JButton btn_register;
    public static RegisterGUI instance;

    public RegisterGUI() {
        add(wrapper);
        setSize(350, 300);
        setLocation(Helper.screenCenterPoint("x", getSize()), Helper.screenCenterPoint("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Kayıt Ol");
        setResizable(false);
        setVisible(true);


        btn_register.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_name) || Helper.isFieldEmpty(fld_uname) || Helper.isPassEmpty(fld_pass)){
                Helper.showMsg("fill");
            }
            else{
                if (User.add(fld_name.getText(),fld_uname.getText(),fld_pass.getText())){
                    Helper.showMsg("done");
                    dispose();
                }
                else {
                    Helper.showMsg("Bu kullanıcı adı daha önceden eklenmiş");
                }

            }
        });
    }

    public static RegisterGUI getInstance() {
        if (instance == null) {
            instance = new RegisterGUI();
        }
        else if (instance.isVisible()) {
            instance.toFront();
        }
        else {

            instance.setVisible(true);
        }
        return instance;
    }

}
