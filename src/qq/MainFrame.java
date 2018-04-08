package qq;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.io.*;
import java.net.InetAddress;

public class MainFrame extends JFrame {
	JPanel contentPane;
    JTextArea txtRecord = new JTextArea();
    JTextField txtMsg = new JTextField();
    JButton btnSend = new JButton();
    final int server_port = 2021;
    final int client_port = 2022;
    public MainFrame() {
        try {
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            jbInit();
            this.receive();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    public void receive() throws Exception{
    	Thread thread = new Thread(){
            DatagramSocket server = new DatagramSocket(server_port);
            public void run(){
                try {
                    while(true){
                        byte[] buf = new byte[1024];
                        DatagramPacket dp = new DatagramPacket(buf,buf.length);
                        server.receive(dp);
                        String msg = new String(dp.getData());
                        txtRecord.append("对方说:" + msg + "\n");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        };
        thread.start();
    }
    public void btnSend_actionPerformed(ActionEvent e) {
        try {
            DatagramSocket client = new DatagramSocket();
            byte[] buf = txtMsg.getText().getBytes();
            DatagramPacket dp = new DatagramPacket(buf,buf.length,InetAddress.getLocalHost(),client_port);
            client.send(dp);
            txtRecord.append("自己说:"+txtMsg.getText()+"\n");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        contentPane = (JPanel) getContentPane();
        contentPane.setLayout(null);
        setSize(new Dimension(400, 318));
        setResizable(false);
        setTitle("QQ");
        txtRecord.setBorder(BorderFactory.createLoweredBevelBorder());
        txtRecord.setBounds(new Rectangle(18, 17, 354, 202));
        txtMsg.setBounds(new Rectangle(18, 235, 287, 30));
        btnSend.setBounds(new Rectangle(312, 235, 61, 30));
        btnSend.setText("发送");
        btnSend.addActionListener(new MainFrame_btnSend_actionAdapter(this));
        contentPane.add(txtRecord);
        contentPane.add(txtMsg);
        contentPane.add(btnSend);
    }
}

class MainFrame_btnSend_actionAdapter implements ActionListener {
    private MainFrame adaptee;
    MainFrame_btnSend_actionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }
    public void actionPerformed(ActionEvent e) {
        adaptee.btnSend_actionPerformed(e);
    }
}
