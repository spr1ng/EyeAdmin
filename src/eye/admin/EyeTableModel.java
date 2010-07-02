/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eye.admin;

import eye.models.RemoteSource;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author stream
 */
class EyeTableModel extends DefaultTableModel {

    private static DbManager dbm = DbManager.getManager();

    public EyeTableModel() {
        super(new Object[]{"Idx", "URL", " "}, 0);
    }

    public void clear() {
        getDataVector().removeAllElements();
        fireTableDataChanged();
    }

    public void delete(RemoteSource rs) {
//        Iterator<RemoteSource> it = select(rs);
//        while(it.hasNext()){
//            dbm.getDbClient().delete(it.next());
//        }
        dbm.getContainer().delete(rs);
        commit();
    }

//    private Iterator<RemoteSource> select(RemoteSource rs){
//        ObjectSet<RemoteSource> remoteSourceSet = dbm.getDbClient().queryByExample(rs);
//        return remoteSourceSet.iterator();
//    }
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
