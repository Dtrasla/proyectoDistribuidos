import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

public class Publisher
{
    public static void main(String[] args) throws Exception
    {
		try (ZContext context = new ZContext()){
			//Socket PUB
			ZMQ.Socket publisher = context.createSocket(SocketType.PUB);
			//Lectura de argumentos
			String tipo = args[0];
			String tiempo = args[1];
			String archivo = "src\\main\\java\\" + args[2];
			switch (tipo){
				case "t":
					publisher.bind("tcp://*:5556");
					break;
				case "p":
					publisher.bind("tcp://*:5557");
					break;
				case "o":
					publisher.bind("tcp://*:5558");
					break;
			}
			int t = Integer.parseInt(tiempo);
			//Lectura de archivo
			double correcto = 0, frango = 0, err = 0;
			try {
				File myObj = new File(archivo);
				Scanner myReader = new Scanner(myObj);
				for(int i = 0; myReader.hasNextLine(); i++){
					if(i == 0)
						correcto = myReader.nextDouble();
					else if(i == 1)
						frango = myReader.nextDouble();
					else
						err = myReader.nextDouble();
				}
				myReader.close();
			} catch (FileNotFoundException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}
			correcto = correcto * 10;
			frango = frango * 10;
			err = err*10;
			System.out.println(correcto + " " + frango + " " + err);
			//Números random
			Random srandom = new Random(System.currentTimeMillis());
			while(!Thread.currentThread().isInterrupted())
			{
				int probabilidad;
				double valor = 0;
				probabilidad = srandom.nextInt(10) + 1;
				if(probabilidad <= (int) correcto ){
					switch (tipo){
						case "t":
							valor = (double)srandom.nextInt(89 - 68 + 1) + 68;
							System.out.println(valor);
							break;
						case "p":
							valor = srandom.nextDouble() + (double)srandom.nextInt(8-6)+6;
							System.out.println(valor);
							break;
						case "o":
							valor = (double)srandom.nextInt(11-2) + 2;
							System.out.println(valor);
							break;
					}
				}
				else if( probabilidad <= (int)(correcto + frango)){
					switch (tipo){
						case "t":
							valor = (double)srandom.nextInt(89 - 68 + 1) + 68;
							valor = valor-30;
							System.out.println(valor);
							break;
						case "p":
							valor = srandom.nextDouble() + (double)srandom.nextInt(8-6)+6;
							valor = valor + 6;
							System.out.println(valor);
							break;
						case "o":
							valor = (double)srandom.nextInt(11-2) + 2;
							valor = valor+11;
							System.out.println(valor);
							break;
					}
				}
				else{
					switch (tipo){
						case "t":
							valor = (double)srandom.nextInt(89 - 68 + 1) + 68;
							valor = valor*-1;
							System.out.println(valor);
							break;
						case "p":
							valor = srandom.nextDouble() + (double)srandom.nextInt(8-6)+6;
							valor = valor*-1;
							System.out.println(valor);
							break;
						case "o":
							valor = (double)srandom.nextInt(11-2) + 2;
							valor = valor*-1;
							System.out.println(valor);
							break;
					}
				}
				String update = String.format(
						"%s %2.1f ", tipo, valor
				);
				System.out.println(update);
				publisher.send(update,0);
				Thread.sleep(t*1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}