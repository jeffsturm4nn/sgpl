/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.awt.Component;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Fabricio
 */
public class DecimalFormatRenderer extends DefaultTableCellRenderer {
    
    private static final NumberFormat FORMATTER = NumberFormat.getInstance(new Locale("pt", "BR"));
    
    public DecimalFormatRenderer(boolean alignment) {
        if(alignment) {  
            super.setHorizontalAlignment(JLabel.RIGHT);
        }
        FORMATTER.setMaximumFractionDigits(2);
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
     
        if (value != null && !value.toString().isEmpty() && 
           ( (value instanceof Double) || (value instanceof Float) ||
             (value instanceof Short) || (value instanceof Long) ||
             (value instanceof Integer) ) ) {
            
            value = FORMATTER.format(value);
        }    
        
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
    
}
