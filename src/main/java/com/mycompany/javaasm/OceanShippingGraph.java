package com.mycompany.javaasm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class OceanShippingGraph {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Graph graph = new Graph();

        // Predefined ports in display order
        String[] portNames = {
                "Malaysia", "Indonesia", "Singapore", "Philippines", "Thailand",
                "Vietnam", "China", "Japan", "South Korea", "India", "Bangladesh"
        };
        
        // Store the original port order for consistent display
        List<String> originalPortOrder = new ArrayList<>(Arrays.asList(portNames));

        int[][] GraphMatrix = {
                { 0, 1, 1, 0, 1, 1, 1, 0, 0, 1, 0 },
                { 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
                { 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0 },
                { 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0 },
                { 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0 },
                { 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
                { 1, 0, 1, 1, 1, 0, 0, 1, 1, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0 },
                { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0 }
        };

        // Build initial graph
        for (String port : portNames) graph.addPort(port);
        for (int i = 0; i < GraphMatrix.length; i++)
            for (int j = i + 1; j < GraphMatrix[i].length; j++)
                if (GraphMatrix[i][j] == 1)
                    graph.addRoute(portNames[i], portNames[j]);

        System.out.println("===============================================================");
        System.out.println("Welcome to International Ocean Shipping System!");
        System.out.println("===============================================================");
        System.out.println("Staff ID: SHIP01");

        // Helper method to get ports in display order
        java.util.function.Supplier<List<String>> getPortsInDisplayOrder = () -> {
            List<String> allPorts = graph.getPorts();
            List<String> displayOrder = new ArrayList<>();
            
            // Add original ports in their defined order
            for (String originalPort : originalPortOrder) {
                if (allPorts.contains(originalPort)) {
                    displayOrder.add(originalPort);
                }
            }
            
            // Add any new ports that weren't in the original list
            for (String newPort : allPorts) {
                if (!originalPortOrder.contains(newPort)) {
                    displayOrder.add(newPort);
                }
            }
            
            return displayOrder;
        };

        while (true) {
            System.out.println("\nMain Menu (Press '0' to exit)");
            System.out.println("---------------------------------------------------------------");
            System.out.println("1. Create/Update Shipping Network");
            System.out.println("2. Search for a Port");
            System.out.println("3. View the Shipping Routes");
            System.out.println("4. Find Shortest Route (BFS)");
            System.out.println("5. Display Map (overall)");
            System.out.println("0. Exit");
            System.out.print("\nSelection: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    boolean updateGraph = true;
                    while (updateGraph) {
                        System.out.println("\nUpdate Network Menu");
                        System.out.println("---------------------");
                        System.out.println("1. Add a port");
                        System.out.println("2. Remove a port");
                        System.out.println("3. Add a route");
                        System.out.println("4. Remove a route");
                        System.out.println("5. Return to main menu");
                        System.out.print("\nSelection: ");
                        int subChoice = sc.nextInt();
                        sc.nextLine();

                        switch (subChoice) {
                            case 1: // Add port
                                String port;
                                String continueChoice;
                                do {
                                    System.out.print("Enter the name of the port: ");
                                    port = sc.nextLine().trim();
                                    
                                    // Input validation (Not Empty String)
                                    if (port.isEmpty()) {
                                        System.out.println("Error: Port name cannot be empty. Please try again.");
                                        
                                        // Validate continue input
                                        do {
                                            System.out.print("Continue? Y/N: ");
                                            continueChoice = sc.nextLine().trim();
                                            if (!continueChoice.equalsIgnoreCase("Y") && !continueChoice.equalsIgnoreCase("N")) {
                                                System.out.println("Invalid input. Please enter Y or N.");
                                            }
                                        } while (!continueChoice.equalsIgnoreCase("Y") && !continueChoice.equalsIgnoreCase("N"));
                                        
                                        if (continueChoice.equalsIgnoreCase("N")) break;
                                        continue;
                                    }
                                    
                                    graph.addPort(port);
                                    
                                    // Validate continue input
                                    do {
                                        System.out.print("Continue? Y/N: ");
                                        continueChoice = sc.nextLine().trim();
                                        if (!continueChoice.equalsIgnoreCase("Y") && !continueChoice.equalsIgnoreCase("N")) {
                                            System.out.println("Invalid input. Please enter Y or N.");
                                        }
                                    } while (!continueChoice.equalsIgnoreCase("Y") && !continueChoice.equalsIgnoreCase("N"));
                                    
                                } while (continueChoice.equalsIgnoreCase("Y"));
                                break;
                                
                            case 2: // Remove port
                                System.out.print("Enter the port to remove: ");
                                graph.removePort(sc.nextLine());
                                break;
                                
                            case 3: // Add route
                                List<String> displayOrder = getPortsInDisplayOrder.get();
                                if (displayOrder.size() < 2) {
                                    System.out.println("Need at least 2 ports.");
                                    break;
                                }
                                
                                System.out.println("Available ports:");
                                for (int i = 0; i < displayOrder.size(); i++)
                                    System.out.println((i + 1) + ". " + displayOrder.get(i));

                                int idx1 = -1, idx2 = -1;
                                while (true) {
                                    System.out.print("Select first port by number: ");
                                    String in1 = sc.nextLine();
                                    try { 
                                        idx1 = Integer.parseInt(in1) - 1; 
                                        if (idx1 >= 0 && idx1 < displayOrder.size()) {
                                            break;
                                        } else {
                                            System.out.println("Invalid selection. Please choose a number between 1 and " + displayOrder.size() + ".");
                                        }
                                    } 
                                    catch (Exception e){ System.out.println("Invalid number. Please enter a valid number."); }
                                }
                                while (true) {
                                    System.out.print("Select second port by number: ");
                                    String in2 = sc.nextLine();
                                    try { 
                                        idx2 = Integer.parseInt(in2) - 1; 
                                        if (idx2 >= 0 && idx2 < displayOrder.size()) {
                                            break;
                                        } else {
                                            System.out.println("Invalid selection. Please choose a number between 1 and " + displayOrder.size() + ".");
                                        }
                                    } 
                                    catch (Exception e){ System.out.println("Invalid number. Please enter a valid number."); }
                                }
                                if (idx1 == idx2) System.out.println("Cannot connect same port.");
                                else graph.addRoute(displayOrder.get(idx1), displayOrder.get(idx2));
                                break;
                                
                            case 4: // Remove route
                                System.out.print("Enter first port: ");
                                String r1 = sc.nextLine();
                                System.out.print("Enter second port: ");
                                String r2 = sc.nextLine();
                                graph.removeRoute(r1, r2);
                                break;
                                
                            case 5:
                                updateGraph = false;
                                break;
                                
                            default:
                                System.out.println("Invalid choice. Please enter a number between 0 and 5.");
                        }
                    }
                    break;

                case 2: // Search port
                    System.out.print("Enter port name to search: ");
                    graph.searchPort(sc.nextLine());
                    break;

                case 3: // View routes
                    System.out.println("\n--- Shipping Network ---");
                    List<String> displayOrder = getPortsInDisplayOrder.get();
                    for (String p : displayOrder)
                        System.out.println(p + " -> " + graph.getNeighbors(p));
                    break;

                case 4: // BFS
                    List<String> portsForBfs = getPortsInDisplayOrder.get();
                    if (portsForBfs.size() < 2) {
                        System.out.println("Need at least 2 ports.");
                        break;
                    }

                    System.out.println("Available ports:");
                    for (int i = 0; i < portsForBfs.size(); i++)
                        System.out.println((i + 1) + ". " + portsForBfs.get(i));

                    int sIdx = -1, eIdx = -1;
                    while (true) {
                        System.out.print("Select start port by number: ");
                        String inS = sc.nextLine();
                        try { 
                            sIdx = Integer.parseInt(inS) - 1; 
                            if (sIdx >= 0 && sIdx < portsForBfs.size()) {
                                break;
                            } else {
                                System.out.println("Invalid selection. Please choose a number between 1 and " + portsForBfs.size() + ".");
                            }
                        } 
                        catch (Exception e){ System.out.println("Invalid number. Please enter a valid number."); }
                    }
                    while (true) {
                        System.out.print("Select destination port by number: ");
                        String inE = sc.nextLine();
                        try { 
                            eIdx = Integer.parseInt(inE) - 1; 
                            if (eIdx >= 0 && eIdx < portsForBfs.size()) {
                                break;
                            } else {
                                System.out.println("Invalid selection. Please choose a number between 1 and " + portsForBfs.size() + ".");
                            }
                        } 
                        catch (Exception e){ System.out.println("Invalid number. Please enter a valid number."); }
                    }

                    String start = portsForBfs.get(sIdx);
                    String end = portsForBfs.get(eIdx);
                    List<String> path = graph.shortestRouteBfs(start, end);

                    if (path.isEmpty()) System.out.println("No route found.");
                    else {
                        System.out.println("Shortest route: " + String.join(" -> ", path));
                        System.out.println("Hops: " + (path.size() - 1));
                        DisplayMap.showWithBfs(graph, path);
                    }
                    break;

                case 5: // Display overall map
                    DisplayMap.show(graph);
                    break;

                case 0: // Exit
                    System.out.println("Exiting system. Thank you!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please enter a number between 0 and 5.");
            }
        }
    }
}
