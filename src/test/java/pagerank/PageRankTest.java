package pagerank;

import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PageRankTest {
    PageRank pr1;
    PageRank pr2;
    PageRank pr3;
    double[] expected1;
    double[] expected2;
    double[] expected3;
    @Before
    public void setUp() {
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
        this.pr1 = new PageRank(graph1, 1.0, 0.000_1, 1_000);
        this.expected1 = new double[]{0.4, 0.4, 0.2};

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
        this.pr2 =new PageRank(graph2, 0.8, 0.000_1, 1_000);
        expected2 = new double[]{7.0 / 33, 5.0 / 33, 21.0 / 33};

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
        this.pr3 = new PageRank(graph3, 0.8, 0.000_1, 1_000);
        expected3 = new double[] {15.0 / 148, 19.0 / 148, 95.0 / 148, 19.0 / 148};
    }

    @Test
    public void testPageRankIterative1() {
        final double tolerance = 0.001;
        final double[] ranks = pr1.iteratePageRank();
        for (int i = 0; i < expected1.length; ++i) {
            assertEquals(ranks[i], expected1[i], tolerance);
        }
    }

    @Test
    public void testPageRankIterative2() {
        final double tolerance = 0.001;
        final double[] ranks = pr2.iteratePageRank();
        for (int i = 0; i < expected2.length; ++i) {
            assertEquals(ranks[i], expected2[i], tolerance);
        }
    }

    @Test
    public void testPageRankIterative3() {
        final double tolerance = 0.001;
        final double[] ranks = pr3.iteratePageRank();
        for (int i = 0; i < expected3.length; ++i) {
            assertEquals(ranks[i], expected3[i], tolerance);
        }
    }


    @Test
    public void testPageRankSolve2() {
        final double tolerance = 0.0001;
        final double[] ranks = pr2.solvePageRank();
        for (int i = 0; i < expected2.length; ++i) {
            assertEquals(ranks[i], expected2[i], tolerance);
        }
    }

    @Test
    public void testPageRankSolve3() {
        final double tolerance = 0.0001;
        final double[] ranks = pr3.solvePageRank();
        for (int i = 0; i < expected3.length; ++i) {
            assertEquals(ranks[i], expected3[i], tolerance);
        }
    }
}