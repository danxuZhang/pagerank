#!/usr/bin/env python3
import sys
import networkx as nx

def parse_edge(line):
    """Parse a line containing an edge in the format 'source destination'."""
    parts = line.strip().split()
    if len(parts) < 2:
        return None
    try:
        src = int(parts[0])
        dst = int(parts[1])
        return (src, dst)
    except ValueError:
        return None

def read_edges_from_file(filename, comment_char='#'):
    """Read edges from a file, skipping comment lines."""
    edges = []
    try:
        with open(filename, 'r') as file:
            for line in file:
                # Skip comment lines
                if line.strip() and line.strip()[0] == comment_char:
                    continue
                    
                edge = parse_edge(line)
                if edge:
                    edges.append(edge)
        return edges
    except FileNotFoundError:
        print(f"Error: File '{filename}' not found.")
        sys.exit(1)
    except Exception as e:
        print(f"Error reading file: {e}")
        sys.exit(1)

def main():
    if len(sys.argv) < 2:
        print("Usage: python pagerank.py <edge_file> [damping_factor] [max_iterations] [tolerance]")
        sys.exit(1)
        
    filename = sys.argv[1]
    
    # Default PageRank parameters
    damping_factor = 0.85
    max_iterations = 100
    tolerance = 1.0e-6
    
    # Parse optional command line arguments
    if len(sys.argv) > 2:
        try:
            damping_factor = float(sys.argv[2])
        except ValueError:
            print("Error: damping_factor must be a float")
            sys.exit(1)
            
    if len(sys.argv) > 3:
        try:
            max_iterations = int(sys.argv[3])
        except ValueError:
            print("Error: max_iterations must be an integer")
            sys.exit(1)
            
    if len(sys.argv) > 4:
        try:
            tolerance = float(sys.argv[4])
        except ValueError:
            print("Error: tolerance must be a float")
            sys.exit(1)
    
    # Read edges from file
    edges = read_edges_from_file(filename)
    
    if not edges:
        print("No valid edges found in the file.")
        sys.exit(1)
    
    # Create a directed graph
    G = nx.DiGraph()
    G.add_edges_from(edges)
    
    # Run PageRank
    pagerank = nx.pagerank(G, alpha=damping_factor, max_iter=max_iterations, tol=tolerance)
    
    # Output results in the format "nodeid rank"
    for node, rank in sorted(pagerank.items()):
        print(f"{node} {rank}")

if __name__ == "__main__":
    main()