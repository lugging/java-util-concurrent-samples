package cn.hykes.concurrent.BlockingQueue;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Desc:
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/12/2
 */
public class DelayQueueTest {

    public static class Task implements Delayed {
        /**
         * 到期时间
         */
        private final long time;

        /**
         * 业务参数
         */
        private final String name;

        public Task(long timeout, String name, long createTime) {
            this.time = createTime + TimeUnit.NANOSECONDS.convert(timeout, TimeUnit.NANOSECONDS);
            this.name = name;
        }

        /**
         * 返回与此对象相关的剩余延迟时间，以给定的时间单位表示
         * 过期时间 - 当前时间
         */
        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(this.time - System.nanoTime() , TimeUnit.NANOSECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            // TODO Auto-generated method stub
            if(o == null || ! (o instanceof Task)) return 1;
            if(o == this) return 0;
            Task s = (Task) o;
            if (this.time > s.time) {
                return 1;
            }else if (this.time == s.time) {
                return 0;
            }else {
                return -1;
            }
        }
    }

    public static void main(String[] args) throws Exception{
        BlockingQueue<Task> delayQueue = new DelayQueue();

        // 为了方便演示，设置创建时间是同一个时间
        long createTime = System.nanoTime();
        for(int i=0;i<5;i++){
            // 创建时间相同，则超时时间就是输出顺序
            int timeout = new Random().nextInt(120);
            delayQueue.add(new Task(timeout, String.valueOf(i), createTime));
            System.out.println(String.format("new task:(%d), timeout:(%d)", i, timeout));
        }

        do{
            Task task = delayQueue.poll();
            if (task != null) {
                System.out.println(String.format("run task:(%s)", task.name));
            }
        }while (delayQueue.size()!= 0);
    }

}
