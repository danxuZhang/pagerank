package pagerank;

import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PageRankTest {
    final double[] expected1 = {0.4, 0.4, 0.2};
    final double[] expected2 = {7.0 / 33, 5.0 / 33, 21.0 / 33};
    final double[] expected3 = {15.0 / 148, 19.0 / 148, 95.0 / 148, 19.0 / 148};
    final ImmutableValueGraph<Integer, Double> graph1 = ValueGraphBuilder
            .directed()
            .allowsSelfLoops(true)
            .<Integer, Double>immutable()
            .putEdgeValue(0, 0, 1.0)
            .putEdgeValue(0, 1, 1.0)
            .putEdgeValue(1, 0, 1.0)
            .putEdgeValue(1, 2, 1.0)
            .putEdgeValue(2, 1, 1.0)
            .build();
    final ImmutableValueGraph<Integer, Double> graph2 = ValueGraphBuilder
            .directed()
            .allowsSelfLoops(true)
            .<Integer, Double>immutable()
            .putEdgeValue(0, 0, 1.0)
            .putEdgeValue(0, 1, 1.0)
            .putEdgeValue(1, 0, 1.0)
            .putEdgeValue(1, 2, 1.0)
            .putEdgeValue(2, 2, 1.0)
            .build();
    final ImmutableValueGraph<Integer, Double> graph3 = ValueGraphBuilder
            .directed()
            .allowsSelfLoops(true)
            .<Integer, Double>immutable()
            .putEdgeValue(0, 1, 1.0)
            .putEdgeValue(0, 2, 1.0)
            .putEdgeValue(0, 3, 1.0)
            .putEdgeValue(1, 0, 1.0)
            .putEdgeValue(1, 3, 1.0)
            .putEdgeValue(2, 2, 1.0)
            .putEdgeValue(3, 1, 1.0)
            .putEdgeValue(3, 2, 1.0)
            .build();

    @Before
    public void setUp() {
    }

    @Test
    public void testPageRankIterative1() {
        PageRank pr1 = new IterativePageRank(1.0, 0.000_1, 1_000);
        final double tolerance = 0.001;
        final double[] ranks = pr1.calculate(graph1);
        for (int i = 0; i < expected1.length; ++i) {
            assertEquals(ranks[i], expected1[i], tolerance);
        }
    }

    @Test
    public void testPageRankIterative2() {
        PageRank pr2 = new IterativePageRank(0.8, 0.000_1, 1_000);
        final double tolerance = 0.001;
        final double[] ranks = pr2.calculate(graph2);
        for (int i = 0; i < expected2.length; ++i) {
            assertEquals(ranks[i], expected2[i], tolerance);
        }
    }

    @Test
    public void testPageRankIterative3() {
        PageRank pr3 = new IterativePageRank(0.8, 0.000_1, 1_000);
        final double tolerance = 0.001;
        final double[] ranks = pr3.calculate(graph3);
        for (int i = 0; i < expected3.length; ++i) {
            assertEquals(ranks[i], expected3[i], tolerance);
        }
    }


    @Test
    public void testPageRankSolve2() {
        PageRank pr2 = new LinearSystemPageRank(0.8, 0.000_1, 1_000);
        final double tolerance = 0.0001;
        final double[] ranks = pr2.calculate(graph2);
        for (int i = 0; i < expected2.length; ++i) {
            assertEquals(ranks[i], expected2[i], tolerance);
        }
    }

    @Test
    public void testPageRankSolve3() {
        PageRank pr3 = new LinearSystemPageRank(0.8, 0.000_1, 1_000);
        final double tolerance = 0.0001;
        final double[] ranks = pr3.calculate(graph3);
        for (int i = 0; i < expected3.length; ++i) {
            assertEquals(ranks[i], expected3[i], tolerance);
        }
    }
}