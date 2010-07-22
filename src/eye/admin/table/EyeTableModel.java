/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eye.admin.table;

import java.util.List;
import javax.swing.table.DefaultTableModel;
import com.db4o.ObjectContainer;
import com.db4o.ext.Db4oRecoverableException;
import eye.admin.MainFrame;
import eye.core.model.Image;
import eye.core.model.Place;
import eye.core.model.RemoteSource;
import eye.server.manager.DBManagerBasicImpl;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Vector;

/**
 *
 * @author stream
 * @version $Id: EyeTableModel.java 109 2010-07-19 00:22:16Z stream $
 */
public class EyeTableModel extends DefaultTableModel {

    private static DBManagerBasicImpl dbm = new DBManagerBasicImpl();
    private ObjectContainer db = dbm.getContainer();
    private Hashtable<String, String> cache = new Hashtable<String, String>();

    public EyeTableModel() {
        super(new Object[]{"Idx", "URL", " "}, 0);
    }

    public void cleanDatabase() {
        Vector<Vector> dv = getDataVector();
        if (MainFrame.getRemoteSourceClass().equals("Images")) {
            for (Vector<String> row : dv) {
                RemoteSource img = new Image(row.get(1));
                delete(img);
            }
        } else {
            for (Vector<String> row : dv) {
                RemoteSource place = new Place(row.get(1));
                delete(place);
            }
        }
        clearTable();
    }

    public void addEmptyRow() {
        Object rowData[] = new Object[]{"  ", "http://", " "};
        addRow(rowData);
        fireTableDataChanged();
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        super.setValueAt(aValue, row, column);
        cache(aValue.toString());
    }

    public void delete(RemoteSource rs) {
        RemoteSource srs = select(rs);
        if (db != null && srs != null) {
            db.delete(select(rs));
            db.commit();
        }
    }

    private RemoteSource select(RemoteSource rs) {
        try {
            List<RemoteSource> remoteSourceList = db.queryByExample(rs);
            return remoteSourceList.get(0);
        } catch (Db4oRecoverableException recex) {
            return null;
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return true;
    }

    public void cache(String remoteSourceURL) {
        cache.put(remoteSourceURL, MainFrame.getRemoteSourceClass());
    }

    public void commitNewData() {
        RemoteSource rs = null;
        if (db != null) {
            if (MainFrame.getRemoteSourceClass().equals("Images")) {
                for (Entry e : cache.entrySet()) {
                    rs = new Image(e.getKey().toString());
                    db.store(rs);
                }
            } else {
                for (Entry e : cache.entrySet()) {
                    rs = new Place(e.getKey().toString());
                    db.store(rs);
                }
            }
            commit();
        }
        cache.clear();
    }

    public void commit() {
        db.commit();
    }

    public void clearTable() {
        getDataVector().removeAllElements();
        fireTableDataChanged();
    }
}
