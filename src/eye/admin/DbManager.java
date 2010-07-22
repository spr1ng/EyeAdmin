/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eye.admin;

import com.db4o.ObjectContainer;
import com.db4o.cs.Db4oClientServer;
import eye.server.manager.AbstractDBManager;
import eye.server.manager.ConfigLoader;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author stream
 * @version $Id: DbManager.java 83 2010-07-09 00:51:11Z stream $
 */
public class DbManager extends AbstractDBManager {

    private static DbManager dbm = new DbManager();
    private static ObjectContainer clientDB;
    private static ConfigLoader conf = ConfigLoader.getInstance();

    private DbManager() {
        try {
            clientDB = Db4oClientServer.openClient(Db4oClientServer.newClientConfiguration(),
                    ConfigLoader.getInstance().getHost(), ConfigLoader.getInstance().getPort(), ConfigLoader.getInstance().getUser(), ConfigLoader.getInstance().getPass());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public static DbManager getManager() {
        return dbm;
    }

    @Override
    public ObjectContainer getContainer() {
        return Db4oClientServer.openClient(Db4oClientServer.newClientConfiguration(), conf.getHost(), conf.getPort(), conf.getUser(), conf.getPass());
    }

    @Override
    public int store(List objects) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
