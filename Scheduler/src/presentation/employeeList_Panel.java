package presentation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class employeeList_Panel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JPanel listPanel;
	private JLabel listLbl;
	private JTable listTable;
	private JButton delBtn;
	private DefaultTableModel model;
	private DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer(); //to adjust the cell layout of the listTable


	public employeeList_Panel() {
		this.listPanel = init();
	}

	private JPanel init() {
		setDefaultModel();
		listPanel = new JPanel();
		listPanel.setLayout(new GridBagLayout());

		listLbl = new JLabel("List of Employees");
		GridBagConstraints gcL = new GridBagConstraints();
		gcL.gridx = 0;
		gcL.gridy = 0;
		gcL.weightx = 1;
		gcL.weighty = 1;
		gcL.anchor = GridBagConstraints.PAGE_START;
		listPanel.add(listLbl, gcL);

		// listTable customizations
		Object[] columnNames = { "ID", "Name"};
		model.setColumnIdentifiers(columnNames);
		listTable = new JTable() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return column > 0;
			}
		};
		listTable.setModel(model); // setting the table to the custom defaultTableModel();
		
//		show_Employee();
		// imports the layout from model which is the default table model
		leftRenderer.setHorizontalAlignment(SwingConstants.LEFT); // changes default settings to align all values in the column
															// to the left
		listTable.setAutoCreateRowSorter(true); // enable automatic sorting based on the data type of the column
		listTable.getColumnModel().getColumn(0).setCellRenderer(leftRenderer); // changes the ID column settings
		listTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		listTable.getColumnModel().getColumn(0).setPreferredWidth(50);
		listTable.getColumnModel().getColumn(1).setPreferredWidth(180);
		listTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

//		listTableActionListener lForTable = new listTableActionListener();
//		listTable.getModel().addTableModelListener(lForTable);

		// adds the scroll bar to the listTable
		JScrollPane pane = new JScrollPane(listTable);
		gcL.gridy = 1;
		gcL.fill = GridBagConstraints.HORIZONTAL;
		listPanel.add(pane, gcL);

		delBtn = new JButton("Delete");
		gcL.gridy = 2;
		gcL.fill = GridBagConstraints.NONE;
		gcL.anchor = GridBagConstraints.LAST_LINE_START;
//		delBtnActionListener lForDelBtn = new delBtnActionListener();
//		delBtn.addActionListener(lForDelBtn);
		listPanel.add(delBtn, gcL);

//		editBtn = new JButton("Edit");
//		gcL.anchor = GridBagConstraints.PAGE_END;
//		editBtnActionListener lForEdtBtn = new editBtnActionListener();
//		editBtn.addActionListener(lForEdtBtn);
//		listPanel.add(editBtn, gcL);
//
//		updateBtn = new JButton("Update");
//		gcL.anchor = GridBagConstraints.LAST_LINE_END;
//		listPanel.add(updateBtn, gcL);
		return listPanel;
	}

	private void setDefaultModel() {
		model = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			// need to override the bottom function to sort based on the type of data
			// (Int,Double,String) of the column
			// if we dont override then the default treats the sorting as strings
			@Override
			public Class<?> getColumnClass(int column) {
				Class returnValue;
				if ((column >= 0) && (column < getColumnCount())) {
					returnValue = getValueAt(0, column).getClass();
				} else {
					returnValue = Object.class;
				}
				return returnValue;
			}
		};
	}
}
