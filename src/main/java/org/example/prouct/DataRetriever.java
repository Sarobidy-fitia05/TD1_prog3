package org.example.prouct;

import java.math.BigDecimal;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
public class DataRetriever {
    public List<Category> getAllCategories() throws SQLException {
        String sql = "SELECT id, name FROM product_category ORDER BY id";
        List<Category> list = new ArrayList<>();
        try (Connection conn = DBConnection.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Category c = new Category(rs.getInt("id"), rs.getString("name"));
                list.add(c);
            }
        }
        return list;
    }
    public List<Product> getProductList(int page, int size) throws SQLException {
        if (page < 1) page = 1;
        int offset = (page - 1) * size;

        String sql =
                "SELECT p.id, p.name, p.price, p.creation_datetime, " +
                        " (SELECT id FROM product_category pc WHERE pc.product_id = p.id LIMIT 1) AS category_id, " +
                        " (SELECT name FROM product_category pc WHERE pc.product_id = p.id LIMIT 1) AS category_name " +
                        "FROM product p " +
                        "ORDER BY p.id " +
                        "LIMIT ? OFFSET ?";

        List<Product> list = new ArrayList<>();
        try (Connection conn = DBConnection.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, size);
            ps.setInt(2, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product p = mapRowToProduct(rs);
                    list.add(p);
                }
            }
        }
        return list;
    }
    public List<Product> getProductsByCriteria(String productName, String categoryName, Instant creationMin, Instant creationMax) throws SQLException {
        return getProductsByCriteria(productName, categoryName, creationMin, creationMax, -1, -1);
    }
    public List<Product> getProductsByCriteria(String productName, String categoryName, Instant creationMin, Instant creationMax, int page, int size) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT p.id, p.name, p.price, p.creation_datetime, ")
                .append(" (SELECT id FROM product_category pc WHERE pc.product_id = p.id LIMIT 1) AS category_id, ")
                .append(" (SELECT name FROM product_category pc WHERE pc.product_id = p.id LIMIT 1) AS category_name ")
                .append("FROM product p ")
                .append("WHERE 1=1 ");

        List<Object> params = new ArrayList<>();

        if (productName != null && !productName.trim().isEmpty()) {
            sb.append(" AND p.name ILIKE ? ");
            params.add("%" + productName + "%");
        }
        if (categoryName != null && !categoryName.trim().isEmpty()) {
            // on filtre les produits qui ont au moins une categorie correspondant au motif
            sb.append(" AND EXISTS (SELECT 1 FROM product_category pc WHERE pc.product_id = p.id AND pc.name ILIKE ?) ");
            params.add("%" + categoryName + "%");
        }
        if (creationMin != null) {
            sb.append(" AND p.creation_datetime >= ? ");
            params.add(Timestamp.from(creationMin));
        }
        if (creationMax != null) {
            sb.append(" AND p.creation_datetime <= ? ");
            params.add(Timestamp.from(creationMax));
        }

        sb.append(" ORDER BY p.id ");

        boolean doPagination = (page > 0 && size > 0);
        if (doPagination) {
            sb.append(" LIMIT ? OFFSET ? ");
        }

        String sql = sb.toString();
        List<Product> list = new ArrayList<>();

        try (Connection conn = DBConnection.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int idx = 1;
            for (Object p : params) {
                if (p instanceof String) {
                    ps.setString(idx++, (String) p);
                } else if (p instanceof Timestamp) {
                    ps.setTimestamp(idx++, (Timestamp) p);
                } else {
                    ps.setObject(idx++, p);
                }
            }
            if (doPagination) {
                int offset = (page - 1) * size;
                ps.setInt(idx++, size);
                ps.setInt(idx, offset);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product prod = mapRowToProduct(rs);
                    list.add(prod);
                }
            }
        }

        return list;
    }

    private Product mapRowToProduct(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        BigDecimal price = rs.getBigDecimal("price");
        Timestamp ts = rs.getTimestamp("creation_datetime");
        Instant creation = ts != null ? ts.toInstant() : null;

        Integer catId = rs.getObject("category_id") == null ? null : rs.getInt("category_id");
        String catName = rs.getString("category_name");
        Category category = null;
        if (catId != null && catName != null) {
            category = new Category(catId, catName);
        }
        return new Product(id, name, price, creation, category);
    }

}
