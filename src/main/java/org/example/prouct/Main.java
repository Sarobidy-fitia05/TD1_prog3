package org.example.prouct;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
public class Main {
    public static void main(String[] args) {
        DataRetriever retriever = new DataRetriever();

        try {
            System.out.println("=== getAllCategories ===");
            List<Category> cats = retriever.getAllCategories();
            cats.forEach(System.out::println);

            System.out.println("\n=== getProductList(page=1,size=10) ===");
            retriever.getProductList(1, 10).forEach(System.out::println);

            System.out.println("\n=== getProductList(page=1,size=5) ===");
            retriever.getProductList(1, 5).forEach(System.out::println);

            System.out.println("\n=== getProductList(page=1,size=3) ===");
            retriever.getProductList(1, 3).forEach(System.out::println);

            System.out.println("\n=== getProductList(page=2,size=2) ===");
            retriever.getProductList(2, 2).forEach(System.out::println);

            System.out.println("\n=== getProductsByCriteria: productName = \"Dell\" ===");
            retriever.getProductsByCriteria("Dell", null, null, null).forEach(System.out::println);

            System.out.println("\n=== getProductsByCriteria: categoryName = \"info\" ===");
            retriever.getProductsByCriteria(null, "info", null, null).forEach(System.out::println);

            System.out.println("\n=== getProductsByCriteria: productName=\"iPhone\", categoryName=\"mobile\" ===");
            retriever.getProductsByCriteria("iPhone", "mobile", null, null).forEach(System.out::println);

            System.out.println("\n=== getProductsByCriteria: creation between 2024-02-01 and 2024-03-01 ===");
            Instant min = LocalDate.of(2024, 2, 1).atStartOfDay(ZoneId.systemDefault()).toInstant();
            Instant max = LocalDate.of(2024, 3, 1).atStartOfDay(ZoneId.systemDefault()).toInstant();
            retriever.getProductsByCriteria(null, null, min, max).forEach(System.out::println);

            System.out.println("\n=== getProductsByCriteria: categoryName='audio' full year filter ===");
            Instant minAll = LocalDate.of(2024, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant();
            Instant maxAll = LocalDate.of(2024, 12, 1).atStartOfDay(ZoneId.systemDefault()).toInstant();
            retriever.getProductsByCriteria(null, "audio", minAll, maxAll).forEach(System.out::println);

            System.out.println("\n=== getProductsByCriteria with pagination: page=1,size=5, productName='Dell' ===");
            retriever.getProductsByCriteria("Dell", null, null, null, 1, 5).forEach(System.out::println);

            System.out.println("\n=== getProductsByCriteria with pagination: page=1,size=10, categoryName='informatique' ===");
            retriever.getProductsByCriteria(null, "informatique", null, null, 1, 10).forEach(System.out::println);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
