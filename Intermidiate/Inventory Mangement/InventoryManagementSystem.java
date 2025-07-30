import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class InventoryManagementSystem extends JFrame {
    private JTextField itemIdField, itemNameField, quantityField, priceField;
    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel statusBar;

    public InventoryManagementSystem() {
        setTitle("Inventory Management System");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // === Main Content Panel ===
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // === Input Panel ===
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create and add input fields
        itemIdField = new JTextField(15);
        itemNameField = new JTextField(15);
        quantityField = new JTextField(15);
        priceField = new JTextField(15);

        // Row 0
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(createInputLabel("Item ID:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(itemIdField, gbc);

        gbc.gridx = 2;
        inputPanel.add(createInputLabel("Item Name:"), gbc);
        gbc.gridx = 3;
        inputPanel.add(itemNameField, gbc);

        // Row 1
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(createInputLabel("Quantity:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(quantityField, gbc);

        gbc.gridx = 2;
        inputPanel.add(createInputLabel("Price:"), gbc);
        gbc.gridx = 3;
        inputPanel.add(priceField, gbc);

        mainPanel.add(inputPanel, BorderLayout.NORTH);

        // === Table ===
        String[] columns = {"Item ID", "Item Name", "Quantity", "Price", "Total Value"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // === Buttons Panel ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Create styled buttons with high contrast colors
        JButton addButton = createStyledButton("Add", new Color(0, 100, 0), e -> addItem());
        JButton updateButton = createStyledButton("Update", new Color(0, 0, 139), e -> updateItem());
        JButton deleteButton = createStyledButton("Delete", new Color(139, 0, 0), e -> deleteItem());
        JButton clearButton = createStyledButton("Clear", new Color(128, 0, 128), e -> clearFields());

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // === Status Bar ===
        statusBar = new JLabel(" Ready");
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        statusBar.setFont(new Font("SansSerif", Font.BOLD, 12));

        // === Add all to Frame ===
        add(mainPanel, BorderLayout.CENTER);
        add(statusBar, BorderLayout.PAGE_END);

        // Add table selection listener
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    itemIdField.setText(tableModel.getValueAt(row, 0).toString());
                    itemNameField.setText(tableModel.getValueAt(row, 1).toString());
                    quantityField.setText(tableModel.getValueAt(row, 2).toString());
                    priceField.setText(tableModel.getValueAt(row, 3).toString());
                }
            }
        });

        setVisible(true);
    }

    private JLabel createInputLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        return label;
    }

    private JButton createStyledButton(String text, Color bgColor, ActionListener action) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(100, 35));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }

    private void addItem() {
        String id = itemIdField.getText().trim();
        String name = itemNameField.getText().trim();
        String qtyText = quantityField.getText().trim();
        String priceText = priceField.getText().trim();

        if (id.isEmpty() || name.isEmpty() || qtyText.isEmpty() || priceText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        try {
            int qty = Integer.parseInt(qtyText);
            double price = Double.parseDouble(priceText);
            double total = qty * price;

            // Check for duplicate ID
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 0).toString().equals(id)) {
                    JOptionPane.showMessageDialog(this, "Item ID already exists!");
                    return;
                }
            }

            tableModel.addRow(new Object[]{id, name, qty, price, total});
            statusBar.setText(" Item added successfully.");
            clearFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter valid numbers for Quantity and Price.");
        }
    }

    private void updateItem() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                String id = itemIdField.getText().trim();
                String name = itemNameField.getText().trim();
                int qty = Integer.parseInt(quantityField.getText().trim());
                double price = Double.parseDouble(priceField.getText().trim());
                double total = qty * price;

                // Check if ID is being changed to an existing one (except current row)
                String originalId = tableModel.getValueAt(selectedRow, 0).toString();
                if (!id.equals(originalId)) {
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        if (i != selectedRow && tableModel.getValueAt(i, 0).toString().equals(id)) {
                            JOptionPane.showMessageDialog(this, "Item ID already exists!");
                            return;
                        }
                    }
                }

                tableModel.setValueAt(id, selectedRow, 0);
                tableModel.setValueAt(name, selectedRow, 1);
                tableModel.setValueAt(qty, selectedRow, 2);
                tableModel.setValueAt(price, selectedRow, 3);
                tableModel.setValueAt(total, selectedRow, 4);
                statusBar.setText(" Item updated.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Select valid row and enter correct data.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a row to update.");
        }
    }

    private void deleteItem() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(
                this, 
                "Are you sure you want to delete this item?", 
                "Confirm Delete", 
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                tableModel.removeRow(selectedRow);
                statusBar.setText(" Item deleted.");
                clearFields();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a row to delete.");
        }
    }

    private void clearFields() {
        itemIdField.setText("");
        itemNameField.setText("");
        quantityField.setText("");
        priceField.setText("");
        table.clearSelection();
        statusBar.setText(" Fields cleared.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new InventoryManagementSystem();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}