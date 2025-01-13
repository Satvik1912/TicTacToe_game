import java.util.*;

// Main Class to Start the Game
public class TicTacToe {
    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}

// Game Class - Manages the game logic
class Game {
    private final Board board;
    private final List<Player> players;
    private int currentPlayerIndex;

    public Game() {
        int n = getBoardSize();
        board = new Board(n);
        players = initializePlayers(n);
        currentPlayerIndex = 0;
    }

    public void start() {
        System.out.println("Game Start!");
        board.printBoard();

        while (true) {
            Player currentPlayer = players.get(currentPlayerIndex);
            System.out.println(currentPlayer.getName() + "'s turn:");
            currentPlayer.makeMove(board);
            board.printBoard();

            if (board.checkWin(currentPlayer.getSymbol())) {
                System.out.println(currentPlayer.getName() + " wins!");
                break;
            }

            if (board.isFull()) {
                System.out.println("It's a draw!");
                break;
            }

            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }
    }

    private int getBoardSize() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the board size (n): ");
        return scanner.nextInt();
    }

    private List<Player> initializePlayers(int n) {
        Scanner scanner = new Scanner(System.in);
        List<Player> playerList = new ArrayList<>();

        System.out.print("Enter number of players (1 to " + (n - 1) + "): ");
        int numPlayers = scanner.nextInt();

        System.out.print("Enter number of bots (0 to " + (n - 1 - numPlayers) + "): ");
        int numBots = scanner.nextInt();

        if ((numPlayers + numBots) < 2 || (numPlayers + numBots) > n - 1) {
            throw new IllegalArgumentException("Invalid number of players and bots.");
        }

        char symbol = 'X';
        for (int i = 1; i <= numPlayers; i++) {
            playerList.add(new HumanPlayer("Player " + i, symbol++));
        }

        for (int i = 1; i <= numBots; i++) {
            playerList.add(new BotPlayer("Bot " + i, symbol++));
        }

        return playerList;
    }
}

// Board Class - Represents the game board
class Board {
    private final char[][] grid;
    private final int size;

    public Board(int size) {
        this.size = size;
        this.grid = new char[size][size];
        for (int i = 0; i < size; i++) {
            Arrays.fill(grid[i], '-');
        }
    }

    public int getSize() {
        return size;
    }

    public boolean placeSymbol(int row, int col, char symbol) {
        if (row < 0 || row >= size || col < 0 || col >= size || grid[row][col] != '-') {
            return false;
        }
        grid[row][col] = symbol;
        return true;
    }

    public boolean checkWin(char symbol) {
        for (int i = 0; i < size; i++) {
            if (checkRow(i, symbol) || checkColumn(i, symbol)) {
                return true;
            }
        }
        return checkDiagonals(symbol);
    }

    public boolean isFull() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid[i][j] == '-') {
                    return false;
                }
            }
        }
        return true;
    }

    public void printBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

    private boolean checkRow(int row, char symbol) {
        for (int col = 0; col < size; col++) {
            if (grid[row][col] != symbol) {
                return false;
            }
        }
        return true;
    }

    private boolean checkColumn(int col, char symbol) {
        for (int row = 0; row < size; row++) {
            if (grid[row][col] != symbol) {
                return false;
            }
        }
        return true;
    }

    private boolean checkDiagonals(char symbol) {
        boolean mainDiagonal = true;
        boolean antiDiagonal = true;

        for (int i = 0; i < size; i++) {
            mainDiagonal &= (grid[i][i] == symbol);
            antiDiagonal &= (grid[i][size - i - 1] == symbol);
        }

        return mainDiagonal || antiDiagonal;
    }
}

// Abstract Player Class
abstract class Player {
    private final String name;
    private final char symbol;

    public Player(String name, char symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public char getSymbol() {
        return symbol;
    }

    public abstract void makeMove(Board board);
}

// Human Player Class
class HumanPlayer extends Player {
    public HumanPlayer(String name, char symbol) {
        super(name, symbol);
    }

    @Override
    public void makeMove(Board board) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter row and column: ");
            int row = scanner.nextInt();
            int col = scanner.nextInt();

            if (board.placeSymbol(row, col, getSymbol())) {
                break;
            } else {
                System.out.println("Invalid move. Try again.");
            }
        }
    }
}

// Bot Player Class
class BotPlayer extends Player {
    private final Random random;

    public BotPlayer(String name, char symbol) {
        super(name, symbol);
        this.random = new Random();
    }

    @Override
    public void makeMove(Board board) {
        int size = board.getSize();
        while (true) {
            int row = random.nextInt(size);
            int col = random.nextInt(size);

            if (board.placeSymbol(row, col, getSymbol())) {
                break;
            }
        }
    }
}