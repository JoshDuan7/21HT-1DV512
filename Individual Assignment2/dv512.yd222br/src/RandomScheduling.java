import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Random;

/*
 * File:	RandomScheduling.java
 * Course: 	21HT - Operating Systems - 1DV512
 * Author: 	yd222br (Yuyao Duan)
 * Date: 	December 2021
 */

// TODO: put this source code file into a new Java package with meaningful name (e.g., dv512.YourStudentID)!

// You can implement additional fields and methods in code below, but
// you are not allowed to rename or remove any of it!

// Additionally, please remember that you are not allowed to use any third-party libraries

public class RandomScheduling {

	public static class ScheduledProcess {
		int processId;
		int burstTime;
		int arrivalMoment;
		
		// The total time the process has waited since its arrival
		int totalWaitingTime;
		
		// The total CPU time the process has used so far
		// (when equal to burstTime -> the process is complete!)
		int allocatedCpuTime;

		public ScheduledProcess(int processId, int burstTime, int arrivalMoment) {
			this.processId = processId;
			this.burstTime = burstTime;
			this.arrivalMoment = arrivalMoment;
		}
		
		// ... add further fields and methods, if necessary
	}
		
	// Random number generator that must be used for the simulation
	Random rng;

	// ... add further fields and methods, if necessary
	int ticks = 0;
	List<ScheduledProcess> processRegistry = new ArrayList<>();

	public RandomScheduling(long rngSeed) {
		this.rng = new Random(rngSeed);
	}
	
	public void reset() {
		ticks = 0;
		processRegistry = new ArrayList<>();
	}
	
	public void runNewSimulation(final boolean isPreemptive, final int timeQuantum,
	    final int numProcesses,
		final int minBurstTime, final int maxBurstTime,
		final int maxArrivalsPerTick, final double probArrival) {
		reset();

		boolean runSimulation = true;
		int completedProcessCounter = 0;
		int id = 0;
		int quantumCounter = 0;
		List<ScheduledProcess> scheduledProcessList = new ArrayList<>();
		ScheduledProcess processForRun = null;

		while(runSimulation) {

			// Determine if it is preemptive mode
			if(isPreemptive==true) {
				quantumCounter++;
				//enable interrupt
				if(quantumCounter==timeQuantum) {
					quantumCounter=0;
					processForRun=null;
				}
			}

			//According to the possibility to add new processes to CPU
			if(rng.nextDouble()<probArrival) {
				//incoming processes can be one or two
				int incomingProcessNum = rng.nextInt(1,maxArrivalsPerTick+1);
				for(int i = 1; i <= incomingProcessNum; i++) {
					if(processRegistry.size()<numProcesses) {
						ScheduledProcess process = new ScheduledProcess(id,rng.nextInt(minBurstTime,maxBurstTime+1),ticks);
						scheduledProcessList.add(process);
						processRegistry.add(process);
						id++;
					}
				}
			}
			ticks++;

			//Simulating CPU randomly pick a process to run
			if(processForRun==null && scheduledProcessList.size()>0) {
//				int index = (int)(Math.random()*scheduledProcessList.size());
//				System.out.println(index);
        processForRun = scheduledProcessList.get(rng.nextInt(scheduledProcessList.size()));
			}

			// Calculating allocated CPU time, if used CPU time equals burst time then it will
			// be removed from scheduled list, otherwise it will continue
			if(processForRun!=null) {
				processForRun.allocatedCpuTime++;
				if(processForRun.allocatedCpuTime == processForRun.burstTime) {
					scheduledProcessList.remove(processForRun);
					processForRun = null;
					completedProcessCounter++;
				}
			}

			//Calculating waiting time
			for (ScheduledProcess p : scheduledProcessList) {
				if (p != processForRun) {
					p.totalWaitingTime++;
				}
			}

			//Determine continue the simulation or not
			if(completedProcessCounter>=numProcesses) {
				runSimulation = false;
			}
			
		}
		// TODO:
		// 1. Run a simulation as a loop, with one iteration considered as one unit of time (tick)
		// 2. The simulation should involve the provided number of processes "numProcesses"
		// 3. The simulation loop should finish after the all of the processes have completed
		// 4. On each tick, a new process might arrive with the given probability (chance)
		// 5. However, the max number of new processes per tick is limited
		// by the given value "maxArrivalsPerTick"
		// 6. The burst time of the new process is chosen randomly in the interval
		// between the values "minBurstTime" and "maxBurstTime" (inclusive)

		// 7. When the CPU is idle and no process is running, the scheduler
		// should pick one of the available processes *at random* and start its execution
		// 8. If the preemptive version of the scheduling algorithm is invoked, then it should 
		// allow up to "timeQuantum" time units (loop iterations) to the process,
		// and then preempt the process (pause its execution and return it to the queue)
		
		// If necessary, add additional fields (and methods) to this class to
		// accomodate your solution

		// Note: for all of the random number generation purposes in this method,
		// use "this.rng" !
	}

	//Below codes are used for section 3.3.2
	//public static Double averageTicks = 0.0;
	//public static Double averageWaitingTime = 0.0;

	public void printResults() {
		// TODO:
		// 1. For each process, print its ID, burst time, arrival time, and total waiting time
		// 2. Afterwards, print the complete execution time of the simulation
		// and the average process waiting time
		double sumWaitingTime = 0;
		System.out.println("---------------------------------------------------------------------");
		Formatter fmt = new Formatter();
		fmt.format("%15s %15s %15s %20s\n", "Process ID", "Burst Time", "Arrival Time", "Total Waiting Time");
		for(ScheduledProcess p : processRegistry) {
			fmt.format("%10s %15s %15s %20s\n",p.processId,p.burstTime,p.arrivalMoment,p.totalWaitingTime);
			sumWaitingTime += p.totalWaitingTime;
		}

		System.out.print(fmt);
		System.out.println("---------------------------------------------------------------------");
		System.out.println("Total Ticks in this simulation: " + ticks);
		System.out.println("Average waiting time is: " + sumWaitingTime / processRegistry.size());

		//below codes are used for section 3.3.2
		//averageTicks+=ticks;
		//averageWaitingTime += (sumWaitingTime / processesRegistry.size());
	}

	public static void main(String args[]) {
		// TODO: replace the seed value below with your birth date, e.g., "20001001"
		final long rngSeed = 19921104; //29550455, 16590675, 13929885 these seeds are used for report section 3.3.2.1
		
		
		// Do not modify the code below — instead, complete the implementation
		// of other methods!
		RandomScheduling scheduler = new RandomScheduling(rngSeed);
		
		final int numSimulations = 5;
		
		final int numProcesses = 10;
		final int minBurstTime = 2;
		final int maxBurstTime = 10;
		final int maxArrivalsPerTick = 2;
		final double probArrival = 0.75;
		
		final int timeQuantum = 2;

		boolean[] preemptionOptions = {false, true}; //To simulate 10000 times, please set here either 'false' or 'true'

		for (boolean isPreemptive: preemptionOptions) {

			for (int i = 0; i < numSimulations; i++) {
				System.out.println("Running " + ((isPreemptive) ? "preemptive" : "non-preemptive")
					+ " simulation #" + i);

				scheduler.runNewSimulation(
					isPreemptive, timeQuantum,
					numProcesses,
					minBurstTime, maxBurstTime,
					maxArrivalsPerTick, probArrival);

				System.out.println("Simulation results:"
					+ "\n" + "----------------------");	
				scheduler.printResults();

				System.out.println("\n");
			}

			//below codes are used for section 3.3.2
			//System.out.println("Non-preemtive average ticks of 10000 simulations: " + averageTicks/10000);
			//System.out.println("Non-preemtive average waiting time of 10000 simulations: " + averageWaitingTime/10000);
			//System.out.println("Preemtive average ticks of 10000 simulations: " + averageTicks/10000);
			//System.out.println("Preemtive average waiting time of 10000 simulations: " + averageWaitingTime/10000);
		}		
		
	}
	
}