/*
 * Copyright 2009 MBARI
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 2.1 
 * (the "License"); you may not use this file except in compliance 
 * with the License. You may obtain a copy of the License at
 *
 * http://www.gnu.org/copyleft/lesser.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package test.moos.ssds.metadata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringWriter;

import junit.framework.TestCase;
import moos.ssds.metadata.Metadata;
import moos.ssds.metadata.StandardKeyword;
import moos.ssds.metadata.util.MetadataException;

import org.apache.log4j.Logger;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;

/**
 * This is the test class to test the StandardKeyword class
 * 
 * @author : $Author: kgomes $
 * @version : $Revision: 1.1.2.7 $
 */
public class TestStandardKeyword extends TestCase {

	/**
	 * The logger for dumping information to
	 */
	static Logger logger = Logger.getLogger(TestStandardKeyword.class);

	/**
	 * @param arg0
	 */
	public TestStandardKeyword(String arg0) {
		super(arg0);
	}

	protected void setUp() {
	}

	/**
	 * This method checks the creation of a <code>StandardKeyword</code> object
	 */
	public void testCreateStandardKeyword() {
		// Create the new StandardKeyword
		StandardKeyword standardKeyword = new StandardKeyword();

		// Set all the values
		standardKeyword.setId(new Long(1));
		try {
			standardKeyword.setName("StandardKeywordOne");
			standardKeyword.setDescription("StandardKeyword one description");
		} catch (MetadataException e) {
			assertTrue("MetadataException caught trying to set values: "
					+ e.getMessage(), false);
		}

		// Now read all of them back
		assertEquals(standardKeyword.getId(), new Long(1));
		assertEquals(standardKeyword.getName(), "StandardKeywordOne");
		assertEquals(standardKeyword.getDescription(),
				"StandardKeyword one description");
	}

	/**
	 * This method checks to see if the toStringRepresentation method works
	 * properly
	 */
	public void testToStringRepresentation() {
		// Create the new StandardKeyword
		StandardKeyword standardKeyword = new StandardKeyword();

		// Set all the values
		standardKeyword.setId(new Long(1));
		try {
			standardKeyword.setName("StandardKeywordOne");
			standardKeyword.setDescription("StandardKeyword one description");
		} catch (MetadataException e) {
			assertTrue("MetadataException caught trying to set values: "
					+ e.getMessage(), false);
		}

		// Check that the string representations are equal
		String stringStandardKeyword = standardKeyword
				.toStringRepresentation(",");
		String stringRep = "StandardKeyword," + "id=1,"
				+ "name=StandardKeywordOne,"
				+ "description=StandardKeyword one description";
		assertEquals(
				"The string represntation should match the set attributes",
				stringStandardKeyword, stringRep);

	}

	/**
	 * This tests the method that sets the values from a string representation
	 */
	public void testSetValuesFromStringRepresentation() {

		// Create the StandardKeyword
		StandardKeyword standardKeyword = new StandardKeyword();

		// Create the string representation
		String stringRep = "StandardKeyword," + "id=1,"
				+ "name=StandardKeywordOne,"
				+ "description=StandardKeyword one description";

		try {
			standardKeyword.setValuesFromStringRepresentation(stringRep, ",");
		} catch (MetadataException e) {
			logger.error("MetadataException caught trying to set "
					+ "values from string representation: " + e.getMessage());
		}

		// Now check that everything was set OK
		assertEquals(standardKeyword.getId(), new Long(1));
		assertEquals(standardKeyword.getName(), "StandardKeywordOne");
		assertEquals(standardKeyword.getDescription(),
				"StandardKeyword one description");
	}

	/**
	 * This method tests the equals method
	 */
	public void testEquals() {
		// Create the string representation
		String stringRep = "StandardKeyword," + "id=1,"
				+ "name=StandardKeywordOne,"
				+ "description=StandardKeyword one description";
		String stringRepTwo = "StandardKeyword," + "id=1,"
				+ "name=StandardKeywordOne,"
				+ "description=StandardKeyword one description";

		StandardKeyword standardKeywordOne = new StandardKeyword();
		StandardKeyword standardKeywordTwo = new StandardKeyword();

		try {
			standardKeywordOne
					.setValuesFromStringRepresentation(stringRep, ",");
			standardKeywordTwo.setValuesFromStringRepresentation(stringRepTwo,
					",");
		} catch (MetadataException e) {
			logger
					.error("MetadataException caught trying to create two StandardKeyword objects");
		}

		assertTrue("The two StandardKeywords should be equal (part one).",
				standardKeywordOne.equals(standardKeywordTwo));
		assertEquals("The two StandardKeywords should be equal (part two).",
				standardKeywordOne, standardKeywordTwo);

		// Now change the ID of the second one and they should still be equal
		standardKeywordTwo.setId(new Long(2));
		assertTrue("The two StandardKeyword should be equal",
				standardKeywordOne.equals(standardKeywordTwo));

		// Now set the ID back, check equals again
		standardKeywordTwo.setId(new Long(1));
		assertEquals(
				"The two StandardKeywords should be equal after ID set back.",
				standardKeywordOne, standardKeywordTwo);

		// Now set the name and they should be different
		try {
			standardKeywordTwo.setName("StandardKeywordTwo");
		} catch (MetadataException e) {
			assertTrue("MetadataException caught trying to set values: "
					+ e.getMessage(), false);
		}
		assertTrue("The two StandardKeyword should not be equal",
				!standardKeywordOne.equals(standardKeywordTwo));

		// Now set it back and change all the non-business key values. The
		// results should be equals
		try {
			standardKeywordTwo.setName("StandardKeywordOne");
			standardKeywordTwo.setDescription("blah blah");
		} catch (MetadataException e) {
			assertTrue("MetadataException caught trying to set values: "
					+ e.getMessage(), false);
		}
		assertEquals(
				"The two StandardKeywords should be equal after ID set back.",
				standardKeywordOne, standardKeywordTwo);
	}

	/**
	 * This method tests the hashCode method
	 */
	public void testHashCode() {
		// Create the string representation
		String stringRep = "StandardKeyword," + "id=1,"
				+ "name=StandardKeywordOne,"
				+ "description=StandardKeyword one description";
		String stringRepTwo = "StandardKeyword," + "id=1,"
				+ "name=StandardKeywordOne,"
				+ "description=StandardKeyword one description";

		StandardKeyword standardKeywordOne = new StandardKeyword();
		StandardKeyword standardKeywordTwo = new StandardKeyword();

		try {
			standardKeywordOne
					.setValuesFromStringRepresentation(stringRep, ",");
			standardKeywordTwo.setValuesFromStringRepresentation(stringRepTwo,
					",");
		} catch (MetadataException e) {
			logger
					.error("MetadataException caught trying to create two StandardKeyword objects: "
							+ e.getMessage());
		}

		assertTrue("The two hashCodes should be equal (part one).",
				standardKeywordOne.hashCode() == standardKeywordTwo.hashCode());
		assertEquals("The two hashCodes should be equal (part two).",
				standardKeywordOne.hashCode(), standardKeywordTwo.hashCode());

		// Now change the ID of the second one and they should not be equal
		standardKeywordTwo.setId(new Long(2));
		assertTrue("The two hashCodes should be equal", standardKeywordOne
				.hashCode() == standardKeywordTwo.hashCode());

		// Now set the ID back, check equals again
		standardKeywordTwo.setId(new Long(1));
		assertEquals("The two hashCodes should be equal after ID set back.",
				standardKeywordOne.hashCode(), standardKeywordTwo.hashCode());

		// Now set the name and they should be different
		try {
			standardKeywordTwo.setName("StandardKeywordTwo");
		} catch (MetadataException e) {
			assertTrue("MetadataException caught trying to set values: "
					+ e.getMessage(), false);
		}
		assertTrue("The two hashCodes should not be equal after name change",
				standardKeywordOne.hashCode() != standardKeywordTwo.hashCode());

		// Now set it back and change all the non-business key values. The
		// results should be equals
		try {
			standardKeywordTwo.setName("StandardKeywordOne");
			standardKeywordTwo.setDescription("blah blah");
		} catch (MetadataException e) {
			assertTrue("MetadataException caught trying to set values: "
					+ e.getMessage(), false);
		}
		assertEquals("The two hashCodes should be equal after ID and name same"
				+ ", but different business keys.", standardKeywordOne
				.hashCode(), standardKeywordTwo.hashCode());
	}

	/**
	 * This test takes a StandardKeyword defined in XML, converts it to an
	 * object, checks the attributes, converts changes some attributes and
	 * converts back to XML.
	 */
	public void testStandardKeywordXMLBinding() {

		// Grab the file that has the XML in it
		File standardKeywordXMLFile = new File("src" + File.separator
				+ "resources" + File.separator + "test" + File.separator
				+ "xml" + File.separator + "StandardKeyword.xml");
		if (!standardKeywordXMLFile.exists())
			assertTrue("Could not find StandardKeyword.xml file for testing.",
					false);
		logger.debug("Will read StandardKeyword XML from "
				+ standardKeywordXMLFile.getAbsolutePath());

		// Create a file reader
		FileReader standardKeywordXMLFileReader = null;
		try {
			standardKeywordXMLFileReader = new FileReader(
					standardKeywordXMLFile);
		} catch (FileNotFoundException e2) {
			assertTrue(
					"Error in creating file reader for standardKeyword XML file: "
							+ e2.getMessage(), false);
		}

		// Grab the binding factory
		IBindingFactory bfact = null;
		try {
			bfact = BindingDirectory.getFactory(Metadata.class);
		} catch (JiBXException e1) {
			assertTrue("Error in getting Binding Factory: " + e1.getMessage(),
					false);
		}

		// Grab a JiBX unmarshalling context
		IUnmarshallingContext uctx = null;
		if (bfact != null) {
			try {
				uctx = bfact.createUnmarshallingContext();
			} catch (JiBXException e) {
				assertTrue("Error in getting UnmarshallingContext: "
						+ e.getMessage(), false);
			}
		}

		// Now unmarshall it
		if (uctx != null) {
			Metadata topMetadata = null;
			StandardKeyword testStandardKeyword = null;
			try {
				topMetadata = (Metadata) uctx.unmarshalDocument(
						standardKeywordXMLFileReader, null);
				testStandardKeyword = topMetadata.getStandardKeywords()
						.iterator().next();

				logger.debug("TestStandardKeyword after unmarshalling: "
						+ testStandardKeyword.toStringRepresentation("|"));
			} catch (JiBXException e1) {
				assertTrue("Error in unmarshalling: " + e1.getMessage(), false);
			} catch (Throwable t) {
				t.printStackTrace();
				logger.error("Throwable caught: " + t.getMessage());
			}

			if (testStandardKeyword != null) {
				assertEquals("ID should be 1", testStandardKeyword.getId()
						.longValue(), Long.parseLong("1"));
				assertEquals("StandardKeyword name should match",
						testStandardKeyword.getName(), "Test StandardKeyword");
				assertEquals("Descripton should match", testStandardKeyword
						.getDescription(), "Test StandardKeyword Description");

				// Now let's change the attributes
				try {
					testStandardKeyword.setName("Changed Test StandardKeyword");
					testStandardKeyword
							.setDescription("Changed Test StandardKeyword Description");
					logger.debug("Changed name and description "
							+ "and will marshall to XML");
				} catch (MetadataException e) {
					assertTrue("Error while changing attributes: "
							+ e.getMessage(), false);
				}

				// Create a string writer
				StringWriter stringWriter = new StringWriter();

				// Marshall out to XML
				IMarshallingContext mctx = null;
				try {
					mctx = bfact.createMarshallingContext();
				} catch (JiBXException e) {
					assertTrue("Error while creating marshalling context: "
							+ e.getMessage(), false);
				}

				if (mctx != null) {
					mctx.setIndent(2);
					try {
						mctx.marshalDocument(testStandardKeyword, "UTF-8",
								null, stringWriter);
					} catch (JiBXException e) {
						assertTrue("Error while marshalling "
								+ "after attribute changes: " + e.getMessage(),
								false);
					}

					logger.debug("Marshalled XML after change: "
							+ stringWriter.toString());

					// Now test the string
					assertTrue("Marshalled XML contain changed name",
							stringWriter.toString().contains(
									"Changed Test StandardKeyword"));
					assertTrue("Marshalled XML contain changed description",
							stringWriter.toString().contains(
									"Changed Test StandardKeyword Description"));
				}

			} else {
				assertTrue("metadata object came back null!", false);
			}
		}

	}

}