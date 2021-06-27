package demo.pattern.callback;

/**
 * @author malaka
 * @create 2020-12-08 22:00
 */
public class CallbackDemo { // 回调函数的测试，回调函数不是设计模式，它是 监听器模式的 重要组成部分

//    int a = 32;

    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("我要休息了");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("我被回调了");
            }
        });
        thread.start();
    }
}
