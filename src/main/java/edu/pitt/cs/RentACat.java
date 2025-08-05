package edu.pitt.cs;

import org.mockito.Mockito;
import static org.mockito.Mockito.*;

public interface RentACat {
    public static RentACat createInstance(InstanceType type) {
        switch (type) {
            case IMPL:
                return new RentACatImpl();
            case BUGGY:
                return new RentACatBuggy();
            case SOLUTION:
                return new RentACatSolution();
            case MOCK:
                // Create a mock RentACat object
                RentACat mock = Mockito.mock(RentACat.class);
                when(mock.returnCat(anyInt())).thenReturn(true);
                when(mock.rentCat(anyInt())).thenReturn(true);
                when(mock.renameCat(anyInt(), anyString())).thenReturn(true);
                when(mock.listCats()).thenReturn("Mocked list of cats");
                return mock;
            default:
                assert (false);
                return null;
        }
    }

    // WARNING: You are not allowed to change any part of the interface.
    // That means you cannot add any method nor modify any of these methods.

    public boolean returnCat(int id);

    public boolean rentCat(int id);

    public boolean renameCat(int id, String name);

    public String listCats();

    public void addCat(Cat c);

}