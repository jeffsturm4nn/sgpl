/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visao.windows;

import controle.ControleLogin;
import flex.db.GenericDAO;
import java.awt.Image;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import modelo.negocio.Usuario;

/**
 *
 * @author Alexandre
 */
public class AddUsuario extends javax.swing.JDialog {
    
    private Usuario usuarioSelecionado; 
    private boolean isNew;
    
    /**
     * Creates new form AddUsuario
     */
    public AddUsuario(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        Image image = new ImageIcon(getClass().getResource("/visao/images/cattle.png")).getImage();
        this.setIconImage(image);
                
        this.setIsNew(true);
        
        super.setLocationRelativeTo(null);
        super.setResizable(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupPermissao = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        campoLogin = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        campoSenha = new javax.swing.JPasswordField();
        campoSenhaNovam = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        radioAdm = new javax.swing.JRadioButton();
        radioCom = new javax.swing.JRadioButton();
        radioVis = new javax.swing.JRadioButton();
        textoEntrada = new javax.swing.JLabel();
        btnSalvar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        textoDescricao = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel1.setText("Login:");

        campoLogin.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel2.setText("Senha:");

        campoSenha.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        campoSenhaNovam.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel3.setText("Senha novamente:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel4.setText("Tipo de usuário:");

        buttonGroupPermissao.add(radioAdm);
        radioAdm.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        radioAdm.setText("Administrador");
        radioAdm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioAdmActionPerformed(evt);
            }
        });

        buttonGroupPermissao.add(radioCom);
        radioCom.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        radioCom.setText("Usuário comum");
        radioCom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioComActionPerformed(evt);
            }
        });

        buttonGroupPermissao.add(radioVis);
        radioVis.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        radioVis.setText("Apenas visualização");
        radioVis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioVisActionPerformed(evt);
            }
        });

        textoEntrada.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        textoEntrada.setForeground(new java.awt.Color(0, 38, 255));
        textoEntrada.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textoEntrada.setText("CADASTRO DE USUÁRIO");

        btnSalvar.setText("Salvar");
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        textoDescricao.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(campoSenha)
                        .addComponent(campoLogin, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addComponent(jLabel1)
                        .addComponent(jLabel3)
                        .addComponent(campoSenhaNovam))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(radioVis)
                            .addComponent(radioCom)
                            .addComponent(radioAdm))))
                .addContainerGap(49, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textoEntrada, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 145, Short.MAX_VALUE)
                        .addComponent(btnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(textoDescricao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(textoEntrada)
                .addGap(43, 43, 43)
                .addComponent(jLabel1)
                .addGap(5, 5, 5)
                .addComponent(campoLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(campoSenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(campoSenhaNovam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioAdm)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioCom)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioVis)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(textoDescricao)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed

        GenericDAO<Usuario> udao = new GenericDAO<>(Usuario.class);
        
        //VERIFICA SE ALGUM CAMPO ESTÁ VAZIO
        
        
        //VERIFICA SE OS CAMPOS DE SENHA ESTÃO IGUAIS
        if(!verificarSenhas( campoSenha.getPassword() , campoSenhaNovam.getPassword() )){
            JOptionPane.showMessageDialog(null, "As senhas estão diferentes! Verifique.");
            return;
        }
                
        if( isNew ){
            //VERIFICA SE JÁ EXISTE UM USUÁRIO CADASTRADO COM O LOGIN
            List<Usuario> resultado = udao.executeSQL("SELECT * "
                                                    + "FROM usuario AS u "
                                                    + "WHERE u.login = \"" + campoLogin.getText() + "\"");
            if( !resultado.isEmpty() ){
                JOptionPane.showMessageDialog(null, "Já existe um usuário cadastrado como " + campoLogin.getText());
                return;
            }
        }
                
        //VERIFICA O TIPO DE USUÁRIO. CASO "ADMINISTRADOR" ESTEJA MARCADO, O USUÁRIO PRECISARÁ CONFIRMAR A ESCOLHA
        int radioSelecionado;

        if(      radioAdm.isSelected() ) { radioSelecionado = 1; }
        else if( radioCom.isSelected() ) { radioSelecionado = 2; }
        else if( radioVis.isSelected() ) { radioSelecionado = 3; }
        else { JOptionPane.showMessageDialog(null, "Selecione o tipo de usuário!"); return; }

        int escolha = 0;
        
        if( radioSelecionado == 1 ){
            escolha = JOptionPane.showOptionDialog(null, "Deseja realmente tornar " + campoLogin.getText().toUpperCase() + " um administrador?\n"
                    + " Ele(a) terá acesso à todos os perfis cadastrados.",
                    "Confirmar seleção de administrador", JOptionPane.YES_NO_OPTION, 
                    JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Não");
        }
        
        if( escolha == 1){
            return;
        }
        
        Usuario usuario;
        
        if( isNew ){
            usuario = new Usuario(campoLogin.getText(), senhaToString(campoSenha.getPassword()), radioSelecionado);
            udao.insert(usuario);
            
            JOptionPane.showMessageDialog(null, "Usuário cadastrado com sucesso!");
        } else {
            Usuario usuarioAtual = ControleLogin.getInstance().getUsuario();
            usuario = this.getUsuarioSelecionado();
            
            String input = JOptionPane.showInputDialog(this, "Edição do usuário " + 
                        usuario.getLogin().toUpperCase() + ".\nDigite seu login para confirmação: ", 
                        "Confirmar Exclusão de Perfil", JOptionPane.OK_CANCEL_OPTION);
             
            if (usuarioAtual.getLogin().equals(input)) {

                usuario.setLogin(campoLogin.getText());
                usuario.setSenha(senhaToString(campoSenha.getPassword()));
                usuario.setTipoUsuario(radioSelecionado);

                udao.update(usuario);
                JOptionPane.showMessageDialog(null, "Usuário atualizado com sucesso!");

            } else if (!usuarioAtual.getLogin().equals(input) && input != null){
                JOptionPane.showMessageDialog(this, "Login Incorreto.", "Login inválido", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (input == null){
                return;
            }
            
                     
        }
        
        this.setUsuarioSelecionado(usuario);
        
        this.setVisible(false);
        this.dispose();
        
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void radioAdmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioAdmActionPerformed
        textoDescricao.setText("O administrador terá acesso à todos os perfis cadastrados");
    }//GEN-LAST:event_radioAdmActionPerformed

    private void radioComActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioComActionPerformed
        textoDescricao.setText("O usuário terá acesso aos perfis selecionados");
    }//GEN-LAST:event_radioComActionPerformed

    private void radioVisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioVisActionPerformed
        textoDescricao.setText("O usuário terá acesso aos perfis apenas para visualização");
    }//GEN-LAST:event_radioVisActionPerformed

    public Usuario getUsuarioSelecionado() {
        return usuarioSelecionado;
    }

    public void setUsuarioSelecionado(Usuario usuarioSelecionado) {
        this.usuarioSelecionado = usuarioSelecionado;
    }

    public boolean isIsNew() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }
        
    public String senhaToString(char[] senhaVet){
        
        String senhaStr = "";
        
        for(int i = 0; i < senhaVet.length; i++){
            senhaStr += senhaVet[i];
        }
        
        return senhaStr;
        
    }
    
    public boolean verificarSenhas(char[] senha1, char[] senha2){
        
        if(senha1.length != senha2.length){
            return false;
        }
        
        for( int i = 0; i < senha1.length; i++ ){
            if(senha1[i] != senha2[i]){
                return false;
            }
        }
        
        return true;
        
    }
    
    public void prepararParaEdicao(Usuario usuario){
        
        this.setIsNew(false);
        this.setUsuarioSelecionado(usuario);
        
        textoEntrada.setText("EDIÇÃO DE USUÁRIO");
        
        campoLogin.setText( usuario.getLogin() );
        campoSenha.setText( usuario.getSenha() );
        campoSenhaNovam.setText( usuario.getSenha() );
        
        //--RadioButton---------------------------
        int tipoUsu = usuario.getTipoUsuario();
        
        switch (tipoUsu) {
            case 1:
                radioAdm.setSelected(true);              
                break;
            case 2:
                radioCom.setSelected(true);
                break;
            case 3:
                radioVis.setSelected(true);
                break;
            default:
                break;
        }
        
    }
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnSalvar;
    private javax.swing.ButtonGroup buttonGroupPermissao;
    private javax.swing.JTextField campoLogin;
    private javax.swing.JPasswordField campoSenha;
    private javax.swing.JPasswordField campoSenhaNovam;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JRadioButton radioAdm;
    private javax.swing.JRadioButton radioCom;
    private javax.swing.JRadioButton radioVis;
    private javax.swing.JLabel textoDescricao;
    private javax.swing.JLabel textoEntrada;
    // End of variables declaration//GEN-END:variables
}