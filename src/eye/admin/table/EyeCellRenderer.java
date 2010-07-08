package eye.admin.table;

import eye.admin.MainFrame;
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author stream
 */
public class EyeCellRenderer extends JButton implements TableCellRenderer {

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowIndex, int vColIndex) {
        if (MainFrame.getRemoteSourceClass() == null){
            setEnabled(false);
        } else {
            setEnabled(true);
        }
        setFocusPainted(hasFocus);
        setText("X");
        return this;
    }
}
