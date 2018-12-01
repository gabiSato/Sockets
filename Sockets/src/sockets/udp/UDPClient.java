package sockets.udp;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {

	static String IV = "AAAAAAAAAAAAAAAA";
	static String chaveencriptacao = "0123456789abcdef";

	public static void main(String args[]) throws Exception {
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName("localhost");
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];
		while(true){
			System.out.println("Cliente preparado para enviar: ");
			//Lê entrada do usuário
			String sentence = inFromUser.readLine();
			//criptografa a mensagem
            		sendData = encrypt(sentence, chaveencriptacao);
			//Cria pacote udp
			DatagramPacket sendPacket = new DatagramPacket(sendData,sendData.length, IPAddress, 9876);
			//envia ao servidor
			clientSocket.send(sendPacket);
			//Recebe resposta do servidor
			DatagramPacket receivePacket = new DatagramPacket(receiveData,receiveData.length);
			clientSocket.receive(receivePacket);
			//descriptografa a mensagem de resposta
            		String modifiedSentence = decrypt(mensagemCriptografada(receiveData), chaveencriptacao);
			System.out.println("Recebido do servidor UDP:" + modifiedSentence);
			//Fecha conexão: clientSocket.close();
		}
	}

    	public static byte[] encrypt(String textopuro, String chaveencriptacao) throws Exception {
        	Cipher encripta = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
        	SecretKeySpec key = new SecretKeySpec(chaveencriptacao.getBytes("UTF-8"), "AES");
        	encripta.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
        	return encripta.doFinal(textopuro.getBytes("UTF-8"));
    	}

    	public static String decrypt(byte[] textoencriptado, String chaveencriptacao) throws Exception{
        	Cipher decripta = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
        	SecretKeySpec key = new SecretKeySpec(chaveencriptacao.getBytes("UTF-8"), "AES");
        	decripta.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
        	return new String(decripta.doFinal(textoencriptado),"UTF-8");
    	}

    	public static byte[] mensagemCriptografada(byte[] receiveData){
        	int contador = 0;

        	for(int i=0; i<receiveData.length; i++){
        	    if(receiveData[i] != 0){
        	        contador++;
        	    }
        	}

        	byte[] textoEncriptado = new byte[contador];

        	for (int i=0; i<textoEncriptado.length; i++){
        	    textoEncriptado[i] = receiveData[i];
        	}
        	return textoEncriptado;
    	}
}
