import java.util.List;

public class SearchResult {
    private final List<String> path;
    private final int nodeCount;

    public SearchResult(List<String> path, int nodeCount) {
        this.path = path;
        this.nodeCount = nodeCount;
    }

    public List<String> getPath() {
        return path;
    }

    public int getNodeCount() {
        return nodeCount;
    }
}
