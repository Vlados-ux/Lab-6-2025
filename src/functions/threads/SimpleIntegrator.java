package functions.threads;

import functions.Function;
import functions.Functions;

public class SimpleIntegrator implements Runnable {
    private Task task;
    private boolean running = true;

    public SimpleIntegrator(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        try {
            while (running) {
                synchronized (task) {
                    task.wait();

                    if (!running) break;

                    Function func = task.getFunction();
                    double left = task.getLeft();
                    double right = task.getRight();
                    double step = task.getStep();

                    try {
                        double result = Functions.integrate(func, left, right, step);

                        System.out.printf("Result %.2f %.2f %.2f %.2f%n", left, right, step, result);
                    } catch (IllegalArgumentException e) {
                        System.out.printf("Error for %.2f %.2f %.2f: %s%n", left, right, step, e.getMessage());
                    }

                    task.notify();
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Интегратор прерван");
            Thread.currentThread().interrupt();
        }
    }

    public void stop() {
        running = false;
        synchronized (task) {
            task.notify();
        }
    }
}