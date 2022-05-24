import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

/*TODO
*  Hacer lo de registro de usuario e inicio*/

public class SistemaCalidad {
    public static void main(String[] args) throws Exception {
        try (ZContext context = new ZContext()) {

            ZMQ.Socket pull = context.createSocket(SocketType.PULL);
            pull.bind("tcp://localhost:5560");

            String alerta;
            while (!Thread.currentThread().isInterrupted()) {
                alerta = pull.recvStr();
                System.out.println(alerta);
            }
            pull.close();
        }
    }


}
