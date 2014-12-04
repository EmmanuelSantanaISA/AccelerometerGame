/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sensor;

/**
 *
 * @author emmanuelsantana
 */
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimpleRead extends Thread implements SerialPortEventListener {

    private CommPortIdentifier portId;
    private Enumeration portList;
    InputStream inputStream;
    SerialPort serialPort;
    Thread readThread;
    private String currentData;
    private Gson gson = new Gson();
    List<String> list = new ArrayList<String>();
    private ISensor sensorApp;

    public void startComm() {
    }

    public SimpleRead(ISensor sensor) {
        try {
            this.sensorApp = sensor;
            portList = CommPortIdentifier.getPortIdentifiers();
            while (portList.hasMoreElements()) {
                portId = (CommPortIdentifier) portList.nextElement();
                System.out.println("Port Name: " + portId.getName());
                if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                    if (portId.getName().equals("/dev/ttyACM0")) {
                        //SimpleRead reader = new SimpleRead();
                        serialPort = (SerialPort) portId.open("SimpleReadApp", 2000);
                        inputStream = serialPort.getInputStream();
                        serialPort.addEventListener(this);
                        serialPort.notifyOnDataAvailable(true);
                        serialPort.setSerialPortParams(9600,
                                SerialPort.DATABITS_8,
                                SerialPort.STOPBITS_1,
                                SerialPort.PARITY_NONE);
                        readThread = new Thread(this);
                        readThread.start();
                    }
                }
            }

        } catch (UnsupportedCommOperationException ex) {
            Logger.getLogger(SimpleRead.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PortInUseException ex) {
            Logger.getLogger(SimpleRead.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SimpleRead.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TooManyListenersException ex) {
            Logger.getLogger(SimpleRead.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void run() {
        try {
            Thread.sleep(200000);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        switch (event.getEventType()) {
//            case SerialPortEvent.BI:
//            case SerialPortEvent.OE:
//            case SerialPortEvent.FE:
//            case SerialPortEvent.PE:
//            case SerialPortEvent.CD:
//            case SerialPortEvent.CTS:
//            case SerialPortEvent.DSR:
//            case SerialPortEvent.RI:
//            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
//                break;
            case SerialPortEvent.DATA_AVAILABLE:
                byte[] readBuffer = new byte[30];


                try {
                    while (inputStream.available() > 0) {
                        int numBytes = inputStream.read(readBuffer);
                    }
                    String input = new String(readBuffer);
                    handleUpdate(input);
                } catch (IOException iOException) {
                    System.err.println(iOException);
                }

                break;
        }
    }

    private void handleUpdate(String input) {
        try {
            for (char s : input.toCharArray()) {
                char currentChar = s;
                if (s != '\0') {
                    switch (currentChar) {
                        case '<':
                            currentData = currentChar + "";
                            break;
                        case '>':
                            currentData += currentChar + "";
                            currentData = currentData.substring(1, currentData.length() - 1);
                            Navigation cs = null;
                            cs = gson.fromJson(currentData, Navigation.class);
                            sensorApp.update(cs);
                            break;
                        default:
                            currentData += currentChar + "";
                            break;
                    }
                }
            }
        } catch (JsonSyntaxException jsonSyntaxException) {
            System.err.println(jsonSyntaxException);
        }
    }
}
