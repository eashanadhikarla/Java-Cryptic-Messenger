import javax.swing.*; 
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class Server
{ 
  
  public static void main(String args[])
  {
    JFrame frm=new JFrame("Server");
    ChatPanel cp=new ChatPanel();
    frm.getContentPane().add(cp); 
	frm.setSize(500,400);
	frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frm.setVisible(true);
  } 
}

class  ChatPanel extends JPanel implements ActionListener
{
  int flag=0;
  JTextArea text;
  JTextField tf;
  JButton btn;
  JScrollPane scrlr;
  int PORT=8164;
  DataInputStream ids;
  DataOutputStream ods;
  ServerSocket sock;
  Socket conn;
  
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
	
	add(BorderLayout.CENTER,scrlr);
	JPanel pnl1=new JPanel();
	pnl1.add(tf);
	pnl1.add(btn);
	add(BorderLayout.SOUTH,pnl1);
	ConnectWait cw1=new ConnectWait();
	cw1.start();
  }
  class ConnectWait extends Thread
  {
    public void run()
	{
	  try
	  {
        call3();
      }
	  catch(Exception e)
 	  {
	    e.printStackTrace();
	  }
	}
  }
  public void call3() throws IOException
  {
    text.append("Server waiting for client's message....");
	try
	{
	  flag=0;
      sock=new ServerSocket(PORT);
      conn=sock.accept();
	  ids=new DataInputStream(conn.getInputStream());
	  ods=new DataOutputStream(conn.getOutputStream());
	  text.append("\nClient Connected");
	  flag=1;
	  GetMsg gm1=new GetMsg();
      gm1.start();
	}
	catch(Exception e)
	{
	  e.printStackTrace();
	}
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
		text.append("\nClient: "+smsg);
		}
		catch(Exception e)
		{
		  e.printStackTrace();
		}
      }while(!smsg.equals("bye"));
	  conn.close();
	  flag=0; 
    }
  };
  
  public void actionPerformed(ActionEvent ev)
  {
    String msg=tf.getText();
	if(flag!=0)
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
  }
}
