import java.util.*;

public class jobScheduler {
    private List<Job> jobs = new ArrayList<>();

    public jobScheduler(List<Job> jobs) {
        //List of 25 'simulated' jobs
        this.jobs = jobs;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //                                      FIFO                                                  //
    ///////////////////////////////////////////////////////////////////////////////////////////////
    public List<Job> Fifo(){
        System.out.println("Hello from Fifo");
        //Sort Jobs by arrival times
        Collections.sort(jobs, new Comparator<Job>() {
            @Override
            public int compare(Job job1, Job job2) {
                return Integer.compare(job1.getArrival(), job2.getArrival());
            }
        });
        //counts how many jobs are done
        int counter = 0;
        int currentTime = 1;
        Job currentJob = null;
        while (counter < jobs.size()){
            //if no jobs have been selected, get the next arriving job to run
            if (currentJob == null){
                currentJob = jobs.get(counter);
            }
            //check if the current job arrived and then runs the job
            if(currentJob.getArrival() <= currentTime){
                currentTime += currentJob.getCpuBurst();
                currentJob.setTurnAroundTime(currentTime - currentJob.getArrival());
                currentJob.setExitTime(currentTime);
                jobs.set(counter, currentJob);
                counter++;
                currentJob = null;
            }
            else {
                //if current job didn't arrive yet then increase time by one unit
                currentTime++;
            }
        }
        return jobs;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //                                      SJF                                                  //
    ///////////////////////////////////////////////////////////////////////////////////////////////
    public List<Job> SJF() {
        System.out.print("Hello From SJF");
        PriorityQueue<Job> jobQueue = null;
        List<Job> completedJobs = new ArrayList<>();
        //counts how many jobs are done
        int counter = 0;
        int currentTime = 0;
        Job currentJob = null;

        Set<Job> completed = new HashSet<>();
        Comparator<Job> jobComparator = Comparator.comparingInt(Job::getCpuBurst)
                .thenComparingInt(Job::getArrival);

        //while there are still jobs to handle
        while(counter < jobs.size()){
            jobQueue = new PriorityQueue<>(jobComparator);
            for(Job job: jobs){
                //if job is ready to run and it hasn't went yet
                if (job.getArrival() <= currentTime && !completed.contains(job)){
                    jobQueue.offer(job);
                }
            }
            if (!jobQueue.isEmpty()){
                currentJob = jobQueue.poll();
                completed.add(currentJob);
                currentTime += currentJob.getCpuBurst();
                currentJob.setExitTime(currentTime);
                currentJob.setTurnAroundTime(currentTime - currentJob.getArrival());
                completedJobs.add(currentJob);
                counter++;
            }
            else{
                currentTime++;
            }
        }

        return completedJobs;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //                                      SRT                                                 //
    ///////////////////////////////////////////////////////////////////////////////////////////////









    ///////////////////////////////////////////////////////////////////////////////////////////////
    //                                      MAIN                                                 //
    ///////////////////////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) {
        List<Job> jobs = new ArrayList<>();

        // Instantiating 20 Job objects
        for (int i = 0; i < 25; i++) {
            Job job = new Job();
            jobs.add(job);
        }
        jobScheduler scheduler = new jobScheduler(jobs);
//        System.out.println(jobsRunner.Fifo());
        System.out.println(scheduler.SJF());
    }




}