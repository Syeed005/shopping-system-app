import java.sql.*;

public class Application {

    private static Application instance;   // Singleton pattern

    public static Application getInstance() {
        if (instance == null) {
            instance = new Application();
        }
        return instance;
    }
    // Main components of this application

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    private DataAdapter dataAdapter;

    private User currentUser = null;

    public User getCurrentUser() { return currentUser; }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    private SellerView productView = new SellerView();

    private BuyerView checkoutScreen = new BuyerView();

    private MainScreen mainScreen = new MainScreen();


    public MainScreen getMainScreen() {
        return mainScreen;
    }


    public SellerView getProductView() {
        return productView;
    }

    public BuyerView getCheckoutScreen() {
        return checkoutScreen;
    }

    public LoginScreen loginScreen = new LoginScreen();

    public LoginScreen getLoginScreen() {
        return loginScreen;
    }

    public LoginController loginController; // = new LoginController(loginScreen, dataAdapter);

    private ProductController productController;

    public ProductController getProductController() {
        return productController;
    }

    private CheckoutController checkoutController;

    public CheckoutController getCheckoutController() {
        return checkoutController;
    }

    public DataAdapter getDataAdapter() {
        return dataAdapter;
    }


    private Application() {
        // create SQLite database connection here!
        try {
            Class.forName("org.sqlite.JDBC");

            String url = "jdbc:sqlite:store.db";

            connection = DriverManager.getConnection(url);
            dataAdapter = new DataAdapter(connection);

        }
        catch (ClassNotFoundException ex) {
            System.out.println("SQLite is not installed. System exits with error!");
            ex.printStackTrace();
            System.exit(1);
        }

        catch (SQLException ex) {
            System.out.println("SQLite database is not ready. System exits with error!" + ex.getMessage());

            System.exit(2);
        }

        // Create data adapter here!

        productController = new ProductController(productView, dataAdapter);

        checkoutController = new CheckoutController(checkoutScreen, dataAdapter);

        loginController = new LoginController(loginScreen, dataAdapter);

    }


    public static void main(String[] args) {
        Application.getInstance().getLoginScreen().setVisible(true);
    }
}
