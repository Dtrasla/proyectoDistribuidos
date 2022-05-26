import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/*TODO
*  Hacer lo de registro de usuario e inicio*/

public class SistemaCalidad {
    SistemaCalidad(){}
    private String  usuarioFinal;
    private String  contraFinal;

    public static void main(String[] args) throws Exception {

        SistemaCalidad sis = new SistemaCalidad();

        boolean usuarioCreado = false;
        boolean inicio = false;
        Scanner sc = new Scanner(System.in);

        char op = 'n';
        String contraHash = "";
        do{
            System.out.println("******* *********");
            System.out.println("(1) Crear Usuario");
            System.out.println("(2) Iniciar Sesion");
            System.out.println("");

            op = sc.nextLine().toCharArray()[0];


            switch(op){
                case '1':
                    if(!usuarioCreado){
                        String usuario = "";
                        String contra = "";

                        System.out.println("*******CREACION DE CUENTA*********");
                        System.out.println("ingrese el nombre de usuario");
                        usuario = sc.nextLine();
                        System.out.println("ingrese la contraseña");
                        contra = sc.nextLine();
                        contraHash = generarHash(contra);

                        usuarioCreado = true;
                        sis.usuarioFinal = usuario;
                        sis.contraFinal = contraHash;
                    }
                    else{
                        System.out.println("Ya se ha creado un usuario, no insista");
                    }


                    break;

                case '2':
                    String usuario = "";
                    String contra = "";
                    String comparar = "";
                    System.out.println("*******INICIO DE SESION *********");
                    System.out.println("ingrese el nombre de usuario");
                    usuario = sc.nextLine();
                    System.out.println("ingrese la contraseña");
                    contra = sc.nextLine();
                    comparar = generarHash(contra);
                    if(comparar.equals(sis.contraFinal) && sis.usuarioFinal.equals(usuario)){
                        sis.calidad();
                    }


                    break;

                default:
                    System.out.println("Opcion Invalida");
                    break;

            }



        }while(!inicio);


    }

    public static String generarHash(String contra){

        String passwordToHash = contra;
        String generatedPassword = null;
        try
        {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Add password bytes to digest
            md.update(passwordToHash.getBytes());

            // Get the hash's bytes
            byte[] bytes = md.digest();

            // This bytes[] has bytes in decimal format. Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            // Get complete hashed password in hex format
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        System.out.println(generatedPassword);
        return generatedPassword;
    }



    public void calidad(){
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
