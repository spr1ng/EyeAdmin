/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eye.admin.table;

import com.db4o.ObjectContainer;
import eye.server.manager.impl.DBManagerBasicImpl;
import eye.core.model.RemoteSource;
import java.util.List;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author stream
 * @version $Id: EyeTableModel.java 71 2010-07-08 03:50:40Z spr1ng $
 */
public class EyeTableModel extends DefaultTableModel {

    private static eye.server.manager.impl.DBManagerBasicImpl dbm = new DBManagerBasicImpl();
    private ObjectContainer db = dbm.getContainer();

    public EyeTableModel() {
        super(new Object[]{"Idx", "URL", " "}, 0);
    }

    public void clear() {
        getDataVector().removeAllElements();
        fireTableDataChanged();
    }

    public void addEmptyRow() {
        Vector row = new Vector();
        row.add(" ");
        row.add("http://");
        row.add(" ");
        addRow(row);
    }

    public void delete(RemoteSource rs) {
        db.delete(select(rs));
        db.commit();
    }

    private RemoteSource select(RemoteSource rs) {
        List<RemoteSource> remoteSourceList = db.queryByExample(rs);
        return remoteSourceList.get(0);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return true;
    }

    public void cache() {
        System.out.println("STUB");
    }

    public void commit() {
        dbm.getContainer().commit();
    }
}
