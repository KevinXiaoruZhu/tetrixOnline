import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Scanner;

/**
 * Created by xiaoru_zhu on 2017/5/29.
 */

public class TetrServer
{
    public static Socket s1;
    public static Socket s2;
    
    public static ServerSocket TetrixServ;
    
    public static void main(String args[]) throws  Exception
    {
        TetrixServ = new ServerSocket(10002);
        
        new Thread(new ServerClose()).start(); // controller can close the server artificially
        
        while (true)
        {
            if (s1 == null)
            {
                s1 = TetrixServ.accept(); // the first client has been connected successfully
                System.out.println("TCP connection 1 is established...");
                new Thread(new ServerThread(s1)).start(); // create new thread to receive data from client 1 and send to client 2
            }
            else
            {
                s2 = TetrixServ.accept();//Both client has been connected
                System.out.println("TCP connection 2 is established and game start...");
                new Thread(new ServerThread(s2)).start(); // create new thread to receive data from client 2 and send to client 1
                
                /**Both two clients connecting to server, server send string ready to clients and game start*/
                DataOutputStream dout1 =  new DataOutputStream(TetrServer.s1.getOutputStream());
                dout1.writeUTF("ready");
                dout1.flush();
                DataOutputStream dout2 =  new DataOutputStream(TetrServer.s2.getOutputStream());
                dout2.writeUTF("ready");
                dout2.flush();
            } // the server can only connect with clients twice, then it will be bolcked and not quit the program.
        }
        
    }
    
}


class ServerThread implements Runnable
{
    private Socket s;
    
    public ServerThread(Socket s)
    {
        this.s = s;
    }
    
    public void run()
    {
        try
        {
            if (s.equals(TetrServer.s1))
            {
                while (true)
                {
                    InputStream in = s.getInputStream();
                    if(TetrServer.s2 != null)
                    {
                        DataInputStream din = new DataInputStream(in);
                        String buf = din.readUTF();
                        DataOutputStream dout =  new DataOutputStream(TetrServer.s2.getOutputStream());
                        dout.writeUTF(buf);
                        dout.flush();
                        //din.close();
                        //dout.close();
                    }
                    
                }
            }
            else // equals to TetrServer.s2
            {
                while (true)
                {
                    InputStream in = s.getInputStream();
                    if(TetrServer.s1 != null)
                    {
                        DataInputStream din = new DataInputStream(in);
                        String buf = din.readUTF();
                        DataOutputStream dout =  new DataOutputStream(TetrServer.s1.getOutputStream());
                        dout.writeUTF(buf);
                        dout.flush();
                        //din.close();
                        //dout.close();
                    }
                    
                }
            }
            
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

class ServerClose implements Runnable
{
    
    public void run()
    {
        while (true)
        {
            System.out.println("Please input '1' to close the server ");
            Scanner in = new Scanner(System.in);
            int inputValue = in.nextInt();
            if(inputValue == 1)
            {
                try
                {
                    TetrServer.TetrixServ.close();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    System.exit(0);
                }
            }
            else
            {
                System.out.println("Fail to close server, please input again.");
                continue;
            }
            
        }
    }
}