package application.network;

import org.json.simple.JSONObject;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Robin on 21/05/2017.
 */
public abstract class ServerBuilder
{
    protected String syncRequest;
    protected String employeeDataFormat;
    protected String checkDataFormat;
    protected String dateTimeFormat;
    protected int timeout;
    protected int port;
    protected int sleepDuration;
    protected ServerSocket serverSocket;

    public ServerBuilder (JSONObject config)
    {
        JSONObject network = (JSONObject) config.get("network");

        if (network != null)
        {
            this.port = Integer.parseInt(network.get("port").toString());
            this.syncRequest = (String) network.get("sync_request");
            this.employeeDataFormat = (String) network.get("employee_data_format");
            this.checkDataFormat = (String) network.get("check_data_format");
            this.dateTimeFormat = (String) network.get("datetime_format");
            this.timeout = Integer.parseInt(network.get("server_timeout").toString());
            this.sleepDuration = Integer.parseInt(network.get("sleep_duration").toString());
        }
    }

    public void setConnection () throws IOException, IllegalArgumentException
    {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(timeout);
    }

    public void println (String msg)
    {
        System.out.println("Server > " + msg);
    }

    public String receive (Socket client) throws Exception
    {
        InputStream     is  = client.getInputStream();
        DataInputStream in  = new DataInputStream(is);
        String          str = in.readUTF();
        println("Received !");
        return str;
    }
}
