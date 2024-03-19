package pagerank;

import java.util.*;

import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.ValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import org.apache.commons.math3.linear.*;

public class PageRank {
    static final double default_alpha = 0.85;
    static final double default_tolerance = 0.000_1;
    static final int default_max_iter = 1_000;

    final double alpha; // damping factor
    final double tolerance; // condition for convergence
    final int max_iter; // max iterations to execute
    final int N; // total num of nodes
    List<Double> ranks;
    List<Double> teleports; // E vector for teleportation
    final ImmutableValueGraph<Integer, Double> graph;

    public PageRank(ValueGraph<Integer, Double> graph)  {
        this(graph, default_alpha, default_tolerance, default_max_iter);
    }

    public PageRank(ValueGraph<Integer, Double> graph, double alpha, double tolerance, int max_iter) {
        this.alpha = alpha;
        this.max_iter = max_iter;
        this.tolerance = tolerance;
        this.graph = (ImmutableValueGraph<Integer, Double>) graph;
        this.N = this.graph.nodes().size();
        this.ranks = new ArrayList<>();
        this.teleports = new ArrayList<>();
        for (int i = 0; i < N; ++i) {
            ranks.add((double)1 / N);
            teleports.add((double)1 / N); // assign uniform 1/N for E
        }
    }

    /**
     * Calculate page rank in an iterative manner,
     * running until converge or reach max_iter.
     * @return calculated page rank
     */
    public double[] iteratePageRank() {
        for (int it = 0; it < max_iter; ++it) {
            List<Double> new_ranks = getNewRanks();
            if (hasConverged(new_ranks)) {
                break;
            }
            ranks = new_ranks;
        }
        return ranks.stream().mapToDouble(Double::doubleValue).toArray();
    }

    private List<Double> getNewRanks() {
        List<Double> new_ranks = new ArrayList<>(ranks);
        for (int i = 0; i < N; ++i) {
            double rank = 0.0;
            for (int j = 0; j < N; ++j) {
                Optional<Double> val = graph.edgeValue(j, i);
                if (val.isPresent()) {
                    rank += ranks.get(j) / graph.outDegree(j);
                }
            }
            rank = alpha * rank + (1 - alpha) * teleports.get(i);
            new_ranks.set(i, rank);
        }
        // Normalize the new ranks
        double total = new_ranks.stream().mapToDouble(Double::doubleValue).sum();
        new_ranks.replaceAll(val -> val / total);
        return new_ranks;
    }

    private boolean hasConverged(List<Double> new_ranks) {
        boolean converged = true;
        for (int i = 0; i < N; ++i) {
            if (Math.abs(new_ranks.get(i) - ranks.get(i)) > tolerance) {
                converged = false;
                break;
            }
        }
        return converged;
    }

    /**
     * Solve page rank by linear algebra.
     * @return solved page rank
     */
    public double[] solvePageRank() {
        // create teleportation vector E[N]
        double [] tele_arr = teleports.stream().mapToDouble(Double::doubleValue).toArray();
        RealVector E = new ArrayRealVector(tele_arr);

        // create graph matrix M[N][N]
        RealMatrix M = new Array2DRowRealMatrix(N, N);
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                Optional<Double> val = graph.edgeValue(i, j);
                if (val.isPresent()) {
                    M.setEntry(j, i, (double) 1 / graph.outDegree(i));
                }
            }
        }

        RealVector R = solve(M, E, alpha);
        return R.toArray();
    }

    /**
     * A generalized linear algebra solution to page rank
     * @param M Graph Matrix, dimension NxN
     * @param E Teleportation Vector, dimension N
     * @param alpha Damping factor
     * @return Solved page rank
     */
    public static RealVector solve(RealMatrix M, RealVector E, double alpha) {
        assert(M.getColumnDimension() == M.getRowDimension());
        assert(M.getColumnDimension() == E.getDimension());
        assert(alpha > 0 && alpha < 1);
        // dimension M[N][N], E[N]
        int N = M.getColumnDimension();
        // create Identity Matrix[N][N]
        RealMatrix I = MatrixUtils.createRealIdentityMatrix(N);

        // Compute the matrix (I - alpha x M)
        RealMatrix A = I.subtract(M.scalarMultiply(alpha));

        // Compute the vector (1-alpha)E
        RealVector b = E.mapMultiply(1 - alpha);

        // Solve
        DecompositionSolver solver = new LUDecomposition(A).getSolver();
        return solver.solve(b);
    }

    public static void main(String[] args) {
        ImmutableValueGraph<Integer, Double> graph = ValueGraphBuilder
                .directed()
                .allowsSelfLoops(true)
                .<Integer, Double>immutable()
                .putEdgeValue(0, 0, 1.0)
                .putEdgeValue(0, 1, 1.0)
                .putEdgeValue(1, 0, 1.0)
                .putEdgeValue(1, 2, 1.0)
                .putEdgeValue(2, 2, 1.0)
                .build();

        final PageRank pr = new PageRank(graph);
        double[] ranks = pr.solvePageRank();
        System.out.println("Linear system solution: ");
        for (final double val : ranks) {
            System.out.printf("\t%.2f", val);
        }
        System.out.println();
        System.out.println("Iterative solution: ");
        ranks = pr.iteratePageRank();
        for (final double val : ranks) {
            System.out.printf("\t%.2f", val);
        }
        System.out.println();
    }
}
