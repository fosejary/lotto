import java.util.*;

public class MakeLotto {

    private int[][] previousNumbersArr = {
            {1, 4, 13, 29, 38, 39, 7},
            {1, 3, 8, 24, 27, 35, 28},
            {6, 14, 16, 18, 24, 42, 44},
            {12, 20, 26, 33, 44, 45, 24},
            {13, 18, 25, 31, 33, 44, 38},
            {2, 4, 25, 26, 36, 37, 28},
            {17, 18, 21, 27, 29, 33, 26},
            {2, 13, 20, 30, 31, 41, 27},
            {2, 4, 15, 23, 29, 38, 7},
            {7, 10, 16, 28, 41, 42, 40}
    };
    private int[] previousNumbers = {1, 4, 13, 29, 38, 39, 7};
    private boolean useBonusNum = true;
    private ArrayList<Integer> numbers;
    private List<List<Integer>> lottoNumArr;
    private Set<Integer> noShowNumberSet = new TreeSet<>();
    private ArrayList<Integer> currentLottoArr;

    public Set<Integer> getNoShowNumberSet() {
        return noShowNumberSet;
    }

    public ArrayList<Integer> getCurrentLottoArr() {
        return currentLottoArr;
    }

    public void setCurrentLottoArr(ArrayList<Integer> currentLottoArr) {
        this.currentLottoArr = currentLottoArr;
    }

    private void makeSeqNumber() {

        numbers = new ArrayList<>(Collections.nCopies(49, 0));
        for (Integer aCurrentLottoArr : currentLottoArr) {
            numbers.set(aCurrentLottoArr - 1, aCurrentLottoArr);
        }
    }

    private void makeTableNumber() {
        lottoNumArr = this.split(numbers, 7);
    }

    public boolean validationNumber() {

        makeSeqNumber();
        makeTableNumber();

        //로또 구단 가로3줄만 조합금지 필터
        if (lotto9DanFilter(0)) return true;
        if (lotto9DanFilter(1)) return true;
        if (lotto9DanFilter(2)) return true;
        if (lotto9DanFilter(3)) return true;
        if (lotto9DanFilter(4)) return true;

        //로또 구단 앞줄 4줄만 조합 금지
        if (lotto9DanFilter2(0)) return true;
        if (lotto9DanFilter2(1)) return true;
        //로또 구단 모서리 패턴
        if (lotto9DanFilter3()) return true;
        //로또 구단 좌우2줄 패턴
        if (lotto9DanFilter4()) return true;
        //로또 구단 가운데 3줄 패턴
        if (lotto9DanFilter5()) return true;
        //삼각형 필터
        if (triangleFilter(0)) return true;
        if (triangleFilter(1)) return true;
        if (triangleFilter(2)) return true;
        if (triangleFilter(3)) return true;

        //시작 숫자 필터링 (가장 작은수는 1-13 사이에서 출현)
        if (startNumberFilter(13)) return true;

        //세로 라인중 해당하는 라인은 반드시 포함 시킴
        //if (checkBasicVerticalAxisLine(5)) return true;

        //가로 라인중 해당하는 라인은 반드시 포함 시킴
        //if (checkBasicHorizontalAxisLine(3)) return true;

        //25를 중심으로 4개씩 사방의 숫자
        if (checkBlockFilter(1, 4)) return true;
        //패턴표 코너의 4수씩 4지점의 숫자
        if (checkCornerFilter(1, 4)) return true;

        //n회귀 당번 포함
//        if (beforeNumberShow(6)) return true;

        //낙수표 조합 불가 라인 (낙수표 한줄에서만 다출하는 경우)
        if (naksuTableCheck(0)) return true;
        if (naksuTableCheck(1)) return true;
        if (naksuTableCheck(2)) return true;
        if (naksuTableCheck(3)) return true;
        if (naksuTableCheck(4)) return true;
        if (naksuTableCheck(5)) return true;

        //낙수표 5주간에서 5개이상 번호가 나오는 경우
        if (naksu5WeekNumberLimitFilter(3, 5)) return true;
        //낙수표 6주 ~ 10주에서 5개이상 번호가 나오는 경우
        if (naksu6to10WeekNumberFilter()) return true;

        //조합불가 2수 포함 제거
        if (failedCheck()) return true;

        //3수 조합 불가 rdy
        if (checkCombinationNumbers()) return true;
        //4수 박스 모양 조합 불가
        if (checkCombinationNumbers2()) return true;

        //로또9단 순위표 ABC 필터 조합 rdy
        if (checkGroupNumbers(0)) return true; //A그룹
        if (checkGroupNumbers(1)) return true; //B그룹
        if (checkGroupNumbers(2)) return true; //C그룹
        if (checkGroupNumbers(3)) return true; //1구간
        if (checkGroupNumbers(4)) return true; //2구간
        if (checkGroupNumbers(5)) return true; //3구간
        if (checkGroupNumbers(6)) return true;

        //낙수표 5분위 레드라인 근접 수 미출현
        if (naksuRedLineCheck()) return true;

        //낙수표 이전회차 동일자리 출현 번호 제거
//        if (checkNaksuBeforeEqualPositionNumber()) return true;

        //끝수 제거
//        if (checkLastNumberRemoveReq(7)) return true;

        //AC값 필터 6 미만과 10 제외
        if (calcArithmeticComplexCount(7, 10, false)) return true;

        //3의 배수가 최소 1개 이하이거나, 최대 4개 이상인 경우 재추첨
        if (check3MultipleNumber(1, 4)) return true;

        //마지막 볼 넘버가 35미만이면 재추첨
        if (checkLastNumberIsOverToNumber(35)) return true;

        //  4. 직전회차 1등 이웃 수 포함, (이웃수가 4개이상 포함된 경우만 재추첨)
        if (checkNeighborhoodNumber(useBonusNum)) return true;

        //  5. 총합이 min ~ max
        if (!betweenMinMaxNum(100, 180)) return true;

        // 5-1. 끝수합이 min ~ max
        if (checkSumLastNumber(15, 38)) return true;

        //고정수
        ArrayList<Integer> fixNumbers = new ArrayList<>();
//        fixNumbers.add(12);
        if (checkFixNumbers(fixNumbers)) return true;

        // 8. 직전회차 1등 수 미포함 조합 제거 (이월수 없는 경우 재추첨)
//        if (checkHasPreviousLottoNumber(true, true)) return true;

        //연번, 격번, 근번 필터
        //if (checkSequenceTotalFilter(true, true, true)) return true;

        //연번 뉴 필터
        if (checkSequenceFilter()) return true;

        //12. 동일 끝 수 없는 조합 제거 (예: 5 -> 5, 15, 25, 35, 45)를 포함하지 않는 경우 제외
        if (checkHasEqualLastPositionNumber()) return true;

        //단번대 n개, 10번대 n개, 20번대 n개, 30번대 n개, 40번대 n개 이상 조합 제거
        if (numberRangeCheck(2, 4, 2, 4, 2)) return true;

        //true지정하면, 해당 번호대는 반드시 출현
        if (numberNoShowCheck(false, true, false, true, false)) return true;

        //  10. 모든 구간 출현 조합 제거 (단번, 10,20,30,40번대 모두 포함되는 경우)
        if (checkAllLineNumber()) return true;

        //  24. 선택 홀짝 조합 제거 (0:6, 1:5, 2:4, 3:3, 4:2, 5:1, 6:0) 제외 선택
        List<Integer> exceptOddList = new ArrayList<>();
        exceptOddList.add(6); //홀:6 짝:0
        exceptOddList.add(5); //홀:5 짝:1
        exceptOddList.add(4); //홀:4 짝:2
//        exceptOddList.add(3); //홀:3 짝:3
//        exceptOddList.add(2); //홀:2 짝:4
        exceptOddList.add(1); //홀:1 짝:5
        exceptOddList.add(0); //홀:0 짝:6
        if (numberOddEvenCheck(exceptOddList)) return true;

        //  26. 소수 미포함 조합 제거 [2,3,5,7,11,13,17,19,23,29,31,37,41,43]
        if (checkMustHavePrimeNumber(4)) return true;

        //  28. 세로 1라인 n개 이상 조합 제거 (예: 4 -> 세로 1라인에서 4개 이상의 번호가 조합된 경우 제거)
        if (checkVerticalLine(1, 3)) return true;
        //  29. 세로 2라인 n개 이상 조합 제거 (예: 4 -> 세로 2라인에서 4개 이상의 번호가 조합된 경우 제거)
        if (checkVerticalLine(2, 3)) return true;
        //  30. 세로 3라인 n개 이상 조합 제거 (예: 4 -> 세로 3라인에서 4개 이상의 번호가 조합된 경우 제거)
        if (checkVerticalLine(3, 3)) return true;
        //  31. 세로 4라인 n개 이상 조합 제거 (예: 4 -> 세로 4라인에서 4개 이상의 번호가 조합된 경우 제거)
        if (checkVerticalLine(4, 3)) return true;
        //  32. 세로 5라인 n개 이상 조합 제거 (예: 4 -> 세로 5라인에서 4개 이상의 번호가 조합된 경우 제거)
        if (checkVerticalLine(5, 3)) return true;
        //  33. 세로 6라인 n개 이상 조합 제거 (예: 4 -> 세로 6라인에서 4개 이상의 번호가 조합된 경우 제거)
        if (checkVerticalLine(6, 3)) return true;
        //  34. 세로 7라인 n개 이상 조합 제거 (예: 4 -> 세로 7라인에서 4개 이상의 번호가 조합된 경우 제거)
        if (checkVerticalLine(7, 3)) return true;

        //  35. 가로 1라인 n개 이상 조합 제거 (예: 4 -> 가로 1라인에서 4개 이상의 번호가 조합된 경우 제거)
        if (checkHorizonLine(1, 3)) return true;
        //  36. 가로 2라인 n개 이상 조합 제거 (예: 4 -> 가로 2라인에서 4개 이상의 번호가 조합된 경우 제거)
        if (checkHorizonLine(2, 3)) return true;
        //  37. 가로 3라인 n개 이상 조합 제거 (예: 4 -> 가로 3라인에서 4개 이상의 번호가 조합된 경우 제거)
        if (checkHorizonLine(3, 3)) return true;
        //  38. 가로 4라인 n개 이상 조합 제거 (예: 4 -> 가로 4라인에서 4개 이상의 번호가 조합된 경우 제거)
        if (checkHorizonLine(4, 3)) return true;
        //  39. 가로 5라인 n개 이상 조합 제거 (예: 4 -> 가로 5라인에서 4개 이상의 번호가 조합된 경우 제거)
        if (checkHorizonLine(5, 3)) return true;
        //  40. 가로 6라인 n개 이상 조합 제거 (예: 4 -> 가로 6라인에서 4개 이상의 번호가 조합된 경우 제거)
        if (checkHorizonLine(6, 3)) return true;
        //  41. 가로 7라인 n개 이상 조합 제거 (예: 4 -> 가로 7라인에서 4개 이상의 번호가 조합된 경우 제거)
        if (checkHorizonLine(7, 3)) return true;

        /** 느낌대로 추가하는 필터 **/
//        if (exFilter2()) return true; //낙수표 패턴 자리 제거
//        if (!exFilter3()) return true;//40번대 2수 필출 필터

//        if (exFilter1()) return true; //낙수표 제외 자릿수 필터임
//        if (exFilter4()) return true; //2,4,6,8 에서 6수 제외

        if (exFilter5()) return true; //세로줄 필터1
        if (exFilter6()) return true; //세로줄 필터2
        if (exFilter7()) return true; //세로줄 필터3
        if (exFilter9()) return true; //세로줄 필터4
        if (exFilter11()) return true; //세로줄 필터5

//        if (exFilter12()) return true; //출현그룹표 필수 필터
//        if (exFilter13()) return true; //출현그룹표 과출 및 직전위치 필터
//        if (exFilter14()) return true; //짜장 필터3

//        if (exFilter17()) return true; //닥터존 회귀1
//        if (exFilter18()) return true; //닥터존 회귀2

//        if (exFilter19()) return true; //9단 출현표 필터1
//        if (exFilter20()) return true; //모의번호 추첨수
//        if (exFilter21()) return true; //1,6,9 끝 없으면 꽝
//        if (exFilter32()) return true; //크로아티아

        if (number1PosFilter(2)) return true; //1끝
        if (number2PosFilter(2)) return true; //2끝
        if (number3PosFilter(2)) return true; //3끝
        if (number4PosFIlter(2)) return true; //4끝
        if (number5PosFilter(2)) return true; //5끝
        if (number6PosFilter(2)) return true; //6끝
        if (number7PosFilter(2)) return true; //7끝
        if (number8PosFilter(2)) return true; //8끝
        if (number9PosFIlter(1)) return true; //9끝
        if (number0PosFilter(2)) return true; //0끝

        return false;

    }

    private boolean checkSequenceFilter() {
        int[][] filters = {
                {1, 2}, {2, 3}, {3, 4}, {4, 5}, {5, 6}, {6, 7}, {7, 8}, {8, 9}, {9, 10},
                {10, 11}, {11, 12}, {12, 13}, {13, 14}, {14, 15}, {15, 16}, {16, 17}, {17, 18}, {18, 19}, {19, 20},
                {20, 21}, {21, 22}, {22, 23}, {23, 24}, {24, 25}, {25, 26}, {26, 27}, {27, 28}, {28, 29}, {29, 30},
                {30, 31}, {31, 32}, {32, 33}, {33, 34}, {34, 35}, {35, 36}, {36, 37}, {37, 38}, {38, 39}, {39, 40},
                {40, 41}, {41, 42}, {42, 43}, {43, 44}, {44, 45}
        };
        int[] chkFilters = new int[filters.length];
        for (int no : currentLottoArr) {
            for (int i = 0; i < filters.length; i++) {
                for (int a : filters[i]) {
                    if (no == a) {
                        chkFilters[i]++;
                    }
                }
            }
        }
        boolean result = true;
        for (int chk : chkFilters) {
            if (chk > 1) {
                result = false;
                break;
            }
        }
        return result;
    }

    private boolean overSeaLimitFilter(int line, int limit) {
        int checkCount = 0;
        int[][] numbers = {
                {6, 14, 18, 29, 35, 38},
                {12, 14, 30, 34, 40, 41},
                {10, 14, 33, 40, 42, 45},
                {12, 18, 20, 26, 29, 37},
                {1, 7, 18, 33, 35, 41},
                {3, 4, 7, 16, 43, 45},
                {13, 14, 27, 30, 33, 41},
                {10, 15, 30, 35, 42, 43},
                {3, 18, 33, 38, 40, 43}
        };

        for (int no : currentLottoArr) {
            for (int a : numbers[line]) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount > limit;
    }

    private boolean lastNumberLimit() {
        int checkCount = 0;
        int[] numbers = {
                38, 39, 40, 41, 42, 43, 44, 45
        };
        for (int a : numbers) {
            if (a == currentLottoArr.get(5)) {
                checkCount++;
            }
        }
        return checkCount == 0;
    }

    private boolean startNumberLimit() {
        int checkCount = 0;
        int[] numbers = {
                9, 10
        };
        for (int a : numbers) {
            if (a == currentLottoArr.get(0)) {
                checkCount++;
            }
        }
        return checkCount > 0;
    }

    private boolean patternVerticalPrintFilter() {
        int checkCount = 0;
        int[] printArr = {0, 2, 2, 1, 0, 1, 0};
        int[][] numbers = {
                {1, 8, 15, 22, 29, 36, 43},
                {2, 9, 16, 23, 30, 37, 44},
                {3, 10, 17, 24, 31, 38, 45},
                {4, 11, 18, 25, 32, 39},
                {5, 12, 19, 26, 33, 40},
                {6, 13, 20, 27, 34, 41},
                {7, 14, 21, 28, 35, 42}
        };
        int[] countArr = new int[printArr.length];
        for (int i = 0; i < 7; i++) {
            for (int no : currentLottoArr) {
                for (int a : numbers[i]) {
                    if (no == a) {
                        countArr[i] += 1;
                    }
                }
            }
        }

        boolean check1Flag = false;
        for (int i = 0; i < 7; i++) {
            if (printArr[i] == countArr[i]) {
                checkCount++;
            }
            if (countArr[i] > 1) {
                //1보다 큰값이 있으면 true
                check1Flag = true;
            }
        }
        return checkCount == 6 || !check1Flag;
    }

    private boolean patternHorizontalPrintFilter() {
        int checkCount = 0;
        int[] printArr = {2, 0, 1, 1, 0, 2, 0};
        int[][] numbers = {
                {1, 2, 3, 4, 5, 6, 7},
                {8, 9, 10, 11, 12, 13, 14},
                {15, 16, 17, 18, 19, 20, 21},
                {22, 23, 24, 25, 26, 27, 28},
                {29, 30, 31, 32, 33, 34, 35},
                {36, 37, 38, 39, 40, 41, 42},
                {43, 44, 45}
        };
        int[] countArr = new int[printArr.length];
        for (int i = 0; i < 7; i++) {
            for (int no : currentLottoArr) {
                for (int a : numbers[i]) {
                    if (no == a) {
                        countArr[i] += 1;
                    }
                }
            }
        }

        for (int i = 0; i < 7; i++) {
            if (printArr[i] == countArr[i]) {
                checkCount++;
            }
        }
        return checkCount == 6;
    }

    private boolean exFilter1() {
        int checkCount = 0;
        int[] numbers = {
                1, 2,
                12, 13, 15, 16,
                20, 24, 26,
                32, 38,
                41, 42, 43, 45
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount > 4;
    }

    private boolean exFilter2() {
        int[][] numbers = {
                {2, 9, 3, 5, 1, 6, 11, 10, 12, 13, 17, 16, 29, 14, 37, 18, 28, 40, 24, 38, 21, 36, 45, 39, 26},//3
                {25, 34, 43, 19, 23, 42, 31},//1
                {20, 30, 15, 35, 4, 33, 32, 41},//0
                {7, 22, 27}//1
        };

        int[] checkCounts = new int[4];
        for (int no : currentLottoArr) {
            for (int i = 0; i < numbers.length; i++) {
                for (int j = 0; j < numbers[i].length; j++) {
                    if (no == numbers[i][j]) {
                        checkCounts[i]++;
                    }
                }
            }
        }
        return (checkCounts[0] == 3 && checkCounts[1] == 1 && checkCounts[2] == 0 && checkCounts[3] == 1);
    }

    private boolean exFilter3() {
        int checkCount = 0;
        int[] numbers = {
                40, 41, 42, 43, 44, 45
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount == 2;
    }

    private boolean exFilter4() {
        int checkCount = 0;
        int[] numbers = {
                2, 12, 22, 32, 42,
                4, 14, 24, 34, 44,
                6, 16, 26, 36,
                8, 18, 28, 38
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount > 5;
    }

    private boolean exFilter5() {
        int checkCount = 0;
        int[] numbers = {
                1, 4, 13, 29, 38, 39
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount > 2;
    }

    private boolean exFilter6() {
        int checkCount = 0;
        int[] numbers = {
                3, 8, 24, 27, 35
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount > 2;
    }

    private boolean exFilter7() {
        int checkCount = 0;
        int[] numbers = {
                6, 14, 16, 18, 42
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount > 2;
    }

    private boolean exFilter8() {
        int[][] numbers = {
                {1, 4, 8, 13, 17, 18, 20, 32, 35, 38, 43, 45},
                {2, 3, 5, 7, 10, 12, 19, 22, 27, 36, 37, 39, 40, 44},
                {14, 16, 21, 24, 25, 26, 29, 30, 31, 33, 34, 41},
                {6, 9, 11, 23, 28, 42},
                {15}
        };
        int[] filterValid = new int[numbers.length];

        for (int no : currentLottoArr) {
            for (int i = 0; i < numbers.length; i++) {
                for (int j = 0; j < numbers[i].length; j++) {
                    if (no == numbers[i][j]) {
                        filterValid[i]++;
                    }
                }
            }
        }

        boolean flag5Count = false;
        boolean flag0Count = true;
        for (int i = 0; i < filterValid.length; i++) {
            if (filterValid[i] == 0) {
                flag0Count = false;
            }
            if (filterValid[i] > 4) {
                flag5Count = true;
            }
        }

        return flag0Count || flag5Count;
    }

    private boolean exFilter9() {
        int checkCount = 0;
        int[] numbers = {
                12, 20, 26, 33, 44, 45
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount > 2;
    }

    private boolean exFilter10() {
        int checkCount = 0;
        int[][] numbers = {
                {3, 13, 23, 33, 43},
                {6, 16, 26, 36},
                {9, 19, 29, 39}
        };
        int[] filterValid = new int[numbers.length];

        for (int no : currentLottoArr) {
            for (int i = 0; i < numbers.length; i++) {
                for (int j = 0; j < numbers[i].length; j++) {
                    if (no == numbers[i][j]) {
                        filterValid[i]++;
                    }
                }
            }
        }

        for (int i = 0; i < filterValid.length; i++) {
            if (filterValid[i] > 0) {
                checkCount++;
            }
        }

        return checkCount == 3;
    }

    private boolean exFilter11() {
        int checkCount = 0;
        int[] numbers = {
                25, 31
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount > 1;
    }

    private boolean exFilter12() {
        int checkCount = 0;
        int[] numbers = {
                22, 28, 30, 34, 38, 40, 32, 39
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount == 0;
    }

    private boolean exFilter13() {
        int checkCount = 0;
        int[] numbers = {
                18, 43, 13,
                33, 37, 10,
                2, 21,
                44,
                26, 7,
                42, 8, 25,
                6, 23
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount > 3;
    }

    private boolean exFilter14() {
        int checkCount = 0;
        int[] numbers = {
                1, 4, 12, 14, 18, 22, 23, 28, 32, 34, 35, 36, 41
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount > 5;
    }

    private boolean exFilter15() {
        int[][] numbers = {
                {23, 21, 34, 12, 6},
                {16, 34, 12, 29},
                {7, 33, 34, 12, 6},
                {13, 24, 15, 25, 38},
                {32, 9, 39, 6},
                {15, 10, 6, 40},
                {25, 37, 8, 6},
                {4, 15, 33},
                {1, 15, 25, 6},
                {7, 32, 37, 8, 39},
                {23, 1, 10, 17},
                {3, 33, 10, 25, 37, 8},
                {16, 23, 32, 31},
                {15, 26, 29},
                {32, 14, 6},
                {3, 7, 27, 2, 10},
                {7, 17, 31, 41, 18},
                {15, 10, 38, 19},
                {16, 27, 10, 29},
                {13, 21, 43},
                {11, 23, 36},
                {7, 24, 10, 8},
                {34, 25, 39},
                {32, 39, 28, 29, 5},
                {16, 11, 18, 35},
                {13, 1, 44, 25, 26, 29},
                {3, 15, 28},
                {27, 14, 31, 18, 40},
                {4, 14, 37, 17, 6},
                {16, 8, 39, 6},
                {35, 1, 12, 28},
                {3, 23, 27, 9},
                {9, 12, 39, 41},
                {11, 27, 9, 41, 18, 5},
                {34, 17, 2},
                {24, 1, 12, 18, 28},
                {32, 34, 39, 6},
                {13, 11, 27, 14, 29},
                {16, 39, 31, 28, 43},
                {13, 15, 33, 10, 28},
                {13, 16, 1, 34, 19},
                {27, 28, 43},
                {7, 12},
                {23, 7, 1, 37, 39, 38},
                {11, 21, 33, 42, 4, 18},
                {23, 7, 17, 40, 43},
                {4, 12, 18},
                {1, 11, 23, 26, 36},
                {24, 33, 26, 28, 43},
                {3, 33, 26, 5},
                {16, 32, 10, 4, 6, 43},
                {2, 11, 16, 24, 34},
                {3, 1, 11, 15, 35, 41},
                {1, 2, 4, 26, 6},
                {32, 28, 36},
                {13, 2, 4, 12, 6, 29},
                {23, 1, 9, 6},
                {13, 16, 1, 25, 22},
                {24, 27, 28},
                {33, 1, 9, 10, 39},
                {7, 32, 4}
        };
        int[] checkCounts = new int[numbers.length];
        for (int no : currentLottoArr) {
            for (int i = 0; i < numbers.length; i++) {
                for (int j = 0; j < numbers[i].length; j++) {
                    if (no == numbers[i][j]) {
                        checkCounts[i]++;
                    }
                }
            }
        }
        boolean result = false;
        int compareVal = checkCounts.length;
        for (int chk : checkCounts) {
            if (checkCounts.length > 5) {
                compareVal = compareVal - 1;
            }
            if (chk == checkCounts.length) {
                result = true;
                break;
            }
        }
        return result;
    }

    private boolean exFilter151() {
        int checkCount = 0;
        int[] numbers = {
                1, 2, 7, 9, 18, 19, 20, 21, 22, 23, 28, 29, 30, 31, 33, 34, 35, 36, 38, 39, 40, 42, 44, 45
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount == 6;
    }

    private boolean exFilter152() {
        int checkCount = 0;
        int[] numbers = {
                6, 12, 15, 16, 19, 23,
                3, 9, 10, 13, 33, 34,
                6, 13, 14, 21, 27, 30,
                1, 4, 27, 36, 38, 44,
                8, 29, 31, 32, 33, 39
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount == 6;
    }

    private boolean exFilter153() {
        int checkCount = 0;
        int[] numbers = {
                5, 11, 18, 19, 38, 39,
                1, 16, 23, 33, 43, 44,
                8, 16, 29, 30, 35, 42,
                8, 18, 26, 33, 35, 43,
                10, 11, 14, 22, 23, 36
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount == 6;
    }

    private boolean exFilter154() {
        int checkCount = 0;
        int[] numbers = {
                2, 3, 15, 22, 38, 44,
                6, 10, 15, 20, 35, 44,
                3, 9, 21, 33, 36, 44,
                3, 4, 27, 35, 40, 42,
                2, 3, 4, 7, 9, 19
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount == 6;
    }

    private boolean exFilter155() {
        int checkCount = 0;
        int[] numbers = {
                20, 27, 37, 39, 42, 44,
                9, 18, 19, 25, 33, 38,
                6, 15, 23, 29, 35, 40,
                2, 3, 17, 23, 32, 45,
                1, 4, 6, 24, 42, 45
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount == 6;
    }

    private boolean exFilter156() {
        int checkCount = 0;
        int[] numbers = {
                2, 14, 30, 32, 34, 44,
                2, 5, 16, 19, 21, 31,
                1, 5, 8, 19, 31, 36,
                2, 12, 14, 29, 35, 37,
                2, 5, 12, 18, 19, 25
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount == 6;
    }

    private boolean exFilter157() {
        int checkCount = 0;
        int[] numbers = {
                1, 7, 13, 14, 26, 35,
                5, 6, 23, 25, 36, 43,
                3, 16, 18, 21, 22, 36,
                9, 18, 30, 34, 44, 45,
                5, 6, 13, 14, 28, 37
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount == 6;
    }

    private boolean exFilter16() {
        int checkCount = 0;
        int[] numbers = {
                4, 11, 18, 25, 32, 39,
                5, 12, 19, 26, 33, 40
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount == 0;

    }

    private boolean exFilter17() {
        int checkCount = 0;
        int[] numbers = {
                15, 23, 38, 7, 10, 28
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount == 0;
    }

    private boolean exFilter18() {
        int checkCount = 0;
        int[] numbers = {
                8, 22, 35, 38, 39, 41
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount == 0;
    }

    private boolean exFilter19() {
        int checkCount = 0;
        int[] numbers = {
                1, 4, 8, 10, 14, 16, 20, 22, 25, 26, 28, 32, 34, 35, 38, 40, 44
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount == 0 || checkCount > 3;
    }

    private boolean exFilter20() {
        int checkCount = 0;
        int[] numbers = {
                29, 30, 31,
                36, 37, 38,
                43, 44, 45
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount > 4;
    }

    private boolean exFilter21() {
        int checkCount = 0;
        int[] numbers = {
                1, 2, 3, 8, 9, 10, 15, 16, 17
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount > 4;
    }

    private boolean number1PosFilter(int limit) {
        int checkCount = 0;
        int[] numbers = {
                1, 11, 21, 31, 41
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount > limit;
    }


    private boolean number2PosFilter(int limit) {
        int checkCount = 0;
        int[] numbers = {
                2, 12, 22, 32, 42
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount > limit;
    }

    private boolean number3PosFilter(int limit) {
        int checkCount = 0;
        int[] numbers = {
                3, 13, 23, 33, 43
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount > limit;
    }

    private boolean number4PosFIlter(int limit) {
        int checkCount = 0;
        int[] numbers = {
                4, 14, 24, 34, 44
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount > limit;
    }

    private boolean number5PosFilter(int limit) {
        int checkCount = 0;
        int[] numbers = {
                5, 15, 25, 35, 45
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount > limit;
    }

    private boolean number6PosFilter(int limit) {
        int checkCount = 0;
        int[] numbers = {
                6, 16, 26, 36
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount > limit;
    }

    private boolean number7PosFilter(int limit) {
        int checkCount = 0;
        int[] numbers = {
                7, 17, 27, 37
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount > limit;
    }

    private boolean number8PosFilter(int limit) {
        int checkCount = 0;
        int[] numbers = {
                8, 18, 28, 38
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount > limit;
    }

    private boolean number9PosFIlter(int limit) {
        int checkCount = 0;
        int[] numbers = {
                9, 19, 29, 39
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount > limit;
    }

    private boolean number0PosFilter(int limit) {
        int checkCount = 0;
        int[] numbers = {
                10, 20, 30, 40
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount > limit;
    }

    private boolean exFilter32() {
        int checkCount = 0;
        int[] numbers = {
                26, 27, 28,
                33, 34, 35,
                40, 41, 42
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount > 4;
    }

    private boolean exFilter33() {
        int checkCount = 0;
        int[] numbers = {
                8, 18, 28, 38
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount == 0;
    }

    private boolean lottoGiniFakeNumberFilter() {
        int checkCount = 0;
        int[] numbers = {6, 8, 10, 12, 15, 17, 20, 22, 38, 40, 42, 44, 41, 43};
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount == 0;
    }

    private boolean lotto9DanWeaknessNumberFilter() {
        int checkCount = 0;
        int[] numbers = {1, 21, 25, 37, 38};
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount > 1;
    }

    private boolean leftNumberMinLimitFilter(int i) {
        int checkCount = 0;
        int[] numbers = {
                1, 2, 3,
                8, 9, 10,
                15, 16, 17,
                22, 23, 24,
                29, 30, 31,
                36, 37, 38,
                43, 44, 45
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount < i;
    }

    private boolean checkHasNoDisplayOver10CountEx() {
        int checkCount = 0;
        int[] numbers = {4, 11, 28, 29, 7, 23, 6, 5, 26};
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount == 0 || checkCount > 3;
    }

    private boolean lottoSaiLayerBasicFilter() {
        int checkCount = 0;
        int[] numbers = {1, 2, 3, 4, 6, 7, 8, 9, 10, 12, 13, 14, 15, 17, 18, 19, 20, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 36, 37, 39, 41, 43, 44, 45};
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount < 5;
    }

    private boolean drZoneReqNumFilterBy124() {
        int checkCount = 0;
        int[] numbers = {1, 3, 7, 8, 9, 12, 13, 16, 18, 19, 25, 28, 32, 34, 36, 38, 43};
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount < 2;
    }

    private boolean drZoneReqNumFilterBy283() {
        int checkCount = 0;
        int[] numbers = {8, 19, 25, 28, 32, 36};
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount == 0;
    }

    private boolean drZoneReqNumFilterBy245() {
        int checkCount = 0;
        int[] numbers = {1, 7, 16, 18, 34, 38};
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount == 0;
    }

    private boolean drZoneReqNumFilterBy50() {
        int checkCount = 0;
        int[] numbers = {3, 7, 12, 31, 34, 38};
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount == 0;
    }

    private boolean lottoGiniFlogFilter() {
        int checkCount = 0;
        int[] numbers = {3, 7, 13, 27, 40, 41, 36};
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount > 2;
    }

    private boolean lottoGini100Filter() {
        int checkCount = 0;
        int[] numbers = {1, 11, 18, 19, 20, 25, 35, 37, 38, 41, 43, 44};
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount == 0 || checkCount > 3;
    }

    private boolean lottoSaiStarNumberFilter() {
        int checkCount = 0;
        int[] numbers = {16, 21, 26, 27, 28, 30, 33, 34, 35};
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount == 0;
    }

    private boolean checkOverSeaFilter(int line) {
        int checkCount = 0;
        int[][] numbers = {
                {14, 18, 30, 33},//4 - 0
                {7, 29, 35, 40, 41, 43},//3 - 1
                {3, 10, 12, 16, 38, 42, 45},//2 - 2
                {1, 4, 6, 13, 15, 17, 20, 24, 26, 27, 34, 37},//1 - 3
                {2, 5, 8, 9, 11, 19, 21, 22, 23, 25, 28, 31, 32, 36, 39, 44}//0 - 4
        };

        for (int no : currentLottoArr) {
            for (int a : numbers[line]) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        if (line == 0) {
            return checkCount > 2;
        } else if (line == 1) {
            return checkCount > 3;
        } else if (line == 2) {
            return checkCount > 5;
        } else if (line == 3) {
            return checkCount > 5;
        } else if (line == 4) { //line 0
            return checkCount > 5;
        } else {
            return true;
        }

    }

    private boolean reqNumberFilter() {
        int checkCount = 0;
        int[] numbers = {
                1, 30, 33
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount == 0;
    }


    private boolean checkSequenceTotalFilter(boolean cnt1Flag, boolean cnt2Flag, boolean cnt3Flag) {

        int b1 = currentLottoArr.get(0);
        int b2 = currentLottoArr.get(1);
        int b3 = currentLottoArr.get(2);
        int b4 = currentLottoArr.get(3);
        int b5 = currentLottoArr.get(4);
        int b6 = currentLottoArr.get(5);

        int cnt1 = 0;
        int cnt2 = 0;
        int cnt3 = 0;

        if (cnt1Flag) {
            if ((b2 - b1) == 1) cnt1++;
            if ((b3 - b2) == 1) cnt1++;
            if ((b4 - b3) == 1) cnt1++;
            if ((b5 - b4) == 1) cnt1++;
            if ((b6 - b5) == 1) cnt1++;
        }

        if (cnt2Flag) {
            if ((b2 - b1) == 2) cnt2++;
            if ((b3 - b2) == 2) cnt2++;
            if ((b4 - b3) == 2) cnt2++;
            if ((b5 - b4) == 2) cnt2++;
            if ((b6 - b5) == 2) cnt2++;
        }

        if (cnt3Flag) {
            if ((b2 - b1) == 3) cnt3++;
            if ((b3 - b2) == 3) cnt3++;
            if ((b4 - b3) == 3) cnt3++;
            if ((b5 - b4) == 3) cnt3++;
            if ((b6 - b5) == 3) cnt3++;
        }

        if (cnt1Flag && cnt1 > 1) {
            return true;
        } else if (cnt2Flag && cnt2 > 1) {
            return true;
        } else if (cnt3Flag && cnt3 > 1) {
            return true;
        } else {
            return (cnt1 + cnt2 + cnt3) == 0;
        }

    }

    private boolean drZoneReqNumFilter() {
        int checkCount = 0;
        int[] numbers = {5, 21, 24, 25, 27, 33, 34, 38, 39, 44};
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount == 0;
    }

    private boolean lottoSaiBasicFilter(int n) {
        int checkCount = 0;
        int[][] numbers = {
                {2, 3, 4, 5, 6, 10, 11, 12, 18}, //1~3
                {2, 11, 20, 29, 38}, //1~2
                {1, 2, 3, 4, 5, 6, 7}, //1~2
                {36, 37, 38, 39, 40, 41, 42}, //1~2
                {1, 11, 21, 31, 41}, //1~2
                {1, 6, 11, 16, 21, 26, 31, 36, 41}, //1~3
                {29, 30, 31, 36, 37, 38, 43, 44, 45}, //1~3 //6
                {19, 20, 21, 26, 27}, //1~2
                {9, 16, 23, 30, 37}, //1~2
                {6, 12, 13, 14, 20}, //0~1
                {9, 15, 16, 17, 23}, //1~2
                {23, 29, 30, 31, 37}, //1~2
                {7, 9, 17, 19}, //1~2 ************
                {6, 16, 26, 28, 30}, //1~2
                {5, 11, 13, 17, 21, 23, 29} //1~2
        };
        for (int no : currentLottoArr) {
            for (int a : numbers[n]) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        if (n == 0 || n == 5 || n == 6) {
            return checkCount > 3;
        } else if (n == 1 || n == 2 || n == 3 || n == 4 || n == 7 || n == 8 || n > 9) {
            return checkCount > 2;
        } else {
            return checkCount > 1;
        }
    }

    private boolean lottoSaiLayerFilter(int line) {
        int checkCount = 0;
        int[][] numbers = {
                {4, 5, 6, 7, 8, 10, 11, 13, 15, 16, 17, 18, 20, 21, 22, 24, 25, 26, 28, 30, 31, 32, 33, 34, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45},
                {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 15, 16, 17, 18, 20, 21, 23, 24, 25, 27, 28, 34, 35, 36, 39, 41, 42, 43, 44},
                {1, 3, 4, 5, 6, 7, 9, 10, 11, 12, 13, 14, 15, 16, 18, 19, 20, 21, 22, 23, 26, 27, 28, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 41, 42, 43, 45},
                {2, 3, 4, 5, 7, 8, 9, 10, 11, 12, 14, 15, 16, 17, 18, 19, 20, 22, 23, 24, 26, 28, 30, 31, 32, 33, 34, 35, 37, 38, 40, 42, 43, 44, 45}
        };
        for (int no : currentLottoArr) {
            for (int a : numbers[line]) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount < 4;
    }

    private boolean lottoSaiLayerDupFilter(int line) {
        int checkCount = 0;
        int[][] numbers = {
                {1, 4, 8, 9, 10, 11, 12, 16, 21, 23, 26, 27, 34, 36, 37, 38, 39, 43, 45},
                {2, 3, 13, 18, 19, 20, 22, 24, 28, 31, 35, 40, 41, 44},
                {5, 6, 7, 14, 15, 17, 25, 29, 30, 32, 42},
                {33}
        };
        for (int no : currentLottoArr) {
            for (int a : numbers[line]) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        if (line == 0) {
            return checkCount > 5 || checkCount == 0;
        } else if (line == 1) {
            return checkCount > 4 || checkCount == 0;
        } else if (line == 2) {
            return checkCount > 3;
        } else {
            return checkCount > 1;
        }
    }

    private boolean lottoSaiLayerRemoveFilter(int line) {
        int checkCount = 0;
        int[][] numbers = {
                {5, 7, 10, 11, 12, 14, 15, 23, 33, 35, 36, 38, 39, 44},
                {2, 4, 5, 6, 9, 11, 16, 18, 20, 21, 26, 28, 30, 35, 42}
        };
        for (int no : currentLottoArr) {
            for (int a : numbers[line]) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount > 2;
    }

    private boolean calcArithmeticComplexCount(int limitAcNum, int outAcNum, boolean useOutAcNum) {
        int ac = 0;
        Set<Integer> complexSet = new TreeSet<>();
        for (int i = 0; i < currentLottoArr.size(); i++) {
            for (int j = i + 1; j < currentLottoArr.size(); j++) {
                complexSet.add((currentLottoArr.get(j) - currentLottoArr.get(i)));
            }
            ac = complexSet.size() - (currentLottoArr.size() - 1);
        }
        if (useOutAcNum) {
            return (ac < limitAcNum) || (ac == outAcNum);
        } else {
            return (ac < limitAcNum);
        }

    }

    private boolean checkCombinationNumbers() {
        int[][] filters = {
                //동끝 3수
                {1, 11, 21}, {1, 11, 31}, {1, 11, 41}, {1, 21, 31}, {1, 21, 41}, {1, 31, 41},
                {2, 12, 22}, {2, 12, 32}, {2, 12, 42}, {2, 22, 32}, {2, 22, 42}, {2, 32, 42},
                {3, 13, 23}, {3, 13, 33}, {3, 13, 43}, {3, 23, 33}, {3, 23, 43}, {3, 33, 43},
                {4, 14, 24}, {4, 14, 34}, {4, 14, 44}, {4, 24, 34}, {4, 24, 44}, {4, 34, 44},
                {5, 15, 25}, {5, 15, 35}, {5, 15, 45}, {5, 25, 35}, {5, 25, 45}, {5, 35, 45},
                {6, 16, 26}, {6, 16, 36}, {6, 26, 36},
                {7, 17, 27}, {7, 17, 37}, {7, 27, 37},
                {8, 18, 28}, {8, 18, 38}, {8, 28, 38},
                {9, 19, 29}, {9, 19, 39}, {9, 29, 39},
                {10, 20, 30}, {10, 20, 40}, {10, 30, 40},
                //패턴표 세로 3연번
                {1, 8, 15}, {2, 9, 16}, {3, 10, 17}, {4, 11, 18}, {5, 12, 19}, {6, 13, 20}, {7, 14, 21},
                {8, 15, 22}, {9, 16, 23}, {10, 17, 24}, {11, 18, 25}, {12, 19, 26}, {13, 20, 27}, {14, 21, 28},
                {15, 22, 29}, {16, 23, 30}, {17, 24, 31}, {18, 25, 32}, {19, 26, 33}, {20, 27, 34}, {21, 28, 35},
                {22, 29, 36}, {23, 30, 37}, {24, 31, 38}, {25, 32, 39}, {26, 33, 40}, {27, 34, 41}, {28, 35, 42},
                {29, 36, 43}, {30, 37, 44}, {31, 38, 45},
                //패턴표 대각 3연번
                {1, 9, 17}, {2, 10, 18}, {3, 11, 19}, {4, 12, 20}, {5, 13, 21}, {7, 13, 19}, {6, 12, 18}, {5, 11, 17}, {4, 10, 16}, {3, 9, 15},
                {8, 16, 24}, {9, 17, 25}, {10, 18, 26}, {11, 19, 27}, {12, 20, 28}, {14, 20, 26}, {13, 19, 25}, {12, 18, 24}, {11, 17, 23}, {10, 16, 22},
                {15, 23, 31}, {16, 24, 32}, {17, 25, 33}, {18, 26, 34}, {19, 27, 35}, {21, 27, 33}, {20, 26, 32}, {19, 25, 31}, {18, 24, 30}, {17, 23, 29},
                {22, 30, 38}, {23, 31, 39}, {24, 32, 40}, {25, 33, 41}, {26, 34, 42}, {28, 34, 40}, {27, 33, 39}, {26, 32, 38}, {25, 31, 37}, {24, 30, 36},
                {29, 37, 45}, {33, 39, 45}, {32, 38, 44}, {31, 37, 43},
                //3연번
                {1, 2, 3}, {2, 3, 4}, {3, 4, 5}, {4, 5, 6}, {5, 6, 7}, {6, 7, 8}, {7, 8, 9}, {8, 9, 10}, {9, 10, 11}, {10, 11, 12},
                {11, 12, 13}, {12, 13, 14}, {13, 14, 15}, {14, 15, 16}, {15, 16, 17}, {16, 17, 18}, {17, 18, 19}, {18, 19, 20}, {19, 20, 21}, {20, 21, 22},
                {21, 22, 23}, {22, 23, 24}, {23, 24, 25}, {24, 25, 26}, {25, 26, 27}, {26, 27, 28}, {27, 28, 29}, {28, 29, 30}, {29, 30, 31}, {30, 31, 32},
                {31, 32, 33}, {32, 33, 34}, {33, 34, 35}, {34, 35, 36}, {35, 36, 37}, {36, 37, 38}, {37, 38, 39}, {38, 39, 40}, {39, 40, 41}, {40, 41, 42},
                {41, 42, 43}, {42, 43, 44}, {43, 44, 45},
                //가로 3연속 불가
                {1, 3, 6}, {3, 6, 12}, {6, 12, 25}, {12, 25, 2}, {25, 2, 17}, {2, 17, 30}, {17, 30, 15}, {30, 15, 7}, {15, 7, 34}, {11, 32, 9},
                {4, 8, 14}, {8, 14, 20}, {14, 20, 31}, {20, 31, 36}, {31, 36, 21}, {36, 21, 41}, {21, 41, 23}, {41, 23, 10},
                {13, 24, 16}, {24, 16, 26}, {26, 37, 28}, {29, 27, 18}, {27, 18, 33}, {38, 35, 42}, {35, 42, 44},
                //세로 3연속 불가
                {1, 4, 13}, {4, 13, 29}, {13, 29, 38}, {29, 38, 39}, {3, 8, 24}, {8, 24, 27}, {24, 27, 35}, {6, 14, 16}, {14, 16, 18}, {16, 18, 42},
                {12, 20, 26}, {20, 26, 33}, {26, 33, 44}, {33, 44, 45}, {2, 36, 37}, {7, 10, 28},
                //대각선
                {1, 8, 16}, {3, 14, 26}, {12, 31, 37}, {30, 23, 28}, {4, 24, 18}, {8, 16, 33}, {13, 27, 42}, {24, 18, 44}, {27, 42, 45},
                {30, 21, 37}, {2, 31, 26}, {25, 20, 16}, {12, 14, 24}, {6, 8, 13}, {31, 26, 18}, {20, 16, 27}, {14, 24, 29}, {26, 18, 35},
                {16, 27, 38}, {18, 35, 39},
                //ㄱ자 필터
                {1, 3, 8}, {3, 6, 15}, {6, 12, 20}, {12, 25, 31}, {25, 2, 36}, {2, 17, 21}, {17, 30, 41}, {30, 15, 23}, {15, 7, 10}, {32, 9, 22},
                {4, 8, 24}, {8, 14, 16}, {14, 20, 26}, {31, 36, 37}, {23, 10, 28}, {13, 24, 27}, {24, 16, 18}, {16, 26, 33}, {29, 27, 35}, {27, 18, 42},
                {18, 33, 44}, {42, 44, 45}
        };
        boolean result = false;
        for (int[] filter : filters) {
            int check = 0;
            for (int aFilter : filter) {
                for (int no : currentLottoArr) {
                    if (no == aFilter) {
                        check++;
                    }
                }
            }
            if (check == 3) {
                result = true;
                break;
            }
        }
        return result;
    }

    private boolean checkCombinationNumbers2() {
        int[][] filters = {
                {1, 4, 3, 8}, {3, 8, 6, 14}, {6, 14, 12, 20}, {12, 20, 25, 31}, {25, 31, 2, 36}, {2, 36, 17, 21}, {17, 21, 30, 41},
                {30, 41, 15, 23}, {15, 23, 7, 10}, {7, 10, 9, 22}, {9, 22, 1, 4}, {3, 8, 9, 22}, {6, 14, 9, 22}, {12, 20, 9, 22},
                {4, 13, 8, 24}, {8, 24, 14, 16}, {14, 16, 20, 26}, {36, 37, 10, 28}, {13, 29, 24, 27}, {24, 27, 16, 18}, {16, 18, 26, 33},
                {29, 38, 27, 35}, {27, 35, 18, 42}, {18, 42, 33, 44}
        };
        boolean result = false;
        for (int[] filter : filters) {
            int check = 0;
            for (int aFilter : filter) {
                for (int no : currentLottoArr) {
                    if (no == aFilter) {
                        check++;
                    }
                }
            }
            if (check == 4) {
                result = true;
                break;
            }
        }
        return result;
    }

    private boolean checkGroupNumbersFilter() {

        int[][] groups = {
                {34, 43, 18, 27, 40},
                {12, 39, 1, 13, 17},
                {14, 20, 33, 10, 45},//A
                {4, 37, 19, 15, 31},
                {11, 36, 38, 2, 5},
                {21, 24, 3, 8, 26},//B
                {44, 7, 42, 25, 16},
                {6, 28, 35, 41, 29},
                {30, 23, 32, 22, 9}//C
        };
        int[] groupCount = new int[groups.length];

        for (int i = 0; i < groups.length; i++) {
            for (int j = 0; j < groups[i].length; j++) {
                for (int no : currentLottoArr) {
                    if (no == groups[i][j]) {
                        groupCount[i]++;
                    }
                }
            }
        }

        int zeroCount = (int) Arrays.stream(groupCount).filter(no -> no == 0).count();
        return zeroCount < 3 || zeroCount > 4;

    }


    private boolean checkGroupNumbers(int group) {
        int checkCount = 0;
        int[][] groups = {
                {
                        34, 27, 18, 43, 13,
                        39, 17, 12, 40, 1,
                        33, 14, 37, 20, 10
                },//A
                {
                        4, 45, 2, 31, 21,
                        36, 11, 24, 3, 44,
                        15, 38, 19, 26, 7
                },//B
                {
                        42, 8, 16, 5, 25,
                        35, 6, 28, 41, 29,
                        23, 30, 22, 32, 9
                },//C
                {34, 27, 18, 43, 13, 4, 45, 2, 31, 21, 42, 8, 16, 5, 25}, //1
                {39, 17, 12, 40, 1, 36, 11, 24, 3, 44, 35, 6, 28, 41, 29}, //2
                {33, 14, 37, 20, 10, 15, 38, 19, 26, 7, 23, 30, 22, 32, 9}, //3
                {
                        34, 27, 18, 43, 13,
                        39, 17, 12, 40, 1,
                        33, 14, 37, 20, 10,
                        42, 8, 16, 5, 25,
                        35, 6, 28, 41, 29,
                        23, 30, 22, 32, 9
                }
        };
        for (int no : currentLottoArr) {
            for (int g : groups[group]) {
                if (no == g) {
                    checkCount++;
                }
            }
        }
        if (group == 0) {
            // A 그룹
            return checkCount > 4;
        } else if (group == 1) {
            // B 그룹
            return checkCount > 4;
        } else if (group == 2) {
            // C 그룹
            return checkCount > 4;
        } else if (group == 3) {
            // 1 구간
            return checkCount > 4;
        } else if (group == 4) {
            // 2 구간
            return checkCount > 4;
        } else if (group == 5) {
            // 3 구간
            return checkCount > 4;
        } else {
            return checkCount == 0;
            // A + C 그룹 끝
        }
    }

    private boolean checkBasicHorizontalAxisLine(int horizonLine) {
        int checkCount = 0;
        int[][] axisNumbers = {
                {1, 2, 3, 4, 5, 6, 7},
                {8, 9, 10, 11, 12, 13, 14},
                {15, 16, 17, 18, 19, 20, 21},
                {22, 23, 24, 25, 26, 27, 28},
                {29, 30, 31, 32, 33, 34, 35},
                {36, 37, 38, 39, 40, 41, 42},
                {43, 44, 45}
        };
        for (int no : currentLottoArr) {
            for (int x : axisNumbers[horizonLine - 1]) {
                if (no == x) {
                    checkCount++;
                }
            }
        }
        return checkCount == 0;
    }

    private boolean checkBasicVerticalAxisLine(int verticalLine) {
        int checkCount = 0;
        int[][] axisNumbers = {
                {1, 8, 15, 22, 29, 36, 43},
                {2, 9, 16, 23, 30, 37, 44},
                {3, 10, 17, 24, 31, 38, 45},
                {4, 11, 18, 25, 32, 39},
                {5, 12, 19, 26, 33, 40},
                {6, 13, 20, 27, 34, 41},
                {7, 14, 21, 28, 35, 42}
        };
        for (int no : currentLottoArr) {
            for (int x : axisNumbers[verticalLine - 1]) {
                if (no == x) {
                    checkCount++;
                }
            }
        }
        return checkCount == 0;
    }

    private boolean naksuRedLineCheck() {
        int checkCount = 0;
        int[] numbers = {
                25, 31,
                2, 36, 37,
                7, 10, 28,
                34
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount > 3;
    }

    private boolean failedCheck() {
        int[][] filters = {
                {1, 2}, {2, 3}, {3, 4}, {4, 5}, {5, 6}, {6, 7}, {7, 8}, {8, 9}, {9, 10},
                {29, 39}, {38, 39}, {6, 16}, {14, 16}, {16, 18}, {44, 45}, {36, 37}
        };
        boolean result = false;
        for (int[] filter : filters) {
            int check = 0;
            for (int aFilter : filter) {
                for (int no : currentLottoArr) {
                    if (no == aFilter) {
                        check++;
                    }
                }
            }
            if (check == 2) {
                result = true;
                break;
            }
        }
        return result;
    }

    private boolean checkNaksuBeforeEqualPositionNumber() {
        int checkCount = 0;
        int[] numbers = {20, 26, 38, 32};
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount > 1;
    }

    private boolean naksu5WeekNumberLimitFilter(int min, int max) {
        int checkCount = 0;
        int[] numbers = {
                1, 3, 6, 12, 25,
                4, 8, 14, 20, 31,
                1, 3, 24, 16, 26,
                29, 27, 18, 33,
                38, 35, 42, 44,
                39, 45
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return (checkCount < min || checkCount > max);
    }

    private boolean naksu6to10WeekNumberFilter() {
        int checkCount = 0;
        int[] numbers = {
                2, 17, 30, 15, 7, 34, 43, 5,
                36, 21, 41, 23, 10,
                37, 28
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount > 3;
    }

    private boolean lotto9DanFilter(int line) {
        int checkCount = 0;
        int[][] numbers = {
                {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21},
                {8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28},
                {15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35},
                {22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42},
                {1, 2, 3, 4, 5, 6, 7, 8, 14, 15, 21, 22, 28, 29, 35, 36, 42, 43}
        };
        for (int no : currentLottoArr) {
            for (int a : numbers[line]) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount == 6;
    }

    private boolean lotto9DanFilter2(int line) {
        int checkCount = 0;
        int[][] numbers = {
                {1, 2, 3, 4, 8, 9, 10, 11, 15, 16, 17, 18, 22, 23, 24, 25, 29, 30, 31, 32, 36, 37, 38, 39, 43, 44, 45},
                {4, 5, 6, 7, 11, 12, 13, 14, 18, 19, 20, 21, 25, 26, 27, 28, 32, 33, 34, 35, 39, 40, 41, 42}
        };
        for (int no : currentLottoArr) {
            for (int a : numbers[line]) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount == 6;
    }

    private boolean lotto9DanFilter3() {
        int checkCount = 0;
        int[] numbers = {1, 2, 8, 9, 6, 7, 13, 14, 29, 30, 34, 35, 36, 37, 41, 42, 43, 44, 45};
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount == 0 || checkCount > 4;
    }

    private boolean lotto9DanFilter4() {
        int checkCount = 0;
        int[] numbers = {
                1, 2, 8, 9, 15, 16, 22, 23, 29, 30, 36, 37, 43, 44,
                6, 7, 13, 14, 20, 21, 27, 28, 34, 35, 41, 42
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount == 6;
    }

    private boolean lotto9DanFilter5() {
        int checkCount = 0;
        int[] numbers = {
                3, 4, 5, 10, 11, 12, 17, 18, 19, 24, 25, 26, 31, 32, 33, 38, 39, 40
        };
        for (int no : currentLottoArr) {
            for (int a : numbers) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount == 6;
    }

    private boolean pongdangFilter(int line) {
        int checkCount = 0;
        int[][] numbers = {
                {1, 2, 8, 9, 15, 16, 22, 23, 29, 30, 36, 37, 43, 44, 4, 5, 11, 12, 18, 19, 25, 26, 32, 33, 39, 40},
                {3, 4, 6, 7, 10, 11, 13, 14, 17, 18, 20, 21, 24, 25, 27, 28, 31, 32, 34, 35, 38, 39, 41, 42, 45}
        };
        for (int no : currentLottoArr) {
            for (int a : numbers[line]) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount == 6;
    }

    private boolean triangleFilter(int line) {
        int checkCount = 0;
        int[][] numbers = {
                {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 15, 16, 17, 18, 19, 22, 23, 24, 25, 29, 30, 31, 36, 37, 43},
                {1, 8, 9, 15, 16, 17, 22, 23, 24, 25, 29, 30, 31, 32, 33, 36, 37, 38, 39, 40, 41, 43, 44, 45},
                {1, 2, 3, 4, 5, 6, 7, 9, 10, 11, 12, 13, 14, 17, 18, 19, 20, 21, 25, 26, 27, 28, 33, 34, 35, 41, 42},
                {7, 13, 14, 19, 20, 21, 25, 26, 27, 28, 31, 32, 33, 34, 35, 37, 38, 39, 40, 41, 42, 43, 44, 45}
        };
        for (int no : currentLottoArr) {
            for (int a : numbers[line]) {
                if (no == a) {
                    checkCount++;
                }
            }
        }
        return checkCount == 6;
    }

    private boolean startNumberFilter(int startNumber) {
        return currentLottoArr.get(0) > startNumber;
    }

    private boolean checkHasNoDisplayOver10Count(int minCount, int maxCount, boolean between) {
        int checkCount = 0;
        Set<Integer> nonDupNumberSet = new TreeSet<>();
        ArrayList<Integer> base45Number = new ArrayList<>();
        for (int i = 1; i <= 45; i++) {
            base45Number.add(i);
        }
        for (int i = 0; i < previousNumbersArr.length; i++) {
            for (int j = 0; j < previousNumbersArr[i].length - 1; j++) {
                nonDupNumberSet.add(previousNumbersArr[i][j]);
            }
        }
        for (int n : nonDupNumberSet) {
            base45Number.set((n - 1), 0);
        }
        Set<Integer> nonDupNumberSetResult = new TreeSet<>(base45Number);
        nonDupNumberSetResult.remove(0);
        noShowNumberSet.addAll(nonDupNumberSetResult);
        for (int no : currentLottoArr) {
            for (int dup : nonDupNumberSetResult) {
                if (no == dup) {
                    checkCount++;
                }
            }
        }
        if (between) {
            if (checkCount >= minCount && checkCount <= maxCount) {
                return false;
            } else {
                return true;
            }
        } else {
            return checkCount > maxCount;
        }
    }

    private boolean checkFixNumbers(ArrayList<Integer> fixNumbers) {
        int checkCount = 0;
        if (fixNumbers.size() > 0) {
            for (int no : currentLottoArr) {
                for (int f : fixNumbers) {
                    if (no == f) {
                        checkCount++;
                    }
                }
            }
            return checkCount < fixNumbers.size();
        } else {
            return false;
        }
    }

    private boolean check3MultipleNumber(int minCount, int maxCount) {
        int checkCount = 0;
        for (int no : currentLottoArr) {
            int calc = no % 3;
            if (calc == 0) {
                checkCount++;
            }
        }
        return (checkCount < minCount) || (checkCount > maxCount);
    }

    private boolean checkLastNumberIsOverToNumber(int lastMinNumber) {
        return (currentLottoArr.get(currentLottoArr.size() - 1)) < lastMinNumber;
    }

    private boolean numberOddEvenCheck(List<Integer> exceptOddList) {
        boolean passFlag = false;
        if (exceptOddList.size() > 0) {
            for (int odd : exceptOddList) {
                int oddNum = 0;
                for (int no : currentLottoArr) {
                    if (no > 0) {
                        int calc = no % 2;
                        if (calc > 0) {
                            oddNum++;
                        }
                    }
                }
                if (odd == oddNum) {
                    passFlag = true;
                    break;
                }
            }
        }
        return passFlag;
    }

    private boolean checkNeighborhoodNumber(boolean useBonusNum) {
        Set<Integer> neighborhoodNumSet = new TreeSet<>();
        int previousLen = (useBonusNum) ? previousNumbers.length : (previousNumbers.length - 1);
        for (int i = 0; i < previousLen; i++) {
            neighborhoodNumSet.add(previousNumbers[i] - 1);
            neighborhoodNumSet.add(previousNumbers[i] + 1);
        }
        int checkCount = 0;
        for (int n : neighborhoodNumSet) {
            for (int no : currentLottoArr) {
                if (n == no) {
                    checkCount++;
                }
            }
        }
        return checkCount == 0 || checkCount > 3;
    }

    private boolean naksuTableCheck(int line) {
        int checkCount = 0;
        int[][] numbers = {
                {1, 3, 6, 12, 25, 2, 17, 30, 15, 7, 34, 43, 5, 11, 32, 9, 40, 19},
                {4, 8, 14, 20, 31, 36, 21, 41, 23, 10, 22},
                {13, 24, 16, 26, 37, 28},
                {29, 27, 18, 33},
                {38, 35, 42, 44},
                {39, 45}
        };
        for (int no : currentLottoArr) {
            for (int n : numbers[line]) {
                if (no == n) {
                    checkCount++;
                }
            }
        }
        if (line == 0) {
            return checkCount > 3;
        } else if (line == 1) {
            return checkCount > 3;
        } else if (line == 2) {
            return checkCount > 2;
        } else if (line == 3) {
            return checkCount > 2;
        } else if (line == 4) {
            return checkCount > 1;
        } else {
            return checkCount > 1;
        }
    }

    private boolean checkAllLineNumber() {
        boolean c1 = false, c10 = false, c20 = false, c30 = false, c40 = false;
        for (int no : currentLottoArr) {
            if ((no >= 1 && no <= 10)) {
                c1 = true;
            }
            if ((no >= 11 && no <= 20)) {
                c10 = true;
            }
            if ((no >= 21 && no <= 30)) {
                c20 = true;
            }
            if ((no >= 31 && no <= 40)) {
                c30 = true;
            }
            if ((no >= 41 && no <= 45)) {
                c40 = true;
            }
        }
        return (c1 && c10 && c20 && c30 && c40);
    }

    private boolean checkComplexCount(int number, int limit) {
        int checkCount = 0;
        for (int no : currentLottoArr) {
            if ((no % number) == 0) {
                checkCount++;
            }
        }
        return checkCount > limit || checkCount == 0;
    }

    private boolean checkHasPreviousLottoNumber(boolean useBonusNum, boolean over4Count) {
        int dupCount = 0;
        for (int no : currentLottoArr) {
            int previousLen = (useBonusNum) ? previousNumbers.length : (previousNumbers.length - 1);
            for (int i = 0; i < previousLen; i++) {
                if (no == previousNumbers[i]) {
                    dupCount++;
                }
            }
        }
        if (over4Count) {
            return dupCount > 2;
        } else {
            return dupCount == 0;
        }

    }

    private boolean beforeNumberShow(int count) {
        int checkCount = 0;
        for (int no : currentLottoArr) {
            for (int n : previousNumbersArr[count - 1]) {
                if (no == n) {
                    checkCount++;
                }
            }
        }
        return checkCount == 0 || checkCount > 2;
    }

    private boolean checkMustHavePrimeNumber(int limit) {
        int checkCount = 0;
        int[] numbers = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43};
        for (int no : currentLottoArr) {
            for (int n : numbers) {
                if (no == n) {
                    checkCount++;
                }
            }
        }
        //소수가 없으면 무한 loop
        return checkCount > limit;
    }

    private boolean numberNoShowCheck(boolean num1s, boolean num10s, boolean num20s, boolean num30s,
                                      boolean num40s) {
        int check1Count = 0, check10Count = 0, check20Count = 0, check30Count = 0, check40Count = 0;
        for (int no : currentLottoArr) {
            if ((no >= 1 && no <= 10)) {
                check1Count++;
            }
            if ((no >= 11 && no <= 20)) {
                check10Count++;
            }
            if ((no >= 21 && no <= 30)) {
                check20Count++;
            }
            if ((no >= 31 && no <= 40)) {
                check30Count++;
            }
            if ((no >= 41 && no <= 45)) {
                check40Count++;
            }
        }

        boolean num1Flag = (num1s && check1Count > 0);
        boolean num10Flag = (num10s && check10Count > 0);
        boolean num20Flag = (num20s && check20Count > 0);
        boolean num30Flag = (num30s && check30Count > 0);
        boolean num40Flag = (num40s && check40Count > 0);

        return (num1s != num1Flag) || (num10s != num10Flag) || (num20s != num20Flag) || (num30s != num30Flag) || (num40s != num40Flag);
    }

    private boolean checkLastNumberReq(ArrayList<Integer> lastNumbers) {
        int checkCount = 0;
        if (lastNumbers.size() > 0) {
            for (int no : currentLottoArr) {
                String noStr = String.valueOf(no);
                if (noStr.length() > 1) {
                    noStr = noStr.substring(1, 2);
                }
                for (int n : lastNumbers) {
                    if (n == Integer.valueOf(noStr)) {
                        checkCount++;
                    }
                }
            }
            return checkCount > 3;
        } else {
            return false;
        }
    }

    private boolean checkLastNumberRemoveReq(int lastNumber) {
        int checkCount = 0;
        for (int no : currentLottoArr) {
            String noStr = String.valueOf(no);
            if (noStr.length() > 1) {
                noStr = noStr.substring(1, 2);
            }
            if (lastNumber == Integer.valueOf(noStr)) {
                checkCount++;
            }
        }
        return checkCount > 0;
    }

    private boolean checkLastPositionNumberReq(int number) {
        int checkCount = 0;
        for (int no : currentLottoArr) {
            String noStr = String.valueOf(no);
            if (noStr.length() > 1) {
                noStr = noStr.substring(1, 2);
            }
            if (noStr.equals(String.valueOf(number))) {
                checkCount++;
            }
        }
        return checkCount == 0;
    }

    private boolean checkLastPositionNumber() {
        Map<String, Integer> lastMap = new HashMap<>();
        for (int no : currentLottoArr) {
            String noStr = String.valueOf(no);
            if (noStr.length() > 1) {
                noStr = noStr.substring(1, 2);
            }
            if (lastMap.containsKey(noStr)) {
                lastMap.put(noStr, lastMap.get(noStr) + 1);
            } else {
                lastMap.put(noStr, 1);
            }
        }
        boolean flag = true;
        for (String key : lastMap.keySet()) {
            if (lastMap.get(key) >= 2) {
                flag = false;
            }
        }

        return flag;
    }

    private boolean checkBlockFilter(int min, int max) {
        int checkCount = 0;
        int[] blockNumbers = {
                9, 10, 12, 13,
                16, 17, 19, 20,
                30, 31, 33, 34,
                37, 38, 40, 41
        };
        for (int no : currentLottoArr) {
            for (int b : blockNumbers) {
                if (no == b) {
                    checkCount++;
                }
            }
        }
        return (checkCount < min && checkCount > max);
    }

    private boolean checkCornerFilter(int min, int max) {
        int checkCount = 0;
        int[] blockNumbers = {
                1, 2, 8, 9,
                6, 7, 13, 14,
                29, 30, 36, 37,
                34, 35, 41, 42
        };
        for (int no : currentLottoArr) {
            for (int b : blockNumbers) {
                if (no == b) {
                    checkCount++;
                }
            }
        }
        return (checkCount < min && checkCount > max);
    }

    private boolean checkHasEqualLastPositionNumber() {
        int checkCount = 0;
        ArrayList<String> hasList = new ArrayList<>();
        for (int no : currentLottoArr) {
            String noStr = String.valueOf(no);
            if (noStr.length() > 1) {
                noStr = noStr.substring(1, 2);
            }
            if (hasList.contains(noStr)) {
                checkCount++;
            } else {
                hasList.add(noStr);
            }
        }
        return checkCount == 0;
    }

    private boolean checkSumLastNumber(int min, int max) {
        int lastSum = 0;
        for (int no : currentLottoArr) {
            String noStr = String.valueOf(no);
            if (noStr.length() > 1) {
                noStr = noStr.substring(1, 2);
            }
            lastSum += Integer.valueOf(noStr);
        }
        return lastSum < min || lastSum > max;
    }

    private boolean numberRangeCheck(int num1, int num10, int num20, int num30, int num40) {
        int check1Count = 0, check10Count = 0, check20Count = 0, check30Count = 0, check40Count = 0;
        for (int no : currentLottoArr) {
            if (no >= 1 && no <= 10) {
                check1Count++;
            } else if (no >= 11 && no <= 20) {
                check10Count++;
            } else if (no >= 21 && no <= 30) {
                check20Count++;
            } else if (no >= 31 && no <= 40) {
                check30Count++;
            } else if (no >= 41 && no <= 45) {
                check40Count++;
            }
        }
        if (num1 <= check1Count) {
            return true;
        } else if (num10 <= check10Count) {
            return true;
        } else if (num20 <= check20Count) {
            return true;
        } else if (num30 <= check30Count) {
            return true;
        } else if (num40 <= check40Count) {
            return true;
        } else {
            return false;
        }

    }

    private boolean checkHorizonLine(int lineNo, int count) {
        Set<Integer> checkSet = new TreeSet<>();
        for (int i = 0; i < lottoNumArr.get(lineNo - 1).size(); i++) {
            if (lottoNumArr.get(lineNo - 1).get(i) > 0) {
                checkSet.add(lottoNumArr.get(lineNo - 1).get(i));
            }
        }
        return checkSet.size() >= count;
    }

    private boolean checkVerticalLine(int lineNo, int count) {
        Set<Integer> checkSet = new TreeSet<>();
        for (int i = 0; i < lottoNumArr.size(); i++) {
            for (int j = 0; j < lottoNumArr.get(i).size(); j++) {
                if (lottoNumArr.get(i).get(lineNo - 1) > 0) {
                    checkSet.add(lottoNumArr.get(i).get(lineNo - 1));
                }
            }
        }
        return checkSet.size() >= count;
    }

    private boolean betweenMinMaxNum(int minSumNumber, int maxSumNumber) {
        int total = 0;
        for (int number : currentLottoArr) {
            total += number;
        }
        return ((total >= minSumNumber) && (total <= maxSumNumber));
    }

    private <T> List<List<T>> split(List<T> resList, int count) {
        if (resList == null || count < 1) {
            return null;
        }
        List<List<T>> ret = new ArrayList<>();
        int size = resList.size();
        if (size <= count) {
            ret.add(resList);
        } else {
            int pre = size / count;
            int last = size % count;
            for (int i = 0; i < pre; i++) {
                List<T> itemList = new ArrayList<>();
                for (int j = 0; j < count; j++) {
                    itemList.add(resList.get(i * count + j));
                }
                ret.add(itemList);
            }

            if (last > 0) {
                List<T> itemList = new ArrayList<>();
                for (int i = 0; i < last; i++) {
                    itemList.add(resList.get(pre * count + i));
                }
                ret.add(itemList);
            }
        }
        return ret;
    }
}
/*
2021년 6월 5일 토요일
오후 9시에 로또 번호 확인 결과.
5번을 포함해서 6개의 번호를 모두 맞추어서 로또 1등에 당첨이 되었다.
진아에게 바로 1등 당첨 사실을 말하고, 서로 부둥켜 안고 너무 기쁜 나머지 쾌재를 내지르고
쉽사리 흥분을 가라앉히지 못했다.
월요일에 회사에는 연차를 사용하고, 바로 농협 본점을 오픈 시간에 맞추어서 찾아갔고,
직원에게 1등 당첨 용지를 보여주고 확인 받은 다음, VIP실로 안내되었고
직원들의 상품 가입 안내를 2시간 가량 듣고, 좋아 보이는 상품을 몇가지 가입하고
나와서 100만원짜리 수표 10장과, 천만원짜리 수표 1장 및 5만원권 100장을 현금으로 인출 받아서
가방에 넣고, 집으로 왔고,
그 주의 주말에 진아와 함께 평소 즐겨보던 유튜브인 '내집마련연구소'에서 항상 나오던 집들중
몇몇 마음에 두고있던 10~13억대 전원주택을 3곳 정도 둘러보았고,
집에 와서 저녁에 어느집이 가장 마음에 들었는지 충분히 이야기를 나눈후에,
차주 평일에 연차를 사용하고, 계약서를 작성하러 홈포터 김현정 플래너를 만나 계약을 체결했다.
입주까지는 6개월 가량 남았다고해서, 앞으로 남은 기간 행복하게 보낼 생각에 부풀어있었다.
그리고 1등 당첨된 금액중 1~2억 정도는 양가 부모님께 도움을 드리기로 했지만,
일시불로 도와드리지는 않고, 매달 50만원~100만원 사이의 용돈을 드리기로 했다.
그리고 동생 영진이가 결혼하게 되면, 집 구하는데 보태라고 2~3천 정도 도와줄 생각이다.
6개월 뒤에 전원주택의 마당에서 옆집 및 윗집 아랫집등 신경 쓰지 않고,
넓은 마당에서 뛰어노는 장군이와 봄이의 모습을 상상하니 너무 기분이 좋다.
감사합니다. 로또 1등에 당첨되게 해주셔서!!
*/