package site.metacoding.chat;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class MyClientSocket {

    Socket socket;
    BufferedWriter writer;
    Scanner sc;

    public MyClientSocket() {
        try {
            socket = new Socket("localhost", 1077); // 연결 (IP주소, 포트)
            writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream()));

            // 스캐너 달고(반복 x)

            // 키보드로부터 입력 받는 부분(반복)
            writer.write("안녕\n"); // 버퍼에 담고(물을 내려야 내려감)
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new MyClientSocket();
    }
}