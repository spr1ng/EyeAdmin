/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eye.admin;

import eye.models.Image;
import eye.models.Place;
import eye.models.RemoteSource;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTable;

/**
 *
 * @author stream
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
            model.commit();
            model.removeRow(rowIndex);
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            //System.out.println("ArrayIndexOutOfBoundsException in eyeCellRemoverListener");
        }
        model.fireTableDataChanged();
    }
}
