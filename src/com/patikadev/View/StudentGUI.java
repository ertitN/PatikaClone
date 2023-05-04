package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.Course;
import com.patikadev.Model.Patika;
import com.patikadev.Model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StudentGUI extends JFrame {
    private JPanel wrapper;
    private JLabel stu_name;
    private JPanel pnl_top;
    private JButton btn_exit;
    private JTabbedPane tab_student;
    private JTable course_table;
    private JPanel pnl_patika;
    private JScrollPane patika_pane;
    private JPanel pnl_courses;
    private JScrollPane course_pane;
    private JTable patika_table;
    private JPopupMenu patika_menu;
    private JPopupMenu course_menu;

    private int PATIKAID;
    private Student student;
     private DefaultTableModel mdl_patika_list;
     private DefaultTableModel mdl_course;

    private Object[] row_patika_list;
    private Object[] row_course;
    private int courseID;


    public StudentGUI(Student student){
        this.student = student;
        add(wrapper);
        setSize(1000,600);
        setLocation(Helper.screenCenterPoint("x", getSize()), Helper.screenCenterPoint("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);

        stu_name.setText(stu_name.getText()+": "+student.getName());



        // PATIKA TABLE //
        Object[] col_patika_list = {"ID","Patika Adı"};
        mdl_patika_list = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        mdl_patika_list.setColumnIdentifiers(col_patika_list);
        row_patika_list = new Object[col_patika_list.length];
        int i;
        for (Patika patika : Patika.getList()){
            i = 0;
            row_patika_list[i++] = patika.getId();
            row_patika_list[i++] = patika.getName();

            mdl_patika_list.addRow(row_patika_list);
        }

        patika_table.setModel(mdl_patika_list);
        patika_table.getColumnModel().getColumn(0).setMaxWidth(75);
        patika_table.getTableHeader().setReorderingAllowed(false);

        // ** PATIKA TABLE ** //



        // COURSE TABLE//
        mdl_course = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        Object[] col_course = {"Kurs ID","Kurs Adı","Programlama Dili"};
        mdl_course.setColumnIdentifiers(col_course);
        row_course = new Object[col_course.length];

         for (Course course : Course.getRegisteredCourse(student.getId())){
            i = 0;
            row_course[i++] = course.getId();
            row_course[i++] = course.getName();
            row_course[i++] = course.getLang();

            mdl_course.addRow(row_course);
        }

        course_table.setModel(mdl_course);
        course_table.getTableHeader().setReorderingAllowed(false);
        course_table.getColumnModel().getColumn(0).setMaxWidth(60);



        // ** TABLE COURSE **//

        btn_exit.addActionListener(e ->{
            dispose();
            LoginGUI loginGUI = new LoginGUI();
        });

        //Patika Menu
        patika_menu = new JPopupMenu("Kurslar");
        JMenuItem showMenu = new JMenuItem("Patikayı Görüntüle");
        patika_menu.add(showMenu);
        patika_table.setComponentPopupMenu(patika_menu);

        patika_table.getSelectionModel().addListSelectionListener(e -> {
        String id = patika_table.getValueAt(patika_table.getSelectedRow(),0).toString();
         this.PATIKAID = Integer.parseInt(id);

        });

        course_table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();
                int selected_row = course_table.rowAtPoint(point);
                course_table.setRowSelectionInterval(selected_row,selected_row);
            }
        });

        showMenu.addActionListener(e ->  {
           CoursePopUpGUI coursePopUpGUI = new CoursePopUpGUI(PATIKAID,student);

        });
        // ** PATIKA MENU **//



        // COURSE MENU //

        course_menu = new JPopupMenu();
        JMenuItem showContent = new JMenuItem(" Kurs İçeriğini Görüntüle");
        JMenuItem deleteRec = new JMenuItem("Kayıtlı Kursu Sil");
        course_menu.add(showContent);
        course_menu.add(deleteRec);
        course_table.setComponentPopupMenu(course_menu);

        course_table.getSelectionModel().addListSelectionListener(e -> {
            try {


            String id = course_table.getValueAt(course_table.getSelectedRow(),0).toString();
            this.courseID = Integer.parseInt(id);
            }
            catch (Exception ignore){

            }

        });

        course_table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();
                int selected_row = course_table.rowAtPoint(point);
                course_table.setRowSelectionInterval(selected_row,selected_row);
            }
        });

       showContent.addActionListener(e ->{
           ContentPopUpMenu contentPopUpMenu = new ContentPopUpMenu(this.courseID,student);
       });


       deleteRec.addActionListener(e -> {
           if (Helper.confirm("sure")){

               if (Student.deleteRegisteredCourse(student.getId(),this.courseID)){
                   Helper.showMsg("done");
                  loadCourseModel();

               }

               else {
                   Helper.showMsg("error");
               }
               }

       });
    }



    public void loadCourseModel(){
        DefaultTableModel clearModel = (DefaultTableModel) course_table.getModel();
        clearModel.setRowCount(0);
        int i;
        for (Course course : Course.getRegisteredCourse(student.getId())){
            i = 0;
            row_course[i++] = course.getId();
            row_course[i++] = course.getName();
            row_course[i++] = course.getLang();

            mdl_course.addRow(row_course);
        }
    }



}