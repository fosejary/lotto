import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class main {

    /*
    11,22,33,44,

    13,18,31,38
     */
    public static void main(String[] args) {

        System.out.println("******************** [로또 번호 분석 알고리즘 시작] ********************");
        long startTime = System.currentTimeMillis();

        Combination combination = new Combination();
        Integer[] arr = {
                1, 2, 3, 4, 5, 6, 7,
                8, 9, 10, 11, 12, 13, 14,
                15, 16, 17, 18, 19, 20, 21,
                22, 23, 24, 25, 26, 27, 28,
                29, 30, 31, 32, 33, 34, 35,
                36, 37, 38, 39, 40, 41, 42,
                43, 44, 45
        };
        ArrayList<Integer> arrList = new ArrayList<>(Arrays.asList(arr));

        /*
         * 10,20,30,40
         * 11,21,31,41
         * 12,22,32,42 - 12
         * 3,23,33 - 15
         * 4,14,24,44 - 19
         * 5,15,35,45 - 23
         * 16,26 - 25
         * 7,17,27,37 - 29
         * 8,18,38 - 32
         * 9,19,39 - 35
         *
         * 제외: 1, 2, 13, 34, 25, 6, 36, 28, 29
         * */
        //2,7,9,16,37

        //제외수 10 11 20 21 25 41
//        arrList.remove(Integer.valueOf(1)); //출현그룹표 약함
//        arrList.remove(Integer.valueOf(2));
//        arrList.remove(Integer.valueOf(3));
//        arrList.remove(Integer.valueOf(4)); //보너스 끝수
//        arrList.remove(Integer.valueOf(5));
//        arrList.remove(Integer.valueOf(6)); //4주 이내 2회 출현 //출현그룹표 약함
//        arrList.remove(Integer.valueOf(7));
//        arrList.remove(Integer.valueOf(8));
//        arrList.remove(Integer.valueOf(9));
//        arrList.remove(Integer.valueOf(10));
//        arrList.remove(Integer.valueOf(11)); //10회차 직전 위치
//        arrList.remove(Integer.valueOf(12));
//        arrList.remove(Integer.valueOf(13));
//        arrList.remove(Integer.valueOf(14)); //보너스 끝수
//        arrList.remove(Integer.valueOf(15));
//        arrList.remove(Integer.valueOf(16));
//        arrList.remove(Integer.valueOf(17));
//        arrList.remove(Integer.valueOf(18));
//        arrList.remove(Integer.valueOf(19)); //10회차 직전 위치
//        arrList.remove(Integer.valueOf(20));
        arrList.remove(Integer.valueOf(21));
        arrList.remove(Integer.valueOf(22));
        arrList.remove(Integer.valueOf(23));
        arrList.remove(Integer.valueOf(24)); //보너스 끝수 //출현그룹표 약함
        arrList.remove(Integer.valueOf(25)); //10회차 직전 위치 //출현그룹표 약함
        arrList.remove(Integer.valueOf(26));
        arrList.remove(Integer.valueOf(27));
        arrList.remove(Integer.valueOf(28)); //4주 이내 2회 출현
        arrList.remove(Integer.valueOf(29)); //출현그룹표 약함
        arrList.remove(Integer.valueOf(30));
//        arrList.remove(Integer.valueOf(31));L //10회차 직전 위치
//        arrList.remove(Integer.valueOf(32));
//        arrList.remove(Integer.valueOf(33));
//        arrList.remove(Integer.valueOf(34)); //보너스 끝수 //4주 이내 2회 출현
//        arrList.remove(Integer.valueOf(35));
//        arrList.remove(Integer.valueOf(36)); //4주 이내 2회 출현 //출현그룹표 약함
//        arrList.remove(Integer.valueOf(37));
//        arrList.remove(Integer.valueOf(38));
//        arrList.remove(Integer.valueOf(39));
//        arrList.remove(Integer.valueOf(40));
//        arrList.remove(Integer.valueOf(41));
//        arrList.remove(Integer.valueOf(42));
//        arrList.remove(Integer.valueOf(43)); //4주 이내 2회 출현
//        arrList.remove(Integer.valueOf(44)); //보너스 끝수
//        arrList.remove(Integer.valueOf(45));

        System.out.println(arrList);
        boolean[] visited = new boolean[arrList.size()];
        combination.comb(arrList, visited, 0, arrList.size(), 6);
        long endTime = System.currentTimeMillis();
        double combTime = (endTime - startTime) / 1000d;
        System.out.println("* 번호 생성 시간: " + combTime + " (초)");

        ArrayList<ArrayList<Integer>> combList = combination.getGenArrList();
        ArrayList<ArrayList<Integer>> allNumberList = new ArrayList<>();

//        Connection con = null;
//        PreparedStatement st = null;
//
//        try {
//
//            System.out.println("db select");
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            con = DriverManager.getConnection("jdbc:mysql://fosejary.cafe24.com:3306?serverTimezone=UTC", "fosejary", "Rladudrl8544");
//            StringBuilder sqlBuiler = new StringBuilder();
//            sqlBuiler.append("select id, no1, no2, no3, no4, no5, no6 from fosejary.lottoAllNumber\n")
//                    .append("where ac in (7,9,10)\n")
//                    .append("and win1 is null and win2 is null and win3 is null\n");
//
//            List<Integer> removeNumberList = new ArrayList<>();
//
//            removeNumberList.add(4);
//            removeNumberList.add(12);
//            removeNumberList.add(29);
//            removeNumberList.add(35);
//
//            int loopCnt = 0;
//            for (Integer removeNumber : removeNumberList) {
//                sqlBuiler.append("and (")
//                        .append("no1 != ").append(removeNumber)
//                        .append(" and no2 != ").append(removeNumber)
//                        .append(" and no3 != ").append(removeNumber)
//                        .append(" and no4 != ").append(removeNumber)
//                        .append(" and no5 != ").append(removeNumber)
//                        .append(" and no6 != ").append(removeNumber)
//                        .append(")");
//                if (loopCnt < removeNumberList.size()) {
//                    sqlBuiler.append("\n");
//                }
//                loopCnt++;
//            }
//
//            String sql = sqlBuiler.toString();
//            System.out.println(sql);
//            ResultSet rs;
//            st = con.prepareStatement(sql);
//            rs = st.executeQuery(sql);
//
//            while (rs.next()) {
//                ArrayList<Integer> numbers = new ArrayList<>();
//                numbers.add(rs.getInt("no1"));
//                numbers.add(rs.getInt("no2"));
//                numbers.add(rs.getInt("no3"));
//                numbers.add(rs.getInt("no4"));
//                numbers.add(rs.getInt("no5"));
//                numbers.add(rs.getInt("no6"));
//                allNumberList.add(numbers);
//            }
//            System.out.println("all number list create is done.");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (st != null) {
//                try {
//                    st.close();
//                } catch (SQLException se) {
//                    se.printStackTrace();
//                }
//            }
//            if (con != null) {
//                try {
//                    con.close();
//                } catch (SQLException se) {
//                    se.printStackTrace();
//                }
//            }
//        }

        ArrayList<ArrayList<Integer>> resultList = new ArrayList<>();
        MakeLotto lotto = new MakeLotto();
        for (ArrayList<Integer> aGenArrList : combList) {
            lotto.setCurrentLottoArr(aGenArrList);
            if (!lotto.validationNumber()) {
                resultList.add(aGenArrList);
            }
        }
        long validTime = System.currentTimeMillis();
        double lastTime = (validTime - startTime) / 1000d;
        System.out.println("* 번호 분석 시간: " + lastTime + " (초)");
        System.out.println("* 경우의 수: " + allNumberList.size());
        int orgMakeNumberCount = resultList.size();
        System.out.println("* 10회 미출수: " + lotto.getNoShowNumberSet());
        int count = 1;
        System.out.println("------------------------------");
        for (ArrayList<Integer> result : resultList) {
            System.out.println(count + ". -> " + result);
            count++;
        }

        ArrayList mainRandomList = new ArrayList();
        Random ra = new Random();
        int mainSize = resultList.size(); //사이즈 따로 구해서
        for (int i = 0; i < mainSize; i++) {
            int rv = ra.nextInt(resultList.size());
            mainRandomList.add(resultList.get(rv));
            resultList.remove(rv);
        }

        for (int i = 0; i < 10; i++) {
            System.out.println(mainRandomList.get(i));
        }

        System.out.println("* 조합 대상 번호 개수: " + arrList.size());
        System.out.println("* 조합수: " + orgMakeNumberCount);
        System.out.println("******************** [로또 번호 분석 알고리즘 종료] ********************");

    }

}