package site.metacoding.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class MyServerSocket { // 이 방식은 stateful 방식의 통신이다

    ServerSocket serverSocket; // 리스너(연결 = 세션 생성)
    Socket socket; // 메시지 통신
    BufferedReader reader;

    // 추가(클라이언트에게 메시지 보내기)
    BufferedWriter writer;
    Scanner sc;

    // private boolean isConnected;

    public MyServerSocket() {
        try {
            // 1. 서버소켓 생성(리스너)
            // well known port : 0~1023 + 오라클에서 쓰는 1521 포트같은 누가 쓰는 포트는 쓰면 안됨
            serverSocket = new ServerSocket(1077);
            System.out.println("서버 소켓 생성됨");
            socket = serverSocket.accept(); // while을 '돌면서' 대기(클이 접속할 때까지)(랜덤 포트로)

            // 메시지 읽기
            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()) // socket.get~ : 선
            );

            // 메시지 보내기
            sc = new Scanner(System.in);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // 클라이언트로 메시지 보내기
            new Thread(() -> {
                while (true) {
                    try {
                        String inputData = sc.nextLine();
                        writer.write(inputData + "\n");
                        writer.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            // 클라이언트로부터 메시지 받아서 출력
            // 얘를 while 돌리기
            while (true) {
                String inputData = reader.readLine();
                System.out.println("받은 메시지:" + inputData);
                // System.out.println("클라이언트 연결됨");
            }
        } catch (Exception e) {
            System.out.println("통신 오류 발생:" + e.getMessage());
            // e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new MyServerSocket();
        System.out.println("메인 종료");
    }
}