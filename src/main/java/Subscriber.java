import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import java.util.StringTokenizer;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;
import zmq.ZError;

public class Subscriber {
	public static final int MIN_PH = 6;
	public static final int MAX_PH = 8;
	public static final int MIN_TMP = 68;
	public static final int MAX_TMP = 89;
	public static final int MIN_OX = 2;
	public static final int MAX_OX = 11;

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

			ZMQ.Socket alertas = context.createSocket(SocketType.PUSH);
			alertas.connect("tcp://localhost:5560");



			for (update_nbr = 0; update_nbr < 10000; update_nbr++) {
				String string = subscriber.recvStr().trim();
				System.out.println(string);
				StringTokenizer sscanf = new StringTokenizer(string, " ");
				String tp = String.valueOf(sscanf.nextToken());
				double valor = Double.valueOf(sscanf.nextToken());


				if(valor >= 0){
					String ruta = "src/main/resources/bd.txt";
					try{
						FileWriter fstream = new FileWriter(ruta,true);
						BufferedWriter out = new BufferedWriter(fstream);
						out.write(string + "\n");
						out.close();
					}
					catch (ZError.IOException e){
						System.out.println("Ha ocurrido un error. ");
						e.printStackTrace();
					}
				}
				if(valor<0){}


				else if(tp.equals("p")  && (valor <= MIN_PH || valor > MAX_PH) ) {
					alertas.send(string);
				}

				else if(tp.equals("o")  && (valor < MIN_OX || valor > MAX_OX) ) {
					alertas.send(string);
				}

				else if(tp.equals("t")  && (valor < MIN_TMP || valor > MAX_TMP) ) {
					alertas.send(string);
				}

			}
		}
	}
}
