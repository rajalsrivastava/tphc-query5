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

- **Parsing large files efficiently:** Optimized reading to avoid memory bottlenecks and ensure smooth operation with large datasets.
- **Ensuring thread safety:** Leveraged ConcurrentHashMap for thread-safe revenue accumulation, avoiding shared mutable states.
- **Joining multiple tables without a DBMS:** Handled all joins in-memory using Map and Set structures, ensuring efficient and correct data processing.
- **Command-line argument parsing:** BBuilt a minimal parser to support dynamic input for region, date range, number of threads, and directory paths.

## Lessons Learned

- Importance of clean separation of concerns for parsing, filtering, and computation, which helped with maintaining a modular and easy-to-understand codebase.
- Hands-on experience with Java's concurrency API and managing multi-threaded tasks.
- Simulated database joins and queries efficiently in Java without the use of a DBMS.
- Gained insights into memory management and thread coordination when working with large datasets.

## Performance Metrics

- Single-threaded execution time: 459 ms
- Multi-threaded execution time: 326 ms
- Speedup: 1.41x

---

**End of Report**

