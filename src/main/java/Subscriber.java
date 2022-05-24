import java.nio.charset.StandardCharsets;
import java.util.Random;

import java.util.StringTokenizer;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

public class Subscriber {
	public static void main(String[] args) throws Exception {
		try (ZContext context = new ZContext()) {
			String tipo = args[0];
			System.out.println("Collecting updates from Sensor server");
			// Socket SUB
			ZMQ.Socket subscriber = context.createSocket(SocketType.SUB);
			// Socket conectado al puerto
			switch (tipo){
				case "t":
					subscriber.connect("tcp://localhost:5556");
					break;
				case "p":
					subscriber.connect("tcp://localhost:5557");
					break;
				case "o":
					subscriber.connect("tcp://localhost:5558");
					break;
			}
			String filter = tipo;
			subscriber.subscribe(filter.getBytes(ZMQ.CHARSET));
			int update_nbr;
			long total_temp = 0;
			for (update_nbr = 0; update_nbr < 100; update_nbr++) {
				String string = subscriber.recvStr().trim();
				System.out.println(string);
				StringTokenizer sscanf = new StringTokenizer(string, " ");
				String tp = String.valueOf(sscanf.nextToken());
				double valor = Double.valueOf(sscanf.nextToken());
			}
		}
	}
}
