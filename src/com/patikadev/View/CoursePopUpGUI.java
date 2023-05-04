package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.Course;
import com.patikadev.Model.Patika;
import com.patikadev.Model.Student;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CoursePopUpGUI extends JFrame {
    private JPanel wrapper;
    private JTable table_course;
    private JScrollPane course_pane;
    private JPanel pnl_header;
    private JLabel patika_name;
    private DefaultTableModel mdl_course_list;
    private Object[] row_course;
    private JPopupMenu courseMenu;

    private String courseID;
    private Student student;
    private int patikaID;


    public CoursePopUpGUI(int patikaID,Student student){
        this.patikaID = patikaID;
        this.student = student;
        add(wrapper);
        setSize(700,350);
        setLocation(Helper.screenCenterPoint("x",getSize()), Helper.screenCenterPoint("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Dersler");
        setResizable(false);
        setVisible(true);

        Object[] col_course = {"ID","Name","Lang"};
        row_course = new Object[col_course.length];
        mdl_course_list = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        mdl_course_list.setColumnIdentifiers(col_course);
        int i;
        for(Course course : Course.getListByPatikaId(patikaID)){
            i = 0;

            row_course[i++] = course.getId();
            row_course[i++] = course.getName();
            row_course[i++] = course.getLang();

            mdl_course_list.addRow(row_course);


        }

        table_course.setModel(mdl_course_list);
        table_course.getColumnModel().getColumn(0).setMaxWidth(75);
        table_course.getTableHeader().setReorderingAllowed(false);

        // CourseMenu

        courseMenu = new JPopupMenu();

        JMenuItem register = new JMenuItem("Kayıt Ol");
        courseMenu.add(register);

        table_course.setComponentPopupMenu(courseMenu);



        table_course.getSelectionModel().addListSelectionListener(e -> {
         this.courseID = table_course.getValueAt(table_course.getSelectedRow(),0).toString();
        });

        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (Student.registerCourse(student.getId(),Integer.parseInt(courseID))){
                        Helper.showMsg("done");
                        dispose();

                }
                else {
                    Helper.showMsg("Bu kursa daha önceden kaydoldunuz.");
                }

            }
        }

        );










    }


}
