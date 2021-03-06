/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flex.table;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import util.Cast;
import util.Regex;


/**
 * @version 1.7.16
 * @author Jefferson Sales
 */
public abstract class GenericTableModifier extends JDialog{

    /**
     * Creates new form GenericTableModifier
     */
    private final Frame parent;
    protected JTable sourceTable;
    
    private List<Object> customRowDataList;
    private List<TableModifyListener> tableModifylisteners;
    private HashMap<Integer, String> columnRegexMap;
    
    private int rowsDisplayed;
    private int stringColumnsOffset;
    
    private boolean allowEmptyRows;
    private boolean allowEmptyCells;
    
    private boolean forceCellEditing;
    
    private boolean columnEditable[];
    private List<boolean[]> cellEditable; 

    private boolean rebuildEditTable;
    
    private List<StringColumnData> stringColumnsData;
    
    
    protected class StringColumnData{
        
        public String columnTitle;
        public int columnWidth;
        public List<String> columnData; 
        public TableCellRenderer columnRenderer;
        
        public StringColumnData() {
            this.columnTitle = "";
            this.columnWidth = 150;
            this.columnData = new ArrayList<>();
            this.columnRenderer = null;
        }
        
        public StringColumnData(int columnWidth, String columnTitle, List<String> columnData) {
            this.columnTitle = columnTitle;
            this.columnWidth = columnWidth;
            this.columnData = columnData;
        }
        
        public StringColumnData(int columnWidth, String columnTitle, List<String> columnData, TableCellRenderer columnRenderer){
            this(columnWidth,columnTitle,columnData);
            this.columnRenderer = columnRenderer;
        }
    }
    
    
    protected GenericTableModifier(Frame parent, JTable sourceTable, boolean forceCellEditing) {
        super(parent, true);
        
        this.parent = parent;
        this.sourceTable = sourceTable;
        this.forceCellEditing = forceCellEditing;
        this.tableModifylisteners = new ArrayList<>();
        this.customRowDataList = new ArrayList<>();
        this.columnRegexMap = new HashMap<>();
        
        this.allowEmptyCells = true;
        this.allowEmptyRows = false;
        
        this.rowsDisplayed = 1;
        this.stringColumnsOffset = 0;
        this.stringColumnsData = new ArrayList<>();
        
        this.columnEditable = new boolean[0];
        this.cellEditable = new ArrayList<>();
        
        this.rebuildEditTable = true;
        
        initComponents();
        
        editTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        editTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        editTable.setAutoCreateColumnsFromModel(false);
        editTable.setCellSelectionEnabled(true);
        editTable.setShowHorizontalLines(true);
        editTable.setShowVerticalLines(true);
        
        this.setLabelText("");
        super.setVisible(false);
        super.setResizable(false);
        
    }
    
    public String createLog(){
        
        final String line = "--------------------------------------------------------------------------------";
        
        String log = "";
        
        log += line + "\nModifier Name: " + getName() + "\n";
        log += "Modifier Class: " + this.getClass().getCanonicalName() + "\n\n";
        
        log += "Number of Registered TableModifyListeners: " + tableModifylisteners.size() + "\n\n";
        
        if(columnRegexMap.size() > 0){
            for(Map.Entry<Integer, String> entry : columnRegexMap.entrySet()){
                log += "Regex Defined for Column #" + entry.getKey() + ": " + entry.getValue() + "\n";
            }
            log += "\n";
        } else { log += "Column Regex Map is empty" + "\n\n"; }
        
        log += "Allow Empty Rows: " + allowEmptyRows + "\n";
        log += "Allow Empty Cells: " + allowEmptyCells + "\n";
        log += "Force Cell Editing: " + forceCellEditing + "\n\n";
        
        log += "EditTable Columns Editable: ";
        for (int i = 0; i < columnEditable.length; i++) {
            log += "[" + i + "-" + columnEditable[i] + "] "; 
        }
        log += "\n\n";
        
//        log += "EditTable Rows Editable: ";
//        for(int i = 0; i < sourceRowEditable.length; i++) {
//            log += "[" + i + "-" + sourceRowEditable[i] + "] ";
//        }
//        log += "\n\n";
        
        log += "Editor Window Width: " + this.getWidth() + "\n";
        log += "EditTable Width: " + editTable.getWidth() + "\n";
        log += "EditTable ScrollPane Width: " + editTableScroll.getWidth() + "\n\n";
        
        log += "EditTable Columns Width: ";
        for (int i = 0; i < editTable.getColumnCount(); i++) {
            log += "[" + i + "-(" + editTable.getColumnModel().getColumn(i).getWidth() + ")] ";
        }
        log += "\n\n";
        
        log += "Edit Table Row Count: " + editTable.getRowCount() + " row(s)" + "\n";
        log += "Edit Table Column Count: " + editTable.getColumnCount() + " column(s)" + "\n\n";
        
        log += "Source Table Row Count: " + sourceTable.getRowCount() + " row(s)" + "\n";
        log += "Source Table Column Count: " + sourceTable.getColumnCount() + " column(s)" + "\n\n";
        
        log += "Rows Displayed in EditTable: " + this.rowsDisplayed + "\n";
        
        return log + "\n";
    }
    
    
    public void addTableModifyListener(TableModifyListener listener){
        tableModifylisteners.add(listener);
    }
    
    public void removeTableModifyListener(TableModifyListener listener){
        tableModifylisteners.remove(listener);
    }
    
    
    protected String[] getSourceTableColumnNames(){
        
        String[] sourceColumnNames = new String[sourceTable.getColumnCount()];

        for(int i=0; i<sourceTable.getColumnCount(); i++){
            
            sourceColumnNames[i] = sourceTable.getColumnName(i);
        }
        
        return sourceColumnNames;
    }
    
    protected Class[] getSourceTableColumnTypes(){
        
        Class[] sourceColumnTypes = new Class[sourceTable.getColumnCount()];
        
        for(int i=0; i<sourceTable.getColumnCount(); i++){
            
            sourceColumnTypes[i] = sourceTable.getColumnClass(i);
        }
        
        return sourceColumnTypes;
    }
    
    protected Object[][] getSourceTableDataMatrix(){
        
        Object[][] sourceDataMatrix = new Object[sourceTable.getRowCount()][sourceTable.getColumnCount()];  
        
        for(int i=0; i<sourceTable.getColumnCount(); i++){
            for (int j = 0; j < sourceTable.getRowCount(); j++) {
                
                sourceDataMatrix[j][i] = sourceTable.getValueAt(j, i);
            }
        }
        
        return sourceDataMatrix;
    }
    
    protected void composeEditTable(){
        
        editTable.setModel( createEditTableModel( getSourceTableDataMatrix(), getSourceTableColumnNames(), getSourceTableColumnTypes() ) );
        
        editTable.createDefaultColumnsFromModel();
        
        copySourceTableRenderers();
        copySourceTableEditors();
        
        copyStringColumnRenders();
        
        getEditTableModel().fireTableStructureChanged();
    }    
    
    protected abstract DefaultTableModel createEditTableModel(Object[][] dataMatrix, String[] columnNames, Class[] columnTypes);
    
    protected void resizeCellEditableMatrix(int startRow, int endRow){
        
        final int realRowCount = (endRow - startRow) + 1;
        
        if(realRowCount!= cellEditable.size()){
            
            List<boolean[]> newCellEditable = new ArrayList<>(realRowCount);
            
            for(int i=0; i<cellEditable.size() && i<realRowCount; i++){
                
                newCellEditable.add(getCellEditableMatrix().get(i));
            }
            
            if (realRowCount > cellEditable.size()) {
                
                for (int i = cellEditable.size(); i < realRowCount; i++) {
                    
                    newCellEditable.add(columnEditable.clone());
                }
            }
            
            setCellEditableMatrix(newCellEditable);
        }
    }
    
    protected void resizeColumnEditableArray(){
        
       // System.out.println("resizeColumnEditableArray()");
        
        final int realColumnCount = editTable.getColumnCount() - getStringColumnsOffset();
        
       // System.out.println("RealColumnCount: " + realColumnCount + " - Column EditableArray Length: " + columnEditable.length);
        
        if(realColumnCount != columnEditable.length){
            
            boolean[] newColumnEditable = new boolean[realColumnCount];
            
            for(int i=0; i<columnEditable.length && i<realColumnCount; i++){
                
                newColumnEditable[i] = columnEditable[i];
            }   
            
            if(realColumnCount > columnEditable.length){
                
                for(int i=columnEditable.length; i<realColumnCount; i++){
                    
                    newColumnEditable[i] = true;
                }
            }
            
            setColumnEditableArray(newColumnEditable);
        }
        
    }
    
    protected void notifyListeners(TableModifiedEvent event){
        
        if(event == null){
            throw new NullPointerException("Evento inv??lido");
        }
        
        for(TableModifyListener listener : tableModifylisteners){
            listener.tableModified(event);
        }
    }
    
    protected void copySourceTableRenderers(){
        
        for(int i=getStringColumnsOffset(); i<sourceTable.getColumnCount(); i++){
            
            TableCellRenderer sourceCellRenderer = sourceTable.getColumnModel().getColumn(i).getCellRenderer();
            
            editTable.getColumnModel().getColumn(i).setCellRenderer(sourceCellRenderer);
        }
    }
    
    protected void copySourceTableEditors(){
        
        for(int i=0; i<sourceTable.getColumnCount(); i++){
            
            TableCellEditor sourceCellEditor = sourceTable.getColumnModel().getColumn(i).getCellEditor();
            
            editTable.getColumnModel().getColumn(i).setCellEditor(sourceCellEditor);
        }
    }
    
    protected final void repackEditor() {
        
        int minDialogWidth = 0;
        
        for (int i = 0; i < getStringColumnsOffset(); i++) {
            
            StringColumnData data = getStringColumnsData().get(i);
            
            editTable.getColumnModel().getColumn(i).setPreferredWidth(data.columnWidth);
            
            minDialogWidth += data.columnWidth;
        }
        
        for (int i = getStringColumnsOffset(); i < editTable.getColumnCount(); i++) {

            int sourceColumnWidth = getSourceTableColumnWidth(i - getStringColumnsOffset());
            TableColumn column = editTable.getColumnModel().getColumn(i); //
            
            column.setPreferredWidth(sourceColumnWidth);
            column.setResizable(false);
            
            minDialogWidth += sourceColumnWidth + 3;
        }

        int minEditTableHeight = rowsDisplayed * editTable.getRowHeight();
        
        editTable.setPreferredScrollableViewportSize((new Dimension(minDialogWidth, minEditTableHeight)));
        
        this.setSize(new Dimension(60 + minDialogWidth, 170 + minEditTableHeight));
        this.setLocationRelativeTo(null);
    
    }
    
    protected void copyStringColumnRenders(){
        
        for(int i=0; i<getStringColumnsOffset(); i++){
            
            StringColumnData data = getStringColumnsData().get(i);
            
            if(data.columnRenderer != null){
                editTable.getColumnModel().getColumn(i).setCellRenderer(data.columnRenderer);
            } 
            else {
                editTable.getColumnModel().getColumn(i).setCellRenderer(editTable.getDefaultRenderer(Object.class));
            }
        }
    }
    
    
    protected abstract void refillEditTable();
    
    protected abstract void updateSourceTable();
    
    protected void setSourceTableValue(Object value, int row, int column){
        
        if(row < 0 || row > sourceTable.getRowCount()-1){
            throw new IllegalArgumentException("sourceTable - Linha inv??lida: " + row);
        }
        else if(column < 0 || column > sourceTable.getColumnCount()-1){
            throw new IllegalArgumentException("sourceTable - Coluna Inv??lida: " + column);
        }
        
        sourceTable.setValueAt(value, row, column);
    }
    
    public Object getSourceTableValue(int row, int column){
        
        if(row < 0 || row > sourceTable.getRowCount()-1){
            throw new IllegalArgumentException("sourceTable - Linha inv??lida: " + row);
        }
        else if(column < 0 || column > sourceTable.getColumnCount()-1){
            throw new IllegalArgumentException("sourceTable - Coluna Inv??lida: " + column);
        }
        
        return sourceTable.getValueAt(row, column);
    }
    
    public void addSourceTableRow(Object[] dataArray, Object customRowData){
        
        getSourceTableModel().addRow(dataArray);
        getSourceTableModel().fireTableRowsInserted(sourceTable.getRowCount()-1, sourceTable.getRowCount()-1);
        
        customRowDataList.add(customRowData);
        
        ArrayList<Integer> rows = new ArrayList<>();
        rows.add(sourceTable.getRowCount()-1);
        
        notifyListeners(new TableModifiedEvent(this, sourceTable, rows, dataArray, 
                            customRowData, TableModifiedEvent.ROW_INSERTED));
    }
    
    public Object[] getSourceTableRowData(int row){
        
        Object[] data = new Object[sourceTable.getColumnCount()];
        
        for (int i = 0; i < data.length; i++) {
            data[i] = sourceTable.getValueAt(row, i);
        }
        
        return data;
    }
    
    public void removeSourceTableRow(int row){
        
        Object[] rowData = getSourceTableRowData(row);
        Object customRowData = getCustomRowData(row);
        
        getSourceTableModel().removeRow(row);
        getSourceTableModel().fireTableRowsDeleted(row, row);
        removeCustomRowData(row);
        
        ArrayList<Integer> rows = new ArrayList<>();
        rows.add(row);
        
        notifyListeners(new TableModifiedEvent(this, sourceTable, rows, rowData, customRowData, TableModifiedEvent.ROW_DELETED));
    }
    
    public void updateSourceTableRow(int row, Object[] dataArray){
        
        for (int i = stringColumnsOffset; i < dataArray.length; i++) {
            sourceTable.setValueAt(dataArray[i], row, i);
        }
        
        getSourceTableModel().fireTableRowsUpdated(row, row);
        
        Object[] rowData = getSourceTableRowData(row);
        Object customRowData = getCustomRowData(row);
        
        ArrayList<Integer> rows = new ArrayList<>();
        rows.add(row);
        
        notifyListeners(new TableModifiedEvent(this, sourceTable, rows, rowData, 
                            customRowData, TableModifiedEvent.ROW_UPDATED));
    }
    
    public JTable getSourceTable() {
        return sourceTable;
    }

    public DefaultTableModel getSourceTableModel(){
        return (DefaultTableModel) sourceTable.getModel();
    }
    
    public int getSourceTableColumnWidth(int column){
        
        if(column < 0 || column > sourceTable.getColumnCount()-1){
            throw new IllegalArgumentException("O valor da coluna passado ?? inv??lido.");
        }        

        return sourceTable.getColumnModel().getColumn(column).getWidth();
    }
    
    
    protected Object convertToSourceTableValue(Object value, int sourceColumn){

        if (value != null && !value.toString().isEmpty()) {
            
            Object sourceValue = Cast.toPrimitiveType(value.toString(), sourceTable.getColumnClass(sourceColumn));

            if (sourceTable.getColumnClass(sourceColumn) != Object.class){
                
                if(sourceValue.getClass() == sourceTable.getColumnClass(sourceColumn)){
                    return sourceValue;
                } else {
                    throw new IllegalArgumentException("O valor n??o ?? do mesmo tipo da coluna correspondente na tabela.");
                }
                
            } else {

                return sourceValue;
            } 
            
        } else {
            return null; 
        }
    }
    
    protected Object[] convertToSourceTableValues(Object[] rowData){
        
        if(rowData == null){
            throw new NullPointerException("O array de dados da linha da tabela passado ?? inv??lido.");
        } 
        else if(rowData.length != sourceTable.getColumnCount()){
            throw new IllegalArgumentException("O n??mero de valores do array passado ?? diferente no n??mero de colunas da tabela.");
        }
        
        Object[] sourceRowData = new Object[sourceTable.getColumnCount()];
        
        for (int i = 0; i < rowData.length; i++) {
            
            sourceRowData[i] = convertToSourceTableValue(rowData[i], i);
        }
        
        return sourceRowData;
    }
    
    
    protected Object convertToEditTableValue(Object value){
        
        if(value != null && !value.toString().isEmpty()){
            
            return Cast.toBRLocaleValue(value);
            
        } else {
            return null;
        }
        
    }
    
    protected Object[] convertToEditTableValues(Object[] values){
        
        if(values == null){
            throw new NullPointerException("O array de dados da linha da tabela passado ?? inv??lido.");
        } 
        else if(values.length != sourceTable.getColumnCount()){
            throw new IllegalArgumentException("O n??mero de valores do array passado ?? diferente no n??mero de colunas da tabela.");
        }
        
        for (int i = 0; i < values.length; i++) {
            values[i] = convertToEditTableValue(values[i]);
        }
        
        return values;
    }
    
    protected void setEditTableValue(Object value, int row, int column){
        
        if(row < 0 || row > editTable.getRowCount()-1){
            throw new IllegalArgumentException("editTable - Linha inv??lida: " + row);
        }
        else if(column < 0 || column > editTable.getColumnCount()-1){
            throw new IllegalArgumentException("editTable - Coluna Inv??lida: " + column);
        }
        
        editTable.setValueAt(value, row, column);
    }
    
    public Object getEditTableValue(int row, int column){
        
        if(row < 0 || row > editTable.getRowCount()-1){
            throw new IllegalArgumentException("editTable - Linha inv??lida: " + row);
        }
        else if(column < 0 || column > editTable.getColumnCount() - 1){
            throw new IllegalArgumentException("editTable - Coluna Inv??lida: " + column);
        }
        
        return editTable.getValueAt(row, column);
    }
    
    protected void removeEditTableRow(int row){
        
        getEditTableModel().removeRow(row);
        getEditTableModel().fireTableRowsDeleted(row, row);
    }
    
    protected void updateEditTableRow(int row, Object[] dataArray){
        
        for (int i = 0; i < dataArray.length; i++) {
            setEditTableValue(dataArray[i], row, i);
        }
        
        getEditTableModel().fireTableRowsUpdated(row, row);
    }
    
    protected void addEditTableRow(Object[] dataArray){
        
        if(dataArray == null){
            throw new NullPointerException("O vetor de valores passado ?? inv??lido.");
        }
        
        getEditTableModel().addRow(dataArray);
        getEditTableModel().fireTableRowsInserted(editTable.getRowCount()-1, editTable.getRowCount()-1);
    }
    
    protected Object[] getEditTableRowStringData(int row){
        
        Object[] data = new Object[editTable.getColumnCount() - getStringColumnsOffset()];
        
        for (int i = getStringColumnsOffset(); i < data.length; i++) {
            data[i] = editTable.getValueAt(row, i);
        }
        
        return (data);
    }
    
    protected Object[] getEditTableRowData(int row){
        return convertToSourceTableValues( getEditTableRowStringData(row) );
    }
    
    public javax.swing.JTable getEditTable() {
        return editTable;
    }

    public DefaultTableModel getEditTableModel(){
        return (DefaultTableModel) editTable.getModel();
    }
    
    
    public void processEditor(){
        
        composeEditTable();
        refillEditTable();
        
        setRebuildEditTable(false);
    }
    
    protected void processEditTable(){
        
        clearEditTable();
        
        if(rebuildEditTable){
            
            composeEditTable();
            
            rebuildEditTable = false;
        }
        
        refillEditTable();
        
        repackEditor();
    }
    
    public void showEditor(ActionEvent e){
        
        processEditTable();

        this.setVisible(true);
    }   
    
    protected void clearEditTable(){
        
        getEditTableModel().setRowCount(0);
    }
    
    protected void hideEditor(){
        this.setVisible(false);
    }
    
    protected void stopCellEditing(){
        
        if(editTable.isEditing()){
            editTable.getCellEditor().stopCellEditing();
        }
    }
    
    protected void cancelCellEditing(){
        
        if(editTable.isEditing()){
            editTable.getCellEditor().cancelCellEditing();
        }
    }
    
    
    protected boolean validateValue(Object value, int sourceColumn){
        
        boolean isValueValid = true;

        if (value == null) {
            
            if(isAllowedEmptyCells()){
                return true;
            }
            
            return false;
        } 

        String stringCellValue = Cast.toString(value);

        if (columnRegexMap.containsKey(sourceColumn)) {

            String regex = columnRegexMap.get(sourceColumn);

            if (!stringCellValue.matches(regex)) {
                isValueValid = false;
            }
            
        } else {
            
            String regex = Regex.getRegexFromType(sourceTable.getColumnClass(sourceColumn));

            if (regex != null && !stringCellValue.matches(regex)) {
                isValueValid = false;
            }
        }

        return isValueValid;
    }
    
    protected boolean validateEditTableValue(int editRow, int editColumn, int sourceColumn){
        
        Object cellValue = getEditTableValue(editRow, editColumn);
        
        if(!validateValue(cellValue, sourceColumn)){
            return false;
        }
        return true;
    }
    
    protected boolean validateEditTableRow(int editRowIndex){
        
        if(checkEmptyRow(editRowIndex)){
            return isAllowedEmptyRows();
        }
        
        Object[] rowData = getEditTableRowStringData(editRowIndex);
        
        for (int i = 0; i < rowData.length; i++) {
            
            if(!validateEditTableValue(editRowIndex, i, i)){
                
                return false;
            }
        }
        
        return true;
    }
    
    protected boolean validateEditTableContent(){
        
        for (int i = 0; i < editTable.getRowCount(); i++) {
            
            if(checkEmptyRow(i)){
                
                if(!isAllowedEmptyRows()){
                    
                    JOptionPane.showMessageDialog(this, "N??o s??o permitidas linhas vazias.", "Linha Vazia", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                
                continue;
            }
            
            for (int j = stringColumnsOffset; j < editTable.getColumnCount(); j++) {
                
                if(checkEmptyValue(i, j)){ 
                    
                    if(!isAllowedEmptyCells()){
                    
                        JOptionPane.showMessageDialog(this, "N??o s??o permitidas c??lulas vazias.", "Valor de Coluna Vazio", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    
                }
                else if(isCellEditable(i, j) && !validateEditTableValue(i, j, j - getStringColumnsOffset())){
                    
                    JOptionPane.showMessageDialog(this, "O valor da coluna \"" + editTable.getColumnName(j) + "\" na linha " + (i+1) +
                            " ?? inv??lido.", "Valor de Coluna Inv??lido", JOptionPane.ERROR_MESSAGE);
                    
                    return false;
                }
            }
        }
        
        return true;
    }
    
    protected boolean checkEmptyValue(int editRow, int editColumn){
        
        Object cellValue = getEditTableValue(editRow, editColumn);
        
        return Cast.toString(cellValue).isEmpty();
    }
    
    protected boolean checkEmptyRow(int editRow){
        
        int numEmptyCells = 0;
        
        for(int i=stringColumnsOffset; i<editTable.getColumnCount(); i++){

            if(checkEmptyValue(editRow, i)){

                numEmptyCells++;
            }
        }
        
        if(numEmptyCells == editTable.getColumnCount() - stringColumnsOffset){
            return true;
        }
        
        return false;
    }
    
    
    public void addStringColumn(int columnWidth, String columnTitle, List<String> columnData, TableCellRenderer columnRenderer){
        
        if(columnData == null){
            throw new NullPointerException("O array de dados da coluna passado ?? inv??lido.");
        }
        else if(columnWidth <= 0){
            throw new IllegalArgumentException("O tamanho da coluna passado ?? inv??lido (tamanho <= 0).");
        }
        
        if(columnData == null){
            columnData = new ArrayList<>();
        }
        
        stringColumnsData.add( new StringColumnData(columnWidth, columnTitle, columnData, columnRenderer) );
        
        stringColumnsOffset++;
        
        clearEditTable();
        composeEditTable();
    }
    
    protected void fillStringColumns(){
        
        for(int i=0; i<stringColumnsOffset; i++){
            
            StringColumnData data = stringColumnsData.get(i);
            
            editTable.getColumnModel().getColumn(i).setHeaderValue( data.columnTitle );
            
            for(int j=0; j<editTable.getRowCount() && j<data.columnData.size(); j++){
                
                setEditTableValue(data.columnData.get(j), j, i);
            }
        }        
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        editTableScroll = new javax.swing.JScrollPane();
        editTable = new javax.swing.JTable();
        editLabel = new javax.swing.JLabel();
        saveButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setBounds(new java.awt.Rectangle(0, 0, 0, 0));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        editTable.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        editTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        editTable.setRowHeight(25);
        editTable.getTableHeader().setReorderingAllowed(false);
        editTableScroll.setViewportView(editTable);

        editLabel.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        editLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        editLabel.setText("Sample Text");
        editLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        editLabel.addHierarchyBoundsListener(new java.awt.event.HierarchyBoundsListener() {
            public void ancestorMoved(java.awt.event.HierarchyEvent evt) {
            }
            public void ancestorResized(java.awt.event.HierarchyEvent evt) {
                editLabelAncestorResized(evt);
            }
        });

        saveButton.setText("Salvar");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancelar");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(editTableScroll, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE))
                .addGap(20, 20, 20))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(editLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(editLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(editTableScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void editLabelAncestorResized(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_editLabelAncestorResized
        // TODO add your handling code here:
    }//GEN-LAST:event_editLabelAncestorResized

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        
        cancelCellEditing();
        hideEditor();
        clearEditTable();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        //cancelButtonActionPerformed(null);
    }//GEN-LAST:event_formWindowClosed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed

        stopCellEditing();

        if (validateEditTableContent()) {
            
            updateSourceTable();
            clearEditTable();
            hideEditor();
        }
    }//GEN-LAST:event_saveButtonActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        cancelCellEditing();
        hideEditor();
        clearEditTable();
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel editLabel;
    protected javax.swing.JTable editTable;
    private javax.swing.JScrollPane editTableScroll;
    private javax.swing.JButton saveButton;
    // End of variables declaration//GEN-END:variables

    
    public Object setCustomRowData(int row, Object data){
        
        if(row < 0 || row > customRowDataList.size()-1){
            throw new IllegalArgumentException("GenericTableModifier.setCustomRowData(int,Object): O numero da linha da tabela passado ?? inv??lido.");
        }
        
        return customRowDataList.set(row, data);
    }
    
    public Object getCustomRowData(int row){
        
        if(row < 0 || row > customRowDataList.size()-1){
            throw new IllegalArgumentException("GenericTableModifier.setCustomRowData(int,Object): O numero da linha da tabela passado ?? inv??lido.");
        }
        
        return customRowDataList.get(row);
    }
    
    public Object removeCustomRowData(int row){
        
        if(row < 0 || row > customRowDataList.size()-1){
            throw new IllegalArgumentException("GenericTableModifier.setCustomRowData(int,Object): O numero da linha da tabela passado ?? inv??lido.");
        }
        
        return customRowDataList.remove(row);
    }
    
    public List<Object> getCustomRowDataList(){
        return customRowDataList;
    }
    
    
    protected javax.swing.JButton getCancelButton() {
        return cancelButton;
    }

    protected javax.swing.JButton getSaveButton() {
        return saveButton;
    }
    
    public Object[] getSourceTableSelectedRowData(){
        return getSourceTableRowData(sourceTable.getSelectedRow());
    }
    
    public String getLabelText() {
        return editLabel.getText();
    }

    public void setLabelText(String text) {
        this.editLabel.setText(text);
    }

    public boolean isForceCellEditingEnabled() {
        return forceCellEditing;
    }

    public void setForceCellEditing(boolean forceCellEditing) {
        this.forceCellEditing = forceCellEditing; 
    }
    
    public void setColumnEditable(int columnIndex, boolean isEditable){
        
        if(columnIndex < 0 || columnIndex > editTable.getColumnCount() - getStringColumnsOffset() - 1){
            throw new IllegalArgumentException("O ??ndice da coluna passado ?? inv??lido. Coluna: " + columnIndex);
        }
        
        columnEditable[columnIndex] = isEditable;
        
        if(editTable.getRowCount() > 0){
            
            for(int i=0; i<cellEditable.size(); i++){
                
                cellEditable.get(i)[columnIndex] = isEditable;
            }
        }
    }
    
    public boolean isColumnEditable(int columnIndex){
        
        if(columnIndex < 0 || columnIndex > editTable.getColumnCount() - getStringColumnsOffset() - 1){
            throw new IllegalArgumentException("O ??ndice da coluna passado ?? inv??lido. Coluna: " + columnIndex);
        }
        
        return columnEditable[columnIndex];
    }
    
    public void setAllColumnsEditable(boolean isEditable){
        
        for(int i=0; i<this.columnEditable.length; i++){
            this.columnEditable[i] = isEditable;
        }
    }
    
    public void setCellEditable(int rowIndex, int columnIndex, boolean isEditable){
        
        if(rowIndex < 0 || rowIndex > editTable.getRowCount() - 1){
            throw new IllegalArgumentException("O ??ndice da linha passado ?? inv??lido. Linha: " + rowIndex);
        }
        else if(columnIndex < 0 || columnIndex > editTable.getColumnCount() - getStringColumnsOffset() - 1){
            throw new IllegalArgumentException("O ??ndice da coluna passado ?? inv??lido. Coluna: " + columnIndex);
        }
        
        cellEditable.get(rowIndex)[columnIndex] = isEditable;
    }
    
    public boolean isCellEditable(int rowIndex, int columnIndex){
        
       if(rowIndex < 0 || rowIndex > editTable.getRowCount() - 1){
            throw new IllegalArgumentException("O ??ndice da linha passado ?? inv??lido. Linha: " + rowIndex);
        }
        else if(columnIndex < 0 || columnIndex > editTable.getColumnCount() - 1){
            throw new IllegalArgumentException("O ??ndice da coluna passado ?? inv??lido. Coluna: " + columnIndex);
        }
       
        if(columnIndex < getStringColumnsOffset()){
            return false;
        } 
        else{
            
            return cellEditable.get(rowIndex)[columnIndex - getStringColumnsOffset()];
        }
    }
    
    public List<boolean[]> getCellEditableMatrix(){
        return cellEditable;
    }
    
    protected void setCellEditableMatrix(List<boolean[]> cellEditable){
        this.cellEditable = cellEditable;
    }
    
    public boolean isAllowedEmptyRows() {
        return allowEmptyRows;
    }

    public void setAllowEmptyRows(boolean allowEmptyRows) {
        this.allowEmptyRows = allowEmptyRows;
    }

    public boolean isAllowedEmptyCells() {
        return allowEmptyCells;
    }

    public void setAllowEmptyCells(boolean allowEmptyCells) {
        this.allowEmptyCells = allowEmptyCells;
    }
    
    public int getRowsDisplayed() {
        return rowsDisplayed;
    }

    public void setRowsDisplayed(int rowsDisplayed) {
        
        if(rowsDisplayed <= 0){
            throw new IllegalArgumentException("O n??mero de linhas a serem exibidas passado ?? inv??lido.");
        }
        
        this.rowsDisplayed = rowsDisplayed;
    }

    protected boolean isRebuildEditTableNeeded() {
        return rebuildEditTable;
    }

    protected void setRebuildEditTable(boolean rebuildEditTable) {
        this.rebuildEditTable = rebuildEditTable;
    }
    
    public JScrollPane getEditTableScroll() {
        return editTableScroll;
    }

    public int getStringColumnsOffset() {
        return stringColumnsOffset;
    }

    protected List<StringColumnData> getStringColumnsData() {
        return stringColumnsData;
    }

    public void setDefaultRenderer(TableCellRenderer render){
        
        if(render == null){
            throw new NullPointerException("O renderer passado ?? inv??lido.");
        }
        
        editTable.setDefaultRenderer(Object.class, render);
    }
    
    public void setDefaultEditor(TableCellEditor editor){
        
        if(editor == null){
            throw new NullPointerException("O editor passado ?? inv??lido.");
        }
        
        editTable.setDefaultEditor(Object.class, editor);
    }

    public boolean[] getColumnEditableArray(){
        return columnEditable;
    }
    
    protected void setColumnEditableArray(boolean[] columnEditable){
        this.columnEditable = columnEditable;
    }
    
    public void setRowEditable(int rowIndex, boolean isEditable){
        
        if(rowIndex < 0 || rowIndex > cellEditable.size()-1){
            throw new IllegalArgumentException("O ??ndice da linha passado ?? inv??lido. Linha: " + rowIndex);
        }
        
       // System.out.println("Column Count from CellEditableArray: " + cellEditable.get(rowIndex).length);
        
        for(int i=0; i< cellEditable.get(rowIndex).length; i++){
            
            cellEditable.get(rowIndex)[i] = isEditable;
        }
    } 
    
    public void setAllRowsEditable(boolean isEditable){
        
        for (int i = 0; i < cellEditable.size(); i++) {
            
            for(int j=0; j<cellEditable.get(0).length; j++){
                
                cellEditable.get(i)[j] = isEditable;
            }
        }
    }

    public int getRealColumnCount(){
        return getColumnEditableArray().length;
    }
}
