import java.util.Random;
public class Job {
  protected int arrival;
  protected int cpuBurst;
  protected int priority;
  protected int exitTime;
  protected int turnAroundTime;
  protected int remainingTime;

  protected int period = 0;

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


//For jobs with assigned priority
  public Job(int priority){
        this.arrival =  random.nextInt(250) + 1;
        this.cpuBurst = random.nextInt(14) + 2;
        this.priority = priority;
        this.remainingTime = this.arrival;
  }

    public Job(){
        this.arrival = random.nextInt(250) + 1;;
        this.cpuBurst = random.nextInt(14) + 2;
        this.remainingTime = this.arrival;
    }

    @Override
    public String toString() {
      if (priority != 0) {
          return "JOB Arrival: " + String.valueOf(arrival) + " CPU burst: " + String.valueOf(cpuBurst) + "\n";
      }
      else{
          return "JOB Arrival: " + String.valueOf(arrival) + " CPU burst: " + String.valueOf(cpuBurst) + " Priority: " + String.valueOf(priority)+ "\n";
      }
    }
}
