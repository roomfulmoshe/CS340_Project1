import java.util.Random;
public class Job {
  protected int arrival;
  protected int cpuBurst;
  protected int priority;
  protected int exitTime;
  protected int turnAroundTime;
  protected int remainingTime;

  protected int period = 0;

  protected static int numJobs = 0;

  protected int jobID;

    public void setArrival(int arrival) {
        this.arrival = arrival;
    }

    public void setCpuBurst(int cpuBurst) {
        this.cpuBurst = cpuBurst;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setExitTime(int exitTime) {
        this.exitTime = exitTime;
    }

    public void setTurnAroundTime(int turnAroundTime) {
        this.turnAroundTime = turnAroundTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public void setPeriod(int period) {
        this.period = period;
    }


    public int getArrival() {
        return arrival;
    }

    public int getCpuBurst() {
        return cpuBurst;
    }

    public int getPriority() {
        return priority;
    }

    public int getExitTime() {
        return exitTime;
    }

    public int getTurnAroundTime() {
        return turnAroundTime;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public int getPeriod() {
        return period;
    }

    private Random random = new Random();

    public Job(){
        this.arrival = random.nextInt(250) + 1;;
        this.cpuBurst = random.nextInt(14) + 2;
        this.remainingTime = this.cpuBurst;
        this.priority = random.nextInt(5) + 1;
        this.jobID = numJobs;
        numJobs++;
    }

    @Override
    public String toString() {
        return "Job ID:"+ String.valueOf(jobID)+ ", JOB Arrival: " + String.valueOf(arrival) + ", CPU burst: " + String.valueOf(cpuBurst) + ", Turn around Time:" + String.valueOf(turnAroundTime) + ", Exit Time: " + String.valueOf(exitTime) + ", remaining time: " + String.valueOf(remainingTime)+ ", Priority: " + String.valueOf(priority) + "\n\n" ;
    }
}
