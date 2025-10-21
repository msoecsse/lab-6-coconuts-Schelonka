/*
 ** Course: CSC 1110A
 ** Fall 2024
 ** Assignment:
 ** Name : Joshua Schelonka
 ** Date Created:
 */

package coconuts;

public interface Subject {

    void attach(HitObserver observer);


    void detach(HitObserver observer);

    void notifyAll(HitEvent event);


}


