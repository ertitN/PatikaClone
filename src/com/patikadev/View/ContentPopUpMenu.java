package com.patikadev.View;

import com.patikadev.Helper.Helper;
import com.patikadev.Model.Content;
import com.patikadev.Model.Question;
import com.patikadev.Model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

public class ContentPopUpMenu extends JFrame {
    private JPanel wrapper;
    private JTable content_table;
    private JPanel pnl_header;
    private JScrollPane content_pane;
    private JPanel pnl_link;
    private JTextField fld_link;
    private DefaultTableModel mdl_content;
    private Object[] row_content;
    private JPopupMenu menu;
    private Content content;
    private Student student;

    public ContentPopUpMenu(int courseID,Student student) {
        content = new Content();
        this.student = student;


        add(wrapper);
        setSize(700,350);
        setLocation(Helper.screenCenterPoint("x",getSize()), Helper.screenCenterPoint("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Ders İçeriği");
        setResizable(false);
        setVisible(true);

        mdl_content = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        Object[] col_content = {"ID","İçerik Adı","Açıklama"};
        row_content = new Object[col_content.length];
        mdl_content.setColumnIdentifiers(col_content);
        int i;
        HashMap<Integer,String> linkMap = new HashMap<>();
        for (Content content : Content.getFetchByCourseId(courseID)){
            i = 0;
            row_content[i++] = content.getId();
            row_content[i++] = content.getTitle();
            row_content[i++] = content.getDesc();
            linkMap.put(content.getId(),content.getLink());
            mdl_content.addRow(row_content);

        }

        
        content_table.setModel(mdl_content);
        content_table.getTableHeader().setReorderingAllowed(false);



        content_table.getSelectionModel().addListSelectionListener(e -> {
                String id = content_table.getValueAt(content_table.getSelectedRow(),0).toString();
                content.setId(Integer.parseInt(id));
                fld_link.setText(linkMap.get(content.getId()));
        });

        content_table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();
                int selected_row = content_table.rowAtPoint(point);
                content_table.setRowSelectionInterval(selected_row,selected_row);
            }
        });


         menu = new JPopupMenu();
        JMenuItem showQuiz = new JMenuItem("Quiz sorularını görüntüle");
        JMenuItem addComments = new JMenuItem("Yorum Ekle");

        menu.add(showQuiz);
        menu.add(addComments);

        content_table.setComponentPopupMenu(menu);

        showQuiz.addActionListener(e -> {
            if (Question.isEmpty(content.getId())){
                    Helper.showMsg("İçeriğe ait quiz sorusu bulunmamaktadır !");
            }
            else {
                QuizGUI quizGUI = new QuizGUI(content.getId());
            }

        });

        addComments.addActionListener(e ->{
            AddCommentsGUI addCommentsGUI = new AddCommentsGUI(content.getId(),student);
        });






    }
}
