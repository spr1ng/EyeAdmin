/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eye.admin;

import com.db4o.ObjectContainer;
import com.db4o.cs.Db4oClientServer;
import db4oserver.AbstractDBManager;
import db4oserver.ConfigLoader;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author stream
 */
public class DbManager extends AbstractDBManager {
    private static DbManager dbm = new DbManager();
    private static ObjectContainer clientDB;
    private DbManager(){
        try {
        clientDB = Db4oClientServer.openClient(Db4oClientServer.newClientConfiguration(),
                ConfigLoader.getInstance().getHost(), ConfigLoader.getInstance().getPort(), ConfigLoader.getInstance().getUser(), ConfigLoader.getInstance().getPass());
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    public static DbManager getManager(){
        return dbm;
    }

    @Override
    public ObjectContainer getContainer() {
        try {
        clientDB = Db4oClientServer.openClient(Db4oClientServer.newClientConfiguration(),
                ConfigLoader.getInstance().getHost(), ConfigLoader.getInstance().getPort(), ConfigLoader.getInstance().getUser(), ConfigLoader.getInstance().getPass());
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        return clientDB;
    }

    @Override
    public int store(List objects) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
