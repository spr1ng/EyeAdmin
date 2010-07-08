/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eye.admin;

import eye.admin.table.RemoteSourceLoader;
import eye.admin.table.EyeTableModel;
import eye.admin.table.EyeCellRenderer;
import eye.admin.table.EyeCellRemover;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;

/**
 *
 * @author stream
 */
public class MainFrame extends JFrame {

    private static EyeTableModel model = new EyeTableModel();
    private static JComboBox objectSelectorBox;
    private ActButtonListener actListener = new ActButtonListener();
    private static PreferenceDialog prefDialog;
    private static AboutBox aboutBox;
    private JProgressBar progressBar = new JProgressBar();
    private ProgressListener progressListener = new ProgressListener(progressBar);
    private JLabel total;
    private JButton refreshButton;
    private static String remoteSourceActClass;

    public MainFrame() {
        super("EyeAdmin");
        prefDialog = new PreferenceDialog(this, true);
        aboutBox = new AboutBox(this, true);
        init();
        setSize(741, 459);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void init() {
        setJMenuBar(createMenuBar());
        add(toolBar(), BorderLayout.NORTH);
        add(table(), BorderLayout.CENTER);
        add(progressBar, BorderLayout.SOUTH);

    }

    public static String getRemoteSourceClass() {
        return remoteSourceActClass;
    }

    private JMenuBar createMenuBar() {
        JMenuBar bar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem fileItem = new JMenuItem("Exit");
        fileItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(fileItem);
        JMenu settingsMenu = new JMenu("Edit");
        JMenuItem prefsMenuItem = new JMenuItem("Preference");
        prefsMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                prefDialog.setVisible(true);
            }
        });
        settingsMenu.add(prefsMenuItem);
        JMenu helpMenu = new JMenu("About");
        JMenuItem aboutItem = new JMenuItem("About EyeAdmin");
        aboutItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                aboutBox.setVisible(true);
            }
        });
        helpMenu.add(aboutItem);
        bar.add(fileMenu);
        bar.add(settingsMenu);
        bar.add(helpMenu);
        return bar;
    }

    private JToolBar toolBar() {
        JToolBar toolBar = new JToolBar();
        JButton addButton = new JButton("New");
        addButton.setActionCommand("add");
//        JButton commitButton = new JButton("Commit");
//        commitButton.setActionCommand("commit");
        addButton.addActionListener(actListener);
        //commitButton.addActionListener(actListener);
        total = new JLabel();
        Dimension dim =  new Dimension(120,30);
        total.setMaximumSize(dim);
        total.setMinimumSize(dim);
        total.setPreferredSize(dim);
        toolBar.add(addButton);
        objectSelectorBox = new JComboBox(new Object[]{"Images", "Places"});
        objectSelectorBox.addActionListener(new ObjectSelectorListener());
        objectSelectorBox.setPreferredSize(new Dimension(300, 30));
        toolBar.add(new JSeparator());
        toolBar.add(objectSelectorBox);
        toolBar.add(total);
        toolBar.add(horizontalSpinner());
        return toolBar;
    }

    private JPanel horizontalSpinner() {
        JPanel p = new JPanel();
        refreshButton = new JButton("↺");
        refreshButton.setActionCommand("refresh");
        refreshButton.addActionListener(actListener);
        JButton pre = new JButton("<<");
        JButton next = new JButton(">>");
        JTextField field = new JTextField(8);
        field.setHorizontalAlignment(JTextField.CENTER);
        p.add(refreshButton);
        p.add(pre);
        p.add(field);
        p.add(next);
        return p;
    }

    private JScrollPane table() {
        JXTable table = new JXTable(model);
        TableColumn col = table.getColumnModel().getColumn(0);
        col.setMaxWidth(60);
        col.setResizable(false);
        TableColumn remover = table.getColumnModel().getColumn(2);
        remover.setCellRenderer(new EyeCellRenderer());
        remover.setCellEditor(new EyeCellRemover());
        remover.setMaxWidth(40);
        remover.setResizable(false);
        table.setColumnControlVisible(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setTerminateEditOnFocusLost(true);
        table.setHighlighters(HighlighterFactory.createSimpleStriping(HighlighterFactory.NOTEPAD));
        table.setRolloverEnabled(true);
        return new JScrollPane(table);
    }

    private void refreshData() {
        refreshButton.setEnabled(false);
        objectSelectorBox.setEnabled(false);
        String item = (String) objectSelectorBox.getSelectedItem();
        remoteSourceActClass = item;
        progressBar.setValue(0);
        model.clear();
        try {
            RemoteSourceLoader sourceLoader = new RemoteSourceLoader(model, total);
            sourceLoader.addPropertyChangeListener(progressListener);
            sourceLoader.execute();
            model.fireTableDataChanged(); 
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "RefreshData: " + ex);
            ex.printStackTrace();
        }
    }

    private class ObjectSelectorListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            refreshData();
        }
    }

    private class ActButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String actionCommand = e.getActionCommand();
            if (actionCommand.equals("add")) {
                model.addEmptyRow();
                model.fireTableDataChanged();
            } else if (actionCommand.equals("refresh")) {
                refreshData();
            } else {
                /*COMMIT STUB*/
            }
        }
    }

    private class ProgressListener implements PropertyChangeListener {

        JProgressBar progressBar;

        ProgressListener(JProgressBar progressBar) {
            this.progressBar = progressBar;
            this.progressBar.setValue(0);
        }

        public void propertyChange(PropertyChangeEvent evt) {
            String strPropertyName = evt.getPropertyName();
            if ("progress".equals(strPropertyName)) {
                this.progressBar.setIndeterminate(false);
                int progress = (Integer) evt.getNewValue();
                this.progressBar.setValue(progress);
                refreshButton.setEnabled(progressBar.getPercentComplete() == 1.0);
                objectSelectorBox.setEnabled(progressBar.getPercentComplete() == 1.0);
            }
        }
    }
}
