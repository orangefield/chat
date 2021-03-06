package site.metacoding.chat_v2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MyServerSocket {

    // 리스너:연결받기(메인스레드)
    ServerSocket serverSocket;
    List<고객전담스레드> 고객리스트; // 날아가지 않게 저장하는 용도

    // 서버는 메시지 받아서 보내기(클라 수마다)

    public MyServerSocket() {
        try {
            serverSocket = new ServerSocket(2000);
            고객리스트 = new Vector<>(); // 동기화 처리된 ArrayList
            while (true) {
                Socket socket = serverSocket.accept(); // main 스레드(while 돌 때마다 socket이 새로 생김)
                System.out.println("클라이언트 연결됨");
                고객전담스레드 t = new 고객전담스레드(socket); // 고객전담스레드의 주소가 필요
                고객리스트.add(t); // 스택 끝나기 전에 담았으니까 가비지 컬렉션 안당한다
                System.out.println("고객리스트 크기" + 고객리스트.size());
                new Thread(t).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    class 고객전담스레드 implements Runnable { // 새로 생긴 소켓을 보관할 공간이 필요
        // 버퍼 정보가 고객마다 있어야 하기 때문에 람다식이 아니라 내부 클래스로

        Socket socket;
        BufferedReader reader;
        BufferedWriter writer;
        boolean isLogin = true;

        public 고객전담스레드(Socket socket) {
            this.socket = socket;
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (isLogin) {
                try {
                    String inputData = reader.readLine();
                    System.out.println("from 클라이언트:" + inputData);

                    // 메시지 받았으니까 List<고객전담스레드> 고객리스트에 담긴
                    // 모든 클라이언트에게 메시지 전송(for문 돌려서)
                    for (고객전담스레드 t : 고객리스트) { // 컬렉션타입:컬렉션
                        if (t != this) {
                            t.writer.write(inputData + "\n");
                            t.writer.flush();
                        }
                    }
                } catch (Exception e) {
                    try {
                        System.out.println("통신 실패:" + e.getMessage());
                        isLogin = false;
                        고객리스트.remove(this); // 자기자신을 날리면 된다
                        reader.close(); // 안해도 상관 없지만 빨리빨리 날리라고
                        writer.close();
                        socket.close();
                    } catch (Exception e1) {
                        System.out.println("연결해제 실패" + e1.getMessage());
                    }
                }

            }
        }

    }

    public static void main(String[] args) {
        new MyServerSocket();
    }

}
