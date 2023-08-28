import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CheckoutController implements ActionListener {
    private BuyerView view;
    private DataAdapter dataAdapter; // to save and load product
    private Order order = null;

    public CheckoutController(BuyerView view, DataAdapter dataAdapter) {
        this.dataAdapter = dataAdapter;
        this.view = view;

        view.getBtnAdd().addActionListener(this);
        view.getBtnPay().addActionListener(this);

        order = new Order();

    }


    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.getBtnAdd())
            addProduct();
        else
        if (e.getSource() == view.getBtnPay())
            makeOrder();
    }

    private void makeOrder() {
        JOptionPane.showMessageDialog(null, "Your purchase is successful...");

        int co = view.getTblItems().getRowCount();
        JTable jj = view.getTblItems();
        int selectedRow = 0;

        Object[] rowData = new Object[jj.getColumnCount()];
        for (int i = 0; i < jj.getRowCount(); i++) {
            rowData[0] = jj.getValueAt(i, 0);
            rowData[1] = jj.getValueAt(i,3);

            Product product = dataAdapter.loadProduct((int)rowData[0]);

            if (product != null){
                product.setQuantity(product.getQuantity()-(double)rowData[1]);
            }

            dataAdapter.saveProduct(product);

        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String currentDate = dateFormat.format(date);
        order.setOrderID(dataAdapter.getOrderId()+1);
        order.setDate(currentDate);
        order.setBuyerID(Application.getInstance().getCurrentUser().getUserID());
        order.getTotalCost();
        order.setTotalTax(order.getTotalCost()*.15);

        dataAdapter.saveOrder(order);

    }

    private void addProduct() {
        String id = JOptionPane.showInputDialog("Enter ProductID: ");
        Product product = dataAdapter.loadProduct(Integer.parseInt(id));
        if (product == null) {
            JOptionPane.showMessageDialog(null, "This product does not exist!");
            return;
        }

        double quantity = Double.parseDouble(JOptionPane.showInputDialog(null,"Enter quantity: "));

        if (quantity < 0 || quantity > product.getQuantity()) {
            JOptionPane.showMessageDialog(null, "This quantity is not valid!");
            return;
        }

        OrderLine line = new OrderLine();
        line.setOrderID(this.order.getOrderID());
        line.setProductID(product.getProductID());
        line.setQuantity(quantity);
        line.setCost(quantity * product.getPrice());
        order.getLines().add(line);
        order.setTotalCost(order.getTotalCost() + line.getCost());



        Object[] row = new Object[5];
        row[0] = line.getProductID();
        row[1] = product.getName();
        row[2] = product.getPrice();
        row[3] = line.getQuantity();
        row[4] = line.getCost();

        this.view.addRow(row);
        this.view.getLabTotal().setText("Total: $" + order.getTotalCost());
        this.view.invalidate();
    }

}