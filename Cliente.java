import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Cliente {
	JFrame ventana=null;
	JButton btn_enviar=null;
	JTextField mensaje=null;
	JTextArea area_chat=null;
	JPanel contenedor_areachat=null;
	JPanel contenedor_btn=null;
	JScrollPane scrol=null;
	BufferedReader lector=null;
	PrintWriter escritor=null;
	Socket socket=null;

	public Cliente() {
		dointerfaz();
		(mensaje).requestFocus();
	}
	//Metodo que no hace nada, seria la interfaz del cliente
		public void dointerfaz() {
			ventana= new JFrame("Cliente");
			btn_enviar=new JButton("Enviar");
			mensaje=new JTextField(4);
			area_chat=new JTextArea(10,12);
			scrol=new JScrollPane(area_chat);
			/*Se crea el panel en donde se mostrara el chat*/
			contenedor_areachat=new JPanel();
			contenedor_areachat.setLayout(new GridLayout(1,1));
			/*Ahora al contenedor le agrego el scrol, lo que
			 se mostrara */
			
			contenedor_areachat.add(scrol);
			/*Se realiza lo mismo pero ahora con el boton enviar*/
			contenedor_btn=new JPanel();
			contenedor_btn.setLayout(new GridLayout(1,2));
			contenedor_btn.add(mensaje);
			contenedor_btn.add(btn_enviar);
			/*A la ventana completa del chat se le asigna 
			 el contenedor del chat */
			ventana.setLayout(new BorderLayout());
			//el contenedor del chat se encontrara lo más arriba posible
			ventana.add(contenedor_areachat,BorderLayout.NORTH);
			//El boton se ubicara en la parte inferior de la ventana
			ventana.add(contenedor_btn,BorderLayout.SOUTH);
			//Asignamos un tamaño de ventana
			ventana.setSize(300,220);
			//para que no cambie de tamaño
			ventana.setResizable(false);
			ventana.setVisible(true);
			//para que cierre en la 'X'
			ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			Thread principal=new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						socket=new Socket("localhost",9999);
						
							leer();
							escribir();
						
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					
				}
				
			});
			principal.start();
			
		}
		/**
		 * Permite leer todos los mensajes que lleguen
		 */
		public void leer() {
			Thread leer_hilito=new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						//Acá obtenemos la entrada del socket con getInputStream()
						lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						//hacemos un ciclo infinito
						while(true) {
							/*
							 Se guarda todo lo que llegue del socket en una variable
							 Para leerlo*/
							String mensaje_recibido = lector.readLine();
							area_chat.append("Servidor: "+mensaje_recibido+"\n");
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}	
					
				}
				
			});
			leer_hilito.start();
		
		}
		
		public void escribir() {
			Thread escribir_hilito = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						escritor = new PrintWriter(socket.getOutputStream(),true);
						
						btn_enviar.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
							String enviar_mensaje = mensaje.getText();
							escritor.println(enviar_mensaje);
							area_chat.append("Ciente: "+enviar_mensaje+"\n");
							mensaje.setText("");

							}
						});
					} catch (Exception ex) {
						ex.printStackTrace();
					}				
				}
				
			});
			escribir_hilito.start();
			
		}
		
	public static void main(String[] args) {
		new Cliente();
		
	}

}
