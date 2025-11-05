# Smart City Scheduling - Assignment 4

Implementation of Strongly Connected Components (SCC), Topological Sorting, and Shortest Paths in DAGs for city-service task scheduling.

## Requirements

- Java 11 or higher
- Maven 3.6 or higher

## Build

From a clean clone:

```bash
mvn clean compile
```

## Run

### Main program (demonstrates algorithms on example graph):

```bash
mvn exec:java
```

### Performance comparison (generates CSV comparison table):

```bash
mvn exec:java@comparison
```

This creates `algorithm_comparison.csv` with performance metrics for all algorithms across all datasets.

## Test

```bash
mvn test
```

## Project Structure

```
src/main/java/
  graph/
    scc/        - Tarjan SCC algorithm
    topo/       - Topological sorting (Kahn, DFS)
    dagsp/      - Shortest/Longest paths in DAGs
  util/
    Metrics.java - Operation counters and timing
    GraphLoader.java - JSON graph loader
  Main.java     - Main entry point
  PerformanceComparison.java - Algorithm comparison

src/test/java/
  graph/scc/    - SCC tests
  graph/topo/   - Topological sort tests
  graph/dagsp/  - DAG shortest path tests
  util/         - Utility tests

data/           - 9 graph datasets
```

## Datasets

All datasets are stored in `/data/` directory:

**Small (6-10 nodes):**
- `small_1_acyclic.json` - Pure DAG, 8 vertices, 7 edges
- `small_2_cyclic.json` - Contains cycles, 7 vertices, 7 edges
- `small_3_mixed.json` - Mixed structure, 10 vertices, 10 edges

**Medium (10-20 nodes):**
- `medium_1_sparse.json` - Sparse graph, 15 vertices, 15 edges
- `medium_2_dense.json` - Dense graph, 12 vertices, 21 edges
- `medium_3_multiple_scc.json` - Multiple SCCs, 18 vertices, 20 edges

**Large (20-50 nodes):**
- `large_1_sparse.json` - Sparse, 25 vertices, 26 edges
- `large_2_medium.json` - Medium density, 35 vertices, 67 edges
- `large_3_dense.json` - Dense, 30 vertices, 84 edges

All graphs are directed and use **edge weights** (weight_model: "edge").

## Algorithms

### 1. SCC (Tarjan's Algorithm)
- Finds strongly connected components
- Builds condensation graph (DAG)
- Tracks DFS visits and edge traversals
- Complexity: O(V + E)

### 2. Topological Sort
- Kahn's algorithm (queue-based)
- DFS-based algorithm (alternative)
- Tracks queue operations and edge traversals
- Complexity: O(V + E)

### 3. Shortest Paths in DAG
- Single-source shortest paths
- Longest path (critical path)
- Path reconstruction
- Tracks edge relaxations
- Complexity: O(V + E)

## Weight Model

Uses **edge weights** (`weight_model: "edge"`). Edge weights represent task dependencies or durations between tasks.

## Metrics

All algorithms track:
- DFS visits
- Edge traversals
- Queue operations (pops/pushes)
- Edge relaxations
- Execution time (nanoseconds/milliseconds)

## Output

### Main Program
- SCC components and sizes
- Condensation graph
- Topological order of components and original vertices
- Shortest distances from source
- Critical path and its length
- Detailed metrics for each algorithm

### Performance Comparison
- Generates `algorithm_comparison.csv` with performance data
- Columns: Dataset, n, m, SCC Time, Topo Time, DAG-SP Time, SCC Visits, DAG Relaxations, SCC Count, SCC Edges

## Documentation

- `PROJECT_REPORT.md` - Detailed analysis and conclusions
- `Comparison_report.txt` - Algorithm performance comparison (if exists)

## Testing

JUnit tests cover:
- SCC correctness on various graph structures
- Topological sort (Kahn and DFS variants)
- Shortest/longest paths
- Edge cases (empty graph, single vertex, etc.)
- Graph loader functionality

All tests are located in `src/test/java/`.

## Notes

- The project builds from a clean clone (run `mvn clean compile`)
- All datasets are provided in `/data/` directory
- Source code is organized in packages as required
- Public classes have Javadoc comments
- Key algorithm steps are commented

