package utils;

public interface MatchObservable {

    void addObserver(MatchObserver observer);

    void removeObserver(MatchObserver observer);
}
