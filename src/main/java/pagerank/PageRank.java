package pagerank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.ValueGraph;
import com.google.common.graph.ValueGraphBuilder;


public class PageRank {
    static final double default_alpha = 0.85;
    static final double default_tolerance = 0.000_1;
    static final int default_max_iter = 1_000;

    final double alpha; // damping factor
    final double tolerance; // condition for convergence
    final int max_iter; // max iterations to execute
    final int N; // total num of nodes
    List<Double> ranks;
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
        for (int i = 0; i < N; ++i) {
            ranks.add((double)1 / N);
        }
    }


    public void calcPageRank() {
        for (int it = 0; it < max_iter; ++it) {
            System.out.printf("Iteration %d: ", it);
            for (double val : ranks) {
                System.out.printf("%.2f\t", val);
            }
            System.out.print("\n");
            List<Double> new_ranks = getNewRanks();
            if (hasConverged(new_ranks)) {
                break;
            }
            ranks = new_ranks;
        }
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
            rank = alpha * rank + (1 - alpha) / N;
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

    public List<Double> getRank() {
        return Collections.unmodifiableList(ranks);
    }

    public static void main(String[] args) {
        ImmutableValueGraph<Integer, Double> graph = ValueGraphBuilder
                .directed()
                .allowsSelfLoops(true)
                .<Integer, Double>immutable()
                .putEdgeValue(0, 0, 0.5)
                .putEdgeValue(0, 1, 0.5)
                .putEdgeValue(1, 0, 0.5)
                .putEdgeValue(1, 2, 0.5)
                .putEdgeValue(2, 1, 1.0)
                .build();

        final PageRank pr = new PageRank(graph);
        pr.calcPageRank();
        List<Double> ranks = pr.getRank();
        for (final double val : ranks) {
            System.out.printf("\t%.2f", val);
        }
    }
}
