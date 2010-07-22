/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eye.admin.table;

import com.db4o.ObjectSet;
import com.db4o.query.Query;
import eye.admin.DbManager;
import eye.admin.MainFrame;
import eye.core.model.Image;
import eye.core.model.Place;
import eye.core.model.RemoteSource;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.SwingWorker;

/**
 *
 * @author stream
 * @version $Id: RemoteSourceLoader.java 123 2010-07-21 06:30:59Z stream $
 */
public class RemoteSourceLoader extends SwingWorker<Vector<Vector>, Vector> {

    private static DbManager dbm = DbManager.getManager();
    private EyeTableModel model;
    private final JLabel total;
    private static Vector titles;

    public RemoteSourceLoader(EyeTableModel model, JLabel total) {
        this.model = model;
        this.total = total;
    }

    @Override
    protected Vector doInBackground() throws Exception {
        Vector remoteSourceRow = retrieveRemoteSourceRow();
        publish(remoteSourceRow);
        return remoteSourceRow;
    }

    private Vector getColumnTitles() {
        titles = new Vector();
        titles.add("Idx");
        titles.add("URL");
        titles.add(" ");
        return titles;
    }

    protected Vector<Vector> retrieveRemoteSourceRow() {
        Query q = dbm.getContainer().query();
        q.constrain(getActiveRemoteSource().getClass());
        ObjectSet<RemoteSource> objectSet = q.execute();
        int fulledImage = 0;
        int checkedPlace = 0;
        Vector dataVector = new Vector();
        if (objectSet.size() == 0) total.setText("0");
        for (int x = 0; x < objectSet.size(); x++) {
            Vector row = new Vector();
            RemoteSource rs = objectSet.next();
            if (MainFrame.getRemoteSourceClass().equals("Images")) {
                if (((Image) rs).hasMetaData()) {
                    fulledImage++;
                    total.setText("<html><strong><font color='green'>&nbsp;" + fulledImage + "</font>" + "/" + objectSet.size() + "</strong></html>");
                    row.add(x + 1 + " ");
                } else {
                    row.add(x + 1);
                }
            } else if (MainFrame.getRemoteSourceClass().equals("Places")) {
                if (((Place)rs).getDate() != null){
                    checkedPlace++;
                    total.setText("<html><strong><font color='green'>&nbsp;" + checkedPlace + "</font>" + "/" + objectSet.size() + "</strong></html>");
                    row.add(x + 1 + " ");
                } else {
                    row.add(x + 1);
                }
            }
            row.add(rs.getUrl());
            row.add(" ");
            //dataVector.add(row);
            model.addRow(row);
            model.fireTableDataChanged();
            setProgress(100 * (x + 1) / objectSet.size());
        }
        return dataVector;
    }

    @Override
    protected void done() {
        setProgress(100);
        Vector<Vector> remoteSourceData = null;
        try {
            remoteSourceData = get();
            for (Vector row : remoteSourceData) {
                model.addRow(row);
            }
        } catch (Exception ignore) {
            System.out.println("RemoteSourceLoader: done :" + ignore);
        }
    }

    /*@Override
    protected void process(List<Vector> rows) {
    for (Vector row : rows) {
    if (isCancelled()) {
    break;
    }
    model.addRow(row);
    }
    }*/
    public RemoteSource getActiveRemoteSource() {
        String remoteSourceClassName = MainFrame.getRemoteSourceClass();
        if (remoteSourceClassName != null) {
            if (remoteSourceClassName.equals("Images")) {
                return new Image();
            }
            return new Place();
        }
        return null;
    }
}
