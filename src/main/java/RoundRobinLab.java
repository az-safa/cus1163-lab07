import java.util.*;

public class RoundRobinLab {

    // Represents an individual process in the system
    static class Process {
        int id;
        int arrivalTime;
        int burstTime;
        int remainingTime;
        int completionTime;
        int turnaroundTime;
        int waitingTime;

        public Process(int id, int arrivalTime, int burstTime) {
            this.id = id;
            this.arrivalTime = arrivalTime;
            this.burstTime = burstTime;
            // Initially, the remaining time is exactly equal to the total burst time needed
            this.remainingTime = burstTime; 
        }
    }

    /**
     * Simulates the Round Robin scheduling algorithm.
     * Manages the ready queue, executes processes in time slices.
     * Calculates the final completion times for each process.
     */
    public static void scheduleRoundRobin(List<Process> processes, int timeQuantum) {
        // Keeps track of the total simulated system time
        int currentTime = 0;

        // Initialize the Ready Queue
        ArrayList<Process> readyQueue = new ArrayList<>();
        for (Process p : processes) {
            readyQueue.add(p);
        }

        // Core Scheduling Loop
        while (!readyQueue.isEmpty()) {
            
            // Get the process at the front of the line
            Process current = readyQueue.remove(0);
            
            // Calculate how long this process gets to run this turn.
            int executeTime = Math.min(timeQuantum, current.remainingTime);
            
            // Move the system clock forward by the amount of time the process ran
            currentTime += executeTime;
            
            // Deduct the time spent running from the process's total required time
            current.remainingTime -= executeTime;
            
            // Handle Process State After Execution
            if (current.remainingTime > 0) {
                // The process still needs more CPU time to finish.
                // Move it to the back of the queue to wait for its next turn.
                readyQueue.add(current);
            } else {
                // The process has completely finished its execution.
                // Record the exact system time it finished.
                current.completionTime = currentTime;
            }
        }

        // Calculate Final Metrics
        for (Process p : processes) {
            // Turnaround Time: Total time from arrival to completion
            p.turnaroundTime = p.completionTime - p.arrivalTime;
            
            // Waiting Time: Total time spent just sitting in the queue (Turnaround minus actual run time)
            p.waitingTime = p.turnaroundTime - p.burstTime;
        }
    }

    /**
     * Calculate and display metrics (FULLY PROVIDED)
     */
    public static void calculateMetrics(List<Process> processes, int timeQuantum) {
        System.out.println("========================================");
        System.out.println("Round Robin Scheduling Simulator");
        System.out.println("========================================\n");
        System.out.println("Time Quantum: " + timeQuantum + "ms");
        System.out.println("----------------------------------------");
        System.out.println("Process | Arrival | Burst | Completion | Turnaround | Waiting");

        double totalTurnaround = 0;
        double totalWaiting = 0;

        for (Process p : processes) {
            System.out.printf("   %d    |    %d    |   %d   |     %d     |     %d     |    %d\n",
                    p.id, p.arrivalTime, p.burstTime, p.completionTime,
                    p.turnaroundTime, p.waitingTime);
            totalTurnaround += p.turnaroundTime;
            totalWaiting += p.waitingTime;
        }

        System.out.println();
        System.out.printf("Average Turnaround Time: %.2fms\n", totalTurnaround / processes.size());
        System.out.printf("Average Waiting Time: %.2fms\n", totalWaiting / processes.size());
        System.out.println("========================================\n\n");
    }

    /**
     * Main method (FULLY PROVIDED)
     */
    public static void main(String[] args) {
        List<Process> processes1 = new ArrayList<>();
        processes1.add(new Process(1, 0, 7));
        processes1.add(new Process(2, 0, 4));
        processes1.add(new Process(3, 0, 2));

        scheduleRoundRobin(processes1, 3);
        calculateMetrics(processes1, 3);

        List<Process> processes2 = new ArrayList<>();
        processes2.add(new Process(1, 0, 7));
        processes2.add(new Process(2, 0, 4));
        processes2.add(new Process(3, 0, 2));

        scheduleRoundRobin(processes2, 5);
        calculateMetrics(processes2, 5);
    }
}
