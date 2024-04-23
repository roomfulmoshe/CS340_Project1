import java.util.*;
import java.text.DecimalFormat;

public class jobScheduler {
    private List<Job> jobs = new ArrayList<>();

    public jobScheduler(List<Job> jobs) {
        //List of 25 'simulated' jobs
        this.jobs = jobs;
    }

    private void displayBar(){
        for(int i =0; i < 625; i++){
            if(i < 10) {
                System.out.print(i + "    ");
            }
            else if(i >= 10 && i < 100){
                System.out.print(i + "   ");
            }
            else{
                System.out.print(i + "  ");
            }
        }
        System.out.println();
        for(int i = 0; i < 625; i++){
            System.out.print("|----");
        }
        System.out.println();
    }


    private void display(int jobID, int cpuBurst) {
        if(jobID >= 0){
            for(int i = 0; i < cpuBurst; i++){
                if(jobID  < 10) {
                    System.out.print("  " + jobID + "  ");
                }
                else{
                    System.out.print(" " + jobID + "  ");
                }
            }
        }
        else{
            for(int i = 0; i < cpuBurst; i++){
                System.out.print("     ");
            }
        }
    }

    //Calculates and displays the ATT and throughPut (throughput is based on number of jobs completed at or before time 150)
    private void displayStats(List<Job> completedJobs){
        int ATT = 0;
        int throughPut = 0;
        for(Job job: jobs){
            ATT+= job.getTurnAroundTime();
            if(job.getExitTime() <= 100){
                throughPut++;
            }
        }
        System.out.println();
        System.out.println("Average Turn Around Time: " + Math.round(  (( (double) ATT / 25 ) *100.0) )/100.0 ) ;
        System.out.println("The throughput at time 100 is: " + throughPut);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //                                      FIFO                                                 //
    ///////////////////////////////////////////////////////////////////////////////////////////////
    public List<Job> Fifo(){
        System.out.println("Hello From FIFO");
        displayBar();
        //Sort Jobs by arrival times
        Collections.sort(jobs, new Comparator<Job>() {
            @Override
            public int compare(Job job1, Job job2) {
                return Integer.compare(job1.getArrival(), job2.getArrival());
            }
        });
        //counts how many jobs are done
        int counter = 0;
        int currentTime = 0;
        Job currentJob = null;
        while (counter < jobs.size()){
            //if no jobs have been selected, get the next arriving job to run
            if (currentJob == null){
                currentJob = jobs.get(counter);
            }
            //check if the current job arrived and then runs the job
            if(currentJob.getArrival() <= currentTime){
                display(currentJob.jobID, currentJob.getCpuBurst());
                currentTime += currentJob.getCpuBurst();
                currentJob.setTurnAroundTime(currentTime - currentJob.getArrival());
                currentJob.setExitTime(currentTime);
                jobs.set(counter, currentJob);
                counter++;
                currentJob = null;
            }
            else {
                //if current job didn't arrive yet then increase time by one unit
                display(-1, 1);
                currentTime++;
            }
        }
        displayStats(jobs);
        System.out.println();
        return jobs;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //                                      SJF                                                  //
    ///////////////////////////////////////////////////////////////////////////////////////////////
    public List<Job> SJF() {
        System.out.println("Hello From SJF");
        displayBar();
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
                display(currentJob.jobID, currentJob.getCpuBurst());
                currentTime += currentJob.getCpuBurst();
                currentJob.setExitTime(currentTime);
                currentJob.setTurnAroundTime(currentTime - currentJob.getArrival());
                completedJobs.add(currentJob);
                counter++;
            }
            else{
                display(-1, 1);
                currentTime++;
            }
        }
        displayStats(completedJobs);
        System.out.println();
        return completedJobs;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //                                      SRT                                                 //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public List<Job> SRT() {
        System.out.println("Hello From SRT");
        displayBar();
        //initially set all remaining times to it's initial CPU burst
        for(Job job: jobs){
            job.setRemainingTime(job.getCpuBurst());
        }
        PriorityQueue<Job> jobQueue = null;
        List<Job> completedJobs = new ArrayList<>();
        //counts how many jobs are done
        int counter = 0;
        int currentTime = 0;
        Job currentJob = null;

        Set<Job> completed = new HashSet<>();
        Comparator<Job> jobComparator = Comparator.comparingInt(Job::getRemainingTime)
                .thenComparingInt(Job::getArrival);

        //while there are still jobs to handle
        while(counter < jobs.size()){
            jobQueue = new PriorityQueue<>(jobComparator);
            for(Job job: jobs){
                //if job is ready to run and it hasn't completed add it to priority queue
                if (job.getArrival() <= currentTime && !completed.contains(job) && job.getRemainingTime() !=0){
                    jobQueue.offer(job);
                }
            }
            if (!jobQueue.isEmpty()){
                currentJob = jobQueue.poll();
                display(currentJob.jobID, 1);
                currentJob.setRemainingTime(currentJob.getRemainingTime() - 1);
                currentTime++;
                if(currentJob.getRemainingTime() == 0){
                    completed.add(currentJob);
                    currentJob.setExitTime(currentTime);
                    currentJob.setTurnAroundTime(currentTime - currentJob.getArrival());
                    completedJobs.add(currentJob);
                    counter++;
                }
            }
            else{
                display(-1,1);
                currentTime++;
            }
        }
        displayStats(completedJobs);
        System.out.println();
        return completedJobs;
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////
    //                                      Highest Priority                                     //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public List<Job> HighestPriority() {
        System.out.println("Hello From Highest Priority");
        displayBar();
        //initially set all remaining times to it's initial CPU burst
        for(Job job: jobs){
            job.setRemainingTime(job.getCpuBurst());
        }
        PriorityQueue<Job> jobQueue = null;
        List<Job> completedJobs = new ArrayList<>();
        //counts how many jobs are done
        int counter = 0;
        int currentTime = 0;
        Job currentJob = null;

        //compare by highest priority then by first arrival
        Set<Job> completed = new HashSet<>();
        Comparator<Job> jobComparator = Comparator.comparingInt(Job::getPriority).reversed()
                .thenComparingInt(Job::getArrival);

        //while there are still jobs to handle
        while(counter < jobs.size()){
            jobQueue = new PriorityQueue<>(jobComparator);
            for(Job job: jobs){
                //if job is ready to run and it hasn't completed add it to priority queue
                if (job.getArrival() <= currentTime && !completed.contains(job) && job.getRemainingTime() !=0){
                    jobQueue.offer(job);
                }
            }

            if (!jobQueue.isEmpty()){
                currentJob = jobQueue.poll();
                currentJob.setRemainingTime(currentJob.getRemainingTime() - 1);
                display(currentJob.jobID, 1);
                currentTime++;
                if(currentJob.getRemainingTime() == 0){
                    completed.add(currentJob);
                    currentJob.setExitTime(currentTime);
                    currentJob.setTurnAroundTime(currentTime - currentJob.getArrival());
                    completedJobs.add(currentJob);
                    counter++;
                }
            }
            else{
                display(-1, 1);
                currentTime++;
            }
        }
        displayStats(completedJobs);
        System.out.println();
        return completedJobs;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //                                      ROUND ROBIN                                           //
    ///////////////////////////////////////////////////////////////////////////////////////////////
    //Parameter CS (Context Switch) set to 0 if there's no context switch
    public List<Job> RR(int CS, int quantam) {
        System.out.println("Hello From Round Robin");
        displayBar();

        //initially set all remaining times to it's initial CPU burst
        for(Job job: jobs){
            job.setRemainingTime(job.getCpuBurst());
        }

        // Create a Queue using LinkedList
        Queue<Job> queue = new LinkedList<>();

        List<Job> completedJobs = new ArrayList<>();
        //counts how many jobs are done
        int counter = 0;
        int currentTime = 0;
        Job currentJob = null;

        Set<Job> completed = new HashSet<>();
        Set<Job> inQ = new HashSet<>();

        //while there are still jobs to handle
        while(counter < jobs.size()){
            for(Job job: jobs){
                //if job is ready to run and it hasn't completed and it's not in the Queue currently
                if (job.getArrival() <= currentTime && !completed.contains(job) && !inQ.contains(job) ){
                    queue.offer(job);
                    inQ.add(job);
                }
            }
            if (!queue.isEmpty()){
                currentJob = queue.poll();
                if(currentJob.getRemainingTime() < quantam){
                    display(currentJob.jobID, currentJob.getRemainingTime());
                    currentTime += currentJob.getRemainingTime();
                    currentJob.setRemainingTime(0);
                    //context switch
                    display(-1, CS);
                    currentTime += CS;
                }
                else {
                    currentJob.setRemainingTime(currentJob.getRemainingTime() - quantam);
                    display(currentJob.jobID, quantam);
                    currentTime+= quantam;
                    display(-1, CS);
                    currentTime+=CS;
                }
                if(currentJob.getRemainingTime() == 0){
                    completed.add(currentJob);
                    inQ.remove(currentJob);
                    currentJob.setExitTime(currentTime);
                    currentJob.setTurnAroundTime(currentTime - currentJob.getArrival());
                    completedJobs.add(currentJob);
                    counter++;
                }
                else{
                    queue.offer(currentJob);
                }
            }
            else{
                //if there's no jobs in the queue just increase the current time by 1
                display(-1, 1);
                currentTime++;
            }
        }
        displayStats(completedJobs);
        System.out.println();
        return completedJobs;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //                                      MAIN                                                 //
    ///////////////////////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) {
        List<Job> jobs = new ArrayList<>();

        // Instantiating 25 Job objects
        for (int i = 0; i < 25; i++) {
            Job job = new Job();
            jobs.add(job);
        }
        jobScheduler scheduler = new jobScheduler(jobs);

        System.out.println("-".repeat(600));
        System.out.println(scheduler.Fifo());
        System.out.println("-".repeat(600));

        System.out.println("-".repeat(600));
        System.out.println(scheduler.SJF());
        System.out.println("-".repeat(600));

        System.out.println("-".repeat(600));
        System.out.println(scheduler.SRT());
        System.out.println("-".repeat(600));

        System.out.println("-".repeat(600));
        System.out.println(scheduler.HighestPriority());
        System.out.println("-".repeat(600));

        System.out.println("-".repeat(600));
        //Round Robin without Context Switch and time quantam of 2
        System.out.println(scheduler.RR(0, 2));
        System.out.println("-".repeat(600));

        System.out.println("-".repeat(600));
        //Round Robin with context switch of 1 time unit and time quantam of 2
        System.out.println(scheduler.RR(1, 2));
        System.out.println("-".repeat(600));
    }
}