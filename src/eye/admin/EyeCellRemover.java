/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eye.admin;

import java.awt.Component;
import java.util.Date;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author stream
 */
public class EyeCellRemover extends AbstractCellEditor implements TableCellEditor {

    private JButton editor;

    public EyeCellRemover() {
        editor = new JButton();
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (MainFrame.getRemoteSourceClass() == null){
            editor.setEnabled(false);
        }
        editor.setText("X");
        editor.addActionListener(new EyeCellRemoverListener(table));
        return editor;
    }

    public Object getCellEditorValue() {
        String editorValue = editor.getText();
        return editorValue;
    }

}
