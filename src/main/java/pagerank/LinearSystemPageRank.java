package pagerank;

import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.ValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.List;

public class LinearSystemPageRank extends PageRank {
    public LinearSystemPageRank() {
        super();
    }

    public LinearSystemPageRank(double d, double tolerance, int max_iter) {
        super(d, tolerance, max_iter);
    }

    /**
     * A generalized linear algebra solution to page rank
     *
     * @param M     Graph Matrix, dimension NxN
     * @param E     Teleportation Vector, dimension N
     * @param d     Damping factor
     * @return Solved page rank
     */
    public static RealVector solve(RealMatrix M, RealVector E, double d) {
        assert (M.getColumnDimension() == M.getRowDimension());
        assert (M.getColumnDimension() == E.getDimension());
        assert (d > 0 && d < 1);
        // dimension M[N][N], E[N]
        int N = M.getColumnDimension();
        // create Identity Matrix[N][N]
        RealMatrix I = MatrixUtils.createRealIdentityMatrix(N);

        // Compute the matrix (I - d x M)
        RealMatrix A = I.subtract(M.scalarMultiply(d));

        // Compute the vector (1-d)E
        RealVector b = E.mapMultiply(1 - d);

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

        final LinearSystemPageRank pr = new LinearSystemPageRank();
        double[] ranks = pr.calculate(graph);
        System.out.println("Linear system solution: ");
        for (final double val : ranks) {
            System.out.printf("\t%.2f", val);
        }
        System.out.println();
    }

    @Override
    public double[] calculate(ValueGraph<Integer, Double> graph, List<Double> teleports) {
        // create teleportation vector E[N]
        RealVector E = list2vector(teleports);

        // create graph matrix M[N][N]
        RealMatrix M = graph2matrix(graph);

        RealVector R = solve(M, E, d);
        return R.toArray();
    }
}
