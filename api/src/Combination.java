import java.util.ArrayList;

class Combination {

    //생성 횟수
    private int count = 0;
    //생성된 로또번호들의 조합
    private ArrayList<ArrayList<Integer>> genArrList = new ArrayList<>();

    public ArrayList<ArrayList<Integer>> getGenArrList() {
        return genArrList;
    }

    public void setGenArrList(ArrayList<ArrayList<Integer>> genArrList) {
        this.genArrList = genArrList;
    }

    void comb(ArrayList<Integer> arr, boolean[] visited, int depth, int n, int r) {
        if (r == 0) {
            print(arr, visited, n);
            count++;
            this.setCount(count);
            return;
        }
        if (depth == n) {
            return;
        }
        visited[depth] = true;
        comb(arr, visited, depth + 1, n, r - 1);
        visited[depth] = false;
        comb(arr, visited, depth + 1, n, r);
    }

    private void print(ArrayList<Integer> arr, boolean[] visited, int n) {
        ArrayList<Integer> eachArr = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (visited[i]) {
                eachArr.add(arr.get(i));
            }
        }
        genArrList.add(eachArr);
    }

    int getCount() {
        return count;
    }

    private void setCount(int count) {
        this.count = count;
    }
}
