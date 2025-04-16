# TPC-H Query 5: Multithreaded Java Implementation Report

## Author

Rajal Srivastava

## Overview

This project implements TPC-H Query 5 in Java using multithreading to improve performance when processing large datasets. The query calculates revenue by nation for orders placed by customers in a specific region during a given time frame.

## Approach

1. **Data Parsing:**

   - The data is parsed from `.tbl` files using custom loaders.
   - Files include: `customer.tbl`, `orders.tbl`, `lineitem.tbl`, `supplier.tbl`, `nation.tbl`, and `region.tbl`.

2. **Filtering Steps:**

   - **Region Filter:** Select only the region specified by the user (e.g., "ASIA").
   - **Nation Filter:** Select nations that belong to the specified region.
   - **Customer Filter:** Select customers from those nations.
   - **Order Filter:** Select orders placed by those customers.
   - **Supplier Filter:** Select suppliers from nations in the selected region.

3. **Multithreading Execution:**

   - A fixed thread pool is created using `Executors.newFixedThreadPool()`.
   - Each `LineItem` is processed in parallel using a lambda-based `Callable<Void>`.
   - Thread-safe accumulation of revenue is handled using a `ConcurrentHashMap`.

4. **Revenue Calculation:**

   - Revenue for each `LineItem` is calculated as: `extended_price * (1 - discount)`.
   - Only `LineItems` linked to the selected orders and suppliers are considered.

5. **Output:**

   - Results are sorted in descending order of revenue.
   - Printed to console and also saved to `results/query5_result.txt`.

## Challenges Encountered

- **Parsing large files efficiently:** Required optimized reading to avoid memory bottlenecks.
- **Ensuring thread safety:** Using `ConcurrentHashMap` and avoiding shared mutable state.
- **Joining multiple tables without a DBMS:** Handled all joins in-memory using `Map` and `Set` structures.
- **Command-line argument parsing:** Built a minimal parser to support user input.

## Lessons Learned

- Importance of clean separation of concerns for parsing, filtering, and computation.
- Hands-on experience with Java's concurrency API.
- How to simulate database joins and queries in pure Java.
- Better appreciation for memory management and thread coordination.

---

**End of Report**

