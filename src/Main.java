import java.util.Random;
import java.util.Scanner;

public class Main {

    private static final char DOT_HUMAN = 'X';
    private static final char DOT_AI = '0';
    private static final char DOT_EMPTY = '*';

    private static int WIN_COUNT = 4;
    //private static final int DIFFICULT_COUNT = 3;

    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    private static char[][] field;
    private static int fieldSizeX;
    private static int fieldSizeY;


    public static void main(String[] args) {

        while (true){
            inizialize();
            printField();
            while (true){
                humanTurn();
                printField();
                if (checkGameState(DOT_HUMAN, "Вы победили!"))
                    break;
                aiTurn();
                printField();
                if (checkGameState(DOT_AI, "Вы проиграли!"))
                    break;
            }

            System.out.println("Желаете сыграть еще раз? (Y - да):");
            if (!scanner.next().equalsIgnoreCase("Y")) {
                break;
            }
        }



    }

    /**
     * Инициализация игрового поля
     */
    static void inizialize(){
        fieldSizeX = 5;
        fieldSizeY = 5;
        field = new char[fieldSizeY][fieldSizeX];

        for (int y = 0; y < fieldSizeY; y++ ){
            for (int x = 0; x < fieldSizeX; x++){
                field[y][x] = DOT_EMPTY;
            }

        }
    }

    /**
     * Печать текущего состояния игрового поля
     */
    private static void printField(){

        System.out.print("+");
        for (int i = 0; i < fieldSizeX; i++) {
            System.out.print("-" + (i + 1));
        }
        System.out.println("-");

        for (int y = 0; y < fieldSizeY; y++) {
            System.out.print(y + 1);
            for (int x = 0; x < fieldSizeX; x++) {
                System.out.print("|" + field[x][y]);
            }
            System.out.println("|");
        }

        for (int i = 0; i < fieldSizeX * 2 + 1; i++) {
            System.out.print("-");
        }
        System.out.println("-");
    }

    /**
     * Ход игрока (человека)
     */
    static void humanTurn(){
        int x;
        int y;

        do {
            System.out.println("Введите координаты хода x и y (от 1 до 3) через пробел");
            x = scanner.nextInt() -1;
            y = scanner.nextInt() -1;
        } while (!isCellValid(x, y) || !isCellEmpty(x, y));



        field[x][y] = DOT_HUMAN;
    }

    /**
     * Ход игрока (компьютера)
     */
    static void aiTurn(){

        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {
                if (willHumanWin(x, y)) {
                    field[x][y] = DOT_AI;
                    return;
                }
            }
        }

        int x;
        int y;

        do {
            x = random.nextInt(fieldSizeX);
            y = random.nextInt(fieldSizeY);
        } while (!isCellEmpty(x, y));

        field[x][y] = DOT_AI;

    }

    /**
     * Проверка на пустоту ячейки
     * @param x
     * @param y
     * @return
     */
    static boolean isCellEmpty(int x, int y){
        return field[x][y] == DOT_EMPTY;
    }

    /**
     * Проверка доступности ячейки игрового поля
     * @param x
     * @param y
     * @return
     */
    static boolean isCellValid(int x, int y){
        return x < fieldSizeX && y < fieldSizeY && x >= 0 && y >= 0;
    }

    /**
     * Проверка состояния игры
     * @param dot - фишка игрока
     * @param s - победный слоган
     * @return - результат состояния игры
     */
    static boolean checkGameState(char dot, String s){
        if (checkWin(dot)) {
            System.out.println(s);
            return true;
        }
        if (checkDraw()) {
            System.out.println("Ничья!");
            return true;
        }
        return false;
    }

    /**
     * Проверка на ничью
     * @return
     */
    static boolean checkDraw(){
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (isCellEmpty(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Проверка победы игрока
     * @param dot - фишка игрока
     * @return
     */
    static boolean checkWin(char dot){
//        // Проверка по 3 горизонталям:
//        if (field[0][0] == dot && field[0][1] == dot && field[0][2] == dot) return true;
//        if (field[1][0] == dot && field[1][1] == dot && field[1][2] == dot) return true;
//        if (field[2][0] == dot && field[2][1] == dot && field[2][2] == dot) return true;
//
//        // Проверка по 3 вертикалям:
//        if (field[0][0] == dot && field[1][0] == dot && field[2][0] == dot) return true;
//        if (field[0][1] == dot && field[1][1] == dot && field[2][1] == dot) return true;
//        if (field[0][2] == dot && field[1][2] == dot && field[2][2] == dot) return true;
//
//        // Проверка по 2 диагоналям:
//        if (field[0][0] == dot && field[1][1] == dot && field[2][2] == dot) return true;
//        if (field[0][2] == dot && field[1][1] == dot && field[2][0] == dot) return true;

        // Общая последовательная проверка:
        for (int x = 0; x < fieldSizeY; x++){
            for (int y = 0; y < fieldSizeX; y++) {
                if (checkWinRight(x, y, dot, WIN_COUNT)) return true;
                if (checkWinDown(x, y, dot, WIN_COUNT)) return true;
                if (checkWinRightDown(x, y, dot, WIN_COUNT)) return true;
                if (checkWinRightUp(x, y, dot, WIN_COUNT)) return true;
            }
        }


        return false;
    }

    /**
     * Для проверки по горизонтали
     * @param x
     * @param y
     * @param dot
     * @param winCount
     * @return
     */
    static boolean checkWinRight(int x, int y, char dot, int winCount) {
        if (isIndexOutOfBoundsRight(x)) return false;
        int count = 0;
        for (int i = 0; i < winCount; i++) {
            if (field[x + i][y] == dot) {
                count++;
            }
            if (count == winCount) return true;
        }
        return false;
    }

    static boolean checkWinLeft(int x, int y, char dot, int winCount) {
        if (isIndexOutOfBoundsLeft(x)) return false;
        int count = 0;
        for (int i = 0; i < winCount; i++) {
            if (field[x - i][y] == dot) {
                count++;
            }
            if (count == winCount) return true;
        }
        return false;
    }

    /**
     * Для проверки по вертикали вниз
     * @param x
     * @param y
     * @param dot
     * @param winCount
     * @return
     */
    static boolean checkWinDown(int x, int y, char dot, int winCount) {
        if (isIndexOutOfBoundsDown(y)) return false;
        int count = 0;
        for (int i = 0; i < winCount; i++) {
            if (field[x][y + i] == dot) {
                count++;
            }
            if (count == winCount) return true;
        }
        return false;
    }

    /**
     * Для проверки по вертикали вверх
     * @param x
     * @param y
     * @param dot
     * @param winCount
     * @return
     */
    static boolean checkWinUp(int x, int y, char dot, int winCount) {
        if (isIndexOutOfBoundsUp(y)) return false;
        int count = 0;
        for (int i = 0; i < winCount; i++) {
            if (field[x][y - i] == dot) {
                count++;
            }
            if (count == winCount) return true;
        }
        return false;
    }

    /**
     * Для проверки по диагонали вниз вправо
     * @param x
     * @param y
     * @param dot
     * @param winCount
     * @return
     */
    static boolean checkWinRightDown(int x, int y, char dot, int winCount) {
        if (isIndexOutOfBoundsRightDown(x, y)) return false;
        int count = 0;
        for (int i = 0; i < winCount; i++) {
            if (field[x + i][y + i] == dot) {
                count++;
            }
            if (count == winCount) return true;
        }
        return false;
    }

    /**
     * Для проверки по диагонали вверх вправо
     * @param x
     * @param y
     * @param dot
     * @param winCount
     * @return
     */
    static boolean checkWinRightUp(int x, int y, char dot, int winCount) {
        if (isIndexOutOfBoundsRightUp(x, y)) return false;
        int count = 0;
        for (int i = 0; i < winCount; i++) {
            if (field[x + i][y - i] == dot) {
                count++;
            }
            if (count == winCount) return true;
        }
        return false;
    }

    /**
     * Для проверки по диагонали вниз влево
     * @param x
     * @param y
     * @param dot
     * @param winCount
     * @return
     */
    static boolean checkWinLeftDown(int x, int y, char dot, int winCount) {
        if (isIndexOutOfBoundsLeftDown(x, y)) return false;
        int count = 0;
        for (int i = 0; i < winCount; i++) {
            if (field[x - i][y + i] == dot) {
                count++;
            }
            if (count == winCount) return true;
        }
        return false;
    }

    /**
     * Для проверки по диагонали вверх влево
     * @param x
     * @param y
     * @param dot
     * @param winCount
     * @return
     */
    static boolean checkWinLeftUp(int x, int y, char dot, int winCount) {
        if (isIndexOutOfBoundsLeftUp(x, y)) return false;
        int count = 0;
        for (int i = 0; i < winCount; i++) {
            if (field[x - i][y - i] == dot) {
                count++;
            }
            if (count == winCount) return true;
        }
        return false;
    }

    /**
     * Для проверки выхода за пределы массива по горизонтали вправо
     * @return
     */
    static boolean isIndexOutOfBoundsRight(int x) {
        return (x + WIN_COUNT) > fieldSizeX;
    }

    /**
     * Для проверки выхода за пределы массива по вертикали вниз
     * @return
     */
    static boolean isIndexOutOfBoundsDown(int y) {
        return (y + WIN_COUNT) > fieldSizeY;
    }

    /**
     * Для проверки выхода за пределы массива по вертикали вверх
     * @param y
     * @return
     */
    static boolean isIndexOutOfBoundsUp(int y) {
        return (y - WIN_COUNT) <= -2;
    }

    /**
     * Для проверки выхода за пределы массива по горизонтали вправо
     * @param x
     * @return
     */
    static boolean isIndexOutOfBoundsLeft(int x) {
        return (x - WIN_COUNT) <= -2;
    }

    /**
     * Для проверки выхода за пределы массива по диагонали вниз вправо
     * @return
     */
    static boolean isIndexOutOfBoundsRightDown(int x, int y) {
        return isIndexOutOfBoundsRight(x) || isIndexOutOfBoundsDown(y);
    }

    /**
     * Для проверки выхода за пределы массива по диагонали вверх вправо
     * @return
     */
    static boolean isIndexOutOfBoundsRightUp(int x, int y) {
        return isIndexOutOfBoundsRight(x) || isIndexOutOfBoundsUp(y);
    }

    /**
     * Для проверки выхода за пределы массива по диагонали вниз влево
     * @param x
     * @param y
     * @return
     */
    static boolean isIndexOutOfBoundsLeftDown(int x, int y) {
        return isIndexOutOfBoundsLeft(x) || isIndexOutOfBoundsDown(y);
    }

    /**
     * Для проверки выхода за пределы массива по диагонали вверх влево
     * @param x
     * @param y
     * @return
     */
    static boolean isIndexOutOfBoundsLeftUp(int x, int y) {
        return isIndexOutOfBoundsLeft(x) || isIndexOutOfBoundsUp(y);
    }

    /**
     * Проверка, соберет ли 3 подряд человек, если поставит фишку
     * @param x
     * @param y
     * @return
     */
    static boolean willHumanWin(int x, int y) {

        if (isCellEmpty(x, y)) {
            WIN_COUNT = 3;
            field[x][y] = DOT_HUMAN;
            if (checkWinEverywhere(x, y, DOT_HUMAN)) {
                WIN_COUNT = 4;
                field[x][y] = DOT_EMPTY;
                return true;
            }
            field[x][y] = DOT_EMPTY;
            WIN_COUNT = 4;
        }

        return false;
    }

    /**
     * Проверка совпадения 3 фишек во все стороны от заданной точки
     * @param dot
     * @return
     */
    static boolean checkWinEverywhere(int x, int y, char dot){

        if (checkWinRight(x, y, dot, WIN_COUNT)) return true;
        if (checkWinLeft(x, y, dot, WIN_COUNT)) return true;
        if (checkWinDown(x, y, dot, WIN_COUNT)) return true;
        if (checkWinUp(x, y, dot, WIN_COUNT)) return true;
        if (checkWinRightDown(x, y, dot, WIN_COUNT)) return true;
        if (checkWinRightUp(x, y, dot, WIN_COUNT)) return true;
        if (checkWinLeftDown(x, y, dot, WIN_COUNT)) return true;
        if (checkWinLeftUp(x, y, dot, WIN_COUNT)) return true;

        return false;
    }

}