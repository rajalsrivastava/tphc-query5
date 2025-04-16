# TPC-H Query 5: Multithreaded Java Implementation

## Overview
This project implements TPC-H Query 5 in Java using multithreading to improve performance when processing large datasets. The query calculates revenue by nation for orders placed by customers in a specific region during a given time frame.

## Features
- Parses `.tbl` data files from TPC-H benchmark.
- Filters and joins data in-memory without using a DBMS.
- Multithreaded line item processing for better performance.
- Outputs results sorted by revenue per nation.

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
javac -d out src/*.java src/model/*.java src/utils/*.java
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
INDIA: 1808514891.98
INDONESIA: 1772198392.63
VIETNAM: 1768665739.23
CHINA: 1752398058.53
JAPAN: 1639749208.13
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

## License
This repository is for educational purposes only and does not redistribute TPC-H data generation code.

