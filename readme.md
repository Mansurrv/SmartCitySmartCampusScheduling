# Assignment 4: Smart Campus Scheduling Analysis

## The analysis file is in [readme.md](src/main/java/org/example/readme.md)

My project implements task dependency analysis for the Smart Campus/City planning system. The main goal is to process cyclic dependencies, prioritize tasks, and determine the critical path (the longest) to minimize the overall project completion time.

## Agorithm used:

1. Strongly Connected Components: Tarjan's Algorithm for detecting and compressing cyclic dependencies.
2. Topological Sort: Kahn's Algorithm for ordering components in a directed acyclic graph.
3. Shortest/Longest Paths in DAG: Dynamic programming in topological order for linear path search.

## Project Launch Instructions

### Requirements

1. Java Development Kit (JDK) 17+
2. Apache Maven

### Clone repository

```
git clone <URL>
cd <project name>
```

### Project Assembly

Build the project using Maven. This will download the necessary dependencies (e.g., Jackson for JSON parsing) and compile the source code.

```
mvn clean install
```

### Launch Analysis

The main logic for launching is located in the Main class. This program will sequentially process all 9 data files from the /data/ directory, measure metrics, and save the results to a CSV file.

```
mvn exec:java -Dexec.mainClass="org.example.Main"
```

### Result

After successful completion, the following file will be created in the project root folder: <br>
- results/results.csv: Contains all performance metrics (time, operation counters) and key results (critical path length, path itself) for all 9 generated graphs.

### Running Tests

```
mvn test
```

### Repository Structure

```
SmartCitySmartCampusScheduling/
├── src/main/java/
│   ├── org/example/
│   │   ├── algorithm/
│   │   │   ├── dag/        
│   │   │   ├── kahn/       
│   │   │   └── tarjan/     
│   │   └── Main.java       
├── src/test/java/          
├── data/                   
├── results/                
├── pom.xml                 
└── README.md               
```