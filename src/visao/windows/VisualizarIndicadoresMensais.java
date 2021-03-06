/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visao.windows;

import controle.ControlePerfil;
import controle.ControleIndicadoresMensais;
import flex.db.GenericDAO;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JScrollBar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import modelo.negocio.DadosEconMensais;
import modelo.negocio.DadosTecMensais;
import modelo.negocio.Perfil;
import util.DecimalFormatRenderer;
import util.FixedColumnTable;
import util.Util;

/**
 * @author Alexandre
 */
public class VisualizarIndicadoresMensais extends javax.swing.JFrame {

    private final Perfil atual;
    private List<DadosEconMensais> dems;
    private List<DadosTecMensais>  dtms;
    private final GenericDAO<DadosEconMensais> demdao;
    private final GenericDAO<DadosTecMensais>  dtmdao;
    private final ControleIndicadoresMensais crm;
    private FixedColumnTable tabelaFixa;
    
    /**
     * Creates new form VisualizarRelatoriosMensais
     */
    public VisualizarIndicadoresMensais() {
        initComponents();
        
        Image image = new ImageIcon(getClass().getResource("/visao/images/cattle.png")).getImage();
        this.setIconImage(image);
        
        tabelaIndicadoresMensais.setShowGrid(true);
        tabelaIndicadoresMensais.getTableHeader().setFont(super.getFont().deriveFont(Font.BOLD));
        tabelaIndicadoresMensais.getTableHeader().setResizingAllowed(false);
        
        crm = ControleIndicadoresMensais.getInstance();
        
        atual = ControlePerfil.getInstance().getPerfilSelecionado();
        perfilLabel.setText(atual.getNome() + " " + ControlePerfil.getInstance().getAno() );
        
        super.setLocationRelativeTo(null);
        super.setResizable(false);
        
        demdao = new GenericDAO<>(DadosEconMensais.class);          
        dtmdao = new GenericDAO<>(DadosTecMensais.class);
        
        if( crm.getTipoIndicador() == 1 ) { // Tipo Indicadores Econ??micos  
            
            dems = demdao.executeSQL("SELECT ano, mes, quantidade, valorUnitario, idDEM_especificacaoFK "
                        + "FROM dados_economicos_mensais AS d "
                        + "WHERE (d.ano >= " + crm.getAnoIni() + "  AND "
                               + "d.ano <= " + crm.getAnoFim() + "  AND "
                               + "d.mes >= " + crm.getMesIni() + "  AND "
                               + "d.mes <= " + crm.getMesFim() + ") AND "
                               + "d.idPerfilFK = " + atual.getId()
                        + " ORDER BY d.ano, d.mes");
            
            dtms = dtmdao.executeSQL("SELECT ano, idDTM_indicadorFK, dado, mes "
                        + "FROM dados_tecnicos_mensais AS d "
                        + "WHERE (d.ano >= " + crm.getAnoIni() + "  AND "
                               + "d.ano <= " + crm.getAnoFim() + "  AND "
                               + "d.mes >= " + crm.getMesIni() + "  AND "
                               + "d.mes <= " + crm.getMesFim() + ") AND "
                               + "d.idPerfilFK = " + atual.getId() + " AND d.idDTM_indicadorFK <= 3"
                        + " ORDER BY d.ano, d.mes");
            
            preencherTabelaIEM(dems, dtms);   
            
            tabelaFixa = new FixedColumnTable(2, jScrollPane1);
        } else if( crm.getTipoIndicador() == 2 ){ // Tipo Indicadores T??cnicos
            
            dems = demdao.executeSQL("SELECT ano, mes, quantidade, idDEM_especificacaoFK "
                        + "FROM dados_economicos_mensais AS d "
                        + "WHERE (d.ano >= " + crm.getAnoIni() + "  AND "
                               + "d.ano <= " + crm.getAnoFim() + "  AND "
                               + "d.mes >= " + crm.getMesIni() + "  AND "
                               + "d.mes <= " + crm.getMesFim() + ") AND "
                               + "d.idPerfilFK = " + atual.getId() + " AND (d.idDEM_especificacaoFK = 7 OR d.idDEM_especificacaoFK = 70)"
                        + " ORDER BY d.ano, d.mes");
            
            dtms = dtmdao.executeSQL("SELECT ano, mes, dado, idDTM_indicadorFK "
                        + "FROM dados_tecnicos_mensais AS d "
                        + "WHERE (d.ano >= " + crm.getAnoIni() + "  AND "
                               + "d.ano <= " + crm.getAnoFim() + "  AND "
                               + "d.mes >= " + crm.getMesIni() + "  AND "
                               + "d.mes <= " + crm.getMesFim() + ") AND "
                               + "d.idPerfilFK = " + atual.getId()
                        + " ORDER BY d.ano, d.mes");
            
            preencherTabelaITM(dtms, dems);
            
            tabelaFixa = new FixedColumnTable(2, jScrollPane1);
            tabelaFixa.getFixedTable().setDefaultRenderer(Object.class, tabelaIndicadoresMensais.getDefaultRenderer(Object.class));
            
        }
        tabelaFixa.getFixedTable().getTableHeader().setFont(super.getFont().deriveFont(Font.BOLD));
        
    }
    
    
    private void preencherTabelaIEM(List<DadosEconMensais> iem, List<DadosTecMensais> itm){
        
        textoEntrada.setText("INDICADORES ECON??MICOS MENSAIS");
        super.setTitle("SGPL - " + atual.getNome() + " - Indicadores Econ??micos Mensais");
        
        DefaultTableModel modelIndicadores = (DefaultTableModel) tabelaIndicadoresMensais.getModel();
        modelIndicadores.setNumRows(0);
        
        int anoCont = crm.getAnoIni();
        int anoFim  = crm.getAnoFim();
        int mesCont = crm.getMesIni();
        int mesFim  = crm.getMesFim(); 
        Object[] temp;        
        modelIndicadores.addColumn("Indicadores", crm.getIndEconomMensais());
        modelIndicadores.addColumn("Unidade", crm.getUniEconomMensais());
        
        do{
            if( mesCont > 12 ){
                mesCont = 1;
                anoCont++;
            }
            
            temp = crm.getConteudoEconomico(iem, itm, mesCont, anoCont);
            
            //----FINAL---------------------------------------------------------
            if (temp != null) {
                modelIndicadores.addColumn(Util.nomeMes(mesCont) + "/" + anoCont, temp);
                Util.clearVector(temp);
            }
            mesCont++;
        }while(anoCont < anoFim || mesCont <= mesFim);
        
        if (tabelaIndicadoresMensais.getColumnCount() <= 2) {
            modelIndicadores.setRowCount(0);
        }
        
        for(int i = 2; i < tabelaIndicadoresMensais.getColumnCount(); i++) {
            tabelaIndicadoresMensais.getColumnModel().getColumn(i).setPreferredWidth(100);
        }
        
        tabelaIndicadoresMensais.setModel(modelIndicadores);
        tabelaIndicadoresMensais.getColumnModel().getColumn(0).setPreferredWidth(390);
        tabelaIndicadoresMensais.setDefaultRenderer(Object.class, new DecimalFormatRenderer(false));
        
    }
    
    private void preencherTabelaITM(List<DadosTecMensais> dtms, List<DadosEconMensais> dems){
        
        textoEntrada.setText("INDICADORES T??CNICOS MENSAIS");
        super.setTitle("SGPL - " + atual.getNome() + " - Indicadores T??cnicos Mensais");
        
        DefaultTableModel modelIndicadores = (DefaultTableModel) tabelaIndicadoresMensais.getModel();
        modelIndicadores.setNumRows(0);
        
        int anoCont = crm.getAnoIni();
        int anoFim  = crm.getAnoFim();
        int mesCont = crm.getMesIni();
        int mesFim  = crm.getMesFim();
        Object[] temp;
        
        modelIndicadores.addColumn("Indicadores", crm.getIndTecnMensais());
        modelIndicadores.addColumn("Unidade", crm.getUniTecnMensais());
        
        do{
            
            if( mesCont > 12 ){
                mesCont = 1;
                anoCont++;
            }
            
            temp = crm.getConteudoTecnico(dtms, dems, mesCont, anoCont);
            
            //----FINAL---------------------------------------------------------
            if(temp != null) {
                modelIndicadores.addColumn(Util.nomeMes(mesCont) + "/" + anoCont, temp);

                Util.clearVector(temp);
            }
            mesCont++;
        }while(anoCont < anoFim || mesCont <= mesFim);
        
        if (tabelaIndicadoresMensais.getColumnCount() <= 2) {
            modelIndicadores.setRowCount(0);
        }
        
        for(int i = 2; i < tabelaIndicadoresMensais.getColumnCount(); i++) {
            tabelaIndicadoresMensais.getColumnModel().getColumn(i).setPreferredWidth(100);
        }
        
        tabelaIndicadoresMensais.getColumnModel().getColumn(0).setPreferredWidth(380);
        tabelaIndicadoresMensais.setDefaultRenderer(Object.class, new DecimalFormatRenderer(false) {
            private final Color BG = null;
            
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); 
                
                if (row == 0 || row == 22) {
                    this.setBackground(Color.LIGHT_GRAY);
                    this.setFont(getFont().deriveFont(Font.BOLD));
                } else {
                    this.setBackground(null);
                }
                
                if (isSelected) {
                    super.setBackground(BG == null ? table.getSelectionBackground(): null);
                }
                return this;
            }
        });
        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnVoltar = new javax.swing.JButton();
        textoEntrada = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelaIndicadoresMensais = new javax.swing.JTable();
        retornarBT = new javax.swing.JButton();
        avancarBT = new javax.swing.JButton();
        perfilLabel = new javax.swing.JLabel();
        excelBT = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        btnVoltar.setText("Voltar");
        btnVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVoltarActionPerformed(evt);
            }
        });

        textoEntrada.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        textoEntrada.setForeground(new java.awt.Color(0, 38, 255));
        textoEntrada.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textoEntrada.setText("INDICADORES MENSAIS");

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        tabelaIndicadoresMensais.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }

        )   {
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });
        tabelaIndicadoresMensais.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tabelaIndicadoresMensais.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabelaIndicadoresMensais.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tabelaIndicadoresMensais);
        tabelaIndicadoresMensais.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        retornarBT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/visao/images/left_arrow.png"))); // NOI18N
        retornarBT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                retornarBTActionPerformed(evt);
            }
        });

        avancarBT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/visao/images/right_arrow.png"))); // NOI18N
        avancarBT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                avancarBTActionPerformed(evt);
            }
        });

        perfilLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        perfilLabel.setForeground(new java.awt.Color(0, 38, 255));
        perfilLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        perfilLabel.setText("jLabel1");

        excelBT.setText("Exportar para Excel");
        excelBT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                excelBTActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(btnVoltar, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(275, 275, 275)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(perfilLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(textoEntrada, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(30, 30, 30)
                        .addComponent(excelBT)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 221, Short.MAX_VALUE)
                        .addComponent(retornarBT, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(avancarBT, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(textoEntrada, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(excelBT, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnVoltar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(perfilLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(retornarBT, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(avancarBT, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 560, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVoltarActionPerformed
        new MenuPrincipal().setVisible(true);
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_btnVoltarActionPerformed

    private void retornarBTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_retornarBTActionPerformed
        JScrollBar barPanel = jScrollPane1.getHorizontalScrollBar();
        barPanel.setValue(barPanel.getValue() - 600);
    }//GEN-LAST:event_retornarBTActionPerformed

    private void avancarBTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_avancarBTActionPerformed
        JScrollBar barPanel = jScrollPane1.getHorizontalScrollBar();
        barPanel.setValue(barPanel.getValue() + 600);
    }//GEN-LAST:event_avancarBTActionPerformed

    private void excelBTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_excelBTActionPerformed
        Util.CSVWriter(tabelaIndicadoresMensais, textoEntrada.getText());
    }//GEN-LAST:event_excelBTActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        this.dispose();
        new MenuPrincipal().setVisible(true);
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton avancarBT;
    private javax.swing.JButton btnVoltar;
    private javax.swing.JButton excelBT;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel perfilLabel;
    private javax.swing.JButton retornarBT;
    private javax.swing.JTable tabelaIndicadoresMensais;
    private javax.swing.JLabel textoEntrada;
    // End of variables declaration//GEN-END:variables
}
