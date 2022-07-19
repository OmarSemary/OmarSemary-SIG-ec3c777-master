package com.company.view;


/*
  @implNote Udacity Project
 * @author Omar El Semary

 */

import com.company.controller.FileOperations;
import com.company.invoice.Invoice;
import com.company.invoice.Item;
import javax.naming.directory.NoSuchAttributeException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class SIG extends JFrame implements ActionListener {

    private final JTable invoices;
    private final JLabel invoiceNumValue;
    private final JTextField invoiceDateValue;
    private final JTextField customerNameValue;
    private final JLabel invoiceTotalValue;
    private final JTable invoiceItemsTable;
    private final String currentDataDirectory = "./data/";
    private final FileOperations fileOperations = new FileOperations(null, this, currentDataDirectory);
    // Invoices
    private Invoice[] invoicesArray = fileOperations.updateInvoicesArray( fileOperations.loadInvoiceHeader(""), fileOperations.loadInvoiceLine(""));

    public SIG() throws HeadlessException {
        super("Sale Invoice Generator");
        setLayout(new FlowLayout());

        // Menu bar
        JMenuItem loadItem = new JMenuItem("Load files",'L');
        JMenuItem saveItem = new JMenuItem("Save files",'S');
        loadItem.setAccelerator(KeyStroke.getKeyStroke('L',KeyEvent.CTRL_DOWN_MASK));
        saveItem.setAccelerator(KeyStroke.getKeyStroke('S',KeyEvent.CTRL_DOWN_MASK));
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
            loadItem.addActionListener(this);
            saveItem.addActionListener(this);
            loadItem.setActionCommand("L");
            saveItem.setActionCommand("S");
            menuBar.add(fileMenu);
            fileMenu.add(loadItem);
            fileMenu.add(saveItem);
            setJMenuBar(menuBar);

        //The left Panel
        JPanel leftPanel = new JPanel();
            leftPanel.setBorder(new EmptyBorder(10,10,10,10));
            leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
                JPanel invoicesTablePanel = new JPanel();
                invoicesTablePanel.setLayout( new FlowLayout());
                invoicesTablePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Invoice Table", TitledBorder.LEFT,
                TitledBorder.TOP));

                    // Invoices Table
                    invoices = new JTable();
                    renderInvoiceTable();
                    invoices.addMouseListener(new MouseAdapter() {
                        public void mousePressed(MouseEvent e) {
                            if (e.getClickCount() == 1) {
                                JTable target = (JTable)e.getSource();
                                int row = target.getSelectedRow();
                                Invoice selectedInvoice = invoicesArray[row];
                                invoiceNumValue.setText( String.valueOf( selectedInvoice.getInvoiceNumber() ) );
                                if ( selectedInvoice.getInvoiceDate() == null) {
                                    invoiceDateValue.setText("");
                                }else {
                                    invoiceDateValue.setText(selectedInvoice.getInvoiceDate());
                                }
                                if ( selectedInvoice.getCustomerName() == null) {
                                    customerNameValue.setText("");
                                } else {
                                    customerNameValue.setText(selectedInvoice.getCustomerName());
                                }
                                if(selectedInvoice.getItemsArray() == null ) {
                                    invoiceTotalValue.setText("0");
                                    String[] columnNames = {"No.", "Item Name", "Item Price", "Count", "Item Total"};
                                    String[][] data = { {"1","","","",""}};
                                    boolean[] isEditable = {false,true,true,true,false};
                                    DefaultTableModel model = new DefaultTableModel(data, columnNames){
                                        @Override
                                        public boolean isCellEditable(int row, int column) {
                                            return isEditable[column];
                                        }
                                    };
                                    invoiceItemsTable.setModel( model);
                                } else {
                                    invoiceTotalValue.setText( String.valueOf( selectedInvoice.getTotalAmount() ) );
                                    String[] columnNames = {
                                            "No.", "Item Name", "Item Price", "Count", "Item Total"};
                                    String[][] data = selectedInvoice.getItemsArray();
                                    boolean[] isEditable = {false,true,true,true,false};
                                    DefaultTableModel model = new DefaultTableModel(data, columnNames){
                                        @Override
                                        public boolean isCellEditable(int row, int column) {
                                            return isEditable[column];
                                        }
                                    };
                                    invoiceItemsTable.setModel( model );
                                }
                            }
                        }
                    });
                    JScrollPane invoices_sp = new JScrollPane(invoices);
                    invoicesTablePanel.add(invoices_sp);

                // Left Panel Buttons

                JPanel leftPanelButtons = new JPanel();
                leftPanelButtons.setLayout(new FlowLayout());
                JButton create = new JButton("Create New Invoice");
                create.addActionListener(this);
                create.setActionCommand("C");
                JButton delete = new JButton("Delete Invoice");
                delete.addActionListener(this);
                delete.setActionCommand("D");
                leftPanelButtons.add(create);
                leftPanelButtons.add(delete);
                leftPanel.add(invoicesTablePanel);
                leftPanel.add(leftPanelButtons);
                add(leftPanel);

        JPanel rightPanel = new JPanel();
            rightPanel.setBorder(new EmptyBorder(10,10,10,10));
            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

                // Invoice Data
                JPanel invoiceData = new JPanel();
                invoiceData.setBorder(new EmptyBorder(10,0,10,200));
                invoiceData.setLayout(new GridLayout(4, 1, 0, 7));

                    JLabel invoiceNum = new JLabel("Invoice Number");
                    invoiceData.add(invoiceNum);

                    invoiceNumValue = new JLabel("");
                    invoiceData.add(invoiceNumValue);

                    JLabel invoiceDate = new JLabel("Invoice Date");
                    invoiceData.add(invoiceDate);

                    invoiceDateValue = new JTextField();
                    invoiceData.add(invoiceDateValue);

                    JLabel customerName = new JLabel("Customer Name");
                    invoiceData.add(customerName);

                    customerNameValue = new JTextField();
                    invoiceData.add(customerNameValue);

                    JLabel invoiceTotal = new JLabel("Invoice Total");
                    invoiceData.add(invoiceTotal);

                    invoiceTotalValue = new JLabel("");
                    invoiceData.add(invoiceTotalValue);


        // Invoice Items
                JPanel invoiceItems = new JPanel();
                invoiceItems.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Invoice Items", TitledBorder.LEFT,
                TitledBorder.TOP));
                    String[][] invoiceItemsData ={};
                    String[] invoiceItemsColumn ={"No.","Item Name","Item Price","Count","Item Total"};
                    DefaultTableModel model = new DefaultTableModel(invoiceItemsData, invoiceItemsColumn);
                    invoiceItemsTable = new JTable(model);
        JLabel info = new JLabel("You can insert & remove items from item table");
        JLabel info2 = new JLabel(" by pressing delete and insert key on keyboard.");
        invoiceItems.add(info);
        invoiceItems.add(info2);
                    JScrollPane invoiceItems_sp = new JScrollPane(invoiceItemsTable);
                    invoiceItems.add(invoiceItems_sp);


        // The right Panel
        JButton save = new JButton("Save");
                save.addActionListener(this);
                save.setActionCommand("SE");
        JButton cancel = new JButton("Cancel");
                cancel.addActionListener(this);
                cancel.setActionCommand("CE");


                JPanel buttonsPanel = new JPanel();
                buttonsPanel.add(save);
                buttonsPanel.add(cancel);

            rightPanel.add(invoiceData);
            rightPanel.add(invoiceItems);
            rightPanel.add(buttonsPanel);

        add(rightPanel);
        setLayout(new GridLayout(1, 2, 10, 15));
        setSize(1000, 500);
        setLocation(100,100);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        invoiceItemsTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode()==KeyEvent.VK_INSERT){
                    DefaultTableModel model = (DefaultTableModel) invoiceItemsTable.getModel();
                    model.addRow(new Object[]{String.valueOf( model.getRowCount() + 1 ), "", "", ""});
                }else if(e.getKeyCode()==KeyEvent.VK_DELETE){
                    int lastRow = invoiceItemsTable.getRowCount();
                    int selectedRow = invoiceItemsTable.getSelectedRow();
                    DefaultTableModel model = (DefaultTableModel) invoiceItemsTable.getModel();
                    model.removeRow(selectedRow);
                    if (lastRow-1 == 0){
                        model.addRow(new Object[]{String.valueOf( model.getRowCount() + 1 ), "", "", ""});
                    }else {
                        invoiceItemsTable.requestFocus();
                        invoiceItemsTable.setRowSelectionInterval(0,0);

                    }
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            // Load file
            case "L":
                fileOperations.updateInvoices(invoices);
                invoicesArray = fileOperations.loadFile(invoicesArray);
                renderInvoiceTable();
                break;
            // Save file
            case "S":
                fileOperations.updateInvoices(invoices);
                fileOperations.saveFile(invoicesArray);
                break;
            case "C":
                // Create empty row
                DefaultTableModel model = (DefaultTableModel) invoices.getModel();
                int invRowCount = invoices.getRowCount();
                if (invoices.getValueAt(invRowCount-1,3) == ""){
                    JOptionPane.showMessageDialog(new JFrame(),
                            "The last invoice is empty " + "Invoice No." + invRowCount + "" +
                                    " \n and doesn't contain a total amount", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                else {
                    model.addRow(new Object[]{String.valueOf(model.getRowCount() + 1), "", "", ""});
                    invoices.setModel(model);
                    Invoice[] newInvoicesArray = Arrays.copyOf(invoicesArray, invoicesArray.length + 1);
                    Invoice newInvoice = new Invoice();
                    newInvoice.setInvoiceNumber(model.getRowCount());
                    invoicesArray = newInvoicesArray;
                    invoicesArray[invoicesArray.length - 1] = newInvoice;
                }
                break;
            case "D":

                // Delete selected row
                model = (DefaultTableModel) invoices.getModel();
                DefaultTableModel model2 = (DefaultTableModel) invoiceItemsTable.getModel();
                int row = invoices.getSelectedRow();
                if (row < 0) {
                    JOptionPane.showMessageDialog(new JFrame(), "Select an invoice", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } if (row == 0){
                JOptionPane.showMessageDialog(new JFrame(), "You can't delete the master invoice you can update it", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
                else {
                    model.removeRow(row);
                    invoices.setModel(model);
                    Invoice[] DeleteInvoicesArray = new Invoice[ invoicesArray.length - 1];
                    for (int i = 0; i < DeleteInvoicesArray.length; i++) {
                        if (i < row) {
                            DeleteInvoicesArray[i] = invoicesArray[i];

                        }
                        else {
                            invoicesArray[i+1].setInvoiceNumber( invoicesArray[i+1].getInvoiceNumber() - 1 );
                            DeleteInvoicesArray[i] = invoicesArray[i+1];
                        }

                    }
                    invoicesArray = DeleteInvoicesArray;
                    invoiceDateValue.setText("");
                    customerNameValue.setText("");
                    invoiceTotalValue.setText("");
                    invoiceNumValue.setText("");
                    model2.setRowCount(0);
                    model2.fireTableDataChanged();
                }

                break;
            case "SE":
                int rowCount = invoiceItemsTable.getRowCount();
                invoiceItemsTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
                try {

                    if (invoiceItemsTable.getValueAt(invoiceItemsTable.getSelectedRow(), 2) == "") {
                        throw new NoSuchAttributeException() ;
                    } if (invoiceItemsTable.getValueAt(rowCount-1, 3) == "") {
                        throw new NoSuchAttributeException() ;
                    }
                    int invoiceNumber = Integer.parseInt( invoiceNumValue.getText() );
                    invoicesArray[invoiceNumber - 1].setInvoiceDate( invoiceDateValue.getText() );
                    invoicesArray[invoiceNumber - 1].setCustomerName( customerNameValue.getText() );
                    model = (DefaultTableModel) invoiceItemsTable.getModel();
                    int itemsCount = 0;
                    for (int i = 0; i < model.getRowCount(); i++) {
                        if( model.getValueAt(i, 0) == "" ) { break;}
                        itemsCount++;
                    }
                    Item[] items = new Item[itemsCount];

                    for (int i = 0; i < itemsCount; i++) {
                        Item item = new Item();
                        item.setItemName( (String) model.getValueAt(i, 1) );
                        item.setItemPrice(Integer.parseInt((String) model.getValueAt(i, 2)));
                        item.setCount(Integer.parseInt((String) model.getValueAt(i, 3)));
                        model.setValueAt( String.valueOf(item.getItemTotal()) , i, 4);
                        items[i] = item;
                    }
                    invoicesArray[invoiceNumber - 1].setItems(items);
                    invoiceTotalValue.setText(String.valueOf(invoicesArray[invoiceNumber - 1].getTotalAmount()));
                    model.addRow(new Object[]{String.valueOf( model.getRowCount() + 1 ), "", "", ""});
                    renderInvoiceTable();
                    if (invoiceItemsTable.getValueAt(0,4) == ""){
                        throw new RuntimeException();
                    }
                }
                    catch (NoSuchAttributeException saveButton) {
                    JOptionPane.showMessageDialog(new JFrame(), "You must add a price for the item and their count.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                catch (RuntimeException saveButton){
                    JOptionPane.showMessageDialog(new JFrame(), "You can't save an empty invoice.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                break;

            case "CE":

                // Discard change
                int selectedInvoice;
                try {

                    selectedInvoice = Integer.parseInt(invoiceNumValue.getText());
                    invoiceDateValue.setText("");
                    customerNameValue.setText("");
                    invoiceTotalValue.setText("");
                    invoiceNumValue.setText("");

                    String[] columnNames = {
                            "No.", "Item Name", "Item Price", "Count", "Item Total"};
                    String [][] originalData = invoicesArray[selectedInvoice - 1].getItemsArray();
                    DefaultTableModel originalModel = new DefaultTableModel(originalData, columnNames);
                    invoiceItemsTable.setModel(originalModel);
                    DefaultTableModel modelA = (DefaultTableModel) invoiceItemsTable.getModel();
                    modelA.setRowCount(0);
                    modelA.fireTableDataChanged();
                } catch (Exception ee) {
                    JOptionPane.showMessageDialog(new JFrame(), "Select an invoice", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

                break;

        }
    }

    private void renderInvoiceTable() {
        String[][] invoicesData = new String[invoicesArray.length][4];
        for (int i = 0; i < invoicesArray.length; i++) {
            invoicesData[i] = invoicesArray[i].getInvoiceArray();
        }

        String[] invoicesColumn ={"No.","Date","Customer","Total"};
        DefaultTableModel model = new DefaultTableModel(invoicesData, invoicesColumn){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        invoices.setModel( model );
    }
}