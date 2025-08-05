
package edu.pitt.cs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import static org.junit.Assert.*;

import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RentACatIntegrationTest {

	/**
	 * The test fixture for this JUnit test. Test fixture: a fixed state of a set of
	 * objects used as a baseline for running tests. The test fixture is initialized
	 * using the @Before setUp method which runs before every test case. The test
	 * fixture is removed using the @After tearDown method which runs after each
	 * test case.
	 */

	RentACat r; // Object to test
	Cat c1; // First cat object
	Cat c2; // Second cat object
	Cat c3; // Third cat object

	ByteArrayOutputStream out; // Output stream for testing system output
	PrintStream stdout; // Print stream to hold the original stdout stream
	String newline = System.lineSeparator(); // Platform independent newline ("\n" or "\r\n") for use in assertEquals

	@Before
	public void setUp() throws Exception {
		// 1. Create a new RentACat object and assign to r using a call to
		// RentACat.createInstance(InstanceType).
		// We will use InstanceType.IMPL to create a real RentACat object.
		r = RentACat.createInstance(InstanceType.IMPL);

		// 2. Create a Cat with ID 1 and name "Jennyanydots", assign to c1.
		c1 = Cat.createInstance(InstanceType.IMPL, 1, "Jennyanydots");

		// 3. Create a Cat with ID 2 and name "Old Deuteronomy", assign to c2.
		c2 = Cat.createInstance(InstanceType.IMPL, 2, "Old Deuteronomy");

		// 4. Create a Cat with ID 3 and name "Mistoffelees", assign to c3.
		c3 = Cat.createInstance(InstanceType.IMPL, 3, "Mistoffelees");

		// 5. Redirect system output from stdout to the "out" stream
		out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));
	}

	@After
	public void tearDown() throws Exception {
		// Restore System.out to the original stdout
		System.setOut(stdout);

		// Not necessary strictly speaking since the references will be overwritten in
		// the next setUp call anyway and Java has automatic garbage collection.
		r = null;
		c1 = null;
		c2 = null;
		c3 = null;
	}

	/**
	 * Test case for Cat getCat(int id).
	 * 
	 * <pre>
	 * Preconditions: r has no cats.
	 * Execution steps: Call getCat(2).
	 * Postconditions: Return value is null.
	 *                 System output is "Invalid cat ID." + newline.
	 * </pre>
	 * 
	 * Hint: You will need to use Java reflection to invoke the private getCat(int)
	 * method. efer to the Unit Testing Part 1 lecture and the textbook appendix
	 * hapter on using reflection on how to do this. Please use r.getClass() to get
	 * the class object of r instead of hardcoding it as RentACatImpl.
	 * //
	 */
	@Test
	public void testGetCatNullNumCats0() throws Exception {
		// Preconditions: r has no cats

		// Capture original system output
		PrintStream originalOut = System.out;
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));

		// reflection
		java.lang.reflect.Method method = r.getClass().getDeclaredMethod("getCat", int.class);
		method.setAccessible(true); // Make the private method accessible

		// Execution steps: Call getCat(2)
		Cat cat = (Cat) method.invoke(r, 2);

		// Restore the original system output
		System.setOut(originalOut);

		// Postconditions: Return value is null
		assertNull("Expected null cat when no cats exist.", cat);

		// Postconditions: System output is "Invalid cat ID." + newline
		String expectedOutput = "Invalid cat ID." + newline;
		assertEquals("System output not as expected.", expectedOutput, outContent.toString());
	}

	/**
	 * Test case for Cat getCat(int id).
	 * 
	 * <pre>
	 * Preconditions: c1, c2, and c3 are added to r using addCat(Cat c).
	 * Execution steps: Call getCat(2).
	 * Postconditions: Return value is not null.
	 *                 Returned cat has an ID of 2.
	 * </pre>
	 * 
	 * Hint: You will need to use Java reflection to invoke the private getCat(int)
	 * method. efer to the Unit Testing Part 1 lecture and the textbook appendix
	 * hapter on using reflection on how to do this. Please use r.getClass() to get
	 * the class object of r instead of hardcoding it as RentACatImpl.
	 */
	@Test
	public void testGetCatNumCats3() throws Exception {
		r.addCat(c1);
		r.addCat(c2);
		r.addCat(c3);

		Method getCatMethod = r.getClass().getDeclaredMethod("getCat", int.class);
		getCatMethod.setAccessible(true);

		Cat cat = (Cat) getCatMethod.invoke(r, 2);

		assertNotNull("Expected cat with ID 2 to be returned.", cat);
		assertEquals("Expected cat ID to be 2.", 2, cat.getId());
	}

	/**
	 * Test case for String listCats().
	 * 
	 * <pre>
	 * Preconditions: r has no cats.
	 * Execution steps: Call listCats().
	 * Postconditions: Return value is "".
	 * </pre>
	 */
	@Test
	public void testListCatsNumCats0() {
		// Preconditions: r has no cats, so no need to add any cats

		// Execution steps: Call listCats()
		String result = r.listCats();

		// Postconditions: Return value is ""
		assertEquals("Expected empty string when no cats are present.", "", result);
	}

	/**
	 * Test case for String listCats().
	 * 
	 * <pre>
	 * Preconditions: c1, c2, and c3 are added to r using addCat(Cat c).
	 * Execution steps: Call listCats().
	 * Postconditions: Return value is "ID 1. Jennyanydots\nID 2. Old
	 *                 Deuteronomy\nID 3. Mistoffelees\n".
	 * </pre>
	 */
	@Test
	public void testListCatsNumCats3() {
		// Create a REAL RentACat instance
		RentACat r = RentACat.createInstance(InstanceType.IMPL);

		// Create mock Cat instances
		Cat c1 = Cat.createInstance(InstanceType.IMPL, 1, "Jennyanydots");
		Cat c2 = Cat.createInstance(InstanceType.IMPL, 2, "Old Deuteronomy");
		Cat c3 = Cat.createInstance(InstanceType.IMPL, 3, "Mistoffelees");

		// Add the cats to the RentACat instance
		r.addCat(c1);
		r.addCat(c2);
		r.addCat(c3);

		// Call the listCats method
		String result = r.listCats();

		String expectedOutput = "ID 1. Jennyanydots\nID 2. Old Deuteronomy\nID 3. Mistoffelees\n";
		assertEquals("Expected list of cats to match expected:", expectedOutput, result);
	}

	/**
	 * Test case for boolean renameCat(int id, String name).
	 * 
	 * <pre>
	 * Preconditions: r has no cats.
	 * Execution steps: Call renameCat(2, "Garfield").
	 * Postconditions: Return value is false.
	 *                 c2 is not renamed to "Garfield".
	 *                 System output is "Invalid cat ID." + newline.
	 * </pre>
	 */
	@Test
	public void testRenameFailureNumCats0() {
		// Preconditions: r has no cats (already ensured by setUp)

		boolean result = r.renameCat(2, "Garfield");

		// Postconditions assertions
		assertFalse("Expected renameCat to return false when no cats exist.", result);

		// Verify system output is "Invalid cat ID." + newline
		String expectedOutput = "Invalid cat ID." + newline;
		assertEquals("System output should be 'Invalid cat ID.'", expectedOutput, out.toString());

	}

	/**
	 * Test case for boolean renameCat(int id, String name).
	 * 
	 * <pre>
	 * Preconditions: c1, c2, and c3 are added to r using addCat(Cat c).
	 * Execution steps: Call renameCat(2, "Garfield").
	 * Postconditions: Return value is true.
	 *                 c2 is renamed to "Garfield".
	 * </pre>
	 */
	@Test
	public void testRenameNumCat3() throws Exception {
		// Preconditions: Add c1, c2, and c3 to r using addCat(Cat c)
		r.addCat(c1);
		r.addCat(c2);
		r.addCat(c3);

		// Execution steps: Call renameCat(2, "Garfield")
		boolean result = r.renameCat(2, "Garfield");

		// Postconditions: Verify return value is true
		assertTrue("Expected renameCat to return true.", result);

		// Verify c2 is renamed to "Garfield" using reflection to access private getCat
		// method
		Method getCatMethod = r.getClass().getDeclaredMethod("getCat", int.class);
		getCatMethod.setAccessible(true);
		Cat renamedCat = (Cat) getCatMethod.invoke(r, 2);

		assertNotNull("Expected cat with ID 2 to exist.", renamedCat);
		assertEquals("Expected cat to be renamed to Garfield.", "Garfield", renamedCat.getName());
	}

	/**
	 * Test case for boolean rentCat(int id).
	 * 
	 * <pre>
	 * Preconditions: c1, c2, and c3 are added to r using addCat(Cat c).
	 * Execution steps: Call rentCat(2).
	 * Postconditions: Return value is true.
	 *                 c2 is rented as a result of the execution steps.
	 *                 System output is "Old Deuteronomy has been rented." + newline
	 * </pre>
	 */
	@Test
	public void testRentCatNumCats3() {
		// Preconditions: Add c1, c2, and c3 to r using addCat(Cat c)
		r.addCat(c1);
		r.addCat(c2);
		r.addCat(c3);

		// Execution steps: Call rentCat(2)
		boolean result = r.rentCat(2);

		// Postconditions: Verify return value is true
		assertTrue("Expected rentCat to return true.", result);

		// Verify c2 is rented
		assertTrue("Expected cat with ID 2 to be rented.", c2.getRented());

		// Verify system output is "Old Deuteronomy has been rented."
		String expectedOutput = "Old Deuteronomy has been rented." + newline;
		assertEquals("System output not as expected.", expectedOutput, out.toString());
	}

	/**
	 * Test case for boolean rentCat(int id).
	 * 
	 * <pre>
	 * Preconditions: c1, c2, and c3 are added to r using addCat(Cat c).
	 *                c2 is rented.
	 * Execution steps: Call rentCat(2).
	 * Postconditions: Return value is false.
	 *                 c2 stays rented.
	 *                 System output is "Sorry, Old Deuteronomy is not here!" + newline
	 * </pre>
	 */
	@Test
	public void testRentCatFailureNumCats3() {
		// Preconditions: Add c1, c2, and c3 to r using addCat(Cat c)
		r.addCat(c1);
		r.addCat(c2);
		r.addCat(c3);

		// Rent c2 first
		r.rentCat(2);

		// Redirect system output to capture it
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));

		// Execution steps: Call rentCat(2)
		boolean result = r.rentCat(2);

		// Postconditions: Verify return value is false
		assertFalse("Expected rentCat to return false when cat is already rented.", result);

		// Verify c2 remains rented
		assertTrue("Expected cat with ID 2 to remain rented.", c2.getRented());

		// Verify system output is "Sorry, Old Deuteronomy is not here!"
		String expectedOutput = "Sorry, Old Deuteronomy is not here!" + newline;
		assertEquals("System output not as expected.", expectedOutput, outContent.toString());
	}

	/**
	 * Test case for boolean returnCat(int id).
	 * 
	 * <pre>
	 * Preconditions: c1, c2, and c3 are added to r using addCat(Cat c).
	 *                c2 is rented.
	 * Execution steps: Call returnCat(2).
	 * Postconditions: Return value is true.
	 *                 c2 is returned as a result of the execution steps.
	 *                 System output is "Welcome back, Old Deuteronomy!" + newline
	 * </pre>
	 */
	@Test
	public void testReturnCatNumCats3() {
		// Preconditions: Add c1, c2, and c3 to r using addCat(Cat c)
		r.addCat(c1);
		r.addCat(c2);
		r.addCat(c3);

		// Rent c2 first
		r.rentCat(2);

		// Redirect system output to capture it
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));

		// Execution steps: Call returnCat(2)
		boolean result = r.returnCat(2);

		// Postconditions: Verify return value is true
		assertTrue("Expected returnCat to return true when cat is successfully returned.", result);

		// Verify c2 is returned (not rented)
		assertFalse("Expected cat with ID 2 to be returned.", c2.getRented());

		// Verify system output is "Welcome back, Old Deuteronomy!"
		String expectedOutput = "Welcome back, Old Deuteronomy!" + newline;
		assertEquals("System output not as expected.", expectedOutput, outContent.toString());
	}

	/**
	 * Test case for boolean returnCat(int id).
	 * 
	 * <pre>
	 * Preconditions: c1, c2, and c3 are added to r using addCat(Cat c).
	 * Execution steps: Call returnCat(2).
	 * Postconditions: Return value is false.
	 *                 c2 stays not rented.
	 *                 System output is "Old Deuteronomy is already here!" + newline
	 * </pre>
	 */
	@Test
	public void testReturnFailureCatNumCats3() {
		// Preconditions: Add c1, c2, and c3 to r using addCat(Cat c)
		r.addCat(c1);
		r.addCat(c2);
		r.addCat(c3);

		// Redirect system output to capture it
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));

		// Execution steps: Call returnCat(2)
		boolean result = r.returnCat(2);

		// Postconditions: Verify return value is false
		assertFalse("Expected returnCat to return false when cat is not rented.", result);

		// Verify c2 stays not rented
		assertFalse("Expected cat with ID 2 to stay not rented.", c2.getRented());

		// Verify system output is "Old Deuteronomy is already here!"
		String expectedOutput = "Old Deuteronomy is already here!" + newline;
		assertEquals("System output not as expected.", expectedOutput, outContent.toString());
	}

	@Test
	public void testRentReturnRenamedCat() {
		c2 = Cat.createInstance(InstanceType.IMPL, 2, "Old Deuteronomy");
		r.addCat(c2);
		r.renameCat(2, "Garfield");

		assertTrue(r.rentCat(2));
		out.reset();
		assertTrue(r.returnCat(2));
		assertEquals("Welcome back, Garfield!" + newline, out.toString());
	}

	@Test
	public void testListCatsAfterRename() {
		r.addCat(c1);
		r.addCat(c2);
		r.renameCat(2, "Garfield");
		String expected = "ID 1. Jennyanydots\nID 2. Garfield\n";
		assertEquals(expected, r.listCats());
	}

}