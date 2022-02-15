package site.metacoding.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class MyClientSocket {

    Socket socket;
    BufferedWriter writer;
    Scanner sc;

    // 추가(메시지 받는 용도)
    BufferedReader reader;

    public MyClientSocket() {
        try {
            // 서버로 메시지 보내기
            socket = new Socket("localhost", 1077); // 연결 (IP주소, 포트)
            writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream()));

            // 서버로부터 메시지 받기
            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            // 메시지 받아서 출력
            new Thread(() -> {
                while (true) {
                    try {
                        String inputData = reader.readLine();
                        System.out.println("받은 메시지:" + inputData);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            // 메시지 보내기
            // 스캐너 달고(반복 x)
            sc = new Scanner(System.in);
            // 키보드로부터 입력 받는 부분(반복)
            while (true) {
                writer.write(sc.nextLine() + "\n"); // 버퍼에 담고(물을 내려야 내려감)
                writer.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new MyClientSocket();
    }
}
