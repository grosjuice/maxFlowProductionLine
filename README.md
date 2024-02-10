# Production Line Max Flow Analyzer

This Java project implements the Edmonds-Karp algorithm to analyze and find the maximum flow in a production line. Unlike traditional max flow problems where capacities are assigned to edges, this project introduces the concept of vertex capacities. Each vertex in the production line represents a stage or process, and its capacity denotes the maximum flow it can handle. The program identifies bottlenecks in the production line and is inspired by a research paper on the subject.

## Project Overview

### Classes

1. **Main.java:** The main class handles command-line arguments, file input/output, and orchestrates the execution of the Edmonds-Karp algorithm specific to the production line scenario.

2. **Edge.java:** The `Edge` class represents an edge in the flow network. It encapsulates attributes such as the source vertex (`u`), target vertex (`v`), capacity, and flow. This class provides methods for manipulating capacity, obtaining flow information, and performing operations related to flow.

3. **FlowNetwork.java:** The `FlowNetwork` class represents the production line flow network. It contains methods for reading the input file, running the Edmonds-Karp algorithm, and generating detailed output. The class utilizes the `Edge` class to model individual edges in the network.

### Usage

To run the program, provide two command-line arguments:

```bash
java com.villeneuve.justin.Main input.txt output.txt
