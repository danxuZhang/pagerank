package pagerank;

import com.google.common.graph.ValueGraph;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import java.util.List;
import java.util.Optional;

public class IterativePageRank extends PageRank {
    public IterativePageRank() {
        super();
    }

    public IterativePageRank(double d, double tolerance, int max_iter) {
        super(d, tolerance, max_iter);
    }

    @Override
    public double[] calculate(ValueGraph<Integer, Double> graph, List<Double> teleports) {
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
            rank = d * rank + (1 - d) * teleports.getEntry(i);
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
}
