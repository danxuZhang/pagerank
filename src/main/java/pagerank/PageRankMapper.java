package pagerank;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class PageRankMapper extends Mapper<LongWritable, Text, Text, Text> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws InterruptedException {
        // Your mapping logic here
        // For example, parse each line of input to extract page ID and linked pages
        // Emit (pageID, "linkedPageID1,linkedPageID2,...")
    }
}
