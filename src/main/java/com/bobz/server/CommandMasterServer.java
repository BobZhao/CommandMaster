package com.bobz.server;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Author: BobZhao
 * Date:   4/18/16 01:15
 * Description:
 */
@ServerEndpoint("/ws/CommandTunnel")
public class CommandMasterServer {

    @OnOpen
    public void onOpen(Session session) {

        System.out.println(session.getId() + " has connected.");

//        try {
//            InetAddress addr = InetAddress.getLocalHost();
//            session.getBasicRemote().sendText(addr.getHostName());
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    @OnError
    public void onError(Throwable throwable) {
        System.out.println(throwable.getMessage());
    }

    @OnClose
    public void onClose(Session session) {

        System.out.println(session.getId() + " has closed.");
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        System.out.println(session.getId() + " is going to run " + message);

        String result = runShell(message);
        //result = URLEncoder.encode(result, "UTF-8");
        System.out.println(result);
        session.getBasicRemote().sendText(result);
    }

    private static String runShell(String cmd) {

        StringBuilder result = new StringBuilder();
        try {
            Process process = Runtime.getRuntime()
                    .exec(new String[]{"/bin/sh", "-c", cmd}, null, null);
            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));
            process.waitFor();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line).append("\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.toString();
    }



}
