import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * File:	MultithreadedService.java
 * Course: 	21HT - Operating Systems - 1DV512
 * Author: 	yd222br (Yuyao Duan)
 * Date: 	January 2022
 */

// TODO: put this source code file into a new Java package with meaningful name (e.g., dv512.YourStudentID)!

// You can implement additional fields and methods in code below, but
// you are not allowed to rename or remove any of it!

// Additionally, please remember that you are not allowed to use any third-party libraries

public class MultithreadedService {

    // TODO: implement a nested public class titled Task here
    // which must have an integer ID and specified burst time (duration) in milliseconds,
    // see below
    // Add further fields and methods to it, if necessary
    // As the task is being executed for the specified burst time, 
    // it is expected to simply go to sleep every X milliseconds (specified below)
    public static class Task extends Thread {
        int taskId;
        long burstTime;
        long sleepTimeMs;
        long beginTime;
        long endTime;
        boolean isTaskCompleted = false;
        long allocatedCpuTime = 0;

        public Task(int taskId, long burstTime, long sleepTimeMs) {
            this.taskId = taskId;
            this.burstTime = burstTime;
            this.sleepTimeMs = sleepTimeMs;
        }

        public int getTaskId() {
            return taskId;
        }

        public long getBurstTime() {
            return burstTime;
        }

        public long getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(long beginTime) {
            this.beginTime = beginTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public long getAllocatedCpuTime() {
            return allocatedCpuTime;
        }

    }

    // Random number generator that must be used for the simulation
	  Random rng;

    // ... add further fields, methods, and even classes, if necessary

    //List for containing the task objects
    List<Task> taskList = new ArrayList<>();

	public MultithreadedService (long rngSeed) {
        this.rng = new Random(rngSeed);
    }


	public void reset() {
		// TODO - remove any information from the previous simulation, if necessary
      taskList = new ArrayList<>();
    }
    

    // If the implementation requires your code to throw some exceptions, 
    // you are allowed to add those to the signature of this method
    public void runNewSimulation(final long totalSimulationTimeMs,
        final int numThreads, final int numTasks,
        final long minBurstTimeMs, final long maxBurstTimeMs, final long sleepTimeMs) {
        reset();

        //Set up system start time and system end time
        long systemStartTime = System.currentTimeMillis();
        long systemEndTime = System.currentTimeMillis();

        //Set number of threads according to the parameter
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < numTasks; i++) {
            long burstTime = rng.nextLong(minBurstTimeMs,maxBurstTimeMs+1);

            //Create tasks and then add to the list
            taskList.add(new Task(i, burstTime, sleepTimeMs){
                @Override
                public void run() {
                    setBeginTime(System.currentTimeMillis());
                    try {
                        while (getAllocatedCpuTime() < getBurstTime()) {
                            Thread.sleep(sleepTimeMs);
                            allocatedCpuTime += sleepTimeMs;
                        }
                        if (allocatedCpuTime >= burstTime) {
                            isTaskCompleted = true;
                            setEndTime(System.currentTimeMillis());
                        }
                    } catch (InterruptedException e) {
                    }
                }
            });
            //Send tasks for running
            executor.submit(taskList.get(i));
        }

        //Using while loop to simulate the 15 secs program running
        while ((systemEndTime - systemStartTime) < totalSimulationTimeMs) {
            systemEndTime = System.currentTimeMillis();
            // if system running time more than 15 secs
            // then interrupt running tasks
            if ((systemEndTime - systemStartTime) >= totalSimulationTimeMs) {
                executor.shutdownNow();
            }
        }


        // TODO:
        // 1. Run the simulation for the specified time, totalSimulationTimeMs
        // 2. While the simulation is running, use a fixed thread pool with numThreads
        // (see https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/Executors.html#newFixedThreadPool(int) )
        // to execute Tasks (implement the respective class, see above!)
        // 3. The total maximum number of tasks is numTasks, 
        // and each task has a burst time (duration) selected randomly
        // between minBurstTimeMs and maxBurstTimeMs (inclusive)
        // 4. The implementation should assign sequential task IDs to the created tasks (0, 1, 2...)
        // and it should assign them to threads in the same sequence (rather any other scheduling approach)
        // 5. When the simulation time is up, it should make sure to stop all of the currently executing
        // and waiting threads!

    }


    public void printResults() {
        // TODO:
        // 1. For each *completed* task, print its ID, burst time (duration),
        // its start time (moment since the start of the simulation), and finish time
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS");
        Formatter fmt = new Formatter();
        fmt.format("%1s\n","Completed tasks:");
        fmt.format("%1s %15s %30s %35s\n", "Task ID", "Burst Time", "Start Time(Hour:Min:Sec:Ms)", "Finish Time(Hour:Min:Sec:Ms)");
        for(int i = 0; i < taskList.size(); i++) {
            Task task = taskList.get(i);
            Date dateBegin = new Date(task.getBeginTime());
            Date dateEnd = new Date(task.getEndTime());
            if (task.isTaskCompleted) {
                fmt.format("%5s %15s %25s %35s\n",
                        task.getTaskId(),task.getBurstTime(),dateFormat.format(dateBegin),dateFormat.format(dateEnd));
            }

        }

        // 2. Afterwards, print the list of tasks IDs for the tasks which were currently
        // executing when the simulation was finished/interrupted
        fmt.format("%1s\n","Interrupted tasks:");
        fmt.format("%1s %15s %30s\n", "Task ID", "Burst Time", "Start Time(Hour:Min:Sec:Ms)");
        for(int i = 0; i < taskList.size(); i++) {
            Task task = taskList.get(i);
            Date dateBegin = new Date(task.getBeginTime());
            if (task.allocatedCpuTime > 0 && task.allocatedCpuTime < task.burstTime) {
                fmt.format("%5s %15s %25s\n",
                        task.getTaskId(),task.getBurstTime(),dateFormat.format(dateBegin));
            }
        }

        // 3. Finally, print the list of tasks IDs for the tasks which were waiting for execution,
        // but were never started as the simulation was finished/interrupted
        fmt.format("%1s\n", "Waiting tasks:");
        fmt.format("%1s %15s\n", "Task ID", "Burst Time");
        for(int i = 0; i < taskList.size(); i++) {
            Task task = taskList.get(i);
            if (task.allocatedCpuTime == 0) {
                fmt.format("%5s %15s\n",task.getTaskId(),task.getBurstTime());
            }
        }
        System.out.println(fmt);
	}




    // If the implementation requires your code to throw some exceptions, 
    // you are allowed to add those to the signature of this method
    public static void main(String args[]) {
		// TODO: replace the seed value below with your birth date, e.g., "20001001"
		final long rngSeed = 19921104;
				
        // Do not modify the code below — instead, complete the implementation
        // of other methods!
        MultithreadedService service = new MultithreadedService(rngSeed);
        
        final int numSimulations = 3;
        final long totalSimulationTimeMs = 15*1000L; // 15 seconds
        
        final int numThreads = 4;
        final int numTasks = 30;
        final long minBurstTimeMs = 1*1000L; // 1 second  
        final long maxBurstTimeMs = 10*1000L; // 10 seconds
        final long sleepTimeMs = 100L; // 100 ms

        for (int i = 0; i < numSimulations; i++) {
            System.out.println("Running simulation #" + i);

            service.runNewSimulation(totalSimulationTimeMs,
                numThreads, numTasks,
                minBurstTimeMs, maxBurstTimeMs, sleepTimeMs);

            System.out.println("Simulation results:"
					+ "\n" + "----------------------");	
            service.printResults();

            System.out.println("\n");
        }

        System.out.println("----------------------");
        System.out.println("Exiting...");
        
        // If your program has not completed after the message printed above,
        // it means that some threads are not properly stopped! -> this issue will affect the grade
    }
}
