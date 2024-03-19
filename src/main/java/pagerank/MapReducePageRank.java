package pagerank;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.google.common.graph.ValueGraph;

import java.util.List;

public class MapReducePageRank extends PageRank{
    public MapReducePageRank() {
        super();
    }
    public MapReducePageRank(double alpha, double tolerance, int max_iter) {
        super(alpha, tolerance, max_iter);
    }

    @Override
    public double[] calculate(ValueGraph<Integer, Double> graph, List<Double> E) {
        return new double[0];
    }
}
