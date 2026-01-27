package org.example.p3_Java_Advance_Concept.c1_generics.runner.scenarios;

import org.example.p3_Java_Advance_Concept.c1_generics.contracts.IPair;
import org.example.p3_Java_Advance_Concept.c1_generics.contracts.IRepository;
import org.example.p3_Java_Advance_Concept.c1_generics.impl.Pair;
import org.example.p3_Java_Advance_Concept.c1_generics.model.Product;

import java.util.HashMap;
import java.util.Map;

/**
 * Demonstrates generic interfaces and their implementations.
 */
public class GenericInterfaceScenario {

    public void execute() {
        System.out.println("┌────────────────────────────────────────┐");
        System.out.println("│    SCENARIO 5: GENERIC INTERFACES      │");
        System.out.println("└────────────────────────────────────────┘\n");

        demonstratePairInterface();
        demonstrateRepositoryPattern();
    }

    private void demonstratePairInterface() {
        System.out.println("▸ IPair<K, V> Interface");
        System.out.println("  ---------------------");

        // Using the interface type
        IPair<String, Integer> pair1 = new Pair<>("count", 42);
        IPair<Integer, String> pair2 = new Pair<>(1, "First");

        System.out.println("  IPair<String, Integer>:");
        System.out.println("    getKey():   " + pair1.getKey());
        System.out.println("    getValue(): " + pair1.getValue());

        System.out.println("\n  IPair<Integer, String>:");
        System.out.println("    getKey():   " + pair2.getKey());
        System.out.println("    getValue(): " + pair2.getValue());

        System.out.println("\n  💡 One interface, infinite type combinations!");
        System.out.println();
    }

    private void demonstrateRepositoryPattern() {
        System.out.println("▸ IRepository<T, ID> - Real-World Pattern");
        System.out.println("  ----------------------------------------");

        // Create a product repository (inline implementation)
        IRepository<Product, String> productRepo = new ProductRepository();

        // Add products
        Product laptop = new Product("P001", "Laptop", 999.99, "Electronics");
        Product phone = new Product("P002", "Phone", 499.99, "Electronics");
        Product book = new Product("P003", "Java Guide", 49.99, "Books");

        productRepo.save(laptop);
        productRepo.save(phone);
        productRepo.save(book);

        System.out.println("  Saved 3 products to repository");
        System.out.println("  count(): " + productRepo.count());

        System.out.println("\n  findById(\"P001\"): " + productRepo.findById("P001"));
        System.out.println("  findById(\"P002\"): " + productRepo.findById("P002"));
        System.out.println("  findById(\"P999\"): " + productRepo.findById("P999"));

        System.out.println("\n  delete(\"P002\"): " + productRepo.delete("P002"));
        System.out.println("  count() after delete: " + productRepo.count());

        System.out.println("\n  ✨ BENEFIT: Same interface for User, Order, Payment, etc.");
        System.out.println("     IRepository<User, Long>");
        System.out.println("     IRepository<Order, UUID>");
        System.out.println("     IRepository<Payment, String>");
        System.out.println();
    }

    /**
     * Inner class implementing IRepository for Products.
     */
    private static class ProductRepository implements IRepository<Product, String> {
        private final Map<String, Product> storage = new HashMap<>();

        @Override
        public Product findById(String id) {
            return storage.get(id);
        }

        @Override
        public void save(Product entity) {
            storage.put(entity.getId(), entity);
        }

        @Override
        public boolean delete(String id) {
            return storage.remove(id) != null;
        }

        @Override
        public int count() {
            return storage.size();
        }
    }
}
