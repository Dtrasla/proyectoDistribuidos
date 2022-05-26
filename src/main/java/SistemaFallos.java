import java.io.*;

public class SistemaFallos {

    SistemaFallos(){}

    public static void main(String[] args){
        SistemaFallos sistema = new SistemaFallos();
        try{
            sistema.compilar();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Runnable coso = new Monitor("p");
        Thread cc = new Thread(coso);

        cc.start();
    }

    private void compilar() throws IOException {
        ProcessBuilder pb = new ProcessBuilder("javac", "Subscriber.java");
//        pb.redirectError();
        pb.directory(new File("src"));
        Process p = pb.start();
    }

    private static ProcessBuilder iniciarMonitor(String tipo){



        String archivo = "Subscriber";
//        String[] args = new String[2];
//        args[0] = archivo;
//        args[1] = tipo;
        ProcessBuilder monitor = new ProcessBuilder("java", "Subscriber", tipo);
        monitor.directory(new File("src"));

        return monitor;
    }

    public static class Monitor implements Runnable{
        String tipo;

        public Monitor(String tipo){
            this.tipo = tipo;
        }



        public void run(){
            ProcessBuilder monitor = new ProcessBuilder("java", "Subscriber.java", tipo);

            while(!Thread.currentThread().isInterrupted()){

                try {
                    Process mon = monitor.start();
                    Long pid = mon.pid();
                    System.out.println(pid);
                    mon.waitFor();
                    InputStream error = mon.getErrorStream();
                    InputStreamReader isrerror = new InputStreamReader(error);
                    BufferedReader bre = new BufferedReader(isrerror);
                    String linee;
                    while ((linee = bre.readLine()) != null) {
                        System.out.println(linee);
                    }

                    System.out.printf(
                            "\nEl monitor de con PID: %d terminó inesperadamente\n", pid
                    );


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }

    }
}



