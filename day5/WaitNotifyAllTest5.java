package day5;

public class WaitNotifyAllTest5 {

    public static void main(String[] args) {
        // 创建饭菜对象
        KitChenRoom chenRoom = new KitChenRoom();
        // 创建小明妈妈线程，做饭
        new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                chenRoom.cook();
            }
        },"小明妈妈线程：").start();
        // 爸爸线程：做饭
        new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                chenRoom.cook();
            }
        },"小明爸爸线程：").start();
        // 创建小明自己线程，吃饭
        new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                chenRoom.eat();
            }
        },"小明自己线程：").start();
        // 大明线程：吃饭
        new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                chenRoom.eat();
            }
        },"大明哥哥线程：").start();
    }

    public static class KitChenRoom {
        // 是否有吃的
        private boolean hasFood = false;
        // 设置同步锁，做饭和吃饭只能同时有一个在执行，不能边做边吃
        private final Object lock = new Object();
        // 做饭
        public void cook() {
            // 加锁
            synchronized (lock) {
                // 如果有吃的，就不做饭
                while(hasFood) {
                    // 还有吃的，先不做饭
                    try {
                        lock.wait(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                // 否则就做饭，
                System.out.println(Thread.currentThread().getName() + "没吃的了，给娃做饭！");
                // 做好之后，修改为true
                hasFood = true;
                // 通知其他线程吃饭
                lock.notify();
            }
        }

        // 吃饭
        public void eat() {
            synchronized (lock) {
                // 如果没吃的，就喊妈妈做饭，暂时吃不了
                while (!hasFood) {
                    try {
                        lock.wait(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                // 否则就吃饭
                System.out.println(Thread.currentThread().getName() + "感谢老妈，恰饭，恰饭");
                // 吃完之后，修改为false
                hasFood = false;
                // 通知其他线程吃饭
                lock.notify();
            }
        }
    }
}

