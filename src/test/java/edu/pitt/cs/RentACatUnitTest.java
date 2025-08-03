package edu.pitt.cs;

import java.lang.reflect.Method;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RentACatUnitTest {

	RentACat r;
	// These are re‑initialised inside individual tests
	Cat c1, c2, c3;

	ByteArrayOutputStream out;
	PrintStream stdout;
	final String newline = System.lineSeparator();

	@Before
	public void setUp() {
		r = RentACat.createInstance(InstanceType.SOLUTION);
		stdout = System.out;
		out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));
	}

	@After
	public void tearDown() {
		System.setOut(stdout);
	}

	// Empty list
	@Test
	public void testListCatsNumCats0() {
		assertEquals("", r.listCats());
	}

	// Listing cats (mocks are fine)
	@Test
	public void testListCatsNumCats3() {
		c1 = Cat.createInstance(InstanceType.MOCK, 1, "Jennyanydots");
		c2 = Cat.createInstance(InstanceType.MOCK, 2, "Old Deuteronomy");
		c3 = Cat.createInstance(InstanceType.MOCK, 3, "Mistoffelees");

		when(c1.getId()).thenReturn(1);
		when(c1.getName()).thenReturn("Jennyanydots");
		when(c1.getRented()).thenReturn(false);

		when(c2.getId()).thenReturn(2);
		when(c2.getName()).thenReturn("Old Deuteronomy");
		when(c2.getRented()).thenReturn(false);

		when(c3.getId()).thenReturn(3);
		when(c3.getName()).thenReturn("Mistoffelees");
		when(c3.getRented()).thenReturn(false);

		when(c1.toString()).thenReturn("ID 1. Jennyanydots");
		when(c2.toString()).thenReturn("ID 2. Old Deuteronomy");
		when(c3.toString()).thenReturn("ID 3. Mistoffelees");

		r.addCat(c1);
		r.addCat(c2);
		r.addCat(c3);

		String expected = "ID 1. Jennyanydots\n" +
				"ID 2. Old Deuteronomy\n" +
				"ID 3. Mistoffelees\n";
		assertEquals(expected, r.listCats());
	}

	// renameCat but there are no cats
	@Test
	public void testRenameFailureNumCats0() {
		assertFalse(r.renameCat(2, "Garfield"));
		assertEquals("Invalid cat ID." + newline, out.toString());
	}

	// renameCat – MOCK
	@Test
	public void testRenameNumCat3() throws Exception {
		Cat realC2 = Cat.createInstance(InstanceType.MOCK, 2, "Old Deuteronomy");

		when(realC2.getName()).thenReturn("Old Deuteronomy").thenReturn("Garfield");

		r.addCat(realC2);

		assertTrue(r.renameCat(2, "Garfield"));

		Method getCat = r.getClass().getDeclaredMethod("getCat", int.class);
		getCat.setAccessible(true);
		Cat renamed = (Cat) getCat.invoke(r, 2);
		assertEquals("Garfield", renamed.getName());
	}

	// rentCat – mock
	@Test
	public void testRentCatNumCats3() {
		c2 = mock(Cat.class);
		when(c2.getId()).thenReturn(2);
		when(c2.getName()).thenReturn("Old Deuteronomy");
		when(c2.getRented()).thenReturn(false);

		r.addCat(c2);
		assertTrue(r.rentCat(2));
		assertEquals("Old Deuteronomy has been rented." + newline, out.toString());
	}

	// rentCat – real cat
	@Test
	public void testRentCatFailureAlreadyRented() {
		Cat realC1 = Cat.createInstance(InstanceType.IMPL, 1, "Jennyanydots");
		r.addCat(realC1);

		assertTrue(r.rentCat(1));
		out.reset();
		assertFalse(r.rentCat(1)); // second rent should fail
		assertTrue(out.toString().contains("Sorry, Jennyanydots is not here!"));
	}

	// returnCat – real cat
	@Test
	public void testReturnCatSuccess() {
		Cat realC1 = Cat.createInstance(InstanceType.IMPL, 1, "Jennyanydots");
		r.addCat(realC1);
		r.rentCat(1);
		out.reset();

		assertTrue(r.returnCat(1));
		assertEquals("Welcome back, Jennyanydots!" + newline, out.toString());
		assertFalse(realC1.getRented());
	}

	// returnCat – real cat
	@Test
	public void testReturnSameCatTwice() {
		Cat realC1 = Cat.createInstance(InstanceType.IMPL, 1, "Jennyanydots");
		r.addCat(realC1);
		r.rentCat(1);
		r.returnCat(1);
		out.reset();

		assertFalse(r.returnCat(1));
		assertTrue(out.toString().contains("Jennyanydots is already here!"));
	}

	// rent and return - real cal
	@Test
	public void testRentThenReturnCat() {
		Cat realC1 = Cat.createInstance(InstanceType.IMPL, 1, "Jennyanydots");
		r.addCat(realC1);

		assertTrue(r.rentCat(1));
		out.reset();
		assertTrue(r.returnCat(1));
		assertEquals("Welcome back, Jennyanydots!" + newline, out.toString());
	}

	//
	// listCats - real cats
	// -----------------------------------------------------------------------
	@Test
	public void testListCatsSkipsRented() {
		Cat jenny = Cat.createInstance(InstanceType.IMPL, 1, "Jenny");
		Cat deut = Cat.createInstance(InstanceType.IMPL, 2, "Deut");
		r.addCat(jenny);
		r.addCat(deut);

		r.rentCat(1);
		String list = r.listCats();
		assertFalse(list.contains("Jenny"));
		assertTrue(list.contains("Deut"));
	}

	// addCat duplicate IDs – mock

	@Test
	public void testAddSameCatTwice() {
		c1 = mock(Cat.class);
		when(c1.getId()).thenReturn(1);
		when(c1.getName()).thenReturn("Jennyanydots");
		// simulate state change: first call false, second true
		when(c1.getRented()).thenReturn(false).thenReturn(true);

		r.addCat(c1);
		assertTrue(r.rentCat(1)); // first rent ok
		out.reset();

		assertFalse(r.rentCat(1)); // should now fail
		assertTrue(out.toString().contains("Sorry, Jennyanydots is not here!"));
	}

	@Test
	public void testRentCatNegativeId() {
		assertFalse(r.rentCat(-1));
		assertTrue(out.toString().contains("Invalid cat ID."));
	}

	// @Test
	// public void testRenameCatNullName() {
	// 	Cat realC1 = Cat.createInstance(InstanceType.IMPL, 1, "Jennyanydots");
	// 	r.addCat(realC1);
	// 	assertFalse(r.renameCat(1, null));
	// }

	// sanity check :)
	@Test
	public void testCreateInstanceImpl() {
		assertNotNull(RentACat.createInstance(InstanceType.IMPL));
	}

	@Test
	public void testCreateInstanceBuggy() {
		assertNotNull(RentACat.createInstance(InstanceType.BUGGY));
	}

	@Test
	public void testCreateInstanceSolution() {
		assertNotNull(RentACat.createInstance(InstanceType.SOLUTION));
	}

	@Test
	public void testCreateInstanceMock() {
		RentACat mockR = RentACat.createInstance(InstanceType.MOCK);
		assertNotNull(mockR);
		assertEquals("Mocked list of cats", mockR.listCats());
	}

	@Test
	public void testRentCatFailureNumCats3() {
		c1 = mock(Cat.class);
		c2 = mock(Cat.class);
		c3 = mock(Cat.class);

		// Configure mocks
		when(c1.getId()).thenReturn(1);
		when(c1.getName()).thenReturn("Jennyanydots");
		when(c1.getRented()).thenReturn(false);

		when(c2.getId()).thenReturn(2);
		when(c2.getName()).thenReturn("Old Deuteronomy");
		when(c2.getRented()).thenReturn(true); // Already rented

		when(c3.getId()).thenReturn(3);
		when(c3.getName()).thenReturn("Mistoffelees");
		when(c3.getRented()).thenReturn(false);

		r.addCat(c1);
		r.addCat(c2);
		r.addCat(c3);

		// Try to rent already rented cat
		boolean result = r.rentCat(2);

		assertFalse("Expected rentCat to return false when cat is already rented.", result);

		String expectedOutput = "Sorry, Old Deuteronomy is not here!" + newline;
		assertEquals("System output not as expected.", expectedOutput, out.toString());
	}

	@Test
	public void testReturnCatNumCats3() {
		// Create mock cats
		c1 = mock(Cat.class);
		c2 = mock(Cat.class);
		c3 = mock(Cat.class);

		// Configure mocks
		when(c1.getId()).thenReturn(1);
		when(c1.getName()).thenReturn("Jennyanydots");
		when(c1.getRented()).thenReturn(false);

		when(c2.getId()).thenReturn(2);
		when(c2.getName()).thenReturn("Old Deuteronomy");
		when(c2.getRented()).thenReturn(true); // Cat is rented

		when(c3.getId()).thenReturn(3);
		when(c3.getName()).thenReturn("Mistoffelees");
		when(c3.getRented()).thenReturn(false);

		// Add cats to rental system
		r.addCat(c1);
		r.addCat(c2);
		r.addCat(c3);

		// Return the rented cat
		boolean result = r.returnCat(2);

		// Verify it returns true
		assertTrue("Expected returnCat to return true when returning a rented cat.", result);

		// Verify that returnCat() was called on the mock
		verify(c2).returnCat();

		// Verify correct output message
		String expectedOutput = "Welcome back, Old Deuteronomy!" + newline;
		assertEquals("System output not as expected.", expectedOutput, out.toString());
	}

	@Test
	public void testReturnFailureCatNumCats3() {
		c1 = mock(Cat.class);
		c2 = mock(Cat.class);
		c3 = mock(Cat.class);

		when(c1.getId()).thenReturn(1);
		when(c1.getName()).thenReturn("Jennyanydots");
		when(c1.getRented()).thenReturn(false);

		when(c2.getId()).thenReturn(2);
		when(c2.getName()).thenReturn("Old Deuteronomy");
		when(c2.getRented()).thenReturn(false); // Cat is NOT rented

		when(c3.getId()).thenReturn(3);
		when(c3.getName()).thenReturn("Mistoffelees");
		when(c3.getRented()).thenReturn(false);

		r.addCat(c1);
		r.addCat(c2);
		r.addCat(c3);

		boolean result = r.returnCat(2);

		assertFalse("Expected returnCat to return false when cat is not rented.", result);

		String expectedOutput = "Old Deuteronomy is already here!" + newline;
		assertEquals("System output not as expected.", expectedOutput, out.toString());
	}

}
