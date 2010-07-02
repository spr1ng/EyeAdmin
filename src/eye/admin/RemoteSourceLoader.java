/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eye.admin;

import com.db4o.ObjectSet;
import com.db4o.query.Query;
import eye.models.Image;
import eye.models.Place;
import eye.models.RemoteSource;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.SwingWorker;

/**
 *
 * @author stream
 */
public class RemoteSourceLoader extends SwingWorker<Vector<Vector>, Vector> {

    private static DbManager dbm = DbManager.getManager();
    private EyeTableModel model;
    private ObjectSet<RemoteSource> objectSet;
    private final JLabel total;

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


    protected Vector<Vector> retrieveRemoteSourceRow() {
        Query q = dbm.getContainer().query();
        q.constrain(getActiveRemoteSource().getClass());
        objectSet = q.execute();
        total.setText("Total " + MainFrame.getRemoteSourceClass() + ": " + objectSet.size());
        Vector dataVector = new Vector();
        for (int x = 0; x < objectSet.size(); x++) {
            Vector row = new Vector();
            row.add(x+1);
            row.add(objectSet.next().getUrl());
            row.add(" ");
            dataVector.add(row);
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
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
        for (Vector row : remoteSourceData){
            model.addRow(row);
        }
    }

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