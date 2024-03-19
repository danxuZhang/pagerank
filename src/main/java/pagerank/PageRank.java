package pagerank;

import java.util.*;

import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.ValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class PageRank {
    static final double default_alpha = 0.85;
    static final double default_tolerance = 0.000_1;
    static final int default_max_iter = 1_000;

    final double alpha; // damping factor
    final double tolerance; // condition for convergence
    final int max_iter; // max iterations to execute

    public PageRank() {
        this(default_alpha, default_tolerance, default_max_iter);
    }

    public PageRank(double alpha, double tolerance, int max_iter) {
        this.alpha = alpha;
        this.max_iter = max_iter;
        this.tolerance = tolerance;
    }

    /**
     * Calculate page rank in an iterative manner,
     * running until converge or reach max_iter.
     * Uniform teleportation matrix is used.
     *
     * @param graph Graph to calculate
     * @return page rank
     */
    public double[] iteratePageRank(ValueGraph<Integer, Double> graph) {
        final int N = graph.nodes().size();
        List<Double> teleports = new ArrayList<>();
        for (int i = 0; i < N; ++i) {
            teleports.add((double) 1 / N); // assign uniform 1/N for E
        }
        return iteratePageRank(graph, teleports);
    }

    /**
     * Calculate page rank in an iterative manner,
     * running until converge or reach max_iter.
     *
     * @param graph     Graph to calculate
     * @param teleports teleportation vector E
     * @return page rank
     */
    public double[] iteratePageRank(ValueGraph<Integer, Double> graph, List<Double> teleports) {
        final int N = graph.nodes().size();

        RealVector tele = list2vector(teleports);
        RealVector ranks = new ArrayRealVector(N, 1.0 / N);
        for (int it = 0; it < max_iter; ++it) {
            RealVector new_ranks = getNewRanks(graph, ranks, tele);
            if (hasConverged(ranks, new_ranks)) {
                break;
            }
            ranks = new_ranks;
        }
        return ranks.toArray();
    }

    private RealVector getNewRanks(ValueGraph<Integer, Double> graph, RealVector ranks, RealVector teleports) {
        int N = ranks.getDimension();
        RealVector new_ranks = new ArrayRealVector(N);
        for (int i = 0; i < N; ++i) {
            double rank = 0.0;
            for (int j = 0; j < N; ++j) {
                Optional<Double> val = graph.edgeValue(j, i);
                if (val.isPresent()) {
                    rank += ranks.getEntry(j) / graph.outDegree(j);
                }
            }
            rank = alpha * rank + (1 - alpha) * teleports.getEntry(i);
            new_ranks.setEntry(i, rank);
        }
        // Normalize the new ranks
        double sum = new_ranks.getL1Norm();
        return new_ranks.mapDivide(sum);
    }

    private boolean hasConverged(RealVector ranks, RealVector new_ranks) {
        assert (ranks.getDimension() == new_ranks.getDimension());
        return ranks.subtract(new_ranks).getL1Norm() < tolerance;
    }

    /**
     * Solve page rank by linear algebra.
     * Uniform teleportation vector E is used.
     *
     * @param graph Graph to calculate
     * @return page rank
     */
    public double[] solvePageRank(ValueGraph<Integer, Double> graph) {
        final int N = graph.nodes().size();
        List<Double> teleports = new ArrayList<>();
        for (int i = 0; i < N; ++i) {
            teleports.add((double) 1 / N); // assign uniform 1/N for E
        }
        return solvePageRank(graph, teleports);
    }

    /**
     * Solve Page Rank by linear algebra
     *
     * @param graph     Graph to calculate
     * @param teleports Teleportation vector E
     * @return page rank
     */
    public double[] solvePageRank(ValueGraph<Integer, Double> graph, List<Double> teleports) {
        // create teleportation vector E[N]
        RealVector E = list2vector(teleports);

        // create graph matrix M[N][N]
        RealMatrix M = graph2matrix(graph);

        RealVector R = solve(M, E, alpha);
        return R.toArray();
    }

    /**
     * A generalized linear algebra solution to page rank
     *
     * @param M     Graph Matrix, dimension NxN
     * @param E     Teleportation Vector, dimension N
     * @param alpha Damping factor
     * @return Solved page rank
     */
    public static RealVector solve(RealMatrix M, RealVector E, double alpha) {
        assert (M.getColumnDimension() == M.getRowDimension());
        assert (M.getColumnDimension() == E.getDimension());
        assert (alpha > 0 && alpha < 1);
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

    public static RealMatrix graph2matrix(ValueGraph<Integer, Double> graph) {
        final int N = graph.nodes().size();
        RealMatrix M = new Array2DRowRealMatrix(N, N);
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                Optional<Double> val = graph.edgeValue(i, j);
                if (val.isPresent()) {
                    M.setEntry(j, i, (double) 1 / graph.outDegree(i));
                }
            }
        }
        return M;
    }

    public static RealVector list2vector(List<Double> list) {
        return new ArrayRealVector(list.stream().mapToDouble(Double::doubleValue).toArray());
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

        final PageRank pr = new PageRank();
        double[] ranks = pr.solvePageRank(graph);
        System.out.println("Linear system solution: ");
        for (final double val : ranks) {
            System.out.printf("\t%.2f", val);
        }
        System.out.println();
        System.out.println("Iterative solution: ");
        ranks = pr.iteratePageRank(graph);
        for (final double val : ranks) {
            System.out.printf("\t%.2f", val);
        }
        System.out.println();
    }
}
