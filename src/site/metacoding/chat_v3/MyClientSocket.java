package site.metacoding.chat_v3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class MyClientSocket {

    String username;

    Socket socket;

    // 스레드
    BufferedWriter writer;
    Scanner sc;

    // 스레드
    BufferedReader reader;

    public MyClientSocket() {
        try {
            // 소켓 연결
            socket = new Socket("127.0.0.1", 2000);

            // 버퍼 달기
            sc = new Scanner(System.in);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 새로운 스레드(읽기 전용)
            new Thread(new 읽기전담스레드()).start();

            // 최초 username 전송 프로토콜
            System.out.println("아이디를 입력하세요.");
            username = sc.nextLine();
            writer.write(username + "\n");
            writer.flush();
            System.out.println(username + "이 서버로 전송되었습니다.");

            // 메인 스레드(쓰기 전용)
            while (true) {
                String keyboardInputData = sc.nextLine();
                writer.write(keyboardInputData + "\n"); // 버퍼에 담기, 중개자(서버)에게만 보내면 됨
                writer.flush(); // 버퍼에 담긴 것을 stream으로 흘려보내기
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class 읽기전담스레드 implements Runnable {

        @Override
        public void run() {
            try {
                while (true) {
                    String inputData = reader.readLine();
                    System.out.println("받은 메시지:" + inputData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        new MyClientSocket();
    }
}
