/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visao.windows;

import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import util.Pair;

/**
 *
 * @author Jefferson Sales
 * @param <T>
 */
public class ItemSelector< T > extends JDialog {

    private final List<Pair<String, T>> optionList;
    private final List<JRadioButton> radioList;
    private T selectedOption;
    
    public ItemSelector(Frame parent, List<Pair<String, T>> optionList) {
        super(parent, true);
        
        this.optionList = optionList;
        this.radioList = new ArrayList<>();
        
        initComponents();
        
        Image image = new ImageIcon(getClass().getResource("/visao/images/cattle.png")).getImage();
        this.setIconImage(image);
        
        panel.setLayout(new GridLayout(optionList.size(), 1));
        
        super.setTitle("SGPL - Selecione:");
        super.setLocationRelativeTo(null);
        
        insertOptions();
    }
    
    private void insertOptions(){
     
        for (int i = 0; i < optionList.size(); i++) {

            JRadioButton radio = new JRadioButton(optionList.get(i).first);

            radio.setHorizontalAlignment(SwingConstants.CENTER);

            radio.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    
                    if (e.getClickCount() == 2) {
                        selectedOption = getSelectedOptionData();
                        ItemSelector.this.setVisible(false);
                    }
                }
            });

            radioGroup.add(radio);
            panel.add(radio);
            radioList.add(radio);
        }

    }
    
    private T getSelectedOptionData(){
        
        for (int i=0; i<optionList.size(); i++) {
            
            if(radioList.get(i).isSelected()){
                return optionList.get(i).second;
            } 
        }
        
        return null;
    }
    
    public T showSelector(){
        
        this.setVisible(true);
        return selectedOption;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        radioGroup = new javax.swing.ButtonGroup();
        label = new javax.swing.JLabel();
        panel = new javax.swing.JPanel();
        okayBT = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        label.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label.setText("Sample Text");

        panel.setAutoscrolls(true);

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 184, Short.MAX_VALUE)
        );

        okayBT.setText("OK");
        okayBT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okayBTActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
            .addGroup(layout.createSequentialGroup()
                .addGap(78, 78, 78)
                .addComponent(okayBT, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(17, 17, 17)
                .addComponent(okayBT, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okayBTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okayBTActionPerformed
        selectedOption = getSelectedOptionData();
        this.setVisible(false);
    }//GEN-LAST:event_okayBTActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        selectedOption = null;
    }//GEN-LAST:event_formWindowClosed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel label;
    private javax.swing.JButton okayBT;
    private javax.swing.JPanel panel;
    private javax.swing.ButtonGroup radioGroup;
    // End of variables declaration//GEN-END:variables

    public String getLabel() {
        return label.getText();
    }

    public void setLabel(String label) {
        this.label.setText(label);
    }
    
/*
    public static void main(String[] args) {
        
        List<Pair<String,String>> options = new ArrayList<>();
        options.add(new Pair<>("asdf","555555555"));
        options.add(new Pair<>("asdf","9999999"));
        options.add(new Pair<>("yuoi","88888888888"));
        options.add(new Pair<>("zxcv","7777777777"));
        
        ItemSelector<String> selector = new ItemSelector<>(null,options);
        selector.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        System.out.println(selector.showSelector());
        System.exit(0);
    }
*/
    
}
