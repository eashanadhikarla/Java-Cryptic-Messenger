import javax.swing.*; 
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class Client
{ 
  public static void main(String args[])
  {
    JFrame frm=new JFrame("Client");
    ChatPanel cp=new ChatPanel();
    frm.getContentPane().add(cp); 
	frm.setSize(500,400);
	frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frm.setVisible(true);
  } 
}

class  ChatPanel extends JPanel implements ActionListener
{
  JTextArea text;
  JTextField tf;
  JButton btn;
  JScrollPane scrlr;
  int PORT=8164,flag=0;
  Socket sock;
  DataInputStream ids;
  DataOutputStream ods;
  String ipaddress;
  
  ChatPanel()
  {
    setLayout(new BorderLayout());
	btn=new JButton("Send");
	btn.addActionListener(this);
	text=new JTextArea(10,20);
	text.setLineWrap(true);
	text.setEditable(false);
	scrlr=new JScrollPane(text);
	scrlr.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	scrlr.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	
	tf=new JTextField();
	tf.setPreferredSize(new Dimension(300,45));
	
	add(BorderLayout.NORTH,scrlr);
	JPanel pnl1=new JPanel();
	pnl1.add(tf);
	pnl1.add(btn);
	add(BorderLayout.SOUTH,pnl1);
	text.append("\nEnter the IP Address of Server : ");
  }
  class GetMsg extends Thread
  {
    public void run()
    {
      try
	  {
	    call1();
	  }
	  catch(Exception e)
	  {
	    e.printStackTrace();
	  }
    }
    public void call1() throws IOException
    {
      String smsg="";
      do
      {
	    try
		{
	    smsg=ids.readUTF();
		text.append("\nServer: "+smsg);
		}
		catch(Exception e)
		{
		  e.printStackTrace();
		}
      }while(!smsg.equals("bye"));
	   flag=0;
	  sock.close();
    }
  };
  
  public void actionPerformed(ActionEvent ev)
  {
    String msg=tf.getText();
	if(flag==0)
	{
	  try
	  {
	    sock=new Socket(msg,PORT);
        text.append("\nConnected.....");
		ods=new DataOutputStream(sock.getOutputStream());
		ids=new DataInputStream(sock.getInputStream());
		flag=1;
		GetMsg gm1=new GetMsg();
		gm1.start();
	  }
	  catch(Exception e)
	  {
	    text.append("\nInvalid IP Address.....Try Again");
	  }
	}
	else
	{
	  try
	  {
	    call2(msg);
	  }
	  catch(Exception e)
	  {
	    e.printStackTrace();
	  }
	}
	tf.setText("");
  }
  public void call2(String msg)
  {
    try
	{
    ods.writeUTF(msg);
	}
	catch(Exception e)
	{
	  e.printStackTrace();
	}
	text.append("\nMe: "+msg);
	//text.append("\n");
  }
}
