import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class InClassUnZip {

    static final int size = 31_000_001;
    static final int Threshold = size/256 ;
    static double[] out1;
    static double[] out2;
    protected static  void unZipGather( double[] input) {

        double[] output = new double[input.length];
        double[] output2 = new double[input.length];
        long start = System.nanoTime();
        for (int i = 1; i < input.length; i = i + 2) {
            //int y = index[i];
            output[i] = input[i];

        }
    }
    public static void parallelUnZip(double[]input){
        int N = input.length;
        if(N%2 ==0 ){
             out1 = new double[N/2];
             out2 = new double[N/2];
        } else {
             out1 = new double[(N/2)+1];
             out2 = new double[N/2];
        }
        ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        long start = System.nanoTime();
        pool.invoke(new RecursiveUnZip(0,input.length,input,out1,out2));


    }
    private static boolean isUnZipCorrect(double[]input, double[] out1,double[] out2){
        int n = 0;
        int x;
        for(int i =0; i <out1.length; i++){
            if(out1[i] != input[i*2]){
                return false;
            }
        }
        for(int i=1; i <out2.length; i++){
            if(out2[i] != input[(i*2)+1]){
                return false;
            }
        }
        return true;

    }
    public static void main(String[] args){
        double[] input = new double[size];
        for(int i =0; i < input.length; i++){
            input[i] = (Math.random()*10);
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
        parallelUnZip(input);
        long concurrentTotal = (System.nanoTime()-concurrentStart)/1_000_000;
        System.out.println("Concurrent version Time:" + " "+concurrentTotal+ " "+ "seconds");
         System.out.println(isUnZipCorrect(input,out1,out2));

    }
    public static class RecursiveUnZip extends RecursiveAction {
        int start;
        int end;
        double[] input;
        double [] out1; // even outputs
        double[] out2; // odd outputs
        public RecursiveUnZip(int start, int end, double[] input , double [] out1, double[] out2){
            this.start = start;
            this.end = end;
            this.input=input;
            this.out1 = out1;
            this.out2=out2;
        }
        @Override
        protected void compute() {
            if((end-start) > Threshold){
                for( int i = start; i < end; i++){
                    if(i%2 ==0){
                        out1[i/2] = input[i];
                    }
                    else {
                        out2[i/2] = input[i];
                    }
                }

            }else{
                int middle = (start+end)/2;
                RecursiveUnZip unzipLeft = new RecursiveUnZip(start, middle, input, out1, out2);
                unzipLeft.fork();
                RecursiveUnZip unzipRight = new RecursiveUnZip(middle,end,input,out1,out2);
                unzipRight.compute();
                unzipLeft.join();

            }
        }
    }
}
