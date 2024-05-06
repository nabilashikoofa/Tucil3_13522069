public class Node {
    private final String word;
    private final int cost;       
    private final int heuristic;  
    private final int gScore;
    private final int fScore;     

    public Node(String word, int cost){
        this.word = word;
        this.cost = cost;
        this.heuristic = 0;
        this.fScore = 0;
        this.gScore = 0;
    }

    public Node(String word, int gScore, int fScore) {
        this.word = word;
        this.gScore = gScore;
        this.fScore = fScore;
        this.cost = 0;
        this.heuristic = 0;
    }

    public Node(String word, int cost, int heuristic, int fScore) {
        this.word = word;
        this.cost = cost;
        this.heuristic = heuristic;
        this.fScore = fScore;
        this.gScore = 0;
    }

    public String getWord() {
        return word;
    }

    public int getCost() {
        return cost;
    }

    public int getHeuristic() {
        return heuristic;
    }

    public int getFScore() {
        return fScore;
    }

    public int getGScore() {
        return gScore;
    }
}
