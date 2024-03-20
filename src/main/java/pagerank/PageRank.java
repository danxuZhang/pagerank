package pagerank;

import com.google.common.graph.ValueGraph;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class PageRank {
    static final double default_d = 0.85;
    static final double default_tolerance = 0.000_1;
    static final int default_max_iter = 1_000;
    final double d;
    final double tolerance;
    final int max_iter;

    public PageRank() {
        this(default_d, default_tolerance, default_max_iter);
    }

    public PageRank(double d, double tolerance, int max_iter) {
        this.d = d;
        this.tolerance = tolerance;
        this.max_iter = max_iter;
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

    public double[] calculate(ValueGraph<Integer, Double> graph) {
        final int N = graph.nodes().size();
        List<Double> teleports = new ArrayList<>();
        for (int i = 0; i < N; ++i) {
            teleports.add((double) 1 / N); // assign uniform 1/N for E
        }
        return calculate(graph, teleports);

    }

    public abstract double[] calculate(ValueGraph<Integer, Double> graph, List<Double> E);
}
