package wait_notify;

import sun.awt.windows.ThemeReader;

public class WaitNotifyAll {
    private final Object mon = new Object();
    private char currentLetter = 'A';

    public static void main(String[] args) {
        WaitNotifyAll waitNotifyAll = new WaitNotifyAll();
        new Thread(() -> {
            waitNotifyAll.printA();
        }).start();
        new Thread(() -> {
            waitNotifyAll.printB();
        }).start();
        new Thread(() -> {
            waitNotifyAll.printC();
        }).start();
    }

    public void printA() {
        synchronized (mon) {
            try {
                for (int i = 0; i < 50; i++) {
                    while (currentLetter != 'A') {
                        mon.wait();
                    }
                    System.out.print('A');
                    currentLetter = 'B';
                    Thread.sleep(1000);
                    mon.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void printB() {
        synchronized (mon) {
            try {
                for (int i = 0; i < 50; i++) {
                    while (currentLetter != 'B') {
                        mon.wait();
                    }
                    System.out.print('B');
                    currentLetter = 'C';
                    Thread.sleep(1000);
                    mon.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void printC() {
        synchronized (mon) {
            try {
                for (int i = 0; i < 50; i++) {
                    while (currentLetter != 'C') {
                        mon.wait();
                    }
                    System.out.print('C');
                    currentLetter = 'A';
                    Thread.sleep(1000);
                    mon.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
