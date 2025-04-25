# TPC-H Query 5: Multithreaded Java Implementation

## Overview
This project implements TPC-H Query 5 in Java using multithreading to improve performance when processing large datasets. The query calculates revenue by nation for orders placed by customers in a specific region during a given time frame.

## Features
- Parses `.tbl` data files from TPC-H benchmark.
- Filters and joins data in-memory without using a DBMS.
- Multithreaded line item processing for better performance.
- Outputs results sorted by revenue per nation.
- Supports command-line configuration for region, date range, and number of threads.

## Requirements
- Java 8 or higher

## Folder Structure
```
tpch-query5/
├── src/            # Java source code
├── data/           # Directory for .tbl files
├── results/        # Output result directory
├── out/            # Compiled .class files
├── README.md       # This file
├── report.md       # Detailed implementation report
├── .gitignore      # Git ignore rules
```

## How to Build
```bash
javac -d out src/*.java src/model/*.java src/utils/*.java src/parser/*.java
```

## How to Run
```bash
java -Xmx4g -cp out Main \
    --region "ASIA" \
    --start-date "1994-01-01" \
    --end-date "1995-01-01" \
    --threads 10 \
    --data-dir "./data" \
    --result-dir "./results"
```

## Output Example
```
Multi-threaded execution time: 326 ms
Single-threaded execution time: 459 ms
==== Runtime Summary ====
Multi-threaded time: 326 ms
Single-threaded time: 459 ms
Speedup: 1.41x
CHINA: 269846053.23
INDONESIA: 269039572.94
VIETNAM: 268544386.56
INDIA: 268003150.58
JAPAN: 250022486.48
✅ Results saved to: /mnt/c/Users/rajal/Projects/tpch-query5/./results/query5_result.txt

```

## Generating TPC-H `.tbl` Files
This project does not include the `tpch-dbgen` utility due to licensing restrictions.

To generate `.tbl` files:
1. Clone the official TPC-H dbgen tool:
   ```bash
   git clone https://github.com/electrum/tpch-dbgen
   cd tpch-dbgen
   make
   ./dbgen -s 1
   ```
2. Move the generated `.tbl` files into the `data/` folder of this project.

## Notes
- Use `-Xmx4g` or adjust JVM memory settings depending on your machine's available memory.
- Ensure `.tbl` files use the `|` delimiter as expected.
- For more details on the implementation, refer to the report.md.

## License
This repository is for educational purposes only and does not redistribute TPC-H data generation code.

