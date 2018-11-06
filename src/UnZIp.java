import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class UnZIp {
    protected final static int size = 10;
    protected final static int Threshold = 5;

    /**
     * Through splitting up the pool to run with
     * each thread doing a separate portion I did notice some speed up
     * From thread to thread.
     * I have to find a better way to split up the data to notice even better Speed up between the Serial Version and the
     * Parallel Version.
     * I tested the code with arrays starting from size 10 to 1_000_000
     * I can resubmit my code when and if I can find a better way to split up the work after I come and talk to you
     */
    protected static void poolUnZip(double[] input){
        ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        UnZip unZip = new UnZip(0,input);
        pool.invoke(unZip);
        UnZip unZip2 = new UnZip(1,input);
        pool.invoke(unZip2);
    }




    protected static  void unZipGather( double[] input){

        double[] output = new double[input.length];
        double[] output2 = new double[input.length];
        long start = System.nanoTime();
        for(int i = 1; i < input.length; i=i+2){
           //int y = index[i];
            output[i] = input[i];

        }
        for(int i = 0; i < input.length; i=i+2){
            output2[i] = input[i];
        }
       long end = System.nanoTime();
        long total = (end-start)/1_000_000;
        System.out.println("Serial Version Took:" +" "+total+" "+"seconds");
        System.out.println("Thw Two unZipped Arrays are:");
        System.out.println("First Array:");
        for(double d: output){
            System.out.println(d + ",");
        }
        System.out.println("Second Array:");
        for(double x : output2){
            System.out.println(x +",");
        }
    }

    public static void main(String[] args){
        double[] input = new double[size];
        for(int i =0; i < input.length; i++){
            input[i] = (Math.random());
        }

         System.out.println("Original Array initialized");
         long serialStart = System.nanoTime();
        unZipGather(input);
        long serialTotal = (System.nanoTime()-serialStart)/1_000_000;
        System.out.println("Serial version Time:" + " "+serialTotal + " "+ "seconds");

//        for(double x : input){
//            System.out.println(x+',');
//        }
//        System.out.println("Zipped pattern of the array:" +"\n");
//        unZipGather(input);
        long concurrentStart = System.nanoTime();
        poolUnZip(input);
        long concurrentTotal = (System.nanoTime()-concurrentStart)/1_000_000;
        System.out.println("Serial version Time:" + " "+concurrentTotal+ " "+ "seconds");


    }

    protected static class UnZip extends RecursiveAction{
         double[] input;
         double[] output1;

         int i;
         int m;
        public UnZip(int i, double[] input){
            this.input = input;
            output1 = new double[input.length];
            this.i = i;
            m = i+2;


         }

        @Override
        protected void compute() {
            if(input.length == 1){
                output1[i] = input[i];
            } else{
                long start = System.nanoTime();
                for(int i = this.i; i < input.length; i=i+2){
                    output1[i] = input[i];
                }
                long end = System.nanoTime();
                long total = (end-start)/100_000;
                System.out.println("Took:" +" "+ total +" "+ "seconds");
            }

//          System.out.println("Unzipped Array:");
//            for(double d : output1){
//                System.out.println(d+',');
//            }
       }
    }
}