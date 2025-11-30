import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.*;

class Edge {
    int to;
    int weight;
    Edge(int to, int weight) {
        this.to = to;
        this.weight = weight;
    }
}
