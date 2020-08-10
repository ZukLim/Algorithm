import java.util.StringTokenizer;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

/*
   1. 아래와 같은 명령어를 입력하면 컴파일이 이루어져야 하며, Solution2 라는 이름의 클래스가 생성되어야 채점이 이루어집니다.
       javac Solution2.java -encoding UTF8

   2. 컴파일 후 아래와 같은 명령어를 입력했을 때 여러분의 프로그램이 정상적으로 출력파일 output2.txt 를 생성시켜야 채점이 이루어집니다.
       java Solution2

   - 제출하시는 소스코드의 인코딩이 UTF8 이어야 함에 유의 바랍니다.
   - 수행시간 측정을 위해 다음과 같이 time 명령어를 사용할 수 있습니다.
       time java Solution2
   - 일정 시간 초과시 프로그램을 강제 종료 시키기 위해 다음과 같이 timeout 명령어를 사용할 수 있습니다.
       timeout 0.5 java Solution2   // 0.5초 수행
       timeout 1 java Solution2     // 1초 수행
 */

class Solution2 {
    static final int MAX_N = 20000;
    static final int MAX_E = 80000;

    static int N, E;
    static int[] U = new int[MAX_E], V = new int[MAX_E], W = new int[MAX_E];
    static int Answer;
    static int[] S = new int[MAX_N + 1];
    static int[] size = new int[MAX_N + 1];


    public static void main(String[] args) throws Exception {
		/*
		   동일 폴더 내의 input2.txt 로부터 데이터를 읽어옵니다.
		   또한 동일 폴더 내의 output2.txt 로 정답을 출력합니다.
		 */
        BufferedReader br = new BufferedReader(new FileReader("input2.txt"));
        StringTokenizer stk;
        PrintWriter pw = new PrintWriter("output2.txt");

		/*
		   10개의 테스트 케이스가 주어지므로, 각각을 처리합니다.
		 */
        for (int test_case = 1; test_case <= 10; test_case++) {
			/*
			   각 테스트 케이스를 표준 입력에서 읽어옵니다.
			   먼저 정점의 개수와 간선의 개수를 각각 N, E에 읽어들입니다.
			   그리고 각 i번째 간선의 양 끝점의 번호를 U[i], V[i]에 읽어들이고, i번째 간선의 가중치를 W[i]에 읽어들입니다. (0 ≤ i ≤ E-1, 1 ≤ U[i] ≤ N, 1 ≤ V[i] ≤ N)
			 */
            stk = new StringTokenizer(br.readLine());
            N = Integer.parseInt(stk.nextToken());
            E = Integer.parseInt(stk.nextToken());
            stk = new StringTokenizer(br.readLine());
            for (int i = 0; i < E; i++) {
                U[i] = Integer.parseInt(stk.nextToken());
                V[i] = Integer.parseInt(stk.nextToken());
                W[i] = Integer.parseInt(stk.nextToken());
            }

            /////////////////////////////////////////////////////////////////////////////////////////////
			/*
			   이 부분에서 여러분의 알고리즘이 수행됩니다.
			   문제의 답을 계산하여 그 값을 Answer에 저장하는 것을 가정하였습니다.
			 */
            /////////////////////////////////////////////////////////////////////////////////////////////

            // 총 시간 복잡도 : O(ElogN)

            // 연결된 정점끼리의 집합 초기화 : theta(N)
            for (int i = 0; i <= N; i++) S[i] = 0;

            // 연결된 정점끼리의 집합의 크기 초기화 : theta(N)
            for (int i = 0; i <= N; i++) size[i] = 1;

            // Weight를 heap을 이용해 내림차순으로 정렬 : O(ElogE) = O(ElogN) => 총 시간 복잡도
            heapSort();

            int cnt = 0;
            int tmp = 0;
            Answer = 0;

            // 최악의 경우 : O(Elog*N)
            while (cnt < N - 1) {
                if (differ(U[tmp], V[tmp])) {
                    Answer += W[tmp];
                    union(U[tmp], V[tmp]);
                    cnt++;
                }
                tmp++;
            }

            // output2.txt로 답안을 출력합니다.
            pw.println("#" + test_case + " " + Answer);
			/*
			   아래 코드를 수행하지 않으면 여러분의 프로그램이 제한 시간 초과로 강제 종료 되었을 때,
			   출력한 내용이 실제로 파일에 기록되지 않을 수 있습니다.
			   따라서 안전을 위해 반드시 flush() 를 수행하시기 바랍니다.
			 */
            pw.flush();
        }

        br.close();
        pw.close();
    }

    static void swap(int i, int j) {
        int a, b, c;
        a = U[i];
        U[i] = U[j];
        U[j] = a;
        b = V[i];
        V[i] = V[j];
        V[j] = b;
        c = W[i];
        W[i] = W[j];
        W[j] = c;
    }

    static void heapSort() {
        buildHeap();

        for (int i = E - 1; i > 0; i--) {
            swap(0, i);
            heapify(0, i - 1);
        }
    }

    static void buildHeap() {
        for (int i = (E - 2) / 2; i >= 0; i--) {
            heapify(i, E - 1);
        }
    }

    static void heapify(int i, int n) {
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        int smaller;

        if (right <= n) {
            if (W[left] <= W[right]) smaller = left;
            else smaller = right;
        } else if (left <= n) {
            smaller = left;
        } else return;

        if (W[smaller] <= W[i]) {
            swap(i, smaller);
            heapify(smaller, n);
        }
    }

    static int find(int i) {
        if (S[i] == 0) return i;
        S[i] = find(S[i]);
        return S[i];
    }

    static void union(int i, int j) {
        int r1 = find(i);
        int r2 = find(j);

        if (r1 != r2) {
            if (size[r1] >= size[r2]) {
                S[r2] = r1;
                size[r1] += size[r2];
            } else {
                S[r1] = r2;
                size[r2] += size[r1];
            }
        }
    }

    static boolean differ(int i, int j) {
        return find(i) != find(j);
    }
}
