import java.util.*;

public class jobScheduler {
    private List<Job> jobs = new ArrayList<>();

    public jobScheduler(List<Job> jobs) {
        //List of 25 'simulated' jobs
        this.jobs = jobs;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //                                      FIFO                                                 //
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
//                System.out.print(String.valueOf(currentJob.jobID) + "****...****");
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

    public List<Job> SRT() {
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
            System.out.println(jobQueue + " Current Time: " + String.valueOf(currentTime));
            if (!jobQueue.isEmpty()){
                currentJob = jobQueue.poll();
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
                currentTime++;
            }
        }

        return completedJobs;
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////
    //                                      Highest Priority                                     //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public List<Job> HighestPriority() {
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
                currentTime++;
            }
        }

        return completedJobs;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //                                      ROUND ROBIN                                           //
    ///////////////////////////////////////////////////////////////////////////////////////////////
    //Parameter CS (Context Switch) set to 0 if there's no context switch
    public List<Job> RR(int CS, int quantam) {
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
                    currentTime += currentJob.getRemainingTime();
                    currentJob.setRemainingTime(0);
                    //context switch
                    currentTime += CS;
                }
                else {
                    currentJob.setRemainingTime(currentJob.getRemainingTime() - quantam);
                    currentTime+= quantam;
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
                currentTime++;
            }
        }

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
        System.out.println(scheduler.Fifo());

//        System.out.println(scheduler.RR(1, 2));
    }
}