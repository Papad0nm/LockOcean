# 🌊 LockOcean – International Shipping Network

LockOcean is a graph-based simulation system that models **international ocean shipping routes**.  
Each port is represented as a **vertex**, and each shipping route as an **edge**.  
The system supports **port and route management**, **shortest route search**, and **network visualization** using the **Breadth-First Search (BFS)** algorithm.

---

## 🚀 Features

| Feature | Description |
|----------|-------------|
| **Port Management** | Add or remove ports dynamically. |
| **Route Management** | Create or delete routes between two ports. |
| **Shortest Path (BFS)** | Find the shortest route between ports using BFS traversal. |
| **View Shipping Routes** | Display all existing ports and connections. |
| **Map Visualization** | Display the full maritime network and highlight the shortest path. |
| **Error Handling** | Input validation and informative error messages. |

---

## 🧮 Graph Representation

The shipping network is represented using an **Adjacency Matrix**, where:
- Each **row** and **column** represents a port.
- A value of `1` means a direct route exists between ports.

Example:
```java
int[][] GraphMatrix = {
    {0,1,1,0,1,1,1,0,0,1,0}, // 0 - Malaysia
    {1,0,0,1,0,0,0,0,0,0,0}, // 1 - Indonesia
    {1,0,0,0,0,0,1,0,0,0,0}, // 2 - Singapore
    ...
};
