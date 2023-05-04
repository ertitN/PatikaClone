package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;
import com.patikadev.Helper.Item;
import com.patikadev.Model.Content;
import com.patikadev.Model.Course;
import com.patikadev.Model.Educator;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static com.patikadev.Helper.Helper.*;

public class EducatorGUI extends JFrame {
    private JPanel wrapper;
    private JLabel lbl_welcome;
    private JButton btn_exit;
    private JTable table_course_list;
    private JPanel pnl_header;
    private JPanel pnl_course_list;
    private JScrollPane scrl_course_list;
    private JTabbedPane tab_educator;
    private JPanel pnl_add_content;
    private JTextField fld_content_title;
    private JTextField fld_content_desc;
    private JTextField fld_content_link;
    private JTextField fld_course_id;
    private JLabel content_lbl_1;
    private JLabel link_lbl;
    private JTextField course_id;
    private JButton addButton;
    private JTable table_content_list;
    private JScrollPane scrl_content_list;
    private JPanel fld_delete;
    private JTextField txt_delete;
    private JButton btn_delete;
    private JComboBox cmb_course;
    private JPanel fld_search;
    private JTextField txt_search;
    private JButton btn_search;
    private JLabel lbl_content_name;
    private JTextArea quiz_txt_area;
    private JTextField txt_select_content;
    private JButton quiz_add_btn;
    private JPanel content_panel;
    private DefaultTableModel mdl_course_list;
    private Object[] row_course_list;

    private DefaultTableModel mdl_content_list;
    private Object[] row_content_list;
    private Educator educator;
    private JPopupMenu menu;
    private Content content;

    public EducatorGUI(Educator educator){
        content = new Content();
        this.educator = educator;
        add(wrapper);
        setSize(1000,600);
        setLocation(Helper.screenCenterPoint("x", getSize()), Helper.screenCenterPoint("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);

        lbl_welcome.setText("Eğitmen: "+educator.getName());

        // ModelCourseList

        mdl_course_list = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;

            }
        };
        Object[] col_course_list = {"Kurs ID","name","lang"};
        mdl_course_list.setColumnIdentifiers(col_course_list);
        row_course_list = new Object[col_course_list.length];
        int i;
        for (Course course : Course.getListByUser(educator.getId())){
            i = 0;


            row_course_list[i++] = course.getId();
            row_course_list[i++] = course.getName();
            row_course_list[i++] = course.getLang();
            mdl_course_list.addRow(row_course_list);
        }



        table_course_list.setModel(mdl_course_list);
        table_course_list.getTableHeader().setReorderingAllowed(false);

        table_course_list.getColumnModel().getColumn(0).setMaxWidth(75);
        table_course_list.getSelectionModel().addListSelectionListener(e -> {
            String select_course_id = table_course_list.getValueAt(table_course_list.getSelectedRow(),0).toString();
            fld_course_id.setText(select_course_id);
        });



                // ** ModelCourseList ** //

        // ModelContentList //


        mdl_content_list= new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                if(column==0 || column==4) return false;
                return super.isCellEditable(row, column);
            }
        };
        Object[] col_content_list = {"İçeriğin Ait Olduğu Ders","İçerik Adı","İçerik Açıklaması","Link","İçerik ID"};
        mdl_content_list.setColumnIdentifiers(col_content_list);
        row_content_list = new Object[col_content_list.length];

        loadContentModel();
        loadCourseCombo();
        table_content_list.setModel(mdl_content_list);
        table_content_list.getTableHeader().setReorderingAllowed(false);

        table_content_list.getModel().addTableModelListener(e -> {
                    if (e.getType() == TableModelEvent.UPDATE){
                        String title = table_content_list.getValueAt(table_content_list.getSelectedRow(),1).toString();
                        String desc = table_content_list.getValueAt(table_content_list.getSelectedRow(),2).toString();
                        String link = table_content_list.getValueAt(table_content_list.getSelectedRow(),3).toString();
                        int id = Integer.parseInt(table_content_list.getValueAt(table_content_list.getSelectedRow(),4).toString());

                       if (Content.update(title,desc,link,id)){
                           Helper.showMsg("done");
                           loadContentModel();
                           loadCourseCombo();
                        }
                       else {
                           Helper.showMsg("error");
                       }
                    }
        });

        table_content_list.getSelectionModel().addListSelectionListener(e ->{
            try{

                String content_id = table_content_list.getValueAt(table_content_list.getSelectedRow(),4).toString();
                content.setId(Integer.parseInt(content_id));
                txt_delete.setText(content_id);
                txt_select_content.setText(content_id);

            }
            catch (Exception ignored){

            }
        });

        btn_exit.addActionListener(e -> {
            dispose();
            LoginGUI loginGUI = new LoginGUI();
        });
        addButton.addActionListener(e -> {
            if (isFieldEmpty(fld_content_title) || isFieldEmpty(fld_content_desc) || isFieldEmpty(fld_content_link)
            || isFieldEmpty(fld_course_id)){
                showMsg("fill");
            }
            else {
                String title = fld_content_title.getText();
                String desc = fld_content_desc.getText();
                String link = fld_content_link.getText();
                int id = Integer.parseInt(fld_course_id.getText());

                if (Content.add(title,desc,link,id)){
                    showMsg("done");
                    fld_content_title.setText(null);
                    fld_content_desc.setText(null);
                    fld_content_link.setText(null);
                    fld_course_id.setText(null);
                    loadContentModel();
                }


            }
        });
        btn_delete.addActionListener(e -> {

            String text = txt_delete.getText().toString();
                int content_id = Integer.parseInt(txt_delete.getText());

                if (Helper.confirm("sure")){
                if (Content.delete(content_id)){
                    showMsg("done");
                    txt_delete.setText(null);
                    loadContentModel();
                }
                else{
                    showMsg("error");
                }
                }

            });
        btn_search.addActionListener(e -> {

            if (isFieldEmpty(txt_search) && cmb_course.getSelectedItem().toString().equals("")){
            loadContentModel();
            }
            else {
                loadFilteredList();
            }

        });


        table_content_list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();
                int selected_row = table_content_list.rowAtPoint(point);
                table_content_list.setRowSelectionInterval(selected_row,selected_row);
            }
        });

        menu  = new JPopupMenu();

        JMenuItem addQuizQuestion = new JMenuItem("Quiz Sorusu Ekle");
        menu.add(addQuizQuestion);

        table_content_list.setComponentPopupMenu(menu);

        addQuizQuestion.addActionListener(e -> {
                AddQuestionPopUpGUI addQuestionPopUpGUI = new AddQuestionPopUpGUI(content);
        });
    }






    public void loadFilteredList(){
        DefaultTableModel clearModel = (DefaultTableModel) table_content_list.getModel();
        clearModel.setRowCount(0);
        String contentName =  txt_search.getText();
        String courseName = cmb_course.getSelectedItem().toString();
        int i;

        for (Content c : Content.filterByContentName(contentName,courseName,educator.getId())){
            i = 0;
            row_content_list[i++]=c.getCourseName();
            row_content_list[i++]=c.getTitle();
            row_content_list[i++]=c.getDesc();
            row_content_list[i++]=c.getLink();
            row_content_list[i++]=c.getId();
            mdl_content_list.addRow(row_content_list);
        }

    }
    public void loadContentModel(){
        DefaultTableModel clearModel = (DefaultTableModel) table_content_list.getModel();
        clearModel.setRowCount(0);
        int i;
        for (Content c: Content.getFetchByUserID(educator.getId())){
           i = 0;
            row_content_list[i++]= c.getId();
            row_content_list[i++]=c.getTitle();
            row_content_list[i++]=c.getDesc();
            row_content_list[i++]=c.getLink();
            row_content_list[i++]=c.getCourse().getName();
            mdl_content_list.addRow(row_content_list);
        }
    }

    public void loadCourseCombo(){
        cmb_course.removeAllItems();
        Object obj = new String("");
        cmb_course.addItem(obj);
        for (Course c: Course.getListByUser(educator.getId())){
            cmb_course.addItem(new Item(c.getId(),c.getName()));

        }
    }

    public static void main(String[] args) {
        EducatorGUI ed = new EducatorGUI(new Educator());
    }


}