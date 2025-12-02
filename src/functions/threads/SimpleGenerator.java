package functions.threads;

import functions.Function;
import functions.basic.Log;

public class SimpleGenerator implements Runnable {
    private Task task;
    private boolean running = true;

    public SimpleGenerator(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < task.getTasksCount() && running; i++) {
                synchronized (task) {
                    double base = 1 + Math.random() * 9;
                    Log logFunc = new Log(base);

                    double left = Math.random() * 100;
                    double right = 100 + Math.random() * 100;
                    double step = Math.random();

                    task.setFunction(logFunc);
                    task.setLeft(left);
                    task.setRight(right);
                    task.setStep(step);

                    System.out.printf("Source %.2f %.2f %.2f%n", left, right, step);

                    task.notify();

                    if (i < task.getTasksCount() - 1) {
                        task.wait();
                    }
                }

                Thread.sleep(1);
            }

            synchronized (task) {
                running = false;
                task.notify();
            }

        } catch (InterruptedException e) {
            System.out.println("Генератор прерван");
            Thread.currentThread().interrupt();
        }
    }

    public void stop() {
        running = false;
    }
}