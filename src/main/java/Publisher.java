import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.Scanner;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;
import org.zeromq.ZMQException;

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
			boolean ocupado = false;
			switch (tipo){
				case "t":
					try{
						publisher.bind("tcp://26.240.17.231:5554");

					}catch (ZMQException e){
						ocupado = true;

					}
					finally{
						if(ocupado){
							publisher.bind("tcp://26.240.17.231:5555");
						}
					}


					//publisher.bind("tcp://26.240.17.231:5556");
					break;
				case "p":
					try{
						publisher.bind("tcp://26.240.17.231:5556");

					}catch (ZMQException e){
						ocupado = true;

					}
					finally{
						if(ocupado){
							publisher.bind("tcp://26.240.17.231:5557");
						}
					}
				case "o":
					try{
						publisher.bind("tcp://26.240.17.231:5558");

					}catch (ZMQException e){
						ocupado = true;

					}
					finally{
						if(ocupado){
							publisher.bind("tcp://26.240.17.231:5559");
						}
					}
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
//					System.out.println("Correcto " + probabilidad);
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
//					System.out.println("Fuera " + probabilidad);
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
//					System.out.println("Error " + probabilidad);

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
				String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date());
				String update = String.format(
						"%s %2.1f %s", tipo, valor, timeStamp
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