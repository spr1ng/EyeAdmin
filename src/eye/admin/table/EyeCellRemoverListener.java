/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eye.admin.table;

import eye.admin.MainFrame;
import eye.core.model.Image;
import eye.core.model.Place;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTable;

/**
 *
 * @author stream
 * @version $Id: EyeCellRemoverListener.java 84 2010-07-09 02:15:22Z stream $
 */
public class EyeCellRemoverListener implements ActionListener {

    private JTable table;

    public EyeCellRemoverListener(JTable table) {
        this.table = table;
    }

    public void actionPerformed(ActionEvent e) {
        int rowIndex = table.getSelectedRow();
        String remoteSourceClassName = MainFrame.getRemoteSourceClass();
        EyeTableModel model = ((EyeTableModel) table.getModel());
        try {
            String remoteSourceUrl = model.getValueAt(rowIndex, 1).toString();
            System.out.println(remoteSourceUrl);
            if (remoteSourceClassName.equals("Images")) {
                model.delete(new Image(remoteSourceUrl, null));
            } else {
                model.delete(new Place(remoteSourceUrl, null));
            }
            model.removeRow(rowIndex);
        } catch (ArrayIndexOutOfBoundsException aioobe) {
        }
        model.fireTableDataChanged();
    }
}
