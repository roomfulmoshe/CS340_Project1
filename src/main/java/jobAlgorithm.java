import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class jobAlgorithm {
    private List<Job> jobs = new ArrayList<>();

    public jobAlgorithm(List<Job> jobs) {
        //List of 25 'simulated' jobs
        this.jobs = jobs;
    }

    public List<Job> Fifo(){
        System.out.println("Hello from Fifo");
        //Sort Jobs by arrival times
        Collections.sort(jobs, new Comparator<Job>() {
            @Override
            public int compare(Job job1, Job job2) {
                return Integer.compare(job1.getArrival(), job2.getArrival());
            }
        });

        int counter = 0;
        int currentTime = 1;
        Job currentJob = null;
        while (counter < jobs.size()){
            if (currentJob == null){
                currentJob = jobs.get(counter);
            }
            if(currentJob.arrival <= currentTime){
                currentTime += currentJob.cpuBurst;
                currentJob.setTurnAroundTime(currentTime - currentJob.arrival);
                currentJob.setExitTime(currentTime);
                jobs.set(counter, currentJob);
                System.out.println(currentJob.toString());
                counter++;
                currentJob = null;
                continue;
            }
            currentTime++;
        }
        return jobs;
    }
}