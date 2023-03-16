import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckInventory {
    private Connection conn;

    public CheckInventory() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BTBai3", "root", "password");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public String checkInventory(String productId, int quantity) {
        String result = "";
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM inventory WHERE product_id = ?");
            stmt.setString(1, productId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int inStock = rs.getInt("quantity");
                if (quantity <= inStock) {
                    result = String.format("Sản phẩm %s có đủ hàng trong kho!", productId);
                } else {
                    result = String.format("Sản phẩm %s chỉ còn %d sản phẩm trong kho.", productId, inStock);
                }
            } else {
                result = String.format("Không tìm thấy mã sản phẩm %s trong kho.", productId);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return result;
    }
}