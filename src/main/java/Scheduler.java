import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    public static void main(String[] args) {
        List<Job> jobs = new ArrayList<>();

        // Instantiating 20 Job objects
        for (int i = 0; i < 25; i++) {
            Job job = new Job();
            jobs.add(job);
        }
        jobAlgorithm jobsRunner = new jobAlgorithm(jobs);
//        System.out.println(jobsRunner.Fifo());
        System.out.println(jobsRunner.SJF());
    }
}
