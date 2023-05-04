package com.patikadev.View;

import com.patikadev.Helper.*;
import com.patikadev.Model.*;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Objects;
import static com.patikadev.Helper.Config.PROJECT_TITLE;
import static com.patikadev.Helper.Helper.*;

public class OperatorGUI extends JFrame {
    private JPanel wrapper;
    private JTabbedPane tab_operator;
    private JLabel lbl_welcome;
    private JPanel pnl_top;
    private JButton btn_logout;
    private JPanel pnl_user_list;
    private JScrollPane scrl_user_list;
    private JTable tbl_user_list;
    private JPanel pnl_user_form;
    private JTextField fld_user_name;
    private JTextField fld_user_uname;
    private JPasswordField fld_user_pass;
    private JComboBox cmb_user_type;
    private JButton btn_user_add;
    private JTextField fld_user_id;
    private JButton btn_user_delete;
    private JTextField fld_sh_user_name;
    private JTextField fld_sh_user_uname;
    private JComboBox cmb_sh_user_type;
    private JButton btn_user_sh;
    private JPanel pnl_patika_list;
    private JScrollPane scrl_patika_list;
    private JTable tbl_patika_list;
    private JPanel pnl_patika_add;
    private JTextField fld_patika_name;
    private JButton btn_patika_add;
    private JPanel pnl_course_list;
    private JPanel pnl_user_top;
    private JScrollPane scrl_course_list;
    private JTable tbl_course_list;
    private JPanel pnl_course_add;
    private JTextField fld_course_name;
    private JTextField fld_course_lang;
    private JComboBox cmb_course_patika;
    private JComboBox cmb_course_user;
    private JButton btn_course_add;
    private JPanel pnl_content_list;
    private JTable tbl_content_list;
    private JScrollPane scrl_content;
    private JPanel pnl_content_search;
    private JTextField fld_content_search;
    private JLabel lbl_content_search;
    private JButton content_search_btn;
    private JComboBox cmb_course;
    private DefaultTableModel mdl_user_list;

    private Object[] row_user_list;
    private final Operator operator;
    private DefaultTableModel mdl_patika_list;

    private Object[] row_patika_list;
    private JPopupMenu patikaMenu;
    private DefaultTableModel mdl_course_list;
    private Object[] row_course_list;
    private DefaultTableModel mdl_content_list;
    private Object[] row_content_list;
    private Course course;

    private Content content;



    public OperatorGUI(Operator operator){
        course = new Course();
        content = new Content();
        this.operator=operator;
        add(wrapper);
        setSize(1000,600);
        setLocation(Helper.screenCenterPoint("x",getSize()), Helper.screenCenterPoint("y",getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(PROJECT_TITLE);
        setVisible(true);

        lbl_welcome.setText("Hoşgeldin: " + operator.getName());

        // UserList
        mdl_user_list = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                if(column==0) return false;
                return super.isCellEditable(row, column);
            }
        };
        Object[] col_user_list = {"ID", "Ad Soyad", "Kullanıcı Adı", "Şifre", "Üyelik Tipi"};
        mdl_user_list.setColumnIdentifiers(col_user_list);
        row_user_list = new Object[col_user_list.length];
        loadUserModel();

        tbl_user_list.setModel(mdl_user_list);
        tbl_user_list.getTableHeader().setReorderingAllowed(false);
        tbl_user_list.getColumnModel().getColumn(0).setMaxWidth(50);

        tbl_user_list.getSelectionModel().addListSelectionListener(e -> {
            try {
                String select_user_id = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(),0).toString();
                fld_user_id.setText(select_user_id);
            }
            catch (Exception ignored){

            }

        });

        tbl_user_list.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE){
                int user_id = Integer.parseInt(tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(),0).toString());
                String user_name = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(),1).toString();
                String user_uname = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(),2).toString();
                String user_pass = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(),3).toString();
                String user_type = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(),4).toString();

                if (User.update(user_id, user_name, user_uname, user_pass, user_type)) showMsg("done");
                loadUserModel();
                loadEducaterCombo();
                loadCourseModel();
            }
        });
        // ## UserList

        // PatikaList
        patikaMenu = new JPopupMenu();
        JMenuItem updateMenu = new JMenuItem("Güncelle");
        JMenuItem deleteMenu = new JMenuItem("Sil");
        patikaMenu.add(updateMenu);
        patikaMenu.add(deleteMenu);

        updateMenu.addActionListener(e -> {
            int select_id = Integer.parseInt(tbl_patika_list.getValueAt(tbl_patika_list.getSelectedRow(), 0).toString());
            UpdatePatikaGUI updateGUI = new UpdatePatikaGUI(Patika.getFetch(select_id));
            updateGUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadPatikaModel();
                    loadPatikaCombo();
                    loadCourseModel();
                }
            });
        });

        deleteMenu.addActionListener(e -> {
            if(Helper.confirm("sure")){
                int select_id = Integer.parseInt(tbl_patika_list.getValueAt(tbl_patika_list.getSelectedRow(), 0).toString());
                if (Patika.delete(select_id)){
                    Helper.showMsg("done");
                    loadPatikaModel();
                    loadPatikaCombo();
                    loadCourseModel();
                }else{
                    Helper.showMsg("error");
                }
            }
        });

        mdl_patika_list = new DefaultTableModel();
        Object [] col_patika_list = {"ID", "Patika Adı"};
        mdl_patika_list.setColumnIdentifiers(col_patika_list);
        row_patika_list = new Object[col_patika_list.length];
        loadPatikaModel();

        tbl_patika_list.setModel(mdl_patika_list);
        tbl_patika_list.setComponentPopupMenu(patikaMenu);
        tbl_patika_list.getTableHeader().setReorderingAllowed(false);
        tbl_patika_list.getColumnModel().getColumn(0).setMaxWidth(50);

        tbl_patika_list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();
                int selected_row = tbl_patika_list.rowAtPoint(point);
                tbl_patika_list.setRowSelectionInterval(selected_row,selected_row);
            }
        });
        // ## PatikaList ## //

        // CourseList
        mdl_course_list = new DefaultTableModel();
        Object[] col_course_list = {"ID", "Ders Adı", "Programlama Dili", "Patika","Eğitmen"};
        mdl_course_list.setColumnIdentifiers(col_course_list);
        row_course_list = new Object[col_course_list.length];
        loadCourseModel();
        tbl_course_list.setModel(mdl_course_list);
        tbl_course_list.getColumnModel().getColumn(0).setMaxWidth(50);
        tbl_course_list.getTableHeader().setReorderingAllowed(false);
        loadPatikaCombo();
        loadEducaterCombo();
        // ## CourseList ## //

        // Content List

        mdl_content_list = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0 || column == 3){
                    return false;
                }
                return true;
            }
        };

        Object[] col_content_list = {"İçerik ID","İçerik Adı","Açıklama","Ders Bilgisi"};
        mdl_content_list.setColumnIdentifiers(col_content_list);
        row_content_list = new Object[col_content_list.length];
        loadContentModel();
        loadCourseCombo();
        tbl_content_list.setModel(mdl_content_list);
        tbl_content_list.getColumnModel().getColumn(0).setMaxWidth(50);


        tbl_content_list.getSelectionModel().addListSelectionListener(e -> {
            try {
                String select_course_name = tbl_content_list.getValueAt(tbl_content_list.getSelectedRow(),1).toString();
            }
            catch (Exception ignored){

            }

        });

        tbl_content_list.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE){
                int content_id = Integer.parseInt(tbl_content_list.getValueAt(tbl_content_list.getSelectedRow(),0).toString());
                String content_name = tbl_content_list.getValueAt(tbl_content_list.getSelectedRow(),1).toString();
                String content_desc = tbl_content_list.getValueAt(tbl_content_list.getSelectedRow(),2).toString();

                if (Content.update(content_name,content_desc,content_id)) showMsg("done");
            }
        });

        tbl_content_list.getTableHeader().setReorderingAllowed(false);

        JPopupMenu contentPopUp = new JPopupMenu();
        JMenuItem deleteContent = new JMenuItem("Sil");
        contentPopUp.add(deleteContent);
        tbl_content_list.setComponentPopupMenu(contentPopUp);

        tbl_content_list.getSelectionModel().addListSelectionListener(e -> {
            try{
            int id = Integer.parseInt( tbl_content_list.getValueAt(tbl_content_list.getSelectedRow(),0).toString());
            content.setId(id);
            }
            catch (IndexOutOfBoundsException exception){

            }
        });


        deleteContent.addActionListener(e -> {

           if (Helper.confirm("sure")){
              if (Content.delete(content.getId())){
                  Helper.showMsg("done");

              }
              else {
                  Helper.showMsg("error");
              }
               loadContentModel();
           }
        });



        btn_user_add.addActionListener(e -> {
            if (isFieldEmpty(fld_user_name) || isFieldEmpty(fld_user_uname) || isPassEmpty(fld_user_pass)) {
                showMsg("fill");
            }else {
                String name = fld_user_name.getText();
                String uname = fld_user_uname.getText();
                String pass = String.valueOf(fld_user_pass.getPassword());
                String type = Objects.requireNonNull(cmb_user_type.getSelectedItem()).toString();

                if (User.add(name,uname,pass,type)){
                    showMsg("done");
                    loadUserModel();
                    loadEducaterCombo();
                    fld_user_name.setText(null);
                    fld_user_uname.setText(null);
                    fld_user_pass.setText(null);
                }
            }
        });

        btn_user_delete.addActionListener(e -> {
            if (isFieldEmpty(fld_user_id)) showMsg("fill");
            else{
                if (Helper.confirm("sure")){
                    int user_id = Integer.parseInt(fld_user_id.getText());
                    if (User.delete(user_id)){
                        showMsg("done");
                        loadUserModel();
                        loadEducaterCombo();
                        loadCourseModel();
                        fld_user_id.setText(null);
                    }else showMsg("error");
                }
            }
        });

        btn_user_sh.addActionListener(e -> {
            String name = fld_sh_user_name.getText();
            String uname = fld_sh_user_uname.getText();
            String type = cmb_sh_user_type.getSelectedItem().toString();
            String query = User.searchQuery(name,uname,type);
            loadUserModel(User.searchUserList(query));
        });

        btn_logout.addActionListener(e -> {
            dispose();
            LoginGUI loginGUI = new LoginGUI();
        });

        btn_patika_add.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_patika_name)) Helper.showMsg("fill");
            else{
                if (Patika.add(fld_patika_name.getText())){
                    Helper.showMsg("done");
                    loadPatikaModel();
                    loadPatikaCombo();
                    fld_patika_name.setText(null);
                }else Helper.showMsg("error");
            }
        });

        btn_course_add.addActionListener(e -> {
            Item patikaItem = (Item) cmb_course_patika.getSelectedItem();
            Item userItem = (Item) cmb_course_user.getSelectedItem();
            if (Helper.isFieldEmpty(fld_course_name) || Helper.isFieldEmpty(fld_course_lang)) Helper.showMsg("fill");
            else {
                if (Course.add(userItem.getKey(), patikaItem.getKey(), fld_course_name.getText(), fld_course_lang.getText())){
                    Helper.showMsg("done");
                    loadCourseModel();
                    fld_course_lang.setText(null);
                    fld_course_name.setText(null);
                }else{
                    Helper.showMsg("error");
                }
            }

        });

        content_search_btn.addActionListener(e ->{
            String name = (String) cmb_course.getSelectedItem().toString();
            loadFilteredModel(fld_content_search.getText(),name);
        });
    }

    private void loadCourseModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_course_list.getModel();
        clearModel.setRowCount(0);
        for (Course obj: Course.getList()){
            int i=0;
            row_course_list[i++] = obj.getId();
            row_course_list[i++] = obj.getName();
            row_course_list[i++] = obj.getLang();
            row_course_list[i++] = obj.getPatika().getName();

            row_course_list[i++] = obj.getEducator().getName();

            mdl_course_list.addRow(row_course_list);
        }
    }

    private void loadContentModel(){
        DefaultTableModel clearModel = (DefaultTableModel) tbl_content_list.getModel();
        clearModel.setRowCount(0);
        for (Content content: Content.getMergedContentandCourse()){
            int i=0;
            row_content_list[i++] = content.getId();
            row_content_list[i++] = content.getTitle();
            row_content_list[i++] = content.getDesc();
            row_content_list[i++] = content.getCourseName();
            mdl_content_list.addRow(row_content_list);
        }
    }

    private void loadFilteredModel(String contentName, String courseName){
        DefaultTableModel clearModel = (DefaultTableModel) tbl_content_list.getModel();
        clearModel.setRowCount(0);
        for (Content content: Content.filterByContentName(contentName,courseName)){
            int i=0;
            row_content_list[i++] = content.getId();
            row_content_list[i++] = content.getTitle();
            row_content_list[i++] = content.getDesc();
            row_content_list[i++] = content.getCourseName();
            mdl_content_list.addRow(row_content_list);
        }
    }

    public void loadCourseCombo(){
        cmb_course.removeAllItems();
        Object obj = new String("");
        cmb_course.addItem(obj);
        for (Course c: Course.getList()){
            cmb_course.addItem(new Item(c.getId(),c.getName()));

        }
    }

    private void loadPatikaModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_patika_list.getModel();
        clearModel.setRowCount(0);
        for (Patika obj: Patika.getList()){
            int i=0;
            row_patika_list[i++]=obj.getId();
            row_patika_list[i++]=obj.getName();
            mdl_patika_list.addRow(row_patika_list);
        }
    }

    public void loadUserModel(){
        DefaultTableModel clearModel = (DefaultTableModel) tbl_user_list.getModel();
        clearModel.setRowCount(0);
        for (User obj: User.getList()){
            int i=0;
            row_user_list[i++]=obj.getId();
            row_user_list[i++]=obj.getName();
            row_user_list[i++]=obj.getUname();
            row_user_list[i++]=obj.getPass();
            row_user_list[i++]=obj.getType();
            mdl_user_list.addRow(row_user_list);
        }
    }

    public void loadUserModel(ArrayList<User> list){
        DefaultTableModel clearModel = (DefaultTableModel) tbl_user_list.getModel();
        clearModel.setRowCount(0);
        for (User obj: list){
            int i=0;
            row_user_list[i++]=obj.getId();
            row_user_list[i++]=obj.getName();
            row_user_list[i++]=obj.getUname();
            row_user_list[i++]=obj.getPass();
            row_user_list[i++]=obj.getType();
            mdl_user_list.addRow(row_user_list);
        }
    }



    public void loadPatikaCombo(){
        cmb_course_patika.removeAllItems();
        for (Patika obj: Patika.getList()){
            cmb_course_patika.addItem(new Item(obj.getId(), obj.getName()));
        }
    }

    public void loadEducaterCombo(){
        cmb_course_user.removeAllItems();
        for (User obj: User.getListOnlyeducator()){
            cmb_course_user.addItem(new Item(obj.getId(), obj.getName()));
        }
    }



}