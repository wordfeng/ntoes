package org.feng.async.performance;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.nio.ByteBuffer;
import java.util.concurrent.ThreadFactory;

public class DisruptorTest {
    public static void main(String[] args) throws Exception {
        // 队列中的元素  生产者生产的对象
        class Element {

            private int value;

            public int get() {
                return value;
            }

            public void set(int value) {
                this.value = value;
            }

        }

        // 生产者的线程工厂
        ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "simpleThread");
            }
        };

        // RingBuffer生产工厂,初始化RingBuffer的时候使用
        EventFactory<Element> factory = new EventFactory<Element>() {
            @Override
            public Element newInstance() {
                return new Element();
            }
        };

        // 处理Event的handler  消费Element
        EventHandler<Element> handler = new EventHandler<Element>() {
            @Override
            public void onEvent(Element element, long sequence, boolean endOfBatch) {
//                System.out.println(Thread.currentThread().getName());
                System.out.println("Element: " + element.get());
            }
        };

        // 阻塞策略
        BlockingWaitStrategy strategy = new BlockingWaitStrategy();

        // 指定RingBuffer的大小 2的n次方
        int bufferSize = 16;

        // 创建disruptor，采用单生产者模式
        Disruptor<Element> disruptor = new Disruptor<>(factory, bufferSize, threadFactory, ProducerType.SINGLE, strategy);
//        Disruptor<Element> disruptor = new Disruptor<>(Element::new, bufferSize, threadFactory);

        // 设置EventHandler
        disruptor.handleEventsWith(handler);

        // 启动disruptor的线程
        disruptor.start();

        RingBuffer<Element> ringBuffer = disruptor.getRingBuffer();

        // 生产Element
//        ByteBuffer bb = ByteBuffer.allocate(8);
        for (int l = 0; true; l++) {
            // 获取下一个可用位置的下标
            long sequence = ringBuffer.next();
            try {
                // 返回可用位置的元素
                Element event = ringBuffer.get(sequence);
                // 设置该位置元素的值
                event.set(l);
            } catch (Exception ignore) {
            } finally {
                ringBuffer.publish(sequence);
            }
            Thread.sleep(100);
//            bb.putInt(0, l);
//            ringBuffer.publishEvent((event, sequence, buffer) -> event.set(buffer.getInt(0)), bb);
        }
    }
}
